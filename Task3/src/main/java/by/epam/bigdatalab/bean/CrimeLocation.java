package by.epam.bigdatalab.bean;

import java.util.Objects;

public class CrimeLocation {

    private Double latitude;
    private Double longitude;
    private CrimeLocationStreet street;

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public CrimeLocationStreet getStreet() {
        return street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrimeLocation that = (CrimeLocation) o;
        return Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(street, that.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, street);
    }

    @Override
    public String toString() {
        return "CrimeLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", street=" + street +
                '}';
    }
}
