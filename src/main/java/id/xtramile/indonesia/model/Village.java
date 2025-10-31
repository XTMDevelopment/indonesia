package id.xtramile.indonesia.model;

public class Village {
    private final int code;
    private final int districtCode;
    private final String name;
    private final double latitude;
    private final double longitude;

    public Village(int code, int districtCode, String name, double latitude, double longitude) {
        this.code = code;
        this.districtCode = districtCode;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getCode() {
        return code;
    }

    public int getDistrictCode() {
        return districtCode;
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
        Village village = (Village) o;
        return code == village.code;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(code);
    }

    @Override
    public String toString() {
        return "Village{" +
                "code=" + code +
                ", districtCode=" + districtCode +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
