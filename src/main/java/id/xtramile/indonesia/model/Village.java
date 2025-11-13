package id.xtramile.indonesia.model;

/**
 * Represents a village (kelurahan/desa) in Indonesia.
 * <p>
 * A village is the fourth-level administrative division, belonging to a district.
 * Each village has a unique code, name, parent district code, and geographic coordinates.
 *
 * @author Rigsto
 * @since 1.0
 */
public class Village {
    /**
     * The unique village code (10 digits).
     */
    private final long code;
    /**
     * The code of the parent district.
     */
    private final long districtCode;
    /**
     * The name of the village.
     */
    private final String name;
    /**
     * The latitude coordinate of the village.
     */
    private final double latitude;
    /**
     * The longitude coordinate of the village.
     */
    private final double longitude;

    /**
     * Constructs a new Village.
     *
     * @param code         the unique village code
     * @param districtCode the code of the parent district
     * @param name         the name of the village
     * @param latitude     the latitude coordinate
     * @param longitude    the longitude coordinate
     */
    public Village(long code, long districtCode, String name, double latitude, double longitude) {
        this.code = code;
        this.districtCode = districtCode;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the village code.
     *
     * @return the village code
     */
    public long getCode() {
        return code;
    }

    /**
     * Gets the parent district code.
     *
     * @return the district code
     */
    public long getDistrictCode() {
        return districtCode;
    }

    /**
     * Gets the village name.
     *
     * @return the village name
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
        Village village = (Village) o;
        return code == village.code;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(code);
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
