package id.xtramile.indonesia.cache;

import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryIndonesiaCacheTest {

    private InMemoryIndonesiaCache cache;

    private Province province1;
    private Province province2;
    private City city1;
    private City city2;
    private District district1;
    private District district2;
    private Village village1;
    private Village village2;

    @BeforeEach
    void setUp() {
        cache = new InMemoryIndonesiaCache();

        province1 = new Province(11L, "Jakarta", -6.2088, 106.8456);
        province2 = new Province(12L, "Sumatera Utara", 3.5952, 98.6722);

        city1 = new City(1101L, 11L, "Jakarta Pusat", -6.1818, 106.8318);
        city2 = new City(1201L, 12L, "Medan", 3.5833, 98.6667);

        district1 = new District(110101L, 1101L, "Gambir", -6.1751, 106.8208);
        district2 = new District(120101L, 1201L, "Medan Barat", 3.5842, 98.6756);

        village1 = new Village(1101011001L, 110101L, "Gambir", -6.1751, 106.8208);
        village2 = new Village(1201011001L, 120101L, "Medan Barat", 3.5842, 98.6756);
    }

    @Test
    void testPutProvinces() {
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(11L, province1);
        provinces.put(12L, province2);

        cache.putProvinces(provinces);

        Map<Long, Province> result = cache.getProvinces();
        assertEquals(2, result.size());
        assertEquals(province1, result.get(11L));
        assertEquals(province2, result.get(12L));
        assertTrue(cache.isLoaded());
    }

    @Test
    void testPutProvincesClearsPrevious() {
        Map<Long, Province> provinces1 = new HashMap<>();
        provinces1.put(11L, province1);
        cache.putProvinces(provinces1);

        Map<Long, Province> provinces2 = new HashMap<>();
        provinces2.put(12L, province2);
        cache.putProvinces(provinces2);

        Map<Long, Province> result = cache.getProvinces();
        assertEquals(1, result.size());
        assertEquals(province2, result.get(12L));
    }

    @Test
    void testPutCities() {
        Map<Long, City> cities = new HashMap<>();
        cities.put(1101L, city1);
        cities.put(1201L, city2);

        cache.putCities(cities);

        Map<Long, City> result = cache.getCities();
        assertEquals(2, result.size());
        assertEquals(city1, result.get(1101L));

        Map<Long, List<City>> citiesByProvince = cache.getCitiesByProvince();
        assertEquals(2, citiesByProvince.size());
        assertEquals(1, citiesByProvince.get(11L).size());
        assertEquals(city1, citiesByProvince.get(11L).get(0));
    }

    @Test
    void testPutCitiesMultiplePerProvince() {
        City city3 = new City(1102L, 11L, "Jakarta Selatan", -6.2615, 106.8106);
        Map<Long, City> cities = new HashMap<>();
        cities.put(1101L, city1);
        cities.put(1102L, city3);

        cache.putCities(cities);

        Map<Long, List<City>> citiesByProvince = cache.getCitiesByProvince();
        assertEquals(1, citiesByProvince.size());
        assertEquals(2, citiesByProvince.get(11L).size());
    }

    @Test
    void testPutDistricts() {
        Map<Long, District> districts = new HashMap<>();
        districts.put(110101L, district1);
        districts.put(120101L, district2);

        cache.putDistricts(districts);

        Map<Long, District> result = cache.getDistricts();
        assertEquals(2, result.size());

        Map<Long, List<District>> districtsByCity = cache.getDistrictsByCity();
        assertEquals(2, districtsByCity.size());
        assertEquals(1, districtsByCity.get(1101L).size());
    }

    @Test
    void testPutVillages() {
        Map<Long, Village> villages = new HashMap<>();
        villages.put(1101011001L, village1);
        villages.put(1201011001L, village2);

        cache.putVillages(villages);

        Map<Long, Village> result = cache.getVillages();
        assertEquals(2, result.size());

        Map<Long, List<Village>> villagesByDistrict = cache.getVillagesByDistrict();
        assertEquals(2, villagesByDistrict.size());
        assertEquals(1, villagesByDistrict.get(110101L).size());
    }

    @Test
    void testGetProvincesReturnsCopy() {
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(11L, province1);
        cache.putProvinces(provinces);

        Map<Long, Province> result1 = cache.getProvinces();
        Map<Long, Province> result2 = cache.getProvinces();

        assertNotSame(result1, result2);
        result1.put(99L, province2);
        assertEquals(1, cache.getProvinces().size());
    }

    @Test
    void testGetCitiesReturnsCopy() {
        Map<Long, City> cities = new HashMap<>();
        cities.put(1101L, city1);
        cache.putCities(cities);

        Map<Long, City> result1 = cache.getCities();
        Map<Long, City> result2 = cache.getCities();

        assertNotSame(result1, result2);
    }

    @Test
    void testGetDistrictsReturnsCopy() {
        Map<Long, District> districts = new HashMap<>();
        districts.put(110101L, district1);
        cache.putDistricts(districts);

        Map<Long, District> result1 = cache.getDistricts();
        Map<Long, District> result2 = cache.getDistricts();

        assertNotSame(result1, result2);
    }

    @Test
    void testGetVillagesReturnsCopy() {
        Map<Long, Village> villages = new HashMap<>();
        villages.put(1101011001L, village1);
        cache.putVillages(villages);

        Map<Long, Village> result1 = cache.getVillages();
        Map<Long, Village> result2 = cache.getVillages();

        assertNotSame(result1, result2);
    }

    @Test
    void testRefresh() {
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(11L, province1);
        cache.putProvinces(provinces);
        assertTrue(cache.isLoaded());

        cache.refresh();

        assertFalse(cache.isLoaded());
        assertTrue(cache.getProvinces().isEmpty());
        assertTrue(cache.getCities().isEmpty());
        assertTrue(cache.getDistricts().isEmpty());
        assertTrue(cache.getVillages().isEmpty());
    }

    @Test
    void testIsLoadedFalseWhenEmpty() {
        assertFalse(cache.isLoaded());

        Map<Long, Province> provinces = new HashMap<>();
        cache.putProvinces(provinces);
        assertFalse(cache.isLoaded());
    }

    @Test
    void testIsLoadedTrueAfterPut() {
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(11L, province1);
        cache.putProvinces(provinces);
        assertTrue(cache.isLoaded());
    }

    @Test
    void testGetStats() {
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(11L, province1);
        provinces.put(12L, province2);

        Map<Long, City> cities = new HashMap<>();
        cities.put(1101L, city1);
        cities.put(1201L, city2);

        Map<Long, District> districts = new HashMap<>();
        districts.put(110101L, district1);
        districts.put(120101L, district2);

        Map<Long, Village> villages = new HashMap<>();
        villages.put(1101011001L, village1);
        villages.put(1201011001L, village2);

        cache.putProvinces(provinces);
        cache.putCities(cities);
        cache.putDistricts(districts);
        cache.putVillages(villages);

        CacheStats stats = cache.getStats();
        assertNotNull(stats);
        assertEquals(2, stats.getProvinceCount());
        assertEquals(2, stats.getCityCount());
        assertEquals(2, stats.getDistrictCount());
        assertEquals(2, stats.getVillageCount());
        assertTrue(stats.getLastRefreshTime() > 0);
    }

    @Test
    void testRefreshTimeUpdated() throws InterruptedException {
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(11L, province1);

        cache.putProvinces(provinces);
        long firstTime = cache.getStats().getLastRefreshTime();

        Thread.sleep(10);

        cache.putCities(new HashMap<>());
        long secondTime = cache.getStats().getLastRefreshTime();

        assertTrue(secondTime >= firstTime);
    }
}

