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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LoaderCacheIntegrationTest {

    private IndonesiaDataLoader loader;
    private IndonesiaDataCache cache;

    @BeforeEach
    void setUp() {
        loader = new CsvIndonesiaDataLoader();
        cache = new InMemoryIndonesiaCache();
    }

    @Test
    void testLoadAndCacheProvinces() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();

        assertFalse(provinces.isEmpty());

        cache.putProvinces(provinces);

        Map<Long, Province> cached = cache.getProvinces();
        assertEquals(provinces.size(), cached.size());

        for (Province province : provinces.values()) {
            Province cachedProvince = cached.get(province.getCode());
            assertNotNull(cachedProvince);
            assertEquals(province, cachedProvince);
        }
    }

    @Test
    void testLoadAndCacheAllEntities() throws DataLoadException {
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
        assertEquals(provinces.size(), stats.getProvinceCount());
        assertEquals(cities.size(), stats.getCityCount());
        assertEquals(districts.size(), stats.getDistrictCount());
        assertEquals(villages.size(), stats.getVillageCount());
    }

    @Test
    void testRelationshipConsistencyBetweenLoaderAndCache() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();
        Map<Long, City> cities = loader.loadCities();

        cache.putProvinces(provinces);
        cache.putCities(cities);

        Map<Long, List<City>> citiesByProvince = cache.getCitiesByProvince();

        for (City city : cities.values()) {
            assertTrue(cache.getProvinces().containsKey(city.getProvinceCode()),
                    "City " + city.getCode() + " references province " + city.getProvinceCode() + " not in cache");

            List<City> provinceCities = citiesByProvince.get(city.getProvinceCode());
            assertNotNull(provinceCities);
            assertTrue(provinceCities.contains(city));
        }
    }

    @Test
    void testFullHierarchicalChainFromLoaderToCache() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();
        Map<Long, City> cities = loader.loadCities();
        Map<Long, District> districts = loader.loadDistricts();
        Map<Long, Village> villages = loader.loadVillages();

        cache.putProvinces(provinces);
        cache.putCities(cities);
        cache.putDistricts(districts);
        cache.putVillages(villages);

        Province testProvince = provinces.values().iterator().next();
        List<City> provinceCities = cache.getCitiesByProvince().get(testProvince.getCode());

        if (provinceCities != null && !provinceCities.isEmpty()) {
            City testCity = provinceCities.get(0);
            List<District> cityDistricts = cache.getDistrictsByCity().get(testCity.getCode());

            if (cityDistricts != null && !cityDistricts.isEmpty()) {
                District testDistrict = cityDistricts.get(0);
                List<Village> districtVillages = cache.getVillagesByDistrict().get(testDistrict.getCode());

                assertEquals(testProvince.getCode(), testCity.getProvinceCode());
                assertEquals(testCity.getCode(), testDistrict.getCityCode());
                assertEquals(testDistrict.getCode(),
                        districtVillages != null && !districtVillages.isEmpty()
                                ? districtVillages.get(0).getDistrictCode()
                                : -1);

                if (districtVillages != null && !districtVillages.isEmpty()) {
                    Village testVillage = districtVillages.get(0);
                    assertEquals(testDistrict.getCode(), testVillage.getDistrictCode());
                }
            }
        }
    }

    @Test
    void testRefreshAndReload() throws DataLoadException {
        Map<Long, Province> provinces1 = loader.loadProvinces();
        cache.putProvinces(provinces1);

        assertTrue(cache.isLoaded());
        assertEquals(provinces1.size(), cache.getStats().getProvinceCount());

        cache.refresh();
        assertFalse(cache.isLoaded());
        assertTrue(cache.getProvinces().isEmpty());

        Map<Long, Province> provinces2 = loader.loadProvinces();
        cache.putProvinces(provinces2);

        assertTrue(cache.isLoaded());
        assertEquals(provinces2.size(), cache.getStats().getProvinceCount());

        assertEquals(provinces1.size(), provinces2.size());
        assertEquals(provinces1.keySet(), provinces2.keySet());
    }

    @Test
    void testMultipleLoadCacheCycles() throws DataLoadException {
        for (int i = 0; i < 3; i++) {
            Map<Long, Province> provinces = loader.loadProvinces();
            cache.putProvinces(provinces);

            assertTrue(cache.isLoaded());
            assertEquals(provinces.size(), cache.getStats().getProvinceCount());

            if (i < 2) {
                cache.refresh();
            }
        }
    }

    @Test
    void testDataIntegrityAfterMultipleOperations() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();
        Map<Long, City> cities = loader.loadCities();
        Map<Long, District> districts = loader.loadDistricts();
        Map<Long, Village> villages = loader.loadVillages();

        cache.putProvinces(provinces);
        cache.putCities(cities);
        cache.putDistricts(districts);
        cache.putVillages(villages);

        Map<Long, Province> cachedProvinces = cache.getProvinces();
        Map<Long, City> cachedCities = cache.getCities();
        Map<Long, District> cachedDistricts = cache.getDistricts();
        Map<Long, Village> cachedVillages = cache.getVillages();

        for (int i = 0; i < 5; i++) {
            Map<Long, Province> provinces1 = cache.getProvinces();
            Map<Long, City> cities1 = cache.getCities();
            Map<Long, District> districts1 = cache.getDistricts();
            Map<Long, Village> villages1 = cache.getVillages();

            assertEquals(cachedProvinces.size(), provinces1.size());
            assertEquals(cachedCities.size(), cities1.size());
            assertEquals(cachedDistricts.size(), districts1.size());
            assertEquals(cachedVillages.size(), villages1.size());
        }
    }

    @Test
    void testIndexConsistencyAfterFullLoad() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();
        Map<Long, City> cities = loader.loadCities();
        Map<Long, District> districts = loader.loadDistricts();
        Map<Long, Village> villages = loader.loadVillages();

        cache.putProvinces(provinces);
        cache.putCities(cities);
        cache.putDistricts(districts);
        cache.putVillages(villages);

        Map<Long, List<City>> citiesByProvince = cache.getCitiesByProvince();
        long totalIndexedCities = citiesByProvince.values().stream()
                .mapToLong(List::size)
                .sum();
        assertEquals(cities.size(), totalIndexedCities);

        Map<Long, List<District>> districtsByCity = cache.getDistrictsByCity();
        long totalIndexedDistricts = districtsByCity.values().stream()
                .mapToLong(List::size)
                .sum();
        assertEquals(districts.size(), totalIndexedDistricts);

        Map<Long, List<Village>> villagesByDistrict = cache.getVillagesByDistrict();
        long totalIndexedVillages = villagesByDistrict.values().stream()
                .mapToLong(List::size)
                .sum();
        assertEquals(villages.size(), totalIndexedVillages);
    }
}
