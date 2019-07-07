package torumpca.pl.gut.mt.algorithm.ship;

import torumpca.pl.gut.mt.algorithm.Craft;
import torumpca.pl.gut.mt.algorithm.Utils;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.mt.algorithm.model.VectorComponents;

@SuppressWarnings("UnnecessaryLocalVariable")
public class NewCraft implements Craft {

    @Override
    public double getAverageSpeed() {
        return 10;
    }

    @Override
    public double getAverageSpeedInMpS() {
        return 0;
    }

    @Override
    public double getAverageCostOfHourOnSea() {
        return 0;
    }

    @Override
    public double getMaxWindSpeed() {
        return 10;
    }

    @Override
    public float getMaxSideWindSpeed() {
        return 10;
    }

    @Override
    public double calculateTravelCost(Coordinates from, Coordinates to, VectorComponents wind) {

        // kat pomiędzy kursem statku a dodatnią półosią v wskazującą północ
        final double courseToSteer = Utils.calculateInitialBearing(from, to);

        // predkość jednostki przy założeniu bezwietrznej pogody
        final double noWindSpeed = getAverageSpeed();

        // oszacowana predkosc jednostki
        final double windInfluencedSpeed =
                calculateWindInfluencedCourse(courseToSteer, noWindSpeed, wind);

        if (windInfluencedSpeed != 0) {
            // dystans do pokonania
            final double distance = Utils.getGreatCircleDistance(from, to);

            // szacowany czas podróży
            final double estimatedTripTime = distance / windInfluencedSpeed;

            return estimatedTripTime;
        } else {
            return Double.MAX_VALUE;
        }
    }

    private double calculateWindInfluencedCourse(double courseToSteer, double noWindSpeed,
            VectorComponents wind) {

        final double normalizedAzimuth = (courseToSteer + 2 * Math.PI) % (2 * Math.PI);

        // kąt obrotu dla wektora opisującego prędkość wiatru potrzebny do wyznaczenia składowych
        // wiatru względem układu współrzędnych w którym oś u wyznaczona jest przez kurs jednostki
        final double rotationAngle = -(normalizedAzimuth - Math.PI / 2);

        // skladowe prędkości wiatru względem kiedunktu w którym porusza się jednostka
        final VectorComponents normalizedWindComponents = Utils.rotateVector(rotationAngle, wind);

        final double sideWind = normalizedWindComponents.v;
        if (Math.abs(sideWind) > getMaxSideWindSpeed()) {
            return 0d;
        }

        final double backWind = normalizedWindComponents.u;
        if (Math.abs(backWind) > getMaxWindSpeed()) {
            return 0d;
        }

        final double estimatedSpeed =
                noWindSpeed + normalizedWindComponents.u / getMaxWindSpeed() * 0.2d * noWindSpeed;

        return estimatedSpeed;
    }

}
