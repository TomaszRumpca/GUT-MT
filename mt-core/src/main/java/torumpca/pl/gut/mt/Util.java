package torumpca.pl.gut.mt;

import torumpca.pl.gut.mt.dsm.model.LatLon;
import torumpca.pl.gut.mt.dsm.model.VectorComponents;

/**
 * Created by Tomasz Rumpca on 2016-04-23.
 */
public class Util {

    /**
     * Wyznacza azymut z punktu 'from' do celu w punkcie 'to'
     * @param from lokalizacja punktu startowego
     * @param to lokalizacja punktu docelowego
     * @return wyznaczony azymut - kąt zawarty między północną a danym kierunkiem poziomym
     */
    public static double getAzimuth(LatLon from, LatLon to){

        final double fromLatRad = Math.toRadians(from.latitude);
        final double fromLonRad = Math.toRadians(from.longitude);
        final double toLatRad = Math.toRadians(to.latitude);
        final double toLonRad = Math.toRadians(to.longitude);

        final double y = Math.sin(toLonRad-fromLonRad) * Math.cos(toLatRad);
        final double x = Math.cos(fromLatRad)*Math.sin(toLatRad) -
                Math.sin(fromLatRad)*Math.cos(toLatRad)*Math.cos(toLonRad-fromLonRad);
        return Math.atan2(y, x);
    }

    /**
     * Obraca wektor wejściowy o kąt teta przeciwnie do kierunku wskazówek zegara
     * @param theta kąt obrotu w radianach
     * @param vector wektor wejściowy
     * @return wektor obrócony o zadany kąt teta
     */
    public static VectorComponents rotateVector(double theta, VectorComponents vector){
        double x = vector.u * Math.cos(theta) - vector.v * Math.sin(theta);
        double y = vector.u * Math.sin(theta) + vector.v * Math.cos(theta);
        return new VectorComponents(x,y);
    }


    public static double getAngleBetween(VectorComponents a, VectorComponents b){
        return Math.acos((a.u*b.u + a.v * b.v)/(Math.sqrt(a.u*a.v) + Math.sqrt(b.u*b.v)));
    }

    //TODO make metersInKnot configurable
    public static double knotsToMetersPerSecond(double speedInKnots){
        return speedInKnots*1852d/3600d;
    }


    //TODO make earth radious configurable
    public static double getDistance(LatLon sourceLocation, LatLon destLocation) {

        final double earthR = 6371000; // metres

        final double fromLatRad = Math.toRadians(sourceLocation.latitude);
        final double fromLonRad = Math.toRadians(sourceLocation.longitude);
        final double toLatRad = Math.toRadians(destLocation.latitude);
        final double toLonRad = Math.toRadians(destLocation.longitude);

        final double deltaLat = toLatRad-fromLatRad;
        final double deltaLon = toLonRad-fromLonRad;

        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                Math.cos(fromLatRad) * Math.cos(toLatRad) *
                        Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthR * c;
    }
}
