package id.xtramile.indonesia.model;

/**
 * Represents a village (kelurahan/desa) in Indonesia.
 * <p>
 * A village is the fourth-level administrative division, belonging to a district.
 * Each village has a unique code, name, parent district code, and geographic coordinates.
 *
 * @author Rigsto
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
     * @param code         unique village code
     * @param districtCode parent district code
     * @param name         village name
     * @param latitude     latitude coordinate
     * @param longitude    longitude coordinate
     */
    public Village(long code, long districtCode, String name, double latitude, double longitude) {
        this.code = code;
        this.districtCode = districtCode;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getCode() {
        return code;
    }

    public long getDistrictCode() {
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
