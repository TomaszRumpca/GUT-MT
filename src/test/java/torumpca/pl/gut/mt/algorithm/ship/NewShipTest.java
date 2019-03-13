package torumpca.pl.gut.mt.algorithm.ship;

import org.junit.jupiter.api.Test;
import torumpca.pl.gut.mt.algorithm.Utils;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.mt.algorithm.model.VectorComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class NewShipTest {

    @Test
    void calculateTravelCostWithNoWind() {
        final NewShip newShip = new NewShip();

        // 54.5944444, 16.8538888
        final double ustkaLatitude = 54.5944444d;
        final double ustkaLongitude = 16.8538888d;

        final Coordinates origin = new Coordinates(ustkaLatitude, ustkaLongitude);

        //  54.63228889458919, 16.89173329458919
        final Coordinates destination = new Coordinates(ustkaLatitude + 0.0378444945891919d, ustkaLongitude + 0.0378444945891919);

        final VectorComponents noWind = new VectorComponents(0, 0);

        // calculated great circle distance is 4,863 km
        // https://www.movable-type.co.uk/scripts/latlong.html
        final double expectedGreatCircleDistance = 4863;
        // 030° 03′ 34″
        final double expectedInitialBearing = Utils.convert(30, 3, 34);

        final double expectedTravelCost = expectedGreatCircleDistance / newShip.getAverageSpeed();

        final double travelCost = newShip.calculateTravelCost(origin, destination, noWind);

        assertEquals(expectedTravelCost, travelCost, 0.1);
    }

    @Test
    void calculateTravelCostWithFordewind() {
        final NewShip newShip = new NewShip();

        // 54.5944444, 16.8538888
        final double ustkaLatitude = 54.5944444d;
        final double ustkaLongitude = 16.8538888d;

        final Coordinates origin = new Coordinates(ustkaLatitude, ustkaLongitude);

        //  54.63228889458919, 16.89173329458919
        final Coordinates destination = new Coordinates(ustkaLatitude + 0.0378444945891919d, ustkaLongitude + 0.0378444945891919);

        final VectorComponents windNE = new VectorComponents(5, 3);

        // calculated great circle distance is 4,863 km
        // https://www.movable-type.co.uk/scripts/latlong.html
        final double expectedGreatCircleDistance = 4863;

        final double noWindTravelCost = expectedGreatCircleDistance / newShip.getAverageSpeed();

        final double travelCost = newShip.calculateTravelCost(origin, destination, windNE);

        assertTrue(noWindTravelCost > travelCost);
    }
}