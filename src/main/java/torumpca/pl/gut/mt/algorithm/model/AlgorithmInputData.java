package torumpca.pl.gut.mt.algorithm.model;

import torumpca.pl.gut.mt.algorithm.ship.SampleCraft;
import torumpca.pl.gut.mt.algorithm.Craft;

import java.time.LocalDateTime;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public class AlgorithmInputData {

    private Coordinates origin;
    private Coordinates destination;
    private LocalDateTime plannedDepartureDateTime;

    public Coordinates getOrigin() {
        return origin;
    }

    public void setOrigin(Coordinates origin) {
        this.origin = origin;
    }

    public Coordinates getDestination() {
        return destination;
    }

    public void setDestination(Coordinates destination) {
        this.destination = destination;
    }

    public double getOriginLat() {
        return origin.getLatitude();
    }

    public double getOriginLon() {
        return origin.getLongitude();
    }

    public double getGoalLat() {
        return destination.getLatitude();
    }

    public double getGoalLon() {
        return destination.getLongitude();
    }

    public Craft getShip() {
        return new SampleCraft();
    }

    public LocalDateTime getPlannedDepartureDateTime() {
        return plannedDepartureDateTime;
    }

    public void setPlannedDepartureDateTime(LocalDateTime plannedDepartureDateTime) {
        this.plannedDepartureDateTime = plannedDepartureDateTime;
    }
}
