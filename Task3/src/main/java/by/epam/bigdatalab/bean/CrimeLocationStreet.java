package by.epam.bigdatalab.bean;

import java.util.Objects;

public class CrimeLocationStreet {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
