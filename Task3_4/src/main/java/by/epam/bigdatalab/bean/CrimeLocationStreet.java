package by.epam.bigdatalab.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Objects;

public class CrimeLocationStreet {

    @JSONField(name = "id")
    private long id;

    @JSONField(name = "name")
    private String name;

    public CrimeLocationStreet() {

    }

    public CrimeLocationStreet(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrimeLocationStreet that = (CrimeLocationStreet) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "CrimeLocationStreet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
