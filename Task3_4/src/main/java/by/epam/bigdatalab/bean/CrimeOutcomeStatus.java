package by.epam.bigdatalab.bean;

import by.epam.bigdatalab.Util;
import by.epam.bigdatalab.service.DateUtil;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.Objects;

public class CrimeOutcomeStatus {

    private long id;

    @JSONField(name = "category")
    private String category;

    @JSONField(name = "date", format = "yyyy-MM")
    private Date date;

    public CrimeOutcomeStatus() {

    }

    public CrimeOutcomeStatus(long id, String category, Date date) {
        this.id = id;
        this.category = category;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
                ", date=" + DateUtil.formatDate(date) +
                '}';
    }
}
