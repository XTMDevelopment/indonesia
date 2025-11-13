package id.xtramile.indonesia.cache;

/**
 * Statistics about the cached Indonesia administrative data.
 * <p>
 * Contains counts of cached entities and the timestamp of the last refresh.
 *
 * @author Rigsto
 * @since 1.0
 */
public class CacheStats {
    /**
     * Number of provinces in the cache.
     */
    private final int provinceCount;
    /**
     * Number of cities in the cache.
     */
    private final int cityCount;
    /**
     * Number of districts in the cache.
     */
    private final int districtCount;
    /**
     * Number of villages in the cache.
     */
    private final int villageCount;
    /**
     * Timestamp of the last cache refresh in milliseconds since epoch.
     */
    private final long lastRefreshTime;

    /**
     * Constructs a new CacheStats object.
     *
     * @param provinceCount   the number of provinces
     * @param cityCount       the number of cities
     * @param districtCount   the number of districts
     * @param villageCount    the number of villages
     * @param lastRefreshTime the last refresh timestamp in milliseconds
     */
    public CacheStats(int provinceCount, int cityCount, int districtCount, int villageCount, long lastRefreshTime) {
        this.provinceCount = provinceCount;
        this.cityCount = cityCount;
        this.districtCount = districtCount;
        this.villageCount = villageCount;
        this.lastRefreshTime = lastRefreshTime;
    }

    /**
     * Gets the number of provinces in the cache.
     *
     * @return the province count
     */
    public int getProvinceCount() {
        return provinceCount;
    }

    /**
     * Gets the number of cities in the cache.
     *
     * @return the city count
     */
    public int getCityCount() {
        return cityCount;
    }

    /**
     * Gets the number of districts in the cache.
     *
     * @return the district count
     */
    public int getDistrictCount() {
        return districtCount;
    }

    /**
     * Gets the number of villages in the cache.
     *
     * @return the village count
     */
    public int getVillageCount() {
        return villageCount;
    }

    /**
     * Gets the timestamp of the last cache refresh.
     *
     * @return the last refresh time in milliseconds since epoch
     */
    public long getLastRefreshTime() {
        return lastRefreshTime;
    }

    @Override
    public String toString() {
        return "CacheStats{" +
                "provinceCount=" + provinceCount +
                ", cityCount=" + cityCount +
                ", districtCount=" + districtCount +
                ", villageCount=" + villageCount +
                ", lastRefreshTime=" + lastRefreshTime +
                '}';
    }
}
