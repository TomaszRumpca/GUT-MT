package torumpca.pl.gut.mt.data.ksgmet;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.dsm.model.VectorComponents;
import torumpca.pl.gut.mt.dsm.model.WindForecastMetaData;
import torumpca.pl.gut.mt.dsm.model.WindForecastModel;
import torumpca.pl.gut.mt.error.DataNotAvailableException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;

/**
 * Created by Tomasz Rumpca on 2016-04-04.
 */
public class KsgMetAdapter extends AbstractKsgMetAdapter {

    public static final String KSG_MET_BASE_URL = "http://ksgmet.eti.pg.gda.pl/prognozy/CSV/poland";
    public static final String META_DATA_FILE_NAME = "current.nfo";
    public static final String U_WIND_DATA_FILE_NAME = "U_WIND_ON_10M.csv";
    public static final String V_WIND_DATA_FILE_NAME = "V_WIND_ON_10M.csv";

    private Logger LOG = LoggerFactory.getLogger(KsgMetAdapter.class);

    public WindForecastModel getWindForecast(LocalDateTime dateTime)
            throws DataNotAvailableException {

        WindForecastModel dsm = new WindForecastModel();

        try {
            WindForecastMetaData metaData = getWindForecastMetaData();
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
            throw new DataNotAvailableException("Forecast for date {} is not available on KSGMet",
                    e, dateTime.toString());
        }

        return dsm;
    }

    @Override
    public WindForecastMetaData getWindForecastMetaData() throws DataNotAvailableException {
        InputStream metaDataIS=null;
        try {
            URL urlMetaInfo = new URL(Joiner.on("/")
                    .join(KSG_MET_BASE_URL, LocalDateTime.now().getYear(), META_DATA_FILE_NAME));
            URLConnection metaInfo = urlMetaInfo.openConnection();
            metaDataIS = metaInfo.getInputStream();
            return getWindForecastMetaData(metaDataIS);
        } catch (MalformedURLException e) {
            throw new DataNotAvailableException("Invalid URL to meta data file", e);
        } catch (IOException e) {
            throw new DataNotAvailableException("Could not fetch forecast meta data from KsgMet", e);
        }
    }

}
