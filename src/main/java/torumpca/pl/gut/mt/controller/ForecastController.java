package torumpca.pl.gut.mt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("forecast")
public class ForecastController {

    private static final Logger LOG = LoggerFactory.getLogger(ForecastController.class);

    @RequestMapping(value = "dates", method = RequestMethod.GET)
    public List<OffsetDateTime> getForecastAvailableDates(@RequestParam(required = false) boolean cachedData) {
        return ForecastDataAdapterFactory.getDataAdapter(cachedData).getForecastAvailableDates();
    }

    @RequestMapping(value = "meta", method = RequestMethod.GET)
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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getWindForecastMetaData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer hour,
            @RequestParam(required = false) boolean cachedData) {
        final ForecastDataAdapter adapter = ForecastDataAdapterFactory.getDataAdapter(cachedData);

        final LocalTime time = hour != null ? LocalTime.of(hour, 0) : LocalTime.of(0, 0);
        try {
            final WindForecastModel windForecast = adapter.getWindForecast(LocalDateTime.of(date, time));
            return ResponseEntity.ok(windForecast);
        } catch (DataNotAvailableException e) {
            LOG.error("Forecast meta data not available", e);
            return ResponseEntity.noContent().build();
        }
    }

}
