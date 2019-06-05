package torumpca.pl.gut.mt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import torumpca.pl.gut.mt.algorithm.ProblemResolver;
import torumpca.pl.gut.mt.algorithm.model.AlgorithmInputData;
import torumpca.pl.gut.mt.forecast.DataNotAvailableException;
import torumpca.pl.gut.mt.forecast.ForecastDataAdapter;
import torumpca.pl.gut.mt.forecast.ForecastDataAdapterFactory;
import torumpca.pl.gut.mt.forecast.model.WindForecastModel;

import java.time.LocalDateTime;

/**
 * Created by Tomasz Rumpca on 2016-08-15.
 */
@RestController
@RequestMapping("api/")
public class SolutionController {

    private static Logger LOG = LoggerFactory.getLogger(SolutionController.class);

    private final ProblemResolver resolver;

    @Autowired
    public SolutionController(ProblemResolver resolver) {
        this.resolver = resolver;
    }

    @RequestMapping(value = "solve", method = RequestMethod.POST)
    public ResponseEntity solve(@RequestBody AlgorithmInputData algorithmInputData,
                                @RequestParam(required = false) boolean cachedData) {

        final ForecastDataAdapter adapter = ForecastDataAdapterFactory.getDataAdapter(cachedData);

        LocalDateTime plannedDepartureDateTime = algorithmInputData.getPlannedDepartureDateTime();
        if (plannedDepartureDateTime == null) {
            plannedDepartureDateTime = LocalDateTime.now();
            LOG.info("Departure date not provided, current date time will be used - {}", plannedDepartureDateTime);
        }

        WindForecastModel forecast;
        try {
            forecast = adapter.getWindForecast(plannedDepartureDateTime);
        } catch (DataNotAvailableException e) {
            LOG.error("Forecast for {} not available", plannedDepartureDateTime, e);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(resolver.resolve(forecast, algorithmInputData));
    }

}
