package torumpca.pl.gut.mt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    private static Logger LOG = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);


//        final ForecastDataAdapter adapter = ForecastDataAdapterFactory.getDataAdapter();
//        WindForecastModel dsm = null;
//        try {
//            dsm = adapter.getWindForecast(LocalDateTime.now());
//        } catch (DataNotAvailableException e) {
//            LOG.error("Forecast not available.", e);
//        }
//        final UserData userInput = new UserData();
//        final ProblemResolver resolver = ResolverFactory.getResolver();
//
//        resolver.resolve(dsm, userInput);

    }
}
