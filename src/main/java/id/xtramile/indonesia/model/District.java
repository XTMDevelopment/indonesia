package id.xtramile.indonesia.model;

/**
 * Represents a district (kecamatan) in Indonesia.
 * <p>
 * A district is the third-level administrative division, belonging to a city.
 * Each district has a unique code, name, parent city code, and geographic coordinates.
 *
 * @author Rigsto
 */
public class District {
    /**
     * The unique district code (6 digits).
     */
    private final long code;
    /**
     * The code of the parent city.
     */
    private final long cityCode;
    /**
     * The name of the district.
     */
    private final String name;
    /**
     * The latitude coordinate of the district.
     */
    private final double latitude;
    /**
     * The longitude coordinate of the district.
     */
    private final double longitude;

    /**
     * @param code      unique district code
     * @param cityCode  parent city code
     * @param name      district name
     * @param latitude  latitude coordinate
     * @param longitude longitude coordinate
     */
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
