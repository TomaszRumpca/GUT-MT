package torumpca.pl.gut.mt.controller;

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
import torumpca.pl.gut.mt.model.Solution;
import torumpca.pl.gut.mt.model.UserData;
import torumpca.pl.gut.mt.model.WindForecastMetaData;
import torumpca.pl.gut.mt.model.WindForecastModel;
import torumpca.pl.gut.mt.error.DataNotAvailableException;

/**
 * Created by Tomasz Rumpca on 2016-08-15.
 */
@RestController
@RequestMapping("api/")
public class SolutionController {

    public static final String KSG_MET_BASE_URL = "http://ksgmet.eti.pg.gda.pl/prognozy/CSV/poland";
    public static final String META_DATA_FILE_NAME = "current.nfo";
    private static Logger LOG = LoggerFactory.getLogger(SolutionController.class);

    @RequestMapping(value = "solve", method = RequestMethod.POST)
    public Solution solve(@RequestBody UserData userInput) {
        final ForecastDataAdapter adapter = ForecastDataAdapterFactory.getDataAdapter();
        WindForecastModel dsm = null;
        try {
//            dsm = adapter.getWindForecast(LocalDateTime.now());
            dsm = adapter.getWindForecast(userInput.getPlannedDepartureDateTime());
        } catch (DataNotAvailableException e) {
            LOG.error("Forecast not available.", e);
        }

        final ProblemResolver resolver = ResolverFactory.getResolver();

        return resolver.resolve(dsm, userInput);
    }

    @RequestMapping(value = "forecast/meta", method = RequestMethod.GET)
    public WindForecastMetaData getWindForecastMetaData(){

        WindForecastMetaData forecastMetaData = null;

        final ForecastDataAdapter adapter = ForecastDataAdapterFactory.getDataAdapter();

        try {
            forecastMetaData = adapter.getWindForecastMetaData();
        } catch (DataNotAvailableException e) {
            LOG.error("Forecast meta data not available", e);
        }

        return forecastMetaData;
    }

}
