package torumpca.pl.gut.mt.algorithm.mask;

import org.springframework.stereotype.Service;
import torumpca.pl.gut.mt.algorithm.Mask;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;

@Service
public class AllAllowedMask implements Mask {

    @Override
    public boolean isAllowed(Coordinates source, Coordinates target) {
        return true;
    }

}
