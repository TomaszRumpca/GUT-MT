package torumpca.pl.gut.mt.algorithm;

import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.mt.algorithm.model.VectorComponents;

public class Utils {

    /**
     * Wyznacza azymut z punktu 'from' do celu w punkcie 'to'
     *
     * @param from lokalizacja punktu startowego
     * @param to   lokalizacja punktu docelowego
     * @return wyznaczony azymut - kąt zawarty między północną a danym kierunkiem poziomym
     */
    public static double getAzimuth(Coordinates from, Coordinates to) {

        final double fromLatRad = Math.toRadians(from.latitude);
        final double fromLonRad = Math.toRadians(from.longitude);
        final double toLatRad = Math.toRadians(to.latitude);
        final double toLonRad = Math.toRadians(to.longitude);

        final double y = Math.sin(toLonRad - fromLonRad) * Math.cos(toLatRad);
        final double x =
                Math.cos(fromLatRad) * Math.sin(toLatRad) - Math.sin(fromLatRad) * Math.cos(
                        toLatRad) * Math.cos(toLonRad - fromLonRad);
        return Math.atan2(y, x);
    }

    /**
     * Obraca wektor wejściowy o kąt teta przeciwnie do kierunku wskazówek zegara
     *
     * @param theta  kąt obrotu w radianach
     * @param vector wektor wejściowy
     * @return wektor obrócony o zadany kąt teta
     */
    public static VectorComponents rotateVector(double theta, VectorComponents vector) {
        double x = vector.u * Math.cos(theta) - vector.v * Math.sin(theta);
        double y = vector.u * Math.sin(theta) + vector.v * Math.cos(theta);
        return new VectorComponents(x, y);
    }


    public static double getAngleBetween(VectorComponents a, VectorComponents b) {
        return Math.acos((a.u * b.u + a.v * b.v) / (Math.sqrt(a.u * a.v) + Math.sqrt(b.u * b.v)));
    }

    //TODO make metersInKnot configurable
    public static double knotsToMetersPerSecond(double speedInKnots) {
        return speedInKnots * 1852d / 3600d;
    }


    //TODO make earth radious configurable
    public static double getGreatCircleDistance(Coordinates sourceLocation,
            Coordinates destLocation) {

        final double earthR = 6371000; // metres

        final double fromLatRad = Math.toRadians(sourceLocation.latitude);
        final double fromLonRad = Math.toRadians(sourceLocation.longitude);
        final double toLatRad = Math.toRadians(destLocation.latitude);
        final double toLonRad = Math.toRadians(destLocation.longitude);

        final double deltaLat = toLatRad - fromLatRad;
        final double deltaLon = toLonRad - fromLonRad;

        double a =
                Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.cos(fromLatRad) * Math.cos(
                        toLatRad) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthR * c;
    }

    public static double getVectorLength(VectorComponents vectorComponents) {
        return Math.sqrt(Math.pow(vectorComponents.u, 2) + Math.pow(vectorComponents.v, 2));
    }

    public static VectorComponents breakdownVectorComponents(double angleInRadians,
            double vectorLength) {
        final double u = vectorLength * Math.sin(angleInRadians);
        final double v = vectorLength * Math.cos(angleInRadians);
        return new VectorComponents(u, v);
    }

    /**
     * Wyznacza kurs od zadanego punktu początkowego do punktu docelowego w systemie okrężnym.
     * <p>
     * Metoda nie uwzględnia dryfu (kąta o jaki wiatr spycha jednostkę z wyznaczonego kursu) ani
     * znosu spowodowanego prądami (kąta o jaki prąd znosi jednostkę z wyznaczonego kursu).
     *
     * @param origin      punkt początkowy
     * @param destination punkt docelowy
     * @return kąt pomiędzy kierunkiem północy (rzeczywistej) a diametralną jednostki płynącej po
     * ortodromie z punktu początkowego do docelowego w radianach
     */
    public static double calculateCourseToSteer(Coordinates origin, Coordinates destination) {

        // różnica szerokości geograficznej
        final double rPhi = destination.latitude - origin.latitude;

        // różnica długości geograficznej
        final double rLamda = destination.longitude - origin.longitude;

        final double avgPhi = (origin.latitude + destination.latitude) / 2;

        // zboczrnie nawigacyjne
        final double a = rLamda * Math.cos(avgPhi);

        final double courseInRadians;
        // dla katow z zakrespu [ - Pi/2 , Pi/2 ]
        if (destination.latitude >= origin.latitude) {
            courseInRadians = Math.atan(a / rPhi);
        }
        // dla katow z zakresy ( Pi/2, 3Pi/2 )
        else {
            courseInRadians = Math.atan(a / rPhi) + Math.PI;
        }

        return courseInRadians;
    }

    /**
     * Wyznacza kurs od zadanego punktu początkowego do punktu docelowego w systemie okrężnym.
     * <p>
     * Metoda nie uwzględnia dryfu (kąta o jaki wiatr spycha jednostkę z wyznaczonego kursu) ani
     * znosu spowodowanego prądami (kąta o jaki prąd znosi jednostkę z wyznaczonego kursu).
     *
     * @param origin      punkt początkowy
     * @param destination punkt docelowy
     * @return kąt pomiędzy kierunkiem północy (rzeczywistej) a diametralną jednostki płynącej po
     * ortodromie z punktu początkowego do docelowego w radianach
     */
    public static double calculateInitialBearing(Coordinates origin, Coordinates destination) {
        //todo fix it
        double y = Math.sin(destination.longitude - destination.latitude) * Math.cos(
                destination.latitude);
        double x = Math.cos(origin.latitude) * Math.sin(destination.latitude)
                   - Math.sin(origin.latitude) * Math.cos(destination.latitude) * Math.cos(
                destination.longitude - destination.latitude);
        return Math.atan2(y, x);
    }

    /**
     * konwersja współrzędnych geograficznych na format dziesiętny
     *
     * @param degrees stopnie
     * @param minutes minuty
     * @param seconds sekundy
     * @return współrzędna w formacie dziesiętnym
     */
    public static double convert(double degrees, double minutes, double seconds) {
        return (seconds / 3600) + (minutes / 60) + degrees;
    }

}
