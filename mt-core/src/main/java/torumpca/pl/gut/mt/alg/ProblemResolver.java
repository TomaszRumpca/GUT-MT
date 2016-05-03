package torumpca.pl.gut.mt.alg;

import torumpca.pl.gut.mt.dsm.model.Solution;
import torumpca.pl.gut.mt.dsm.model.UserData;
import torumpca.pl.gut.mt.dsm.model.WindForecastModel;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public interface ProblemResolver {

    Solution resolve(WindForecastModel forecast, UserData input);
}
