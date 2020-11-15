package by.epam.bigdatalab.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Objects;

public class Location {

    private long id;

    @JSONField(name = "latitude")
    private double latitude;

    @JSONField(name = "longitude")
    private double longitude;

    @JSONField(name = "street")
    private Street street;

    public Location() {
    }

    public Location(long id, Double latitude, Double longitude, Street street) {
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

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location that = (Location) o;

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
