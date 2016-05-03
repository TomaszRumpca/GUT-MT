package torumpca.pl.gut.mt.data.ksgmet;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.google.common.base.Joiner;
import torumpca.pl.gut.mt.data.ForecastDataAdapter;
import torumpca.pl.gut.mt.dsm.model.VectorComponents;
import torumpca.pl.gut.mt.dsm.model.WindForecastModel;
import torumpca.pl.gut.mt.error.DataNotAvailableException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Tomasz Rumpca on 2016-05-01.
 */
public abstract class AbstractKsgMetAdapter implements ForecastDataAdapter {

    protected VectorComponents[][] getWindForecastData(int uDataCount, int vDataCount,
            InputStream uWindData, InputStream vWindData) throws DataNotAvailableException {

        VectorComponents[][] forecastData = new VectorComponents[uDataCount][vDataCount];

        try {
            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);

            MappingIterator<List<String>> uIterator =
                    mapper.readerFor(List.class).readValues(uWindData);
            MappingIterator<List<String>> vIterator =
                    mapper.readerFor(List.class).readValues(vWindData);

            int i = 0;
            while (uIterator.hasNext() && vIterator.hasNext()) {
                List<String> uRow = uIterator.next();
                List<String> vRow = vIterator.next();
                for (int j = 0; j < uRow.size(); j++) {
                    forecastData[i][j] = new VectorComponents(Double.parseDouble(vRow.get(j)),
                            Double.parseDouble(uRow.get(j)));
                }
                i++;
            }
        } catch (IOException e) {
            throw new DataNotAvailableException("forecast data not available", e);
        } catch (Exception e) {
            throw new DataNotAvailableException("Cannot parse forecast data", e);
        }
        return forecastData;
    }

    protected WindForecastModel getWindForecastModelWithMetaData(InputStream metaDataIS)
            throws DataNotAvailableException {

        final WindForecastModel dsm = new WindForecastModel();

        try {
            final CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);

            final MappingIterator<List<String>> metaDataIterator = mapper.readerFor(List.class).
                    readValues(metaDataIS);

            if (metaDataIterator.hasNext()) {
                List<String> metaData = metaDataIterator.next();

                LocalDateTime date = LocalDateTime.parse(Joiner.on("").
                                join("20", metaData.get(0), metaData.get(1)),
                        DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

                dsm.setForecastStartDateTime(date);
                dsm.setForecastDuration(Integer.parseInt(metaData.get(2)));
                dsm.setLeftBottomLonCoordinate(Double.parseDouble(metaData.get(3)));
                dsm.setLonStep(Double.parseDouble(metaData.get(4)));
                dsm.setLeftBottomLatCoordinate(Double.parseDouble(metaData.get(5)));
                dsm.setLatStep(Double.parseDouble(metaData.get(6)));
                dsm.setLonDataCount(Integer.parseInt(metaData.get(7)));
                dsm.setLatDataCount(Integer.parseInt(metaData.get(8)));
            }
        } catch (IOException e) {
            throw new DataNotAvailableException("forecast meta data not available", e);
        } catch (Exception e) {
            throw new DataNotAvailableException("Cannot parse forecast meta data", e);
        }
        return dsm;
    }

}
