package torumpca.pl.gut.mt.model;

import torumpca.pl.gut.mt.ship.SampleShip;

import java.time.LocalDateTime;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public class UserData {

    private Coordinates originCoordinates = new Coordinates(48.802824d, 13.236774d);
    private Coordinates goalCoordinates = new Coordinates(48.952824d, 13.386774d);
    private LocalDateTime plannedDepartureDateTime;


    public Coordinates getOriginCoordinates() {
        return originCoordinates;
    }

    public void setOriginCoordinates(Coordinates originCoordinates) {
        this.originCoordinates = originCoordinates;
    }

    public Coordinates getGoalCoordinates() {
        return goalCoordinates;
    }

    public void setGoalCoordinates(Coordinates goalCoordinates) {
        this.goalCoordinates = goalCoordinates;
    }

    public double getOriginLat() {
        return originCoordinates.getLatitude();
    }

    public double getOriginLon() {
        return originCoordinates.getLongitude();
    }

    public double getGoalLat() {
        return goalCoordinates.getLatitude();
    }

    public double getGoalLon() {
        return goalCoordinates.getLongitude();
    }

    public Ship getShip() {
        return new SampleShip();
    }

    public LocalDateTime getPlannedDepartureDateTime() {
        return plannedDepartureDateTime;
    }

    public void setPlannedDepartureDateTime(LocalDateTime plannedDepartureDateTime) {
        this.plannedDepartureDateTime = plannedDepartureDateTime;
    }
}
