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
                "category='" + category + '\'' + "\n" +
                ", persistentId='" + persistentId + '\'' + "\n" +
                ", month=" + month + "\n" +
                ", location=" + location + "\n" +
                ", context='" + context + '\'' + "\n" +
                ", id=" + id + "\n" +
                ", locationType='" + locationType + '\'' + "\n" +
                ", locationSubtype='" + locationSubtype + '\'' + "\n" +
                ", outcomeStatus=" + outcomeStatus + "\n" +
                '}';
    }
}
