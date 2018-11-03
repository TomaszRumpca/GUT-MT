package torumpca.pl.gut.mt.model;

/**
 * Created by Tomasz Rumpca on 2016-04-12.
 */
public interface Ship {

    double getAverageSpeed();

    double getAverageSpeedInMpS();

    double getAverageCostOfHourOnSea();

    float getMaxWindSpeed();

    float getMaxSideWindSpeed();

    /**
     *
     * @param distance distance in nautical miles (1 INM = 1852 m)
     * @param normalizedWindComponents forecast of wind components normalized according to course
     * @return calculated time
     */
    double calculateTravelCost(double distance, VectorComponents normalizedWindComponents);
}
