package id.xtramile.indonesia.cache;

/**
 * Statistics about the cached Indonesia administrative data.
 * <p>
 * Contains counts of cached entities and the timestamp of the last refresh.
 *
 * @author Rigsto
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
     * @param provinceCount   number of provinces
     * @param cityCount       number of cities
     * @param districtCount   number of districts
     * @param villageCount    number of villages
     * @param lastRefreshTime last refresh timestamp in milliseconds
     */
    public CacheStats(int provinceCount, int cityCount, int districtCount, int villageCount, long lastRefreshTime) {
        this.provinceCount = provinceCount;
        this.cityCount = cityCount;
        this.districtCount = districtCount;
        this.villageCount = villageCount;
        this.lastRefreshTime = lastRefreshTime;
    }

    public int getProvinceCount() {
        return provinceCount;
    }

    public int getCityCount() {
        return cityCount;
    }

    public int getDistrictCount() {
        return districtCount;
    }

    public int getVillageCount() {
        return villageCount;
    }

    /**
     * @return last refresh time in milliseconds since epoch
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
