package torumpca.pl.gut.mt.model;

import java.util.List;

/**
 * Created by Tomasz Rumpca on 2016-04-11.
 */
public class Solution {

    private final long overallCost;
    private final List optimalPaths;

    public Solution(List optimalPaths, long overallCost) {
        this.overallCost = overallCost;
        this.optimalPaths = optimalPaths;
    }

    public long getOverallCost() {
        return overallCost;
    }

    public List getOptimalPaths() {
        return optimalPaths;
    }

}
