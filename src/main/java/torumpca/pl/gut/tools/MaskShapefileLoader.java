package torumpca.pl.gut.tools;

import org.geotools.data.*;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;

public class MaskShapefileLoader {

    private static final Logger LOG = LoggerFactory.getLogger(MaskShapefileLoader.class);
    private static GeometryLoader geometryLoader = new GeometryLoader();

    /**
     * GeoTools Quickstart demo application. Prompts the user for a shapefile and displays its
     * contents on the screen in a map frame
     */
    public static void main(String[] args) throws Exception {

        LOG.info("Application started, please selected a shape file to load");

        // display a data store file chooser dialog for shapefiles
//        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        File file = new File("C:/DATA/magisterka/MASKA/CLC2018/clc18_PL.shp");
        if (file == null) {
            return;
        }

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();

        FeatureType schema = featureSource.getSchema();
        String name = schema.getGeometryDescriptor().getLocalName();

        String[] typeNames = store.getTypeNames();
        LOG.info("TypeNames: {}", typeNames);

        String typeName = "clc18_PL";
        SimpleFeatureSource source = store.getFeatureSource(typeName);

        String cqlPredicate = "CODE_18 = 523";
        Filter filter = CQL.toFilter(cqlPredicate);

        Query query = new Query(typeName, filter, new String[]{name});

        SimpleFeatureCollection features = source.getFeatures(query);

        LOG.info("Selected features: {}", features.size());

        SimpleFeatureIterator featuresIterator = features.features();

        MultiPolygon multiPolygon = null;

        if (featuresIterator.hasNext()) {
            SimpleFeature feature = featuresIterator.next();
            multiPolygon = (MultiPolygon) feature.getAttribute("the_geom");
            LOG.info("Found multipolygon: {}", multiPolygon);
        }
        featuresIterator.close();

        // Create a map content and add our shapefile to it
        MapContent map = new MapContent();
        map.setTitle("MT-CORE-GEO");

        Style style = SLD.createSimpleStyle(featureSource.getSchema());

        Layer layer = new FeatureLayer(features, style);
        map.addLayer(layer);

//        459267.0828999998 799634.0013999995, 459472.9787999997 799613.2611999996
        LineString lineString2 = geometryLoader.readLineString("LINESTRING(479267.0828999998 779634.0013999995, 479472.9787999997 749613.2611999996)");

        MultiPolygon lineString = geometryLoader.readMultiPolygonFromFile("C:\\Users\\torumpca\\IdeaProjects\\GUT-MT\\src\\main\\resources\\shoreline-ETRS89.txt");


        FeatureLayer featureLayer = getFeatureLayer(featureSource, lineString);
        map.addLayer(featureLayer);

        LOG.info("Multipolygon touches shoreline: {}", multiPolygon.touches(lineString2));
        LOG.info("Multipolygon crosses shoreline: {}", multiPolygon.crosses(lineString2));
        LOG.info("Multipolygon intersects shoreline: {}", multiPolygon.intersects(lineString2));
        LOG.info("Multipolygon overlaps shoreline: {}", multiPolygon.overlaps(lineString2));
        LOG.info("Multipolygon within shoreline: {}", multiPolygon.within(lineString2));

        LOG.info("Line touches shoreline: {}", lineString2.touches(multiPolygon));
        LOG.info("Line crosses shoreline: {}", lineString2.crosses(multiPolygon));
        LOG.info("Line intersects shoreline: {}", lineString2.intersects(multiPolygon));
        LOG.info("Line overlaps shoreline: {}", lineString2.overlaps(multiPolygon));
        LOG.info("Line within shoreline: {}", lineString2.within(multiPolygon));

        FeatureLayer featureLayer2 = getFeatureLayer(featureSource, lineString2);
        map.addLayer(featureLayer2);

        // Now display the map
        JMapFrame.showMap(map);
    }

    private static FeatureLayer getFeatureLayer(SimpleFeatureSource featureSource, Object geometry) {
        SimpleFeatureType featureType = featureSource.getSchema();

        SimpleFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder(featureType);

        simpleFeatureBuilder.add(geometry);
        SimpleFeature traversalPath = simpleFeatureBuilder.buildFeature("traversalPath");
        DefaultFeatureCollection simpleFeatures = new DefaultFeatureCollection();
        simpleFeatures.add(traversalPath);
        return new FeatureLayer(simpleFeatures, SLD.createLineStyle(Color.BLUE, 2));
    }

}
