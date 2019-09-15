package torumpca.pl.gut.mt.algorithm;

import es.usc.citius.hipster.model.function.impl.StateTransitionFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class TransitionFunction extends StateTransitionFunction<Coordinates> {

    private static final Logger LOG = LoggerFactory.getLogger(TransitionFunction.class);

    private final List<Mask> masks;
    private final double latStep;
    private final double lonStep;
    private final double maxMoveDistance;
    private final Coordinates goal;
    private final Coordinates forecastStartingPoint;

    private boolean firstMove = true;

    TransitionFunction(List<Mask> masks, double latStep, double lonStep, double maxMoveDistance,
            Coordinates goal, Coordinates forecastStartingPoint) {
        this.masks = masks;
        this.goal = goal;
        this.latStep = latStep;
        this.lonStep = lonStep;
        this.maxMoveDistance = maxMoveDistance;
        this.forecastStartingPoint = forecastStartingPoint;
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

        if (firstMove) {

            double lat = forecastStartingPoint.latitude;
            double lon = forecastStartingPoint.longitude;

            while (lat <= shipPosition.latitude){
                lat += latStep;
            }
            while (lon <= shipPosition.longitude){
                lon += lonStep;
            }

            final Coordinates c1 = new Coordinates(lat, lon);
            if (validateMove(shipPosition, c1)) {
                validMoves.add(c1);
            }
            final Coordinates c2 = new Coordinates(lat - latStep, lon);
            if (validateMove(shipPosition, c2)) {
                validMoves.add(c2);
            }
            final Coordinates c3 = new Coordinates(lat - latStep, lon - lonStep);
            if (validateMove(shipPosition, c3)) {
                validMoves.add(c3);
            }
            final Coordinates c4 = new Coordinates(lat, lon - lonStep);
            if (validateMove(shipPosition, c4)) {
                validMoves.add(c4);
            }

            firstMove = false;
        }

        // Check for all valid movements
        for (double row = -lonStep; row <= lonStep; row += lonStep) {
            for (double column = -latStep; column <= latStep; column += latStep) {
                if (row != 0 || column != 0) {

                    final Coordinates predictedCoordinates =
                            new Coordinates(shipPosition.latitude + column,
                                    shipPosition.longitude + row);

                    boolean allowed = validateMove(shipPosition, predictedCoordinates);

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

    private boolean validateMove(Coordinates shipPosition, Coordinates predictedCoordinates) {
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
        return allowed;
    }

}
