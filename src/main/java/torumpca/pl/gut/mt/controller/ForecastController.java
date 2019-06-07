package torumpca.pl.gut.mt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import torumpca.pl.gut.mt.forecast.DataNotAvailableException;
import torumpca.pl.gut.mt.forecast.ForecastDataAdapter;
import torumpca.pl.gut.mt.forecast.ForecastDataAdapterFactory;
import torumpca.pl.gut.mt.forecast.model.WindForecastMetaData;
import torumpca.pl.gut.mt.forecast.model.WindForecastModel;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("forecast")
public class ForecastController {

    private static final Logger LOG = LoggerFactory.getLogger(ForecastController.class);

    private final ForecastDataAdapterFactory adapterFactory;

    @Autowired
    public ForecastController(ForecastDataAdapterFactory adapterFactory) {
        this.adapterFactory = adapterFactory;
    }

    @RequestMapping(value = "dates", method = RequestMethod.GET)
    public List<OffsetDateTime> getForecastAvailableDates(@RequestParam(required = false) boolean cachedData) {
        return adapterFactory.getDataAdapter(cachedData).getForecastAvailableDates();
    }

    @RequestMapping(value = "meta", method = RequestMethod.GET)
    public WindForecastMetaData getWindForecastMetaData(@RequestParam(required = false) Integer year,
                                                        @RequestParam(required = false) boolean cachedData) {
        WindForecastMetaData forecastMetaData = null;

        final ForecastDataAdapter adapter = adapterFactory.getDataAdapter(cachedData);

        try {
            forecastMetaData = adapter.getWindForecastMetaData(year);
        } catch (DataNotAvailableException e) {
            LOG.error("Forecast meta data not available", e);
        }

        return forecastMetaData;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getWindForecastMetaData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ZonedDateTime utcDate,
            @RequestParam(required = false) Integer hour,
            @RequestParam(required = false) boolean cachedData) {
        final ForecastDataAdapter adapter = adapterFactory.getDataAdapter(cachedData);

        try {
            final WindForecastModel windForecast = adapter.getWindForecast(utcDate);
            return ResponseEntity.ok(windForecast);
        } catch (DataNotAvailableException e) {
            LOG.error("Forecast meta data not available", e);
            return ResponseEntity.noContent().build();
        }
    }

}
