package torumpca.pl.gut.mt.alg;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.impl.StateTransitionFunction;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.Utils;
import torumpca.pl.gut.mt.model.*;
import torumpca.pl.gut.mt.error.DataNotAvailableException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Tomasz Rumpca on 2016-02-07.
 */
public class AStarResolver implements ProblemResolver {

    private static Logger LOG = LoggerFactory.getLogger(AStarResolver.class);

    private static final String DEGREES = "\u00b0";

    private boolean[][] mask;

    public Solution resolve(final WindForecastModel forecast, UserData input) {

        LOG.info("Start determining the solution using A* algorithm");

        final Ship ship = input.getShip();

        final Coordinates originCoordinates = input.getOrigin();
        final Coordinates goalCoordinates = input.getDestination();

        final Point origin, goal;
        try {
            origin = forecast.getPointFromLatLon(originCoordinates);
            goal = forecast.getPointFromLatLon(goalCoordinates);
        } catch (DataNotAvailableException e) {
            LOG.error("origin and/or destination out of range of available forecast data", e);
            return getSolution(e);
        }

        final LatLon goalLocation = forecast.getLatLonFromPoint(goal);

        final VectorComponents[][] forecastData = forecast.getForecastData();

        //mask = Masks.generateSimpleMask(forecast);
        mask = Masks.getMaskAllValid(forecast);

        //@formatter:off
        SearchProblem problemDef = ProblemBuilder
                .create()
                .initialState(origin)
                .defineProblemWithoutActions()
                .useTransitionFunction(getTransitionFunction())
                .useCostFunction(transition ->
                        costFunction(forecast, ship, forecastData, transition))
                .useHeuristicFunction((Point state) ->
                        heuristicFunction(forecast, ship, goal, goalLocation, state))
                .build();
        //@formatter:on

        Algorithm.SearchResult result = Hipster.createAStar(problemDef).search(goal);

        LOG.info("Result: \n{}", result);

        return getSolution(result, forecast);
    }

    /**
     * szuka punktow w najblizszym otoczeniu statku które mogą stać się następnym elementem na jesgo ścieżce.
     *
     * //TODO powinna uwzględniać ograniczenia dotyczące maksymalnej prędkości wiatru oraz jego maksymalnych podmuchów
     *
     * @param shipPosition punkt z którego wyznaczane są następne możliwe przejścia
     * @return zbiór punktów które mogą zostać odwiedzone w następnym kroku algorytmu, bezpośredni sąsiedzi shipPosition
     */
    private Collection<Point> validLocationsFrom(Point shipPosition) {
        Collection<Point> validMoves = new HashSet<>();
        // Check for all valid movements
        for (int row = -1; row <= 1; row++) {
            for (int column = -1; column <= 1; column++) {
                try {
                    if (isFree(new Point(shipPosition.x + column, shipPosition.y + row))) {
                        validMoves.add(new Point(shipPosition.x + column, shipPosition.y + row));
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    // Invalid move!
                }
            }
        }
        validMoves.remove(shipPosition);

        return validMoves;
    }

    private boolean isFree(Point point) {
        return mask[point.x][point.y];
    }

    private Solution getSolution(Algorithm.SearchResult result, WindForecastModel forecast) {
        long overallCost = result.getElapsed();
        List optimalPaths = result.getOptimalPaths();
        List<List<Coordinates>> mappedResult = new ArrayList<>();
        optimalPaths.stream().forEach(locationsList -> {
            List<Point> path = (List<Point>) locationsList;
            final List<Coordinates> coordinates = path.stream().map(point -> pointToCoordinates(point, forecast)).collect(Collectors.toList());
            mappedResult.add(coordinates);
        });

        return new Solution(mappedResult, overallCost);
    }


    private StateTransitionFunction<Point> getTransitionFunction() {
        return new StateTransitionFunction<Point>() {
            @Override
            public Iterable<Point> successorsOf(Point state) {
                return validLocationsFrom(state);
            }
        };
    }

    private Double costFunction(WindForecastModel forecast, Ship ship, VectorComponents[][] forecastData, Transition<Void, Point> transition) {
        final Point source = transition.getFromState();
        final Point destination = transition.getState();

        if (source.equals(destination)) {
            return Double.MAX_VALUE;
        }

        final LatLon sourceLocation = forecast.getLatLonFromPoint(source);
        final LatLon destLocation = forecast.getLatLonFromPoint(destination);
        final double distance = Utils.getDistance(sourceLocation, destLocation);

        final VectorComponents wind = forecastData[source.x][source.y];

        final double azimuth = Utils.getAzimuth(sourceLocation, destLocation);
        final double normalizedAzimuth = (azimuth + 2 * Math.PI) % (2 * Math.PI);

        final double rotationAngle = -(normalizedAzimuth - Math.PI / 2);
        final VectorComponents normalizedWindComponents =
                Utils.rotateVector(rotationAngle, wind);
        LOG.debug(
                "COST - normAzimuth {}{}, rotation {}{}, norm wind components {},"
                        + " origin wind {}",
                Math.toDegrees(normalizedAzimuth), DEGREES,
                Math.toDegrees(rotationAngle), DEGREES, normalizedWindComponents,
                wind);
        final Double cost =
                ship.calculateTravelCost(distance, normalizedWindComponents);
        LOG.debug("COST - from {} to {} cost {}", source, destination, cost);
        return cost;
    }

    private Double heuristicFunction(WindForecastModel forecast, Ship ship, Point goal, LatLon goalLocation, Point state) {
        final LatLon currentLocation = forecast.getLatLonFromPoint(state);
        final double distance = Utils.getDistance(currentLocation, goalLocation);
        final Double estimatedCost = distance / ship.getAverageSpeedInMpS() * ship
                .getAverageCostOfHourOnSea() / 3600;
        LOG.debug(
                "HEURISTIC - from {} to target in {} estimated cost {} distance {}",
                state, goal, estimatedCost, distance);
        return estimatedCost;
    }

    private Coordinates pointToCoordinates(Point point, WindForecastModel forecast) {
        final double latitude = forecast.getMetaData().leftBottomLatCoordinate + (forecast.getMetaData().latStep * point.x);
        final double longitude = forecast.getMetaData().leftBottomLonCoordinate + (forecast.getMetaData().lonStep * point.y);
        return new Coordinates(latitude, longitude);
    }

    private Solution getSolution(Throwable throwable) {
        //TODO implement getSolution from error
        return null;
    }


}
