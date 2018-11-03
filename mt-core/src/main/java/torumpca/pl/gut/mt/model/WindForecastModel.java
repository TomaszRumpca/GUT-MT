package torumpca.pl.gut.mt.model;

import torumpca.pl.gut.mt.error.DataNotAvailableException;

import java.text.MessageFormat;

/**
 * Created by Tomasz Rumpca on 2016-04-05.
 */
public class WindForecastModel {

    private WindForecastMetaData metaData;

    public VectorComponents[][] forecastData;

    public WindForecastModel() {

    }

    public Point getPointFromLatLon(LatLon coordinates) throws DataNotAvailableException {
        final Point point = new Point();
        point.x = (int) ((coordinates.latitude - metaData.getLeftBottomLatCoordinate()) / metaData
                .getLatStep());
        point.y = (int) ((coordinates.longitude - metaData.getLeftBottomLonCoordinate()) / metaData
                .getLonStep());

        if (point.x < 0 || point.x > metaData.getLonDataCount() || point.y < 0 || point.y > metaData
                .getLatDataCount()) {
            throw new DataNotAvailableException(MessageFormat
                    .format("Forecast does not contain data for location {0}", coordinates));
        }
        return point;
    }

    public LatLon getLatLonFromPoint(Point point) {
        final double latStep = metaData.getLatStep();
        final double lonStep = metaData.getLonStep();
        final LatLon latLon = new LatLon();
        latLon.latitude = metaData.getLeftBottomLatCoordinate() + (point.getX() * latStep);
        latLon.longitude = metaData.getLeftBottomLonCoordinate() + (point.getY() * lonStep);
        return latLon;
    }

    public WindForecastMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(WindForecastMetaData metaData) {
        this.metaData = metaData;
    }

    public VectorComponents[][] getForecastData() {
        return forecastData;
    }

    public void setForecastData(VectorComponents[][] forecastData) {
        this.forecastData = forecastData;
    }

}
