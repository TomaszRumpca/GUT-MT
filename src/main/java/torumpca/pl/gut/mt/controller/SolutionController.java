package torumpca.pl.gut.mt.controller;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import torumpca.pl.gut.mt.algorithm.ProblemResolver;
import torumpca.pl.gut.mt.algorithm.model.AlgorithmInputData;
import torumpca.pl.gut.mt.algorithm.model.Error;
import torumpca.pl.gut.mt.forecast.DataNotAvailableException;
import torumpca.pl.gut.mt.forecast.ForecastDataAdapter;
import torumpca.pl.gut.mt.forecast.ForecastDataAdapterFactory;
import torumpca.pl.gut.mt.forecast.model.WindForecastModel;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by Tomasz Rumpca on 2016-08-15.
 */
@RestController
@RequestMapping("api/")
public class SolutionController {

    @Value("${forecast.zoneId:}")
    String forecastZoneIdName;

    private ZoneId forecastZoneId;

    private static Logger LOG = LoggerFactory.getLogger(SolutionController.class);

    private final ProblemResolver resolver;
    private final ForecastDataAdapterFactory adapterFactory;

    @Autowired
    public SolutionController(ProblemResolver resolver,
            ForecastDataAdapterFactory adapterFactory1) {
        this.resolver = resolver;
        this.adapterFactory = adapterFactory1;
    }

    @PostConstruct
    private void init() {
        if (Strings.isNullOrEmpty(forecastZoneIdName)) {
            forecastZoneId = ZoneId.systemDefault();
        } else {
            forecastZoneId = ZoneId.of(forecastZoneIdName);
        }
    }

    @RequestMapping(value = "solve",
                    method = RequestMethod.POST)
    public ResponseEntity solve(@RequestBody AlgorithmInputData algorithmInputData,
            @RequestParam(required = false) boolean cachedData) {

        final ForecastDataAdapter adapter = adapterFactory.getDataAdapter(cachedData);

        final ZonedDateTime plannedDepartureDateTime =
                algorithmInputData.getPlannedDepartureDateTime();

        final ZonedDateTime departureDateTime;
        if (plannedDepartureDateTime != null) {
            departureDateTime = plannedDepartureDateTime.withZoneSameInstant(forecastZoneId);
        } else {
            departureDateTime = ZonedDateTime.now(forecastZoneId);
            LOG.info("Departure date not provided, current date time will be used - {}",
                    departureDateTime);
        }

        LOG.info("Request for route from {} to {} on {}", algorithmInputData.getOrigin(),
                algorithmInputData.getDestination(), departureDateTime);

        WindForecastModel forecast;
        try {
            forecast = adapter.getWindForecast(departureDateTime);
        } catch (DataNotAvailableException e) {
            String errorMessage =
                    String.format("Forecast for %s not available", departureDateTime.toString());
            LOG.error(errorMessage, e);
            return new ResponseEntity<>(new Error(errorMessage), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(resolver.resolve(forecast, algorithmInputData));
    }

}
