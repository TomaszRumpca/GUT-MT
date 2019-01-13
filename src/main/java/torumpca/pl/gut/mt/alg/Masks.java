package torumpca.pl.gut.mt.alg;

import torumpca.pl.gut.mt.model.VectorComponents;
import torumpca.pl.gut.mt.model.WindForecastModel;

/**
 * Created by Tomasz Rumpca on 2016-04-07.
 */
public class Masks {

    public static boolean[][] generateSimpleMask(WindForecastModel dsm){

        final int rows = dsm.getMetaData().getLatDataCount();
        final int columns = dsm.getMetaData().getLonDataCount();

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

    public static boolean[][] getMaskAllValid(WindForecastModel dsm){

        final int rows = dsm.getMetaData().getLatDataCount();
        final int columns = dsm.getMetaData().getLonDataCount();

        final boolean[][] mask = new boolean[rows][columns];

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns ; j++){
                mask[i][j] = true;
            }
        }

        return mask;
    }

}
