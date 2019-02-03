package torumpca.pl.gut.mt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import torumpca.pl.gut.mt.alg.ProblemResolver;
import torumpca.pl.gut.mt.alg.ResolverFactory;
import torumpca.pl.gut.mt.data.ForecastDataAdapter;
import torumpca.pl.gut.mt.data.ForecastDataAdapterFactory;
import torumpca.pl.gut.mt.error.DataNotAvailableException;
import torumpca.pl.gut.mt.model.Solution;
import torumpca.pl.gut.mt.model.UserData;
import torumpca.pl.gut.mt.model.WindForecastModel;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by Tomasz Rumpca on 2016-08-15.
 */
@RestController
@RequestMapping("api/")
public class SolutionController {

    private static Logger LOG = LoggerFactory.getLogger(SolutionController.class);

    @RequestMapping(value = "solve", method = RequestMethod.POST)
    public Solution solve(@RequestBody String userInput) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        final UserData userData = mapper.readValue(userInput, UserData.class);

        final ForecastDataAdapter adapter = ForecastDataAdapterFactory.getDataAdapter();
        WindForecastModel forecast = null;
        try {
            forecast = adapter.getWindForecast(LocalDateTime.now());
//            forecast = adapter.getWindForecast(userInput.getPlannedDepartureDateTime());
        } catch (DataNotAvailableException e) {
            LOG.error("Forecast not available.", e);
        }

        final ProblemResolver resolver = ResolverFactory.getResolver();

        return resolver.resolve(forecast, userData);
    }

}
