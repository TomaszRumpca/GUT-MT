package torumpca.pl.gut.mt.algorithm.mask;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import torumpca.pl.gut.mt.algorithm.Mask;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.tools.GeometryLoader;

import java.io.FileNotFoundException;
import java.net.URL;

@Service
public class ShorelineMask implements Mask {

    private final static Logger LOG = LoggerFactory.getLogger(ShorelineMask.class);

    private static final String SHORELINE_FILE_NAME = "shoreline-full.txt";

    private final GeometryLoader geometryLoader = new GeometryLoader();
    private MultiPolygon shoreline;

    public ShorelineMask() {
        URL resource = this.getClass().getResource(SHORELINE_FILE_NAME);
        try {
            this.shoreline = geometryLoader.readMultiPolygonFromFile(resource.getFile());
        } catch (ParseException | FileNotFoundException e) {
            LOG.error("failed to load shoreline data fron {} resource file", SHORELINE_FILE_NAME, e);
        }
    }

    @Override
    public boolean isAllowed(Coordinates source, Coordinates target) {
        LineString path = getPathRepresentation(source, target);
        assert path != null;
        return !path.crosses(shoreline);
    }

    private LineString getPathRepresentation(Coordinates source, Coordinates target) {
        //TODO conversion from lat/long to ETRS89
        try {
            return geometryLoader.readLineString("LINESTRING(479267.0828999998 779634.0013999995, 479472.9787999997 749613.2611999996)");
        } catch (ParseException e) {
            return null;
        }
    }

}
