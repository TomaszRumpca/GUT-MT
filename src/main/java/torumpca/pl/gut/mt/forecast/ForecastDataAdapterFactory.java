package torumpca.pl.gut.mt.forecast;

import torumpca.pl.gut.mt.forecast.file.FileAdapter;
import torumpca.pl.gut.mt.forecast.ksgmet.KsgMetAdapter;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public class ForecastDataAdapterFactory {

    public static ForecastDataAdapter getDataAdapter(Boolean cachedData) {

        if (cachedData) {
            return new FileAdapter();
        } else {
            return new KsgMetAdapter();
        }
    }

}
