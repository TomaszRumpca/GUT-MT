package torumpca.pl.gut.mt.forecast.ksgmet;

import com.google.common.base.Joiner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.algorithm.model.VectorComponents;
import torumpca.pl.gut.mt.forecast.AbstractKsgMetAdapter;
import torumpca.pl.gut.mt.forecast.DataNotAvailableException;
import torumpca.pl.gut.mt.forecast.model.WindForecastMetaData;
import torumpca.pl.gut.mt.forecast.model.WindForecastModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomasz Rumpca on 2016-04-04.
 */
public class KsgMetAdapter extends AbstractKsgMetAdapter {

    private static final String KSG_MET_BASE_URL = "http://ksgmet.eti.pg.gda.pl/prognozy/CSV/poland";
    private static final String META_DATA_FILE_NAME = "current.nfo";
    private static final String U_WIND_DATA_FILE_NAME = "U_WIND_ON_10M.csv";
    private static final String V_WIND_DATA_FILE_NAME = "V_WIND_ON_10M.csv";

    private final List<String> requiredForecastFiles;

    private Logger LOG = LoggerFactory.getLogger(KsgMetAdapter.class);

    public KsgMetAdapter() {
        requiredForecastFiles = new ArrayList<>();
        requiredForecastFiles.add(U_WIND_DATA_FILE_NAME);
        requiredForecastFiles.add(V_WIND_DATA_FILE_NAME);
    }

    public WindForecastModel getWindForecast(LocalDateTime dateTime)
            throws DataNotAvailableException {

        LOG.info("fetching forecast for {}", dateTime);

        WindForecastModel dsm = new WindForecastModel();

        try {
            WindForecastMetaData metaData = getWindForecastMetaData(dateTime.getYear());
            dsm.setMetaData(metaData);

            int uDataCount = dsm.getMetaData().getLatDataCount();
            int vDataCount = dsm.getMetaData().getLonDataCount();

            URL urlWindU = new URL(Joiner.on("/")
                    .join(KSG_MET_BASE_URL, dateTime.getYear(), dateTime.getMonthValue(),
                            dateTime.getDayOfMonth(), dateTime.getHour(), U_WIND_DATA_FILE_NAME));
            URLConnection windU = urlWindU.openConnection();
            InputStream windUIS = windU.getInputStream();

            URL urlWindV = new URL(Joiner.on("/")
                    .join(KSG_MET_BASE_URL, dateTime.getYear(), dateTime.getMonthValue(),
                            dateTime.getDayOfMonth(), dateTime.getHour(), V_WIND_DATA_FILE_NAME));
            URLConnection windV = urlWindV.openConnection();
            InputStream windVIS = windV.getInputStream();

            VectorComponents[][] forecastData =
                    getWindForecastData(uDataCount, vDataCount, windUIS, windVIS);
            dsm.setForecastData(forecastData);
            windUIS.close();
            windVIS.close();
        } catch (IOException e) {
            LOG.error("failed to prepare forecast for {}", dateTime.toString(), e);
            throw new DataNotAvailableException("Forecast for date {0} is not available on KSGMet",
                    e, dateTime.toString());
        }

        return dsm;
    }

    @Override
    public WindForecastMetaData getWindForecastMetaData(Integer requestedYear) throws DataNotAvailableException {
        InputStream metaDataIS;

        final Integer year = requestedYear != null ? requestedYear : LocalDateTime.now().getYear();

        try {
            final URL urlMetaInfo = new URL(Joiner.on("/")
                    .join(KSG_MET_BASE_URL, year, META_DATA_FILE_NAME));
            final URLConnection metaInfo = urlMetaInfo.openConnection();
            metaDataIS = metaInfo.getInputStream();
            return getWindForecastMetaData(metaDataIS);
        } catch (MalformedURLException e) {
            throw new DataNotAvailableException("Invalid URL to meta data file", e);
        } catch (IOException e) {
            throw new DataNotAvailableException("Could not fetch forecast meta data from KsgMet", e);
        }
    }

    @Override
    public List<OffsetDateTime> getForecastAvailableDates() {

        final List<OffsetDateTime> availableForecasts = new ArrayList<>();
        for (String year : collectAvailableDirectories(KSG_MET_BASE_URL)) {
            for (String month : collectAvailableMonths(KSG_MET_BASE_URL, year)) {
                for (String day : collectAvailableDays(KSG_MET_BASE_URL, year, month)) {
                    for (String hour : collectAvailableHours(KSG_MET_BASE_URL, year, month, day)) {
                        final OffsetDateTime availableForecast = OffsetDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), 0, 0, 0, OffsetDateTime.now().getOffset());
                        if (validate(availableForecast)) {
                            availableForecasts.add(availableForecast);
                        }
                    }
                }
            }
        }

        return availableForecasts;
    }

    private boolean validate(OffsetDateTime availableForecast) {
        final List<String> availableForecastFiles = collectAvailableDirectories(Joiner.on('/').join(KSG_MET_BASE_URL, availableForecast.getYear(), availableForecast.getMonth().getValue(), availableForecast.getDayOfMonth(), availableForecast.getHour()));
        return availableForecastFiles.containsAll(requiredForecastFiles);
    }


    private List<String> collectAvailableHours(String ksgMetBaseUrl, String year, String month, String day) {
        return collectAvailableDirectories(Joiner.on('/').join(ksgMetBaseUrl, year, month, day));
    }

    private List<String> collectAvailableDays(String ksgMetBaseUrl, String year, String month) {
        return collectAvailableDirectories(Joiner.on('/').join(ksgMetBaseUrl, year, month));
    }

    private List<String> collectAvailableMonths(String baseUrl, String year) {
        return collectAvailableDirectories(Joiner.on('/').join(baseUrl, year));
    }

    private List<String> collectAvailableDirectories(String url) {
        final List<String> dirs = new ArrayList<>();
        try {
            LOG.info("fetching {}", url);
            final Document doc = Jsoup.connect(url).get();
            final Elements directories = doc.select("td a");
            for (Element directoryName : directories) {
                final String name = directoryName.ownText();
                LOG.info("dirname: '{}'", name);
                if (!name.equals("Parent Directory") && !name.equals("current.nfo")) {
                    final String cleanName = name.endsWith("/") ? name.substring(0, name.length() - 1) : name;
                    dirs.add(cleanName);
                }
            }
        } catch (IOException e) {
            LOG.error("failed to access forecast data server", e);
        }
        return dirs;
    }

}
