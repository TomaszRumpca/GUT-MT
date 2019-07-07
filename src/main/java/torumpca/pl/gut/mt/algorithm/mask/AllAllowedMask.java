package torumpca.pl.gut.mt.algorithm.mask;

import torumpca.pl.gut.mt.algorithm.Mask;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;


public class AllAllowedMask implements Mask {

    @Override
    public boolean isAllowed(Coordinates source, Coordinates target) {
        return true;
    }

}
