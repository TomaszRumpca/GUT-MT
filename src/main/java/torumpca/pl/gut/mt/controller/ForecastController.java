package torumpca.pl.gut.mt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import torumpca.pl.gut.mt.data.ForecastDataAdapter;
import torumpca.pl.gut.mt.data.ForecastDataAdapterFactory;
import torumpca.pl.gut.mt.error.DataNotAvailableException;
import torumpca.pl.gut.mt.model.WindForecastMetaData;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("forecast")
public class ForecastController {

    private static final Logger LOG = LoggerFactory.getLogger(ForecastController.class);

    @RequestMapping(value = "/meta", method = RequestMethod.GET)
    public WindForecastMetaData getWindForecastMetaData(@PathVariable(name = "year", required = false) Integer year) {

        WindForecastMetaData forecastMetaData = null;

        final ForecastDataAdapter adapter = ForecastDataAdapterFactory.getDataAdapter();

        try {
            forecastMetaData = adapter.getWindForecastMetaData(year);
        } catch (DataNotAvailableException e) {
            LOG.error("Forecast meta data not available", e);
        }

        return forecastMetaData;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<OffsetDateTime> getForecastAvailableDates(){
        return ForecastDataAdapterFactory.getDataAdapter().getForecastAvailableDates();
    }


}
