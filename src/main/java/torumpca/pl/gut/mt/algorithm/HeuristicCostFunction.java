package torumpca.pl.gut.mt.algorithm;

import es.usc.citius.hipster.model.function.HeuristicFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;

public class HeuristicCostFunction implements HeuristicFunction<Coordinates, Double> {

    private final static Logger LOG = LoggerFactory.getLogger(HeuristicCostFunction.class);

    private final Craft craft;
    private final Coordinates goal;

    HeuristicCostFunction(Craft craft, Coordinates goal) {
        this.craft = craft;
        this.goal = goal;
    }

    @Override
    public Double estimate(Coordinates currentLocation) {
        final double distance = Utils.getGreatCircleDistance(currentLocation, goal);
        final Double estimatedCost =
                distance / craft.getAverageSpeedInMpS() * craft.getAverageCostOfHourOnSea() / 3600;
        LOG.debug("HEURISTIC - from {} to target in {} estimated cost {} distance {}",
                currentLocation, goal, estimatedCost, distance);
        return estimatedCost;
    }
}
