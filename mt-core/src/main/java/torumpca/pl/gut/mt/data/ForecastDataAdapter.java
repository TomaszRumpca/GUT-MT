package torumpca.pl.gut.mt.data;

import torumpca.pl.gut.mt.dsm.model.WindForecastMetaData;
import torumpca.pl.gut.mt.dsm.model.WindForecastModel;
import torumpca.pl.gut.mt.error.DataNotAvailableException;

import java.time.LocalDateTime;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public interface ForecastDataAdapter {

    WindForecastModel getWindForecast(LocalDateTime dateTime) throws DataNotAvailableException;

    WindForecastMetaData getWindForecastMetaData() throws DataNotAvailableException;
}
