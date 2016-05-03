package torumpca.pl.gut.mt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.alg.ProblemResolver;
import torumpca.pl.gut.mt.alg.ResolverFactory;
import torumpca.pl.gut.mt.data.ForecastDataAdapter;
import torumpca.pl.gut.mt.data.ForecastDataAdapterFactory;
import torumpca.pl.gut.mt.dsm.model.UserData;
import torumpca.pl.gut.mt.dsm.model.WindForecastModel;
import torumpca.pl.gut.mt.error.DataNotAvailableException;

import java.time.LocalDateTime;

public class App {

    private static Logger LOG = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        final ForecastDataAdapter adapter = ForecastDataAdapterFactory.getDataAdapter();
        WindForecastModel dsm = null;
        try {
            dsm = adapter.getWindForecast(LocalDateTime.now());
        } catch (DataNotAvailableException e) {
            LOG.error("Forecast not available.", e);
        }
        final UserData userInput = new UserData();
        final ProblemResolver resolver = ResolverFactory.getResolver();

        resolver.resolve(dsm, userInput);

    }
}
