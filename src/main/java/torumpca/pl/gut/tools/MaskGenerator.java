package torumpca.pl.gut.tools;

import torumpca.pl.gut.mt.algorithm.model.Coordinates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;

/**
 * Created by Tomasz Rumpca on 2016-08-15.
 */
public class MaskGenerator {

    private static String GOOGLE_MAPS_GEOCODING_API_KEY = "AIzaSyBXHEE-ztS_fNJ3DGmvHV79e9FcQ2wDCmQ";
    private static String GOOGLE_REVERSE_GEO_SERVICE_URL_PATTERN =
            "https://maps.googleapis.com/maps/api/geocode/json?latlng={0},{1}&key={2}";

    private static double BALTIC_MIN_LAT = 53.709264d;
    private static double BALTIC_MAX_LAT = 66.070288d;
    private static double BALTIC_MIN_LON = 12.147678d;
    private static double BALTIC_MAX_LON = 31.449390d;


    public void generate() throws IOException {

        MaskGenerator generator = new MaskGenerator();

        double initialLat = 48.802824d;
        double initialLon = 13.236774d;
        double latStep = 0.0378444945891919;
        double lonStep = 0.0378444945891919;

        int latCount = 5;//ilosc elementow w kolumnie - liczba wierszy
        int lonCount = 7;//ilosc elementow we wierszu - liczba kolumn
        final char csvSeparator = ',';
        final String newLineSign = "\n";
        final String generatedMaskFileLocation = "generated-mask.txt";
        final String oldMaskFileLocation = "generated-mask-old.txt";

        File maskFile = createMaskFile(generatedMaskFileLocation, oldMaskFileLocation);
        FileWriter maskWriter = new FileWriter(maskFile);

        double maxLat = initialLat + (latStep * latCount);
        double maxLon = initialLon + (lonStep * lonCount);

        try {

            final boolean[][] mask = new boolean[latCount][lonCount];

            int i = 0, j = 0;
            //latitude - rosnie na polnoc - liczba wierszy
            for (double lat = initialLat; lat < maxLat; lat += latStep) {
                StringBuilder csvLine = new StringBuilder();
                //longitude - rosnie na wschod - liczba kolumn
                for (double lon = initialLon; lon < maxLon; lon += lonStep) {
                    final boolean locationOverTheWater =
                            inBalticBoudns(lat, lon) ? generator.overwater(
                                    new Coordinates(lat, lon)) : false;
                    final String overTheWaterStr = locationOverTheWater ? "1" : "0";
                    if (j == 0) {
                        csvLine.append(overTheWaterStr);
                    } else {
                        csvLine.append(csvSeparator).append(overTheWaterStr);
                    }
                    mask[i][j++] = locationOverTheWater;
                }
                csvLine.append(newLineSign);
                maskWriter.write(csvLine.toString());
                i++;
                j = 0;
            }

            maskWriter.flush();
            maskWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static boolean inBalticBoudns(double latitide, double longitude) {
        if (latitide > BALTIC_MIN_LAT && latitide < BALTIC_MAX_LAT) {
            if (longitude > BALTIC_MIN_LON && longitude < BALTIC_MAX_LON) {
                return true;
            }
        }
        return false;
    }

    private static File createMaskFile(String generatedMaskFileLocation,
            String oldMaskFileLocation) {

        final File generatedMaskFile = new File(generatedMaskFileLocation);

        final Path generatedMaskPath = Paths.get(generatedMaskFileLocation);
        final Path oldMaskPath = Paths.get(oldMaskFileLocation);

        try {
            if (generatedMaskFile.exists()) {
                Files.copy(generatedMaskPath, oldMaskPath, StandardCopyOption.REPLACE_EXISTING);
                generatedMaskFile.delete();
            }
            generatedMaskFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return generatedMaskFile;
    }

    private boolean overwater(Coordinates coords) throws IOException {

        String url = MessageFormat.format(GOOGLE_REVERSE_GEO_SERVICE_URL_PATTERN,
                coords.getLatitude().toString(), coords.getLongitude().toString(),
                GOOGLE_MAPS_GEOCODING_API_KEY);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "USER_AGENT");

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(
                "LAT: " + coords.latitude + ", LON: " + coords.longitude + ", Response string: '"
                + response.toString() + "'");
        return response.toString().contains("ZERO_RESULTS");
    }


}
