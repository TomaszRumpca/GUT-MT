package torumpca.pl.gut.mt.data;

import torumpca.pl.gut.mt.data.ksgmet.KsgMetAdapter;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public class ForecastDataAdapterFactory {

    public static ForecastDataAdapter getDataAdapter(){
        return new KsgMetAdapter();
//        return new FileAdapter();
    }

}
