package id.xtramile.indonesia.cache;

public class CacheStats {
    private final int provinceCount;
    private final int cityCount;
    private final int districtCount;
    private final int villageCount;
    private final long lastRefreshTime;

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
