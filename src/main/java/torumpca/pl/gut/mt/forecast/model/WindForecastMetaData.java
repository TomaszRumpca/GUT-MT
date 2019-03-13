package torumpca.pl.gut.mt.forecast.model;

import java.time.LocalDateTime;

/**
 * Created by Tomasz Rumpca on 2016-04-05.
 */
public class WindForecastMetaData {

    public LocalDateTime forecastStartDateTime;
    public int forecastDuration;
    public int latDataCount;//wiersze 54.000 rosnie na polnoc
    public int lonDataCount;//kolumny 18.0000 rosnie na wschod
    public double leftBottomLatCoordinate;
    public double latStep;
    public double leftBottomLonCoordinate;
    public double lonStep;

    public WindForecastMetaData(){

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


}
