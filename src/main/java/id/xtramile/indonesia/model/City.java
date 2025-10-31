package id.xtramile.indonesia.model;

public class City {
    private final int code;
    private final int provinceCode;
    private final String name;
    private final double latitude;
    private final double longitude;

    public City(int code, int provinceCode, String name, double latitude, double longitude) {
        this.code = code;
        this.provinceCode = provinceCode;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getCode() {
        return code;
    }

    public int getProvinceCode() {
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
        return Integer.hashCode(code);
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
