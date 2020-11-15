package by.epam.bigdatalab.bean;

import by.epam.bigdatalab.service.DateUtil;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.Objects;


public class Crime {

    @JSONField(name = "id")
    private long id;

    @JSONField(name = "category")
    private String category;

    @JSONField(name = "persistent_id")
    private String persistentId;

    @JSONField(name = "month", format = "yyyy-MM")
    private Date month;

    @JSONField(name = "context")
    private String context;

    @JSONField(name = "location_type")
    private String locationType;

    @JSONField(name = "location_subtype")
    private String locationSubtype;

    @JSONField(name = "location")
    private Location location;

    @JSONField(name = "outcome_status")
    private OutcomeStatus outcomeStatus;

    public Crime() {

    }

    public Crime(long id, String category, String persistentId, Date month,
                 String context, String locationType, String locationSubtype,
                 Location location, OutcomeStatus outcomeStatus) {
        this.id = id;
        this.category = category;
        this.persistentId = persistentId;
        this.month = month;
        this.context = context;
        this.locationType = locationType;
        this.locationSubtype = locationSubtype;
        this.location = location;
        this.outcomeStatus = outcomeStatus;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public OutcomeStatus getOutcomeStatus() {
        return outcomeStatus;
    }

    public void setOutcomeStatus(OutcomeStatus outcomeStatus) {
        this.outcomeStatus = outcomeStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crime crime = (Crime) o;

        return id == crime.id &&
                category.equals(crime.category) &&
                persistentId.equals(crime.persistentId) &&
                context.equals(crime.context) &&
                locationType.equals(crime.locationType) &&
                locationSubtype.equals(crime.locationSubtype) &&
                month.equals(crime.month) &&
                location.equals(crime.location) &&
                Objects.equals(outcomeStatus, crime.outcomeStatus) &&
                Objects.equals(location, crime.location) &&
                Objects.equals(outcomeStatus, crime.outcomeStatus);
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
                ", month=" + DateUtil.formatDate(month) +
                ", location=" + location +
                ", context='" + context + '\'' +
                ", id=" + id +
                ", locationType='" + locationType + '\'' +
                ", locationSubtype='" + locationSubtype + '\'' +
                ", outcomeStatus=" + outcomeStatus +
                '}';
    }
}
