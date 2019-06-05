package torumpca.pl.gut.mt.algorithm.model;

import java.text.MessageFormat;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return latitude.equals(that.latitude) &&
                longitude.equals(that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
