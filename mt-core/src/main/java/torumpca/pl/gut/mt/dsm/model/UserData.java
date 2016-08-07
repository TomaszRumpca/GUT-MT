package torumpca.pl.gut.mt.dsm.model;

import torumpca.pl.gut.mt.ship.SampleShip;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public class UserData {
    //TODO implement all methods
    public double getOriginLat() {
        return 48.802824;
    }

    public double getGoalLat() {
        return 48.952824;
//        return 52.952824;
    }

    public double getGoalLon() {
        return 13.386774;
//        return 15.386774;
    }

    public double getOriginLon() {
        return 13.236774;
    }

    public Ship getShip() {
        return new SampleShip();
    }
}
