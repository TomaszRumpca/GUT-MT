package torumpca.pl.gut.mt.algorithm;

import torumpca.pl.gut.mt.algorithm.model.AlgorithmInputData;
import torumpca.pl.gut.mt.algorithm.model.Solution;
import torumpca.pl.gut.mt.forecast.model.WindForecastModel;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public interface ProblemResolver {

    Solution resolve(WindForecastModel forecast, AlgorithmInputData input);
}
