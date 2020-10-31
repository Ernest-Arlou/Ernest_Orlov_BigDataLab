package by.epam.bigdatalab.bean;

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
    public String toString() {
        return "CrimeLocationStreet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
