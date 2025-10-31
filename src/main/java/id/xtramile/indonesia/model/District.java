package id.xtramile.indonesia.model;

public class District {
    private final long code;
    private final long cityCode;
    private final String name;
    private final double latitude;
    private final double longitude;

    public District(long code, long cityCode, String name, double latitude, double longitude) {
        this.code = code;
        this.cityCode = cityCode;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getCode() {
        return code;
    }

    public long getCityCode() {
        return cityCode;
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
        District district = (District) o;
        return code == district.code;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(code);
    }

    @Override
    public String toString() {
        return "District{" +
                "code=" + code +
                ", cityCode=" + cityCode +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
