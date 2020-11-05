package by.epam.bigdatalab.bean;

import java.util.Date;
import java.util.Objects;

public class CrimeOutcomeStatus {

    private  int id;
    private String category;
    private Date date;

    public String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    public CrimeOutcomeStatus(){
        id = -1;
        category = "category";
        date = new Date();
    }

    public CrimeOutcomeStatus(int id, String category, Date date){
        this.id = id;
        this.category = category;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(Date date) {
        this.date = date;
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
