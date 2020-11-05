package by.epam.bigdatalab.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.Objects;


public class Crime {

    @JSONField(name = "id")
    private int id;

    @JSONField(name = "category")
    private String category;

    @JSONField(name = "persistent_id")
    private String persistentId;

    @JSONField(name = "month", format="yyyy-MM")
    private Date month;

    @JSONField(name = "context")
    private String context;

    @JSONField(name = "location_type")
    private String locationType;

    @JSONField(name = "location_type")
    private String locationSubtype;

    @JSONField(name = "location")
    private CrimeLocation location;

    @JSONField(name = "outcome_status")
    private CrimeOutcomeStatus outcomeStatus;

    public Crime(){
        id = -1;
        category = "category";
        persistentId = "persistentId";
        month = new Date();
        context = "context";

        locationType = "locationType";
        locationSubtype = "locationSubtype";

        location = new CrimeLocation();
        outcomeStatus = new CrimeOutcomeStatus();
    }

    public Crime(int id, String category, String persistentId, Date month,
                 String context, String locationType, String locationSubtype,
                 CrimeLocation location, CrimeOutcomeStatus crimeOutcomeStatus){
        this.id = id;
        this.category = category;
        this.persistentId = persistentId;
        this.month = month;
        this.context = context;
        this.locationType = locationType;
        this.locationSubtype = locationSubtype;
        this.location = location;
        this.outcomeStatus = crimeOutcomeStatus;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPersistentId() {
        return persistentId;
    }

    public void setPersistentId(String persistentId) {
        this.persistentId = persistentId;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLocationSubtype() {
        return locationSubtype;
    }

    public void setLocationSubtype(String locationSubtype) {
        this.locationSubtype = locationSubtype;
    }

    public CrimeLocation getLocation() {
        return location;
    }

    public void setLocation(CrimeLocation location) {
        this.location = location;
    }

    public CrimeOutcomeStatus getOutcomeStatus() {
        return outcomeStatus;
    }

    public void setOutcomeStatus(CrimeOutcomeStatus outcomeStatus) {
        this.outcomeStatus = outcomeStatus;
    }

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
                Objects.equals(locationSubtype, crime.locationSubtype);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, persistentId, month, location, context, id, locationType, locationSubtype);
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
