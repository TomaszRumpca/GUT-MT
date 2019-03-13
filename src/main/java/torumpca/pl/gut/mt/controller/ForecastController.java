package torumpca.pl.gut.mt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import torumpca.pl.gut.mt.forecast.DataNotAvailableException;
import torumpca.pl.gut.mt.forecast.ForecastDataAdapter;
import torumpca.pl.gut.mt.forecast.ForecastDataAdapterFactory;
import torumpca.pl.gut.mt.forecast.model.WindForecastMetaData;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("forecast")
public class ForecastController {

    private static final Logger LOG = LoggerFactory.getLogger(ForecastController.class);

    @RequestMapping(method = RequestMethod.GET)
    public List<OffsetDateTime> getForecastAvailableDates(@RequestParam(required = false) boolean cachedData) {
        return ForecastDataAdapterFactory.getDataAdapter(cachedData).getForecastAvailableDates();
    }

    @RequestMapping(value = "/meta", method = RequestMethod.GET)
    public WindForecastMetaData getWindForecastMetaData(@RequestParam(required = false) Integer year,
                                                        @RequestParam(required = false) boolean cachedData) {
        WindForecastMetaData forecastMetaData = null;

        final ForecastDataAdapter adapter = ForecastDataAdapterFactory.getDataAdapter(cachedData);

        try {
            forecastMetaData = adapter.getWindForecastMetaData(year);
        } catch (DataNotAvailableException e) {
            LOG.error("Forecast meta data not available", e);
        }

        return forecastMetaData;
    }


}
