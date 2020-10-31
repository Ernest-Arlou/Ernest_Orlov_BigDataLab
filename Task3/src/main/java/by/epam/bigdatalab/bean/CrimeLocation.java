package by.epam.bigdatalab.bean;

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
    public String toString() {
        return "CrimeLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", street=" + street +
                '}';
    }
}
