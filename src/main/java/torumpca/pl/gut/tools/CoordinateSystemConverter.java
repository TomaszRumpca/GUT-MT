package torumpca.pl.gut.tools;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

public class CoordinateSystemConverter {

    public static final DefaultGeographicCRS WGS_84 = DefaultGeographicCRS.WGS84;

    public static void main(String[] args) {
        try {
            CoordinateSystemConverter coordinateSystemConverter = new CoordinateSystemConverter();
        } catch (FactoryException e) {
            e.printStackTrace();
        }
    }

    public CoordinateSystemConverter() throws FactoryException {


        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:23032");


        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);

    }
}
