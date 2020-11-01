package by.epam.bigdatalab.bean;

import java.util.Date;


public class Crime {

    private String category;
    private String persistentId;
    private Date month;

    private CrimeLocation location;

    private String context;
    private Long id;
    private String locationType;
    private String locationSubtype;
    private CrimeOutcomeStatus outcomeStatus;

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
