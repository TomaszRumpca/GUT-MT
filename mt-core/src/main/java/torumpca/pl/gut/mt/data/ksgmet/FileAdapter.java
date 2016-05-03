package torumpca.pl.gut.mt.data.ksgmet;

import torumpca.pl.gut.mt.dsm.model.VectorComponents;
import torumpca.pl.gut.mt.dsm.model.WindForecastModel;
import torumpca.pl.gut.mt.error.DataNotAvailableException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * Created by Tomasz Rumpca on 2016-05-01.
 */
public class FileAdapter extends AbstractKsgMetAdapter {

    public WindForecastModel getWindForecast(LocalDateTime dateTime)
            throws DataNotAvailableException {

        WindForecastModel dsm;

        try {
            final InputStream metaDataIS = new FileInputStream(getResourceFile("current.nfo"));

            dsm = getWindForecastModelWithMetaData(metaDataIS);
            metaDataIS.close();

            int uDataCount = dsm.getLatDataCount();
            int vDataCount = dsm.getLonDataCount();
            InputStream uWind = new FileInputStream(getResourceFile("U_WIND_ON_10M.csv"));
            InputStream vWind = new FileInputStream(getResourceFile("V_WIND_ON_10M.csv"));

            VectorComponents[][] forecastData =
                    getWindForecastData(uDataCount, vDataCount, uWind, vWind);
            uWind.close();
            vWind.close();

            dsm.setForecastData(forecastData);

        } catch (IOException | URISyntaxException e) {
            throw new DataNotAvailableException("Test data not available!", e);
        }

        return dsm;
    }

    private File getResourceFile(String resourceName) throws URISyntaxException {
        final URL metaDataUrl = getClass().getClassLoader().getResource(resourceName);
        if(metaDataUrl != null){
            final URI metaDataUri = metaDataUrl.toURI();
            return new File(metaDataUri);
        }
        return null;
    }

}
