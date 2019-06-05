package torumpca.pl.gut.mt.algorithm;

import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.mt.algorithm.model.VectorComponents;

/**
 * Created by Tomasz Rumpca on 2016-04-12.
 */
public interface Craft {

    double getAverageSpeed();

    double getAverageSpeedInMpS();

    double getAverageCostOfHourOnSea();

    double getMaxWindSpeed();

    float getMaxSideWindSpeed();

    double calculateTravelCost(Coordinates from, Coordinates to, VectorComponents wind);
}
