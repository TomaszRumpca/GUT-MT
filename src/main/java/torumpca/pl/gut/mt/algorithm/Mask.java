package torumpca.pl.gut.mt.algorithm;

import torumpca.pl.gut.mt.algorithm.model.Coordinates;

public interface Mask {

    boolean isAllowed(Coordinates source, Coordinates target);

}
