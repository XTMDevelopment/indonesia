package id.xtramile.indonesia.model;

/**
 * Represents a complete Indonesia administrative hierarchy.
 * <p>
 * This class contains the full administrative structure from province down to village.
 * It can be built from any level of the hierarchy, with parent levels automatically populated.
 *
 * @author Rigsto
 */
public class Indonesia {
    /**
     * The province in the hierarchy.
     */
    private final Province province;
    /**
     * The city in the hierarchy.
     */
    private final City city;
    /**
     * The district in the hierarchy.
     */
    private final District district;
    /**
     * The village in the hierarchy.
     */
    private final Village village;

    /**
     * Constructs a new Indonesia object with the complete administrative hierarchy.
     *
     * @param province the province (may be null)
     * @param city     the city (may be null)
     * @param district the district (may be null)
     * @param village  the village (may be null)
     */
    public Indonesia(Province province, City city, District district, Village village) {
        this.province = province;
        this.city = city;
        this.district = district;
        this.village = village;
    }

    /**
     * Gets the province.
     *
     * @return the province, or null if not set
     */
    public Province getProvince() {
        return province;
    }

    /**
     * Gets the city.
     *
     * @return the city, or null if not set
     */
    public City getCity() {
        return city;
    }

    /**
     * Gets the district.
     *
     * @return the district, or null if not set
     */
    public District getDistrict() {
        return district;
    }

    /**
     * Gets the village.
     *
     * @return the village, or null if not set
     */
    public Village getVillage() {
        return village;
    }

    @Override
    public String toString() {
        return "Indonesia{" +
                "province=" + province +
                ", city=" + city +
                ", district=" + district +
                ", village=" + village +
                '}';
    }
}

