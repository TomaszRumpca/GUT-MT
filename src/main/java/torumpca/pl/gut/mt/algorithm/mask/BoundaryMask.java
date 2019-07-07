package torumpca.pl.gut.mt.algorithm.mask;

import torumpca.pl.gut.mt.algorithm.Mask;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;


public class BoundaryMask implements Mask {

    @Override
    public boolean isAllowed(Coordinates source, Coordinates target) {
        //TODO implement boundaries
        return true;
    }
}
