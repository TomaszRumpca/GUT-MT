package torumpca.pl.gut.mt.algorithm;

import org.junit.jupiter.api.Test;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.mt.algorithm.model.VectorComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    @Test
    void convert() {
        final double nDegrees = 50;
        final double nMinutes = 4;
        final double nSeconds = 10.77;
        final double eDegrees = 19;
        final double eMinutes = 47;
        final double eSeconds = 57.31;

        final double n = Utils.convert(nDegrees, nMinutes, nSeconds);
        final double e = Utils.convert(eDegrees, eMinutes, eSeconds);

        final double expectedN = 50.06966;
        final double expectedE = 19.79925;

        assertEquals(expectedN, n, 0.00001);
        assertEquals(expectedE, e, 0.00001);
    }

    @Test
    void testCourseToWest() {
        final Coordinates origin = new Coordinates(50.06966, 19.79925);
        final Coordinates destination = new Coordinates(50.06966, 18.79925);

        final double expectedCourseInRadians = -1 * Math.PI / 2;

        final double computedCourse = Utils.calculateCourseToSteer(origin, destination);

        assertEquals(expectedCourseInRadians, computedCourse, 0.0001);
    }

    @Test
    void compareCalculationOfCourseToSteerAndAzimuth() {

        final Coordinates origin = new Coordinates(50.06966, 19.79925);
        final Coordinates destination = new Coordinates(50.06966, 18.79925);

        final double computedCourse = Utils.calculateCourseToSteer(origin, destination);

        final double computedAzimuth = Utils.getAzimuth(origin, destination);

        assertEquals(computedAzimuth, computedCourse, 0.01);
    }

    @Test
    void testCourseToNorth() {
        final Coordinates origin = new Coordinates(50.06966, 19.79925);
        final Coordinates destination = new Coordinates(51.06966, 19.79925);

        final double expectedCourseInRadians = 0;

        final double computedCourse = Utils.calculateCourseToSteer(origin, destination);

        assertEquals(expectedCourseInRadians, computedCourse, 0.0001);
    }

    @Test
    void testCourseToNorthWest() {
        final Coordinates origin = new Coordinates(50.06966, 19.79925001);
        final Coordinates destination = new Coordinates(50.06966001, 19.79925);

        final double expectedCourseInRadians = -1 * Math.PI / 4;

        final double computedCourse = Utils.calculateCourseToSteer(origin, destination);

        assertEquals(expectedCourseInRadians, computedCourse, 0.01);
    }

    @Test
    void testCourseToNorthEast() {
        final Coordinates origin = new Coordinates(50.06966, 19.79925);
        final Coordinates destination = new Coordinates(50.06966001, 19.79925001);

        final double expectedCourseInRadians = Math.PI / 4;

        final double computedCourse = Utils.calculateCourseToSteer(origin, destination);

        assertEquals(expectedCourseInRadians, computedCourse, 0.01);
    }

    @Test
    void testCourseToSouth() {
        final Coordinates origin = new Coordinates(50.06966, 19.79925);
        final Coordinates destination = new Coordinates(49.06966, 19.79925);

        final double expectedCourseInRadians = Math.PI;

        final double computedCourse = Utils.calculateCourseToSteer(origin, destination);

        assertEquals(expectedCourseInRadians, computedCourse, 0.0001);
    }


    @Test
    void testCourseToSouthEast() {
        final Coordinates origin = new Coordinates(50.06966001, 19.79925);
        final Coordinates destination = new Coordinates(50.06966, 19.79925001);

        final double expectedCourseInRadians = 3 * Math.PI / 4;

        final double computedCourse = Utils.calculateCourseToSteer(origin, destination);

        assertEquals(expectedCourseInRadians, computedCourse, 0.01);
    }


    @Test
    void testCourseToSouthWest() {
        final Coordinates origin = new Coordinates(50.06966001, 19.79925001);
        final Coordinates destination = new Coordinates(50.06966, 19.79925);

        final double expectedCourseInRadians = 5 * Math.PI / 4;

        final double computedCourse = Utils.calculateCourseToSteer(origin, destination);

        assertEquals(expectedCourseInRadians, computedCourse, 0.01);
    }


    @Test
    void testCourseToEast() {
        final Coordinates origin = new Coordinates(50.06966, 18.79925);
        final Coordinates destination = new Coordinates(50.06966, 19.79925);

        final double expectedCourseInRadians = Math.PI / 2;

        final double computedCourse = Utils.calculateCourseToSteer(origin, destination);

        assertEquals(expectedCourseInRadians, computedCourse, 0.0001);
    }

    @Test
    void testCourse() {

        // 54.5944444, 16.8538888
        final double ustkaLatitude = 54.5944444d;
        final double ustkaLongitude = 16.8538888d;

        final Coordinates origin = new Coordinates(ustkaLatitude, ustkaLongitude);

        //  54.63228889458919, 16.89173329458919
        final Coordinates destination = new Coordinates(ustkaLatitude + 0.0378444945891919d, ustkaLongitude + 0.0378444945891919);

        // https://www.movable-type.co.uk/scripts/latlong.html
        // 030° 03′ 34″
        final double expectedInitialBearingInRadians = Math.toRadians(Utils.convert(30, 3, 34));

        final double computedCourse = Utils.calculateInitialBearing(origin, destination);

        assertEquals(expectedInitialBearingInRadians, computedCourse, 0.0001);
    }

    @Test
    void testComputingVectorComponents() {

        final VectorComponents vectorComponents = Utils.breakdownVectorComponents(Math.PI / 4, 10);

        final double expectedU = Math.sqrt(50);
        final double expectedV = Math.sqrt(50);

        assertEquals(expectedU, vectorComponents.u, 0.00001);
        assertEquals(expectedV, vectorComponents.v, 0.00001);
    }

    @Test
    void testComputingVectorComponents120degrees() {

        final VectorComponents vectorComponents = Utils.breakdownVectorComponents(2 * Math.PI / 3, 10);

        final double expectedU = 5 * Math.sqrt(3);
        final double expectedV = -5;

        assertEquals(expectedU, vectorComponents.u, 0.00001);
        assertEquals(expectedV, vectorComponents.v, 0.00001);
    }


    @Test
    void testComputingVectorComponents210degrees() {

        final VectorComponents vectorComponents = Utils.breakdownVectorComponents(11 * Math.PI / 6, 10);

        final double expectedU = -5;
        final double expectedV = 5 * Math.sqrt(3);

        assertEquals(expectedU, vectorComponents.u, 0.00001);
        assertEquals(expectedV, vectorComponents.v, 0.00001);
    }

    @Test
    public void testDistanceCalculations() {
//        given
        final Coordinates origin = new Coordinates(54d, 18d);
        final Coordinates target = new Coordinates(54d, 19d);

//        when
        final double distance0 = Utils.getGreatCircleDistance(origin, origin);
        final double distanceToTarget = Utils.getGreatCircleDistance(origin, target);

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
        final Coordinates from = new Coordinates(54.000d, 18.000d);
        final Coordinates toNorth = new Coordinates(55.000d, 18.000d);
        final Coordinates toEast = new Coordinates(54.000d, 19.000d);
        final Coordinates toSouth = new Coordinates(53.000d, 18.000d);
        final Coordinates toWest = new Coordinates(54.000d, 17.000d);

//        when
        final double angleToNorth = (Math.toDegrees(Utils.getAzimuth(from, toNorth)) + 360) % 360;
        final double angleToEast = (Math.toDegrees(Utils.getAzimuth(from, toEast)) + 360) % 360;
        final double angleToSouth = (Math.toDegrees(Utils.getAzimuth(from, toSouth)) + 360) % 360;
        final double angleToWest = (Math.toDegrees(Utils.getAzimuth(from, toWest)) + 360) % 360;

//        then
        assertEquals(0, angleToNorth, 1d);
        assertEquals(90, angleToEast, 1d);
        assertEquals(180, angleToSouth, 1d);
        assertEquals(270, angleToWest, 1d);
    }


//    @Test
//    public void testGetDirectionRadians() {
//        given
//        final LatLon from = new Coordinates(54.000d,18.000d);
//        final LatLon toNorth = new Coordinates(55.000d,18.000d);
//        final LatLon toEast = new Coordinates(54.000d,19.000d);
//        final LatLon toSouth = new Coordinates(53.000d,18.000d);
//        final LatLon toWest = new Coordinates(54.000d,17.000d);

//        when
//        final double angleToNorth = (Utils.getAzimuth(from,toNorth) + 2*Math.PI ) % 2*Math.PI;
//        final double angleToEast = (Utils.getAzimuth(from,toEast) + 2*Math.PI ) % 2*Math.PI;
//        final double angleToSouth = (Utils.getAzimuth(from,toSouth) + 2*Math.PI ) % 2*Math.PI;
//        final double angleToWest = (Utils.getAzimuth(from,toWest) + 2*Math.PI ) % 2*Math.PI;

//        then
//        assertEquals(0, angleToNorth, 0.1d);
//        assertEquals(Math.PI/2, angleToEast, 0.1d);
//        assertEquals(Math.PI, angleToSouth, 0.1d);
//        assertEquals(3*Math.PI/2, angleToWest, 0.1d);
//    }


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

//    public void testGetAngleBetween() throws Exception {
//        given
//        final VectorComponents b = new VectorComponents(1, 2);
//        final VectorComponents a = new VectorComponents(1, 1);
//
    //when
//        final double calculatedAngle = Utils.getAngleBetween(a, b);

    //then
//        assertEquals(Math.PI / 2, calculatedAngle, 0.01d);
//    }


}