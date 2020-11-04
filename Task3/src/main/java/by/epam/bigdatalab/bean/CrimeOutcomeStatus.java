package by.epam.bigdatalab.bean;

import java.util.Date;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrimeOutcomeStatus that = (CrimeOutcomeStatus) o;
        return Objects.equals(category, that.category) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, date);
    }

    @Override
    public String toString() {
        return "CrimeOutcomeStatus{" +
                "category='" + category + '\'' +
                ", date=" + date +
                '}';
    }
}
