package torumpca.pl.gut.mt.alg;

import torumpca.pl.gut.mt.dsm.model.VectorComponents;
import torumpca.pl.gut.mt.dsm.model.WindForecastModel;

/**
 * Created by Tomasz Rumpca on 2016-04-07.
 */
public class Masks {

    public static boolean[][] generateSimpleMask(WindForecastModel dsm){

        final int rows = dsm.getLatDataCount();
        final int columns = dsm.getLonDataCount();

        final boolean[][] mask = new boolean[rows][columns];
        final VectorComponents[][] forecastData = dsm.getForecastData();

        for (int i = 0; i < rows / 2; i++){
            boolean nonZeroValFromRowBegin = false;
            boolean nonZeroValFromRowEnd = false;
            for (int j = 0; j < columns / 2; j++){

                if(!nonZeroValFromRowBegin &&
                        (forecastData[i][j].u != 0 || forecastData[i][j].v != 0)){
                    nonZeroValFromRowBegin = true;
                }
                mask[i][j] = nonZeroValFromRowBegin;

                if(!nonZeroValFromRowEnd &&
                        (forecastData[rows-i-1][columns-j-1].u != 0 ||
                                forecastData[rows-i-1][columns-j-1].v != 0)){
                    nonZeroValFromRowEnd = true;
                }
                mask[i][j] = nonZeroValFromRowEnd;
            }
        }

        return mask;
    }
}
