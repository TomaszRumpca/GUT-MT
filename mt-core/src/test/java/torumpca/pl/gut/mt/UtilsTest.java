package torumpca.pl.gut.mt;

import org.junit.jupiter.api.Test;
import torumpca.pl.gut.mt.model.LatLon;
import torumpca.pl.gut.mt.model.VectorComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Tomasz Rumpca on 2016-04-24.
 */
public class UtilsTest {


    @Test
    public void testDistanceCalculations() {
//        given
        final LatLon origin = new LatLon(54d, 18d);
        final LatLon target = new LatLon(54d, 19d);

//        when
        final double distance0 = Utils.getDistance(origin,origin);
        final double distanceToTarget = Utils.getDistance(origin,target);

//        then
        assertEquals(0d, distance0, 0.000001d);
        assertEquals(65420d, distanceToTarget, 100d);
    }



    @Test
    public void testKnotsToMpS() {
//        given
        final double speedInKnots = 1;

//        when
        final double speedInMpS = Utils.knotsToMetersPerSecond(speedInKnots);

//        then
        assertEquals(0.514444D, speedInMpS, 0.000001d);
    }

        @Test
    public void testGetDirection() {
//        given
        final LatLon from = new LatLon(54.000d,18.000d);
        final LatLon toNorth = new LatLon(55.000d,18.000d);
        final LatLon toEast = new LatLon(54.000d,19.000d);
        final LatLon toSouth = new LatLon(53.000d,18.000d);
        final LatLon toWest = new LatLon(54.000d,17.000d);

//        when
        final double angleToNorth = (Math.toDegrees(Utils.getAzimuth(from,toNorth)) + 360 ) % 360;
        final double angleToEast = (Math.toDegrees(Utils.getAzimuth(from,toEast)) + 360 ) % 360;
        final double angleToSouth = (Math.toDegrees(Utils.getAzimuth(from,toSouth)) + 360 ) % 360;
        final double angleToWest = (Math.toDegrees(Utils.getAzimuth(from,toWest)) + 360 ) % 360;

//        then
        assertEquals(0, angleToNorth, 1d);
        assertEquals(90, angleToEast, 1d);
        assertEquals(180, angleToSouth, 1d);
        assertEquals(270, angleToWest, 1d);
    }


    @Test
    public void testGetDirectionRadians() {
//        given
        final LatLon from = new LatLon(54.000d,18.000d);
        final LatLon toNorth = new LatLon(55.000d,18.000d);
        final LatLon toEast = new LatLon(54.000d,19.000d);
        final LatLon toSouth = new LatLon(53.000d,18.000d);
        final LatLon toWest = new LatLon(54.000d,17.000d);

//        when
        final double angleToNorth = (Utils.getAzimuth(from,toNorth) + 2*Math.PI ) % 2*Math.PI;
        final double angleToEast = (Utils.getAzimuth(from,toEast) + 2*Math.PI ) % 2*Math.PI;
        final double angleToSouth = (Utils.getAzimuth(from,toSouth) + 2*Math.PI ) % 2*Math.PI;
        final double angleToWest = (Utils.getAzimuth(from,toWest) + 2*Math.PI ) % 2*Math.PI;

//        then
        assertEquals(0, angleToNorth, 0.1d);
//        assertEquals(Math.PI/2, angleToEast, 0.1d);
//        assertEquals(Math.PI, angleToSouth, 0.1d);
//        assertEquals(3*Math.PI/2, angleToWest, 0.1d);
    }


    @Test
    public void testRotateVector() {
        //given
        final VectorComponents vector = new VectorComponents(3, 0);
        final double theta = Math.PI / 2;

        //when
        final VectorComponents rotatedVector1 = Utils.rotateVector(theta, vector);
        final VectorComponents rotatedVector2 = Utils.rotateVector(theta, rotatedVector1);
        final VectorComponents rotatedVector3 = Utils.rotateVector(theta, rotatedVector2);
        final VectorComponents rotatedVector4 = Utils.rotateVector(theta, rotatedVector3);

        //then
        assertEquals(0, rotatedVector1.u, 0.01d);
        assertEquals(3, rotatedVector1.v, 0.01d);

        assertEquals(-3, rotatedVector2.u, 0.01d);
        assertEquals(0, rotatedVector2.v, 0.01d);

        assertEquals(0, rotatedVector3.u, 0.01d);
        assertEquals(-3, rotatedVector3.v, 0.01d);

        assertEquals(3, rotatedVector4.u, 0.01d);
        assertEquals(0, rotatedVector4.v, 0.01d);

    }

    @Test
    public void testRotateVector2() {
        //given
        final VectorComponents vector = new VectorComponents(1, 1);
        final double theta = Math.PI / 2;

        //when
        final VectorComponents rotatedVector1 = Utils.rotateVector(theta, vector);
        final VectorComponents rotatedVector2 = Utils.rotateVector(theta, rotatedVector1);
        final VectorComponents rotatedVector3 = Utils.rotateVector(theta, rotatedVector2);
        final VectorComponents rotatedVector4 = Utils.rotateVector(theta, rotatedVector3);

        //then
        assertEquals(-1, rotatedVector1.u, 0.01d);
        assertEquals(1, rotatedVector1.v, 0.01d);

        assertEquals(-1, rotatedVector2.u, 0.01d);
        assertEquals(-1, rotatedVector2.v, 0.01d);

        assertEquals(1, rotatedVector3.u, 0.01d);
        assertEquals(-1, rotatedVector3.v, 0.01d);

        assertEquals(1, rotatedVector4.u, 0.01d);
        assertEquals(1, rotatedVector4.v, 0.01d);

    }

    public void testGetAngleBetween() throws Exception {
        //given
        final VectorComponents b = new VectorComponents(1, 2);
        final VectorComponents a = new VectorComponents(1, 1);

        //when
        final double calculatedAngle = Utils.getAngleBetween(a, b);

        //then
        assertEquals(Math.PI / 2, calculatedAngle, 0.01d);
    }


}