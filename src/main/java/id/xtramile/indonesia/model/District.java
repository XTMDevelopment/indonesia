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
     * Constructs a new District.
     *
     * @param code      the unique district code
     * @param cityCode  the code of the parent city
     * @param name      the name of the district
     * @param latitude  the latitude coordinate
     * @param longitude the longitude coordinate
     */
    public District(long code, long cityCode, String name, double latitude, double longitude) {
        this.code = code;
        this.cityCode = cityCode;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the district code.
     *
     * @return the district code
     */
    public long getCode() {
        return code;
    }

    /**
     * Gets the parent city code.
     *
     * @return the city code
     */
    public long getCityCode() {
        return cityCode;
    }

    /**
     * Gets the district name.
     *
     * @return the district name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the latitude coordinate.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude coordinate.
     *
     * @return the longitude
     */
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
