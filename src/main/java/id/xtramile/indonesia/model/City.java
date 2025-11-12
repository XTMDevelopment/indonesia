package id.xtramile.indonesia.model;

/**
 * Represents a city (kabupaten/kota) in Indonesia.
 * <p>
 * A city is the second-level administrative division, belonging to a province.
 * Each city has a unique code, name, parent province code, and geographic coordinates.
 *
 * @author Rigsto
 */
public class City {
    /**
     * The unique city code (4 digits).
     */
    private final long code;
    /**
     * The code of the parent province.
     */
    private final long provinceCode;
    /**
     * The name of the city.
     */
    private final String name;
    /**
     * The latitude coordinate of the city.
     */
    private final double latitude;
    /**
     * The longitude coordinate of the city.
     */
    private final double longitude;

    /**
     * @param code         unique city code
     * @param provinceCode parent province code
     * @param name         city name
     * @param latitude     latitude coordinate
     * @param longitude    longitude coordinate
     */
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
