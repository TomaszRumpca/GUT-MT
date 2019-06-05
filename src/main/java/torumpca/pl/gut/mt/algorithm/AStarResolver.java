package torumpca.pl.gut.mt.algorithm;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.impl.StateTransitionFunction;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import torumpca.pl.gut.mt.algorithm.model.*;
import torumpca.pl.gut.mt.forecast.DataNotAvailableException;
import torumpca.pl.gut.mt.forecast.model.WindForecastModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Tomasz Rumpca on 2016-02-07.
 */
@Service
@Qualifier("AStar")
public class AStarResolver implements ProblemResolver {

    private static Logger LOG = LoggerFactory.getLogger(AStarResolver.class);

    private final List<Mask> masks;

    @Autowired
    public AStarResolver(List<Mask> masks) {
        this.masks = masks;
        List<String> registeredMasks = masks.stream().map(mask -> mask.getClass().getCanonicalName()).collect(Collectors.toList());
        LOG.info("registered {} masks: {}", masks.size(), registeredMasks);
    }

    public Solution resolve(final WindForecastModel forecast, AlgorithmInputData input) {

        double lonStep = forecast.getMetaData().getLonStep();
        double latStep = forecast.getMetaData().getLatStep();

        double maxMoveDistance = Math.sqrt(lonStep * lonStep + latStep * latStep);

        final Coordinates originCoordinates = input.getOrigin();
        final Coordinates goalCoordinates = input.getDestination();
        final Craft craft = input.getShip();

        LOG.info("Finding path from {} to {} for {} using A* algorithm", originCoordinates, goalCoordinates, craft.getClass().getCanonicalName());

        final VectorComponents[][] forecastData = forecast.getForecastData();

        //@formatter:off
        SearchProblem<Void, Coordinates, WeightedNode<Void, Coordinates, Double>> problemDef = ProblemBuilder
                .create()
                .initialState(originCoordinates)
                .defineProblemWithoutActions()
                .useTransitionFunction(getTransitionFunction(latStep, lonStep, maxMoveDistance, goalCoordinates))
                .useCostFunction(transition ->
                        costFunction(forecast, craft, forecastData, transition))
                .useHeuristicFunction((Coordinates state) ->
                        heuristicFunction(craft, state, goalCoordinates))
                .build();

        //@formatter:on

        Algorithm.SearchResult result = Hipster.createAStar(problemDef).search(goalCoordinates);

        LOG.info("Result: \n{}", result);

        return getSolution(result, forecast);
    }

    /**
     * szuka punktow w najblizszym otoczeniu statku które mogą stać się następnym elementem na jesgo ścieżce.
     * <p>
     * //TODO powinna uwzględniać ograniczenia dotyczące maksymalnej prędkości wiatru oraz jego maksymalnych podmuchów
     *
     * @param shipPosition punkt z którego wyznaczane są następne możliwe przejścia
     * @return zbiór punktów które mogą zostać odwiedzone w następnym kroku algorytmu, bezpośredni sąsiedzi shipPosition
     */
    private Collection<Coordinates> validLocationsFrom(Coordinates shipPosition, double latStep, double lonStep, double maxMoveDistance, Coordinates goal) {

        Collection<Coordinates> validMoves = new HashSet<>();
        // Check for all valid movements
        for (double row = -lonStep; row <= lonStep; row += lonStep) {
            for (double column = -latStep; column <= latStep; column += latStep) {
                if (row != 0 || column != 0) {

                    Coordinates predictedCoordinates = new Coordinates(shipPosition.latitude + column, shipPosition.longitude + row);

                    boolean allowed = true;
                    for (Mask mask : masks) {
                        boolean allowedByCurrentMask = mask.isAllowed(shipPosition, predictedCoordinates);
                        if (!allowedByCurrentMask) {
                            LOG.info("Move ({}) -> ({}) disallowed by {}", shipPosition, predictedCoordinates, mask.getClass().getCanonicalName());
                            allowed = false;
                            break;
                        }
                    }

                    if (allowed) {
                        validMoves.add(predictedCoordinates);
                    }
                }
            }
        }

        double deltaLatitude = shipPosition.latitude - goal.latitude;
        double deltaLongitude = shipPosition.longitude - goal.longitude;

        double distanceToGoal = Math.sqrt(deltaLatitude * deltaLatitude + deltaLongitude * deltaLongitude);

        if (distanceToGoal < maxMoveDistance) {
            validMoves.add(goal);
        }

        return validMoves;
    }

    private Solution getSolution(Algorithm.SearchResult result, WindForecastModel forecast) {
        long overallCost = result.getElapsed();
        List optimalPaths = result.getOptimalPaths();
//        List<List<Coordinates>> mappedResult = new ArrayList<>();
//        optimalPaths.stream().forEach(locationsList -> {
//            List<Point> path = (List<Point>) locationsList;
//            final List<Coordinates> coordinates = path.stream().map(point -> pointToCoordinates(point, forecast)).collect(Collectors.toList());
//            mappedResult.add(coordinates);
//        });

        return new Solution(result.getOptimalPaths(), overallCost);
    }


    private StateTransitionFunction<Coordinates> getTransitionFunction(double latStep, double lonStep, double maxMoveDistance, Coordinates goal) {
        return new StateTransitionFunction<Coordinates>() {
            @Override
            public Iterable<Coordinates> successorsOf(Coordinates state) {
                return validLocationsFrom(state, latStep, lonStep, maxMoveDistance, goal);
            }
        };
    }

    private Double costFunction(WindForecastModel forecast, Craft craft, VectorComponents[][] forecastData, Transition<Void, Coordinates> transition) {
        final Coordinates source = transition.getFromState();
        final Coordinates destination = transition.getState();

        if (source.equals(destination)) {
            return Double.MAX_VALUE;
        }

//        final Coordinates sourceLocation = forecast.getLatLonFromPoint(source);
//        final Coordinates destLocation = forecast.getLatLonFromPoint(destination);

        Point sourcePoint;
        try {
            sourcePoint = forecast.getPointFromLatLon(source);
        } catch (DataNotAvailableException e) {
            LOG.trace("dta");
            sourcePoint = new Point(0, 0);
        }


        final VectorComponents wind = forecastData[sourcePoint.x][sourcePoint.y];

        final Double cost =
                craft.calculateTravelCost(source, destination, wind);
        LOG.debug("COST - from {} to {} cost {}", source, destination, cost);
        return cost;
    }

    private Double heuristicFunction(Craft craft, Coordinates currentLocation, Coordinates goalLocation) {
        final double distance = Utils.getGreatCircleDistance(currentLocation, goalLocation);
        final Double estimatedCost = distance / craft.getAverageSpeedInMpS() * craft
                .getAverageCostOfHourOnSea() / 3600;
        LOG.debug(
                "HEURISTIC - from {} to target in {} estimated cost {} distance {}",
                currentLocation, goalLocation, estimatedCost, distance);
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
