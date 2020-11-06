package by.epam.bigdatalab.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Objects;

public class CrimeLocation {

    private long id;

    @JSONField(name = "latitude")
    private double latitude;

    @JSONField(name = "longitude")
    private double longitude;

    @JSONField(name = "street")
    private CrimeLocationStreet street;

    public CrimeLocation() {
        id = -1;
        latitude = 1.;
        longitude = 1.;
        street = new CrimeLocationStreet();
    }

    public CrimeLocation(long id, Double latitude, Double longitude, CrimeLocationStreet street) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public CrimeLocationStreet getStreet() {
        return street;
    }

    public void setStreet(CrimeLocationStreet street) {
        this.street = street;
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
