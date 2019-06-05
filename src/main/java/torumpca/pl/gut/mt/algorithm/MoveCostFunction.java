package torumpca.pl.gut.mt.algorithm;

import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.mt.algorithm.model.Point;
import torumpca.pl.gut.mt.algorithm.model.VectorComponents;
import torumpca.pl.gut.mt.forecast.DataNotAvailableException;
import torumpca.pl.gut.mt.forecast.model.WindForecastModel;


public class MoveCostFunction implements CostFunction<Void, Coordinates, Double> {

    private final static Logger LOG = LoggerFactory.getLogger(MoveCostFunction.class);

    private final WindForecastModel forecast;
    private final Craft craft;

    MoveCostFunction(WindForecastModel forecast, Craft craft) {
        this.forecast = forecast;
        this.craft = craft;
    }

    @Override
    public Double evaluate(Transition<Void, Coordinates> transition) {
        final Coordinates source = transition.getFromState();
        final Coordinates destination = transition.getState();

        if (source.equals(destination)) {
            return Double.MAX_VALUE;
        }
        Point sourcePoint;
        try {
            sourcePoint = forecast.getPointFromLatLon(source);
        } catch (DataNotAvailableException e) {
            LOG.trace("dta");
            sourcePoint = new Point(0, 0);
        }

        VectorComponents[][] forecastData = forecast.getForecastData();
        final VectorComponents wind = forecastData[sourcePoint.x][sourcePoint.y];

        final Double cost =
                craft.calculateTravelCost(source, destination, wind);
        LOG.debug("COST - from {} to {} cost {}", source, destination, cost);
        return cost;
    }
}
