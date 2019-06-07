package torumpca.pl.gut.mt.algorithm.mask;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import torumpca.pl.gut.mt.algorithm.Mask;
import torumpca.pl.gut.mt.algorithm.model.Coordinates;
import torumpca.pl.gut.tools.GeometryLoader;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ShorelineMaskWGS84 implements Mask {

    private final static Logger LOG = LoggerFactory.getLogger(ShorelineMaskWGS84.class);

    private static final String SHORELINE_FILE_NAME = "shoreline-Poland-WGS84.txt";

    private final GeometryLoader geometryLoader = new GeometryLoader();
    private LineString shoreline;
    private static final String PATH_PATTERN = "LINESTRING(%s %s, %s %s)";
//    private final List<Move> blockedMoves = new ArrayList<>();

    public ShorelineMaskWGS84() {
        URL resource = this.getClass().getResource(SHORELINE_FILE_NAME);
        try {
            this.shoreline = geometryLoader.readLineStringFromFile(resource.getFile());
        } catch (ParseException | FileNotFoundException e) {
            LOG.error("failed to load shoreline data fron {} resource file", SHORELINE_FILE_NAME, e);
        }
    }

    @Override
    public boolean isAllowed(Coordinates source, Coordinates target) {
        LineString path = getPathRepresentation(source, target);
        assert path != null;
        boolean allowed = !path.crosses(shoreline);
//        if (!allowed) {
//            Move blockedMove = new Move(source, target);
//            if (blockedMoves.contains(blockedMove)) {
//                LOG.error("second attempt of invalid move from {} to {}", source, target);
//                 TODO make smarter detection of loops
//                return true;
//            } else {
//                blockedMoves.add(blockedMove);
//            }
//        }
        return allowed;
    }

    private LineString getPathRepresentation(Coordinates source, Coordinates target) {
        String wkt = String.format(PATH_PATTERN, source.latitude, source.longitude, target.latitude, target.longitude);
        try {
            return geometryLoader.readLineString(wkt);
        } catch (ParseException e) {
            return null;
        }
    }

    private class Move {
        Coordinates source;
        Coordinates target;

        public Move(Coordinates source, Coordinates target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Move move = (Move) o;
            return source.equals(move.source) &&
                    target.equals(move.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target);
        }
    }

}
