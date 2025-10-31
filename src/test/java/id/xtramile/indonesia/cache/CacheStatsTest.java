package id.xtramile.indonesia.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheStatsTest {

    @Test
    void testConstructorAndGetters() {
        long refreshTime = System.currentTimeMillis();
        CacheStats stats = new CacheStats(10, 20, 30, 40, refreshTime);

        assertEquals(10, stats.getProvinceCount());
        assertEquals(20, stats.getCityCount());
        assertEquals(30, stats.getDistrictCount());
        assertEquals(40, stats.getVillageCount());
        assertEquals(refreshTime, stats.getLastRefreshTime());
    }

    @Test
    void testToString() {
        CacheStats stats = new CacheStats(1, 2, 3, 4, 1000L);
        String result = stats.toString();

        assertNotNull(result);
        assertTrue(result.contains("CacheStats"));
        assertTrue(result.contains("provinceCount=1"));
        assertTrue(result.contains("cityCount=2"));
        assertTrue(result.contains("districtCount=3"));
        assertTrue(result.contains("villageCount=4"));
        assertTrue(result.contains("lastRefreshTime=1000"));
    }
}

