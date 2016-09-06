package torumpca.pl.gut.mt.dsm.model;

import torumpca.pl.gut.mt.ship.SampleShip;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public class UserData {

    private Coordinates originCoords = new Coordinates(48.802824d, 13.236774d);
//    52.952824, 15.386774
    private Coordinates goalCoords = new Coordinates(48.952824d, 13.386774d);

    public Coordinates getOriginCoords() {
        return originCoords;
    }

    public void setOriginCoords(Coordinates originCoords) {
        this.originCoords = originCoords;
    }

    public Coordinates getGoalCoords() {
        return goalCoords;
    }

    public void setGoalCoords(Coordinates goalCoords) {
        this.goalCoords = goalCoords;
    }

    public double getOriginLat() {
        return originCoords.getLatitude();
    }

    public double getOriginLon() {
        return originCoords.getLongitude();
    }

    public double getGoalLat() {
        return goalCoords.getLatitude();
    }

    public double getGoalLon() {
        return goalCoords.getLongitude();
    }

    public Ship getShip() {
        return new SampleShip();
    }
}
