package torumpca.pl.gut.mt.ship;

import torumpca.pl.gut.mt.Util;
import torumpca.pl.gut.mt.dsm.model.Ship;
import torumpca.pl.gut.mt.dsm.model.VectorComponents;

/**
 * Created by Tomasz Rumpca on 2016-04-22.
 */
public class SampleShip implements Ship {

    private static final float AVERAGE_SPEED_IN_KNOTS = 20;
    private static final float AVERAGE_COST_OF_HOUR_ON_SEA = 3600;

    public long calculateTravelTime(double distance, VectorComponents direction,
            VectorComponents wind) {
        return 0;
    }

    public double getAverageSpeed() {
        return AVERAGE_SPEED_IN_KNOTS;
    }

    public double getAverageSpeedInMpS(){
        return Util.knotsToMetersPerSecond(getMaxSideWindSpeed());
    }
    public float getMaxWindSpeed() {
        return 30f;
    }

    public float getMaxSideWindSpeed() {
        return 24f;
    }

    /**
     *
     * @param distance zadany dystans do pokonania
     * @param normalizedWindComponents znormalizowanie względem azymutu składowe wiatru w m/s
     * @return koszt przepłynięcia zadanego dystansu
     */
    public double calculateTravelCost(double distance, VectorComponents normalizedWindComponents) {

        final double sideWind = normalizedWindComponents.v;
        final double backWind = normalizedWindComponents.u;
        if(normalizedWindComponents.v >= getAverageSpeedInMpS()){
            return Double.MAX_VALUE;
        }
        final double windSpeed = Math.sqrt(sideWind*sideWind + backWind*backWind);
        final double windMaxSpeedInMpS = Util.knotsToMetersPerSecond(getMaxWindSpeed());
        if (windSpeed >= windMaxSpeedInMpS){
            return Double.MAX_VALUE;
        }

        final double windPercentageDeps = backWind/windMaxSpeedInMpS;
        final double avgSpeedInMpS = Util.knotsToMetersPerSecond(getAverageSpeed());
        final double travelTimeInSeconds = distance / avgSpeedInMpS;

        final double maxWindDeps = 0.3d;

        final double correctedTravelTime = travelTimeInSeconds * (1 + (maxWindDeps * windPercentageDeps));

        return correctedTravelTime * AVERAGE_COST_OF_HOUR_ON_SEA / 3600;
    }


    public double getAverageCostOfHourOnSea(){
        return AVERAGE_COST_OF_HOUR_ON_SEA;
    }

}
