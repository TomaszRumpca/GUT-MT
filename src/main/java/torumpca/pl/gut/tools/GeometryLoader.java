package torumpca.pl.gut.tools;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class GeometryLoader {

    public LineString readLineString(String wkt) throws ParseException {
        return (LineString) loadWKTString(wkt);
    }

    public MultiPolygon readMultiPolygon(String wkt) throws ParseException {
        return (MultiPolygon) loadWKTString(wkt);
    }

    public LineString readLineStringFromFile(String wktFileName)
            throws ParseException, FileNotFoundException {
        File file = new File(wktFileName);
        return (LineString) loadWKTFile(file);
    }

    public MultiPolygon readMultiPolygonFromFile(String wktFileName)
            throws ParseException, FileNotFoundException {
        File file = new File(wktFileName);
        return (MultiPolygon) loadWKTFile(file);
    }

    private static Object loadWKTString(String wellKnownText) throws ParseException {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        WKTReader reader = new WKTReader(geometryFactory);
        return reader.read(wellKnownText);
    }

    private static Object loadWKTFile(File wellKnownTextFile)
            throws ParseException, FileNotFoundException {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        WKTReader reader = new WKTReader(geometryFactory);
        FileReader wktFileReader = new FileReader(wellKnownTextFile);
        return reader.read(wktFileReader);
    }


}
