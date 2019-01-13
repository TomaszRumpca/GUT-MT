package torumpca.pl.gut.mt.model;

import java.text.MessageFormat;

/**
 * Created by Tomasz Rumpca on 2016-08-15.
 */
public class Coordinates {

    public Double latitude;
    public Double longitude;

    @SuppressWarnings("unused")
    public Coordinates() {
    }

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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
