package torumpca.pl.gut.mt.data.ksgmet;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.dsm.model.VectorComponents;
import torumpca.pl.gut.mt.dsm.model.WindForecastMetaData;
import torumpca.pl.gut.mt.dsm.model.WindForecastModel;
import torumpca.pl.gut.mt.error.DataNotAvailableException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * Created by Tomasz Rumpca on 2016-05-01.
 */
public class FileAdapter extends AbstractKsgMetAdapter {

    Logger LOG = LoggerFactory.getLogger(FileAdapter.class);

    public WindForecastModel getWindForecast(LocalDateTime dateTime)
            throws DataNotAvailableException {

        WindForecastModel modelDsm = new WindForecastModel();

        try {
            WindForecastMetaData dsm = getWindForecastMetaData();
            modelDsm.setMetaData(dsm);

            int uDataCount = dsm.getLatDataCount();
            int vDataCount = dsm.getLonDataCount();
            InputStream uWind = new FileInputStream(getResourceFile("U_WIND_ON_10M.csv"));
            InputStream vWind = new FileInputStream(getResourceFile("V_WIND_ON_10M.csv"));

            VectorComponents[][] forecastData =
                    getWindForecastData(uDataCount, vDataCount, uWind, vWind);
            uWind.close();
            vWind.close();

            modelDsm.setForecastData(forecastData);

        } catch (IOException | URISyntaxException e) {
            throw new DataNotAvailableException("Test data not available!", e);
        }

        return modelDsm;
    }

    @Override
    public WindForecastMetaData getWindForecastMetaData() throws DataNotAvailableException {
       try (InputStream metaDataIS = new FileInputStream(getResourceFile("current.nfo"))){
            return getWindForecastMetaData(metaDataIS);
        } catch (FileNotFoundException e) {
            throw new DataNotAvailableException("File with forecast meta data cannot be found", e);
        } catch (URISyntaxException e) {
            throw new DataNotAvailableException("Invalid URI to local forecast meta data file", e);
        } catch (IOException e) {
            throw new DataNotAvailableException("Could not read forecast meta data file", e);
        }
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
