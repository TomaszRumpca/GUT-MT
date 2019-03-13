package torumpca.pl.gut.mt.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Tomasz Rumpca on 2016-08-13.
 */
@Entity
public class Seaport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String name;
    public String description;
    public String countryIsoCode;
    public String city;
    public Double latitude;
    public Double longitude;

    public Seaport(String name, String description, String countryIsoCode, String city,
            Double latitude, Double longitude) {
        this.name = name;
        this.description = description;
        this.countryIsoCode = countryIsoCode;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Seaport(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
}
