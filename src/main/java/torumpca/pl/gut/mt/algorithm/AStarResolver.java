package torumpca.pl.gut.mt.algorithm;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.impl.StateTransitionFunction;
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
    private WindForecastModel forecast;

    @Autowired
    public AStarResolver(List<Mask> masks) {
        this.masks = masks;
        List<String> registeredMasks = masks.stream().map(mask -> mask.getClass().getCanonicalName()).collect(Collectors.toList());
        LOG.info("registered {} masks: {}", masks.size(), registeredMasks);
    }

    public Solution resolve(final WindForecastModel forecast, AlgorithmInputData input) {

        this.forecast = forecast;
        LOG.info("Start determining the solution using A* algorithm");

        final Craft craft = input.getShip();

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

        final Coordinates goalLocation = forecast.getLatLonFromPoint(goal);

        final VectorComponents[][] forecastData = forecast.getForecastData();

        //@formatter:off
        SearchProblem problemDef = ProblemBuilder
                .create()
                .initialState(origin)
                .defineProblemWithoutActions()
                .useTransitionFunction(getTransitionFunction())
                .useCostFunction(transition ->
                        costFunction(forecast, craft, forecastData, transition))
                .useHeuristicFunction((Point state) ->
                        heuristicFunction(forecast, craft, goal, goalLocation, state))
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
    private Collection<Point> validLocationsFrom(WindForecastModel forecastModel, Point shipPosition) {
        Collection<Point> validMoves = new HashSet<>();
        // Check for all valid movements
        for (int row = -1; row <= 1; row++) {
            for (int column = -1; column <= 1; column++) {
                if (row != 0 || column != 0) {
                    final Point predictedPosition = new Point(shipPosition.x + column, shipPosition.y + row);

                    boolean allowed = true;
                    for (Mask mask : masks) {
                        Coordinates source = forecastModel.getLatLonFromPoint(shipPosition);
                        Coordinates target = forecastModel.getLatLonFromPoint(predictedPosition);
                        boolean allowedByCurrentMask = mask.isAllowed(source, target);
                        if(!allowedByCurrentMask){
                            LOG.info("Move ({}) -> ({}) disallowed by {}", source, target, mask.getClass().getCanonicalName());
                            allowed = false;
                            break;
                        }
                    }

                    if (allowed) {
                        validMoves.add(predictedPosition);
                    }
                }
            }
        }
        return validMoves;
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
                return validLocationsFrom(forecast, state);
            }
        };
    }

    private Double costFunction(WindForecastModel forecast, Craft craft, VectorComponents[][] forecastData, Transition<Void, Point> transition) {
        final Point source = transition.getFromState();
        final Point destination = transition.getState();

        if (source.equals(destination)) {
            return Double.MAX_VALUE;
        }

        final Coordinates sourceLocation = forecast.getLatLonFromPoint(source);
        final Coordinates destLocation = forecast.getLatLonFromPoint(destination);

        final VectorComponents wind = forecastData[source.x][source.y];

        final Double cost =
                craft.calculateTravelCost(sourceLocation, destLocation, wind);
        LOG.debug("COST - from {} to {} cost {}", source, destination, cost);
        return cost;
    }

    private Double heuristicFunction(WindForecastModel forecast, Craft craft, Point goal, Coordinates goalLocation, Point state) {
        final Coordinates currentLocation = forecast.getLatLonFromPoint(state);
        final double distance = Utils.getGreatCircleDistance(currentLocation, goalLocation);
        final Double estimatedCost = distance / craft.getAverageSpeedInMpS() * craft
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
