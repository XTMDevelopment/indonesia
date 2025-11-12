package id.xtramile.indonesia.integration;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.cache.InMemoryIndonesiaCache;
import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.loader.CsvIndonesiaDataLoader;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IndonesiaDataCacheIntegrationTest {

    private IndonesiaDataCache cache;
    private IndonesiaDataLoader loader;

    @BeforeEach
    void setUp() {
        cache = new InMemoryIndonesiaCache();
        loader = new CsvIndonesiaDataLoader();
    }

    @Test
    void testPutAndGetProvinces() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();

        cache.putProvinces(provinces);

        Map<Long, Province> cached = cache.getProvinces();
        assertNotNull(cached);
        assertEquals(provinces.size(), cached.size());
        assertEquals(provinces.keySet(), cached.keySet());

        for (Long code : provinces.keySet()) {
            assertEquals(provinces.get(code), cached.get(code));
        }
    }

    @Test
    void testPutAndGetCities() throws DataLoadException {
        Map<Long, City> cities = loader.loadCities();

        cache.putCities(cities);

        Map<Long, City> cached = cache.getCities();
        assertNotNull(cached);
        assertEquals(cities.size(), cached.size());
        assertEquals(cities.keySet(), cached.keySet());

        Map<Long, List<City>> citiesByProvince = cache.getCitiesByProvince();
        assertNotNull(citiesByProvince);
        assertFalse(citiesByProvince.isEmpty());

        for (City city : cities.values()) {
            List<City> provinceCities = citiesByProvince.get(city.getProvinceCode());
            assertNotNull(provinceCities);
            assertTrue(provinceCities.contains(city));
        }
    }

    @Test
    void testPutAndGetDistricts() throws DataLoadException {
        Map<Long, District> districts = loader.loadDistricts();

        cache.putDistricts(districts);

        Map<Long, District> cached = cache.getDistricts();
        assertNotNull(cached);
        assertEquals(districts.size(), cached.size());

        Map<Long, List<District>> districtsByCity = cache.getDistrictsByCity();
        assertNotNull(districtsByCity);
        assertFalse(districtsByCity.isEmpty());

        for (District district : districts.values()) {
            List<District> cityDistricts = districtsByCity.get(district.getCityCode());
            assertNotNull(cityDistricts);
            assertTrue(cityDistricts.contains(district));
        }
    }

    @Test
    void testPutAndGetVillages() throws DataLoadException {
        Map<Long, Village> villages = loader.loadVillages();

        cache.putVillages(villages);

        Map<Long, Village> cached = cache.getVillages();
        assertNotNull(cached);
        assertEquals(villages.size(), cached.size());

        Map<Long, List<Village>> villagesByDistrict = cache.getVillagesByDistrict();
        assertNotNull(villagesByDistrict);
        assertFalse(villagesByDistrict.isEmpty());

        for (Village village : villages.values()) {
            List<Village> districtVillages = villagesByDistrict.get(village.getDistrictCode());
            assertNotNull(districtVillages);
            assertTrue(districtVillages.contains(village));
        }
    }

    @Test
    void testFullDataLoadAndCache() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();
        Map<Long, City> cities = loader.loadCities();
        Map<Long, District> districts = loader.loadDistricts();
        Map<Long, Village> villages = loader.loadVillages();

        cache.putProvinces(provinces);
        cache.putCities(cities);
        cache.putDistricts(districts);
        cache.putVillages(villages);

        assertTrue(cache.isLoaded());

        CacheStats stats = cache.getStats();
        assertNotNull(stats);
        assertEquals(provinces.size(), stats.getProvinceCount());
        assertEquals(cities.size(), stats.getCityCount());
        assertEquals(districts.size(), stats.getDistrictCount());
        assertEquals(villages.size(), stats.getVillageCount());
        assertTrue(stats.getLastRefreshTime() > 0);
    }

    @Test
    void testRefreshClearsCache() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();
        Map<Long, City> cities = loader.loadCities();

        cache.putProvinces(provinces);
        cache.putCities(cities);

        assertTrue(cache.isLoaded());
        assertFalse(cache.getProvinces().isEmpty());
        assertFalse(cache.getCities().isEmpty());

        cache.refresh();

        assertFalse(cache.isLoaded());
        assertTrue(cache.getProvinces().isEmpty());
        assertTrue(cache.getCities().isEmpty());
        assertTrue(cache.getDistricts().isEmpty());
        assertTrue(cache.getVillages().isEmpty());
    }

    @Test
    void testPutOverwritesPreviousData() throws DataLoadException {
        Map<Long, Province> provinces1 = loader.loadProvinces();
        cache.putProvinces(provinces1);

        int initialSize = cache.getProvinces().size();
        assertTrue(initialSize > 0);

        Long firstKey = provinces1.keySet().iterator().next();
        Province firstProvince = provinces1.values().iterator().next();
        Map<Long, Province> provinces2 = new HashMap<>();
        provinces2.put(firstKey, firstProvince);
        cache.putProvinces(provinces2);

        Map<Long, Province> cached = cache.getProvinces();
        assertEquals(1, cached.size());
        assertEquals(provinces2.keySet(), cached.keySet());
    }

    @Test
    void testIndexedMapsAreUpdatedOnPut() throws DataLoadException {
        Map<Long, City> cities = loader.loadCities();
        cache.putCities(cities);

        Map<Long, List<City>> citiesByProvince = cache.getCitiesByProvince();
        assertFalse(citiesByProvince.isEmpty());

        long totalIndexedCities = citiesByProvince.values().stream()
                .mapToLong(List::size)
                .sum();

        assertEquals(cities.size(), totalIndexedCities);
    }

    @Test
    void testCacheStatsUpdateOnMultiplePuts() throws DataLoadException, InterruptedException {
        Map<Long, Province> provinces = loader.loadProvinces();
        CacheStats stats1 = cache.getStats();
        long initialTime = stats1.getLastRefreshTime();

        cache.putProvinces(provinces);

        CacheStats stats2 = cache.getStats();
        assertTrue(stats2.getLastRefreshTime() >= initialTime);
        assertEquals(provinces.size(), stats2.getProvinceCount());

        Thread.sleep(10);

        Map<Long, City> cities = loader.loadCities();
        cache.putCities(cities);

        CacheStats stats3 = cache.getStats();
        assertTrue(stats3.getLastRefreshTime() > stats2.getLastRefreshTime());
        assertEquals(cities.size(), stats3.getCityCount());
    }

    @Test
    void testConcurrentAccess() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();
        cache.putProvinces(provinces);

        Map<Long, Province> copy1 = cache.getProvinces();
        Map<Long, Province> copy2 = cache.getProvinces();

        assertNotSame(copy1, copy2);
        assertEquals(copy1, copy2);

        if (!copy1.isEmpty()) {
            Long firstKey = copy1.keySet().iterator().next();
            copy1.remove(firstKey);

            Map<Long, Province> copy3 = cache.getProvinces();
            assertTrue(copy3.containsKey(firstKey));
            assertEquals(provinces.size(), copy3.size());
        }
    }

    @Test
    void testHierarchicalIndexConsistency() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();
        Map<Long, City> cities = loader.loadCities();
        Map<Long, District> districts = loader.loadDistricts();
        Map<Long, Village> villages = loader.loadVillages();

        cache.putProvinces(provinces);
        cache.putCities(cities);
        cache.putDistricts(districts);
        cache.putVillages(villages);

        Map<Long, List<City>> citiesByProvince = cache.getCitiesByProvince();
        for (City city : cities.values()) {
            List<City> provinceCities = citiesByProvince.get(city.getProvinceCode());
            assertNotNull(provinceCities, "No cities found for province " + city.getProvinceCode());
            assertTrue(provinceCities.contains(city));
        }

        Map<Long, List<District>> districtsByCity = cache.getDistrictsByCity();
        for (District district : districts.values()) {
            List<District> cityDistricts = districtsByCity.get(district.getCityCode());
            assertNotNull(cityDistricts, "No districts found for city " + district.getCityCode());
            assertTrue(cityDistricts.contains(district));
        }

        Map<Long, List<Village>> villagesByDistrict = cache.getVillagesByDistrict();
        for (Village village : villages.values()) {
            List<Village> districtVillages = villagesByDistrict.get(village.getDistrictCode());
            assertNotNull(districtVillages, "No villages found for district " + village.getDistrictCode());
            assertTrue(districtVillages.contains(village));
        }
    }

    @Test
    void testCacheIsLoadedAfterDataLoad() throws DataLoadException {
        assertFalse(cache.isLoaded());

        Map<Long, Province> provinces = loader.loadProvinces();
        cache.putProvinces(provinces);

        assertTrue(cache.isLoaded());

        cache.refresh();
        assertFalse(cache.isLoaded());

        cache.putProvinces(new HashMap<>());
        assertFalse(cache.isLoaded());
    }
}
