package by.epam.bigdatalab.bean;

import java.util.Date;

public class CrimeOutcomeStatus {

    private String category;
    private Date date;

    public String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }


    @Override
    public String toString() {
        return "CrimeOutcomeStatus{" +
                "category='" + category + '\'' +
                ", date=" + date +
                '}';
    }
}
