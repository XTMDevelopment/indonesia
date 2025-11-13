package id.xtramile.indonesia.model;

/**
 * Represents a province in Indonesia.
 * <p>
 * A province is the first-level administrative division in Indonesia.
 * Each province has a unique code, name, and geographic coordinates.
 *
 * @author Rigsto
 * @since 1.0
 */
public class Province {
    /**
     * The unique province code (2 digits).
     */
    private final long code;
    /**
     * The name of the province.
     */
    private final String name;
    /**
     * The latitude coordinate of the province.
     */
    private final double latitude;
    /**
     * The longitude coordinate of the province.
     */
    private final double longitude;

    /**
     * Constructs a new Province.
     *
     * @param code      the unique province code
     * @param name      the name of the province
     * @param latitude  the latitude coordinate
     * @param longitude the longitude coordinate
     */
    public Province(long code, String name, double latitude, double longitude) {
        this.code = code;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the province code.
     *
     * @return the province code
     */
    public long getCode() {
        return code;
    }

    /**
     * Gets the province name.
     *
     * @return the province name
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
        Province province = (Province) o;
        return code == province.code;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(code);
    }

    @Override
    public String toString() {
        return "Province{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
