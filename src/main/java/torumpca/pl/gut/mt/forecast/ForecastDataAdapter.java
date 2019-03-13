package torumpca.pl.gut.mt.forecast;

import torumpca.pl.gut.mt.forecast.model.WindForecastMetaData;
import torumpca.pl.gut.mt.forecast.model.WindForecastModel;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public interface ForecastDataAdapter {

    WindForecastModel getWindForecast(LocalDateTime dateTime) throws DataNotAvailableException;

    WindForecastMetaData getWindForecastMetaData(Integer year) throws DataNotAvailableException;

    List<OffsetDateTime> getForecastAvailableDates();

}
