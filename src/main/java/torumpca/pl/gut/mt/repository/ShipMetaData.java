package torumpca.pl.gut.mt.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Tomasz Rumpca on 2016-08-13.
 */
@Entity
public class ShipMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String name;
    public String description;
    public String averageSpeedInKnots;
    public String averageCostOfHourOnSea;

    public ShipMetaData(String name, String description, String averageSpeedInKnots,
            String averageCostOfHourOnSea) {
        this.name = name;
        this.description = description;
        this.averageSpeedInKnots = averageSpeedInKnots;
        this.averageCostOfHourOnSea = averageCostOfHourOnSea;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAverageSpeedInKnots() {
        return averageSpeedInKnots;
    }

    public void setAverageSpeedInKnots(String averageSpeedInKnots) {
        this.averageSpeedInKnots = averageSpeedInKnots;
    }

    public String getAverageCostOfHourOnSea() {
        return averageCostOfHourOnSea;
    }

    public void setAverageCostOfHourOnSea(String averageCostOfHourOnSea) {
        this.averageCostOfHourOnSea = averageCostOfHourOnSea;
    }
}
