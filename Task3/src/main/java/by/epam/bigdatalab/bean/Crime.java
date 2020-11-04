package by.epam.bigdatalab.bean;

import java.util.Date;
import java.util.Objects;


public class Crime {

    private String category;
    private String persistentId;
    private Date month;



    private String context;
    private Long id;
    private String locationType;
    private String locationSubtype;

    private CrimeLocation location;
    private CrimeOutcomeStatus outcomeStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crime crime = (Crime) o;
        return Objects.equals(category, crime.category) &&
                Objects.equals(persistentId, crime.persistentId) &&
                Objects.equals(month, crime.month) &&
                Objects.equals(location, crime.location) &&
                Objects.equals(context, crime.context) &&
                Objects.equals(id, crime.id) &&
                Objects.equals(locationType, crime.locationType) &&
                Objects.equals(locationSubtype, crime.locationSubtype) &&
                Objects.equals(outcomeStatus, crime.outcomeStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, persistentId, month, location, context, id, locationType, locationSubtype, outcomeStatus);
    }

    @Override
    public String toString() {
        return "Crime{" +
                "category='" + category + '\'' +
                ", persistentId='" + persistentId + '\'' +
                ", month=" + month +
                ", location=" + location +
                ", context='" + context + '\'' +
                ", id=" + id +
                ", locationType='" + locationType + '\'' +
                ", locationSubtype='" + locationSubtype + '\'' +
                ", outcomeStatus=" + outcomeStatus +
                '}';
    }
}
