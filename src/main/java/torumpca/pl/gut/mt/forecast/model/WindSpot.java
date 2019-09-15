package torumpca.pl.gut.mt.forecast.model;

import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.mt.algorithm.model.VectorComponents;

public class WindSpot {

    private final Coordinates coordinates;
    private final VectorComponents wind;

    public WindSpot(Coordinates coordinates, VectorComponents wind) {
        this.coordinates = coordinates;
        this.wind = wind;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public VectorComponents getWind() {
        return wind;
    }
}
