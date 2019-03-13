package torumpca.pl.gut.mt.forecast.model;

import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.mt.algorithm.model.Point;
import torumpca.pl.gut.mt.algorithm.model.VectorComponents;
import torumpca.pl.gut.mt.forecast.DataNotAvailableException;

import java.text.MessageFormat;

/**
 * Created by Tomasz Rumpca on 2016-04-05.
 */
public class WindForecastModel {

    private WindForecastMetaData metaData;
    private VectorComponents[][] forecastData;

    public WindForecastModel() {

    }

    public Point getPointFromLatLon(Coordinates coordinates) throws DataNotAvailableException {
        final Point point = new Point();
        point.x = (int) ((coordinates.latitude - metaData.getLeftBottomLatCoordinate()) / metaData
                .getLatStep());
        point.y = (int) ((coordinates.longitude - metaData.getLeftBottomLonCoordinate()) / metaData
                .getLonStep());

        if (point.x < 0 || point.x > metaData.getLatDataCount() || point.y < 0 || point.y > metaData
                .getLonDataCount()) {
            throw new DataNotAvailableException(MessageFormat
                    .format("Forecast does not contain data for location {0}", coordinates));
        }
        return point;
    }

    public Coordinates getLatLonFromPoint(Point point) {
        final double latStep = metaData.getLatStep();
        final double lonStep = metaData.getLonStep();
        final Coordinates coordinates = new Coordinates();
        coordinates.latitude = metaData.getLeftBottomLatCoordinate() + (point.getX() * latStep);
        coordinates.longitude = metaData.getLeftBottomLonCoordinate() + (point.getY() * lonStep);
        return coordinates;
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
