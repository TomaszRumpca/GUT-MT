package torumpca.pl.gut.mt.algorithm;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import torumpca.pl.gut.mt.algorithm.model.AlgorithmInputData;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.mt.algorithm.model.Solution;
import torumpca.pl.gut.mt.forecast.model.WindForecastModel;

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
        //TODO powinna uwzględniać ograniczenia dotyczące maksymalnej prędkości wiatru oraz jego
        // maksymalnych podmuchów
        List<String> registeredMasks = masks
                .stream()
                .map(mask -> mask.getClass().getCanonicalName())
                .collect(Collectors.toList());
        LOG.info("registered {} masks: {}", masks.size(), registeredMasks);
    }

    public Solution resolve(final WindForecastModel forecast, AlgorithmInputData input) {

        double lonStep = forecast.getMetaData().getLonStep();
        double latStep = forecast.getMetaData().getLatStep();

        double maxMoveDistance = Math.sqrt(lonStep * lonStep + latStep * latStep);

        final Coordinates originCoordinates = input.getOrigin();
        final Coordinates goalCoordinates = input.getDestination();
        final Craft craft = input.getShip();

        LOG.info("Finding path from {} to {} for {} using A* algorithm", originCoordinates,
                goalCoordinates, craft.getClass().getCanonicalName());

        TransitionFunction transitionFunction =
                new TransitionFunction(masks, latStep, lonStep, maxMoveDistance, goalCoordinates);

        CostFunction<Void, Coordinates, Double> costFunction =
                new MoveCostFunction(forecast, craft);

        HeuristicFunction<Coordinates, Double> heuristicFunction =
                new HeuristicCostFunction(craft, goalCoordinates);

        //@formatter:off
        SearchProblem<Void, Coordinates, WeightedNode<Void, Coordinates, Double>> problemDef = ProblemBuilder
                .create()
                .initialState(originCoordinates)
                .defineProblemWithoutActions()
                .useTransitionFunction(transitionFunction)
                .useCostFunction(costFunction)
                .useHeuristicFunction(heuristicFunction)
                .build();
        //@formatter:on

        Algorithm.SearchResult result = Hipster.createAStar(problemDef).search(goalCoordinates);

        LOG.info("Result: \n{}", result);

        return getSolution(result);
    }

    private Solution getSolution(Algorithm.SearchResult result) {
        long overallCost = result.getElapsed();
        return new Solution(result.getOptimalPaths(), overallCost);
    }

}
