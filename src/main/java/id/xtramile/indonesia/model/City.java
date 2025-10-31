package id.xtramile.indonesia.model;

public class City {
    private final long code;
    private final long provinceCode;
    private final String name;
    private final double latitude;
    private final double longitude;

    public City(long code, long provinceCode, String name, double latitude, double longitude) {
        this.code = code;
        this.provinceCode = provinceCode;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getCode() {
        return code;
    }

    public long getProvinceCode() {
        return provinceCode;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return code == city.code;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(code);
    }

    @Override
    public String toString() {
        return "City{" +
                "code=" + code +
                ", provinceCode=" + provinceCode +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
