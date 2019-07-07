package torumpca.pl.gut.mt.algorithm;

import es.usc.citius.hipster.model.function.impl.StateTransitionFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class TransitionFunction extends StateTransitionFunction<Coordinates> {

    private final static Logger LOG = LoggerFactory.getLogger(TransitionFunction.class);

    private final List<Mask> masks;
    private final double latStep;
    private final double lonStep;
    private final double maxMoveDistance;
    private final Coordinates goal;

    TransitionFunction(List<Mask> masks, double latStep, double lonStep, double maxMoveDistance,
            Coordinates goal) {
        this.masks = masks;
        this.goal = goal;
        this.latStep = latStep;
        this.lonStep = lonStep;
        this.maxMoveDistance = maxMoveDistance;
    }

    /**
     * szuka punktow w najblizszym otoczeniu statku które mogą stać się następnym elementem na jesgo
     * ścieżce.
     *
     * @param shipPosition punkt z którego wyznaczane są następne możliwe przejścia
     * @return zbiór punktów które mogą zostać odwiedzone w następnym kroku algorytmu, bezpośredni
     * sąsiedzi shipPosition
     */
    @Override
    public Iterable<Coordinates> successorsOf(Coordinates shipPosition) {

        Collection<Coordinates> validMoves = new HashSet<>();
        // Check for all valid movements
        for (double row = -lonStep; row <= lonStep; row += lonStep) {
            for (double column = -latStep; column <= latStep; column += latStep) {
                if (row != 0 || column != 0) {

                    Coordinates predictedCoordinates =
                            new Coordinates(shipPosition.latitude + column,
                                    shipPosition.longitude + row);

                    boolean allowed = true;
                    for (Mask mask : masks) {
                        boolean allowedByCurrentMask =
                                mask.isAllowed(shipPosition, predictedCoordinates);
                        if (!allowedByCurrentMask) {
                            LOG.info("Move ({}) -> ({}) disallowed by {}", shipPosition,
                                    predictedCoordinates, mask.getClass().getCanonicalName());
                            allowed = false;
                            break;
                        }
                    }

                    if (allowed) {
                        validMoves.add(predictedCoordinates);
                    }
                }
            }
        }

        double deltaLatitude = shipPosition.latitude - goal.latitude;
        double deltaLongitude = shipPosition.longitude - goal.longitude;

        double distanceToGoal =
                Math.sqrt(deltaLatitude * deltaLatitude + deltaLongitude * deltaLongitude);

        if (distanceToGoal < maxMoveDistance) {
            validMoves.add(goal);
        }

        return validMoves;
    }

}
