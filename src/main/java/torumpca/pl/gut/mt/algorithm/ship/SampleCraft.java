package torumpca.pl.gut.mt.algorithm.ship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.algorithm.Craft;
import torumpca.pl.gut.mt.algorithm.Utils;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.mt.algorithm.model.VectorComponents;

/**
 * Created by Tomasz Rumpca on 2016-04-22.
 */
public class SampleCraft implements Craft {

    private static final float AVERAGE_SPEED_IN_KNOTS = 20;
    private static final float AVERAGE_COST_OF_HOUR_ON_SEA = 3600;
    private static final String DEGREES = "\u00b0";
    private static final Logger LOG = LoggerFactory.getLogger(SampleCraft.class);

    public long calculateTravelTime(double distance, VectorComponents direction,
            VectorComponents wind) {
        return 0;
    }

    public double getAverageSpeed() {
        return AVERAGE_SPEED_IN_KNOTS;
    }

    public double getAverageSpeedInMpS() {
        return Utils.knotsToMetersPerSecond(getMaxSideWindSpeed());
    }

    public double getMaxWindSpeed() {
        return 30f;
    }

    public float getMaxSideWindSpeed() {
        return 24f;
    }

    @Override
    public double calculateTravelCost(Coordinates from, Coordinates to,
            VectorComponents windComponents) {

        final double azimuth = Utils.getAzimuth(from, to);
        final double normalizedAzimuth = (azimuth + 2 * Math.PI) % (2 * Math.PI);

        final double rotationAngle = -(normalizedAzimuth - Math.PI / 2);
        final VectorComponents normalizedWindComponents =
                Utils.rotateVector(rotationAngle, windComponents);
        LOG.debug("COST - normAzimuth {}{}, rotation {}{}, norm wind components {},"
                  + " origin wind {}", Math.toDegrees(normalizedAzimuth), DEGREES,
                Math.toDegrees(rotationAngle), DEGREES, normalizedWindComponents, windComponents);

        final double distance = Utils.getGreatCircleDistance(from, to);
        return calculateTravelCost(distance, normalizedWindComponents);
    }

    /**
     * @param distance       zadany dystans do pokonania
     * @param windComponents znormalizowanie względem azymutu składowe wiatru w m/s
     * @return koszt przepłynięcia zadanego dystansu
     */
    private double calculateTravelCost(double distance, VectorComponents windComponents) {

        final double sideWind = windComponents.v;
        final double backWind = windComponents.u;
        if (windComponents.v >= getAverageSpeedInMpS()) {
            return Double.MAX_VALUE;
        }
        final double windSpeed = Math.sqrt(sideWind * sideWind + backWind * backWind);
        final double windMaxSpeedInMpS = Utils.knotsToMetersPerSecond(getMaxWindSpeed());
        if (windSpeed >= windMaxSpeedInMpS) {
            return Double.MAX_VALUE;
        }

        final double windPercentageDeps = backWind / windMaxSpeedInMpS;
        final double avgSpeedInMpS = Utils.knotsToMetersPerSecond(getAverageSpeed());
        final double travelTimeInSeconds = distance / avgSpeedInMpS;

        final double maxWindDeps = 0.3d;

        final double correctedTravelTime =
                travelTimeInSeconds * (1 + (maxWindDeps * windPercentageDeps));

        return correctedTravelTime * AVERAGE_COST_OF_HOUR_ON_SEA / 3600;
        //        return 1;
    }


    public double getAverageCostOfHourOnSea() {
        return AVERAGE_COST_OF_HOUR_ON_SEA;
    }

}
