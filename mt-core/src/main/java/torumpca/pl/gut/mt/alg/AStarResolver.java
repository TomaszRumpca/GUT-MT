package torumpca.pl.gut.mt.alg;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.impl.StateTransitionFunction;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.Util;
import torumpca.pl.gut.mt.dsm.model.*;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Tomasz Rumpca on 2016-02-07.
 */
public class AStarResolver implements ProblemResolver {

    private static Logger LOG = LoggerFactory.getLogger(AStarResolver.class);

    static boolean[][] mask;

    public static Collection<Point> validLocationsFrom(Point loc) {
        Collection<Point> validMoves = new HashSet<>();
        // Check for all valid movements
        for (int row = -1; row <= 1; row++) {
            for (int column = -1; column <= 1; column++) {
                try {
                    if (isFree(new Point(loc.x + column, loc.y + row))) {
                        validMoves.add(new Point(loc.x + column, loc.y + row));
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    // Invalid move!
                }
            }
        }
        validMoves.remove(loc);

        return validMoves;
    }

    private static boolean isFree(Point point) {
        return mask[point.x][point.y];
    }

    private Solution getSolution(Algorithm.SearchResult result) {
        return null;
    }

    public Solution resolve(final WindForecastModel forecast, UserData input) {

        LOG.info("Start determining the solution using A* algorithm");
        LOG.info("User input: {}", input);

        //        Point origin = forecast.getPointFromLatLon(input.getOriginLat(),
        //                input.getOriginLon());
        //        Point goal = forecast.getPointFromLatLon(input.getGoalLat(), input.getGoalLon());
        //
        final Ship ship = input.getShip();
        Point origin = new Point(0, 2);
        Point goal = new Point(3, 0);
        final LatLon goalLocation = forecast.getLatLonFromPoint(goal);

        final VectorComponents[][] forecastData = forecast.getForecastData();
        mask = Masks.generateSimpleMask(forecast);

        SearchProblem problemDef =
                ProblemBuilder.create().initialState(origin).defineProblemWithoutActions()
                        .useTransitionFunction(new StateTransitionFunction<Point>() {
                            @Override
                            public Iterable<Point> successorsOf(Point state) {
                                return validLocationsFrom(state);
                            }
                        }).useCostFunction(new CostFunction<Void, Point, Double>() {

                    public Double evaluate(Transition<Void, Point> transition) {

                        final Point source = transition.getFromState();
                        final Point destination = transition.getState();

                        if (source.equals(destination)) {
                            return Double.MAX_VALUE;
                        }

                        final LatLon sourceLocation = forecast.getLatLonFromPoint(source);
                        final LatLon destLocation = forecast.getLatLonFromPoint(destination);
                        final double distance = Util.getDistance(sourceLocation, destLocation);

                        final VectorComponents wind = forecastData[source.x][source.y];

                        final double azimuth = Util.getAzimuth(sourceLocation, destLocation);
                        final double normalizedAzimuth = (azimuth + 2 * Math.PI) % (2 * Math.PI);

                        final double rotationAngle = -(normalizedAzimuth - Math.PI / 2);
                        final VectorComponents normalizedWindComponents =
                                Util.rotateVector(rotationAngle, wind);

                        return ship.calculateTravelCost(distance, normalizedWindComponents);
                    }
                }).useHeuristicFunction(new HeuristicFunction<Point, Double>() {
                    @Override
                    public Double estimate(Point state) {
                        final LatLon currentLocation = forecast.getLatLonFromPoint(state);
                        final double distance = Util.getDistance(currentLocation, goalLocation);
                        return distance / ship.getAverageSpeedInMpS() * ship
                                .getAverageCostOfHourOnSea() / 3600;
                    }
                }).build();

        Algorithm.SearchResult result = Hipster.createAStar(problemDef).search(goal);

        LOG.info("Result: {}", result);

        return getSolution(result);
    }

}
