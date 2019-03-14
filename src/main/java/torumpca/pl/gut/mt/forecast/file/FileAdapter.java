package torumpca.pl.gut.mt.forecast.file;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.algorithm.model.VectorComponents;
import torumpca.pl.gut.mt.forecast.AbstractKsgMetAdapter;
import torumpca.pl.gut.mt.forecast.DataNotAvailableException;
import torumpca.pl.gut.mt.forecast.model.WindForecastMetaData;
import torumpca.pl.gut.mt.forecast.model.WindForecastModel;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Tomasz Rumpca on 2016-05-01.
 */
public class FileAdapter extends AbstractKsgMetAdapter {

    private static final Map<Integer, CachedForecast> forecastFiles = new HashMap<>();

    static {
        forecastFiles.put(2017, new CachedForecast("current.nfo", "U_WIND_ON_10M_2017.csv", "V_WIND_ON_10M_2017.csv"));
        forecastFiles.put(2018, new CachedForecast("current.nfo", "U_WIND_ON_10M_2018.csv", "V_WIND_ON_10M_2018.csv"));
        forecastFiles.put(2019, new CachedForecast("current.nfo", "U_WIND_ON_10M_2.csv", "V_WIND_ON_10M_2.csv"));
        forecastFiles.put(2020, new CachedForecast("current.nfo", "U_WIND_ON_10M.csv", "V_WIND_ON_10M.csv"));
    }

    private final static Logger LOG = LoggerFactory.getLogger(FileAdapter.class);

    public WindForecastModel getWindForecast(LocalDateTime dateTime)
            throws DataNotAvailableException {

        WindForecastModel forecastModel = new WindForecastModel();

        try {
            WindForecastMetaData dsm = getWindForecastMetaData(dateTime.getYear());

            CachedForecast cachedForecast = forecastFiles.get(dateTime.getYear());

            if (cachedForecast == null) {
                throw new DataNotAvailableException(String.format("Test data for %s not available!",
                        dateTime.getYear()));
            }

            LOG.info("found cached forecast for {}, meta: {}, u: {}, v: {}", dateTime.getYear(),
                    cachedForecast.getMetaDataFileName(), cachedForecast.getuWindFileName(),
                    cachedForecast.getvWindFileName());

            forecastModel.setMetaData(dsm);

            int uDataCount = dsm.getLatDataCount();
            int vDataCount = dsm.getLonDataCount();
            InputStream uWind = new FileInputStream(getResourceFile(cachedForecast.getuWindFileName()));
            InputStream vWind = new FileInputStream(getResourceFile(cachedForecast.getvWindFileName()));

            VectorComponents[][] forecastData =
                    getWindForecastData(uDataCount, vDataCount, uWind, vWind);
            uWind.close();
            vWind.close();

            forecastModel.setForecastData(forecastData);

        } catch (IOException | URISyntaxException e) {
            throw new DataNotAvailableException("Test data not available!", e);
        }

        return forecastModel;
    }

    @Override
    public WindForecastMetaData getWindForecastMetaData(Integer year) throws DataNotAvailableException {
        final String resourceName;
        final CachedForecast cachedForecast = forecastFiles.get(year);
        if (cachedForecast == null) {
            throw new DataNotAvailableException("no cached forecast meta data file for " + year);
        }
        resourceName = cachedForecast.getMetaDataFileName();
        try (InputStream metaDataIS = new FileInputStream(getResourceFile(resourceName))) {
            return getWindForecastMetaData(metaDataIS);
        } catch (FileNotFoundException e) {
            throw new DataNotAvailableException("File with forecast meta data cannot be found", e);
        } catch (URISyntaxException e) {
            throw new DataNotAvailableException("Invalid URI to local forecast meta data file", e);
        } catch (IOException e) {
            throw new DataNotAvailableException("Could not read forecast meta data file", e);
        }
    }

    @Override
    public List<OffsetDateTime> getForecastAvailableDates() {
        return forecastFiles
                .keySet()
                .stream()
                .map(year -> OffsetDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC))
                .collect(Collectors.toList());
    }

    private File getResourceFile(String resourceName) throws URISyntaxException, DataNotAvailableException {
        final URL metaDataUrl = getClass().getClassLoader().getResource(resourceName);
        if (metaDataUrl != null) {
            final URI metaDataUri = metaDataUrl.toURI();
            return new File(metaDataUri);
        } else {
            throw new DataNotAvailableException(String.format("failed to read %s.", resourceName));
        }
    }

}
