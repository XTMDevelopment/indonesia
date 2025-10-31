package id.xtramile.indonesia.model;

public class District {
    private final int code;
    private final int cityCode;
    private final String name;
    private final double latitude;
    private final double longitude;

    public District(int code, int cityCode, String name, double latitude, double longitude) {
        this.code = code;
        this.cityCode = cityCode;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getCode() {
        return code;
    }

    public int getCityCode() {
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
        return Integer.hashCode(code);
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
