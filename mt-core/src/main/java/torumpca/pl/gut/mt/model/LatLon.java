package torumpca.pl.gut.mt.model;

import java.text.MessageFormat;

/**
 * Created by Tomasz Rumpca on 2016-04-22.
 */
public class LatLon {
    public Double latitude;
    public Double longitude;

    public LatLon() {
    }

    public LatLon(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        if(latitude != null && longitude != null ){
            return MessageFormat
                    .format("[lat:{0}, lon:{1}]", String.valueOf(latitude), String.valueOf(longitude));
        }
        return "[]";
    }
}
