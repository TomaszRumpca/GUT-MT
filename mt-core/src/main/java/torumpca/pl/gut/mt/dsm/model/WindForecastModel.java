package torumpca.pl.gut.mt.dsm.model;

import java.awt.*;
import java.time.LocalDateTime;

/**
 * Created by Tomasz Rumpca on 2016-04-05.
 */
public class WindForecastModel {

    LocalDateTime forecastStartDateTime;
    int forecastDuration;
    int latDataCount;//wiersze 54.000 rosnie na polnoc
    int lonDataCount;//kolumny 18.0000 rosnie na wschod
    double leftBottomLatCoordinate;
    double latStep;
    double leftBottomLonCoordinate;
    double lonStep;

    VectorComponents[][] forecastData;

    public VectorComponents[][] getForecastData() {
        return forecastData;
    }

    public void setForecastStartDateTime(LocalDateTime forecastStartDateTime) {
        this.forecastStartDateTime = forecastStartDateTime;
    }

    public void setForecastDuration(int forecastDuration) {
        this.forecastDuration = forecastDuration;
    }

    public void setLeftBottomLatCoordinate(double leftBottomLatCoordinate) {
        this.leftBottomLatCoordinate = leftBottomLatCoordinate;
    }

    public void setLatStep(double latStep) {
        this.latStep = latStep;
    }

    public void setLeftBottomLonCoordinate(double leftBottomLonCoordinate) {
        this.leftBottomLonCoordinate = leftBottomLonCoordinate;
    }

    public void setLonStep(double lonStep) {
        this.lonStep = lonStep;
    }

    public void setLatDataCount(int latDataCount) {
        this.latDataCount = latDataCount;
    }

    public void setLonDataCount(int lonDataCount) {
        this.lonDataCount = lonDataCount;
    }

    public void setForecastData(VectorComponents[][] forecastData) {
        this.forecastData = forecastData;
    }

    public LocalDateTime getForecastStartDateTime() {
        return forecastStartDateTime;
    }

    public int getForecastDuration() {
        return forecastDuration;
    }

    public double getLeftBottomLatCoordinate() {
        return leftBottomLatCoordinate;
    }

    public double getLatStep() {
        return latStep;
    }

    public double getLeftBottomLonCoordinate() {
        return leftBottomLonCoordinate;
    }

    public double getLonStep() {
        return lonStep;
    }

    public int getLatDataCount() {
        return latDataCount;
    }

    public int getLonDataCount() {
        return lonDataCount;
    }

    public WindForecastModel(){

    }

    public WindForecastModel(LocalDateTime forecastStartDateTime, int forecastDuration,
            double leftBottomLatCoordinate, double latStep, double leftBottomLonCoordinate,
            double lonStep, int latDataCount, int   lonDataCount) {
        this.forecastStartDateTime = forecastStartDateTime;
        this.forecastDuration = forecastDuration;
        this.leftBottomLatCoordinate = leftBottomLatCoordinate;
        this.latStep = latStep;
        this.leftBottomLonCoordinate = leftBottomLonCoordinate;
        this.lonStep = lonStep;
        this.latDataCount = latDataCount;
        this.lonDataCount = lonDataCount;
    }

    public Point getPointFromLatLon(LatLon coordinates) {
        final Point point = new Point();
        point.x = (int)((coordinates.latitude - getLeftBottomLatCoordinate())
                / getLatStep());
        point.y = (int)((coordinates.longitude - getLeftBottomLonCoordinate())
                / getLonStep());
        return point;
    }

    public LatLon getLatLonFromPoint(Point point){
        final double latStep = getLatStep();
        final double lonStep = getLonStep();
        final LatLon latLon = new LatLon();
        latLon.latitude = getLeftBottomLatCoordinate() + (point.getX() * latStep);
        latLon.longitude = getLeftBottomLonCoordinate() + (point.getY() * lonStep);
        return latLon;
    }


}
