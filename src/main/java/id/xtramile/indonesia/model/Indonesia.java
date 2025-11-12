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
     * @param province province (may be null)
     * @param city     city (may be null)
     * @param district district (may be null)
     * @param village  village (may be null)
     */
    public Indonesia(Province province, City city, District district, Village village) {
        this.province = province;
        this.city = city;
        this.district = district;
        this.village = village;
    }

    /**
     * @return province, or null if not set
     */
    public Province getProvince() {
        return province;
    }

    /**
     * @return city, or null if not set
     */
    public City getCity() {
        return city;
    }

    /**
     * @return district, or null if not set
     */
    public District getDistrict() {
        return district;
    }

    /**
     * @return village, or null if not set
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

