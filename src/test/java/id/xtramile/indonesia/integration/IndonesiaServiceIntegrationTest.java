package id.xtramile.indonesia.integration;

import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.cache.InMemoryIndonesiaCache;
import id.xtramile.indonesia.loader.CsvIndonesiaDataLoader;
import id.xtramile.indonesia.model.*;
import id.xtramile.indonesia.service.DefaultIndonesiaService;
import id.xtramile.indonesia.util.IndonesiaServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IndonesiaServiceIntegrationTest {

    private IndonesiaService service;

    @BeforeEach
    void setUp() {
        service = IndonesiaServiceFactory.createDefault();
    }

    @Test
    void testServiceInitialization() {
        assertNotNull(service);
        assertTrue(service.isDataLoaded());

        CacheStats stats = service.getCacheStats();
        assertNotNull(stats);
        assertTrue(stats.getProvinceCount() > 0);
        assertTrue(stats.getCityCount() > 0);
        assertTrue(stats.getDistrictCount() > 0);
        assertTrue(stats.getVillageCount() > 0);
    }

    @Test
    void testFindProvinceWithRealData() {
        List<Province> allProvinces = service.getAllProvinces();
        assertFalse(allProvinces.isEmpty());

        Province firstProvince = allProvinces.get(0);
        Optional<Province> found = service.findProvince(firstProvince.getCode());

        assertTrue(found.isPresent());
        assertEquals(firstProvince.getCode(), found.get().getCode());
        assertEquals(firstProvince.getName(), found.get().getName());
        assertEquals(firstProvince.getLatitude(), found.get().getLatitude());
        assertEquals(firstProvince.getLongitude(), found.get().getLongitude());
    }

    @Test
    void testGetCitiesByProvinceWithRealData() {
        List<Province> provinces = service.getAllProvinces();
        assertFalse(provinces.isEmpty());

        Province province = provinces.get(0);
        List<City> cities = service.getCitiesByProvince(province.getCode());

        assertNotNull(cities);

        for (City city : cities) {
            assertEquals(province.getCode(), city.getProvinceCode());
            assertTrue(city.getCode() > 0);
            assertNotNull(city.getName());
        }
    }

    @Test
    void testGetDistrictsByCityWithRealData() {
        List<Province> provinces = service.getAllProvinces();
        assertFalse(provinces.isEmpty());

        List<City> cities = service.getCitiesByProvince(provinces.get(0).getCode());
        if (!cities.isEmpty()) {

            City city = cities.get(0);
            List<District> districts = service.getDistrictsByCity(city.getCode());

            assertNotNull(districts);

            for (District district : districts) {
                assertEquals(city.getCode(), district.getCityCode());
                assertTrue(district.getCode() > 0);
                assertNotNull(district.getName());
            }
        }
    }

    @Test
    void testGetVillagesByDistrictWithRealData() {
        List<Province> provinces = service.getAllProvinces();
        assertFalse(provinces.isEmpty());

        List<City> cities = service.getCitiesByProvince(provinces.get(0).getCode());
        if (!cities.isEmpty()) {

            List<District> districts = service.getDistrictsByCity(cities.get(0).getCode());
            if (!districts.isEmpty()) {

                District district = districts.get(0);
                List<Village> villages = service.getVillagesByDistrict(district.getCode());

                assertNotNull(villages);

                for (Village village : villages) {
                    assertEquals(district.getCode(), village.getDistrictCode());
                    assertTrue(village.getCode() > 0);
                    assertNotNull(village.getName());
                }
            }
        }
    }

    @Test
    void testSearchProvincesWithRealData() {
        List<Province> allProvinces = service.getAllProvinces();
        if (!allProvinces.isEmpty()) {

            String searchTerm = allProvinces.get(0).getName().substring(0, 3);
            List<Province> results = service.searchProvinces(searchTerm);

            assertNotNull(results);
            assertFalse(results.isEmpty());

            for (Province province : results) {
                assertTrue(province.getName().toLowerCase().contains(searchTerm.toLowerCase()));
            }
        }
    }

    @Test
    void testSearchCitiesWithRealData() {
        List<City> allCities = service.getAllCities();
        if (!allCities.isEmpty()) {

            String searchTerm = allCities.get(0).getName().substring(0, 3);
            List<City> results = service.searchCities(searchTerm);

            assertNotNull(results);
            assertFalse(results.isEmpty());

            for (City city : results) {
                assertTrue(city.getName().toLowerCase().contains(searchTerm.toLowerCase()));
            }
        }
    }

    @Test
    void testSearchDistrictsWithRealData() {
        List<District> allDistricts = service.getAllDistricts();
        if (!allDistricts.isEmpty()) {

            String searchTerm = allDistricts.get(0).getName().substring(0, Math.min(3, allDistricts.get(0).getName().length()));
            List<District> results = service.searchDistricts(searchTerm);

            assertNotNull(results);
            assertFalse(results.isEmpty());

            for (District district : results) {
                assertTrue(district.getName().toLowerCase().contains(searchTerm.toLowerCase()));
            }
        }
    }

    @Test
    void testSearchVillagesWithRealData() {
        List<Village> allVillages = service.getAllVillages();
        if (!allVillages.isEmpty()) {

            String searchTerm = allVillages.get(0).getName().substring(0, Math.min(3, allVillages.get(0).getName().length()));
            List<Village> results = service.searchVillages(searchTerm);

            assertNotNull(results);
            assertFalse(results.isEmpty());

            for (Village village : results) {
                assertTrue(village.getName().toLowerCase().contains(searchTerm.toLowerCase()));
            }
        }
    }

    @Test
    void testBuildFromProvinceWithRealData() {
        List<Province> provinces = service.getAllProvinces();
        if (!provinces.isEmpty()) {

            Province province = provinces.get(0);
            Indonesia indonesia = service.buildFrom(province);

            assertNotNull(indonesia);
            assertEquals(province, indonesia.getProvince());
            assertNull(indonesia.getCity());
            assertNull(indonesia.getDistrict());
            assertNull(indonesia.getVillage());
        }
    }

    @Test
    void testBuildFromCityWithRealData() {
        List<Province> provinces = service.getAllProvinces();
        if (!provinces.isEmpty()) {

            List<City> cities = service.getCitiesByProvince(provinces.get(0).getCode());
            if (!cities.isEmpty()) {

                City city = cities.get(0);
                Indonesia indonesia = service.buildFrom(city);

                assertNotNull(indonesia);
                assertNotNull(indonesia.getProvince());
                assertEquals(city, indonesia.getCity());
                assertEquals(city.getProvinceCode(), indonesia.getProvince().getCode());
                assertNull(indonesia.getDistrict());
                assertNull(indonesia.getVillage());
            }
        }
    }

    @Test
    void testBuildFromDistrictWithRealData() {
        List<Province> provinces = service.getAllProvinces();
        if (!provinces.isEmpty()) {

            List<City> cities = service.getCitiesByProvince(provinces.get(0).getCode());
            if (!cities.isEmpty()) {

                List<District> districts = service.getDistrictsByCity(cities.get(0).getCode());
                if (!districts.isEmpty()) {

                    District district = districts.get(0);
                    Indonesia indonesia = service.buildFrom(district);

                    assertNotNull(indonesia);
                    assertNotNull(indonesia.getProvince());
                    assertNotNull(indonesia.getCity());
                    assertEquals(district, indonesia.getDistrict());
                    assertEquals(district.getCityCode(), indonesia.getCity().getCode());
                    assertNull(indonesia.getVillage());
                }
            }
        }
    }

    @Test
    void testBuildFromVillageWithRealData() {
        List<Province> provinces = service.getAllProvinces();
        if (!provinces.isEmpty()) {

            List<City> cities = service.getCitiesByProvince(provinces.get(0).getCode());
            if (!cities.isEmpty()) {

                List<District> districts = service.getDistrictsByCity(cities.get(0).getCode());
                if (!districts.isEmpty()) {

                    List<Village> villages = service.getVillagesByDistrict(districts.get(0).getCode());
                    if (!villages.isEmpty()) {

                        Village village = villages.get(0);
                        Indonesia indonesia = service.buildFrom(village);

                        assertNotNull(indonesia);
                        assertNotNull(indonesia.getProvince());
                        assertNotNull(indonesia.getCity());
                        assertNotNull(indonesia.getDistrict());
                        assertEquals(village, indonesia.getVillage());
                        assertEquals(village.getDistrictCode(), indonesia.getDistrict().getCode());
                    }
                }
            }
        }
    }

    @Test
    void testGetVillagesByProvinceWithRealData() {
        List<Province> provinces = service.getAllProvinces();
        if (!provinces.isEmpty()) {

            Province province = provinces.get(0);
            List<Village> villages = service.getVillagesByProvince(province.getCode());

            assertNotNull(villages);

            List<City> provinceCities = service.getCitiesByProvince(province.getCode());
            for (City city : provinceCities) {

                List<District> districts = service.getDistrictsByCity(city.getCode());
                for (District district : districts) {

                    List<Village> districtVillages = service.getVillagesByDistrict(district.getCode());
                    for (Village village : districtVillages) {
                        assertTrue(villages.contains(village) ||
                                villages.stream().anyMatch(v -> v.getCode() == village.getCode()));
                    }
                }
            }
        }
    }

    @Test
    void testGetVillagesByCityWithRealData() {
        List<Province> provinces = service.getAllProvinces();
        if (!provinces.isEmpty()) {

            List<City> cities = service.getCitiesByProvince(provinces.get(0).getCode());
            if (!cities.isEmpty()) {

                City city = cities.get(0);
                List<Village> villages = service.getVillagesByCity(city.getCode());

                assertNotNull(villages);

                List<District> districts = service.getDistrictsByCity(city.getCode());
                for (District district : districts) {

                    List<Village> districtVillages = service.getVillagesByDistrict(district.getCode());
                    for (Village village : districtVillages) {
                        assertTrue(villages.contains(village) ||
                                villages.stream().anyMatch(v -> v.getCode() == village.getCode()));
                    }
                }
            }
        }
    }

    @Test
    void testRefreshData() {
        CacheStats statsBefore = service.getCacheStats();
        assertNotNull(statsBefore);

        service.refreshData();

        CacheStats statsAfter = service.getCacheStats();
        assertNotNull(statsAfter);
        assertTrue(service.isDataLoaded());

        assertTrue(statsAfter.getProvinceCount() > 0);
    }

    @Test
    void testHierarchicalDataConsistency() {
        List<Province> provinces = service.getAllProvinces();
        if (!provinces.isEmpty()) {
            Province province = provinces.get(0);
            List<City> cities = service.getCitiesByProvince(province.getCode());

            if (!cities.isEmpty()) {
                City city = cities.get(0);

                assertEquals(province.getCode(), city.getProvinceCode());

                List<District> districts = service.getDistrictsByCity(city.getCode());
                if (!districts.isEmpty()) {

                    District district = districts.get(0);

                    assertEquals(city.getCode(), district.getCityCode());

                    long extractedProvinceCode = district.getCode() / 10000L;
                    assertEquals(province.getCode(), extractedProvinceCode);

                    List<Village> villages = service.getVillagesByDistrict(district.getCode());
                    if (!villages.isEmpty()) {
                        Village village = villages.get(0);

                        assertEquals(district.getCode(), village.getDistrictCode());

                        long extractedProvinceCodeFromVillage = village.getCode() / 100000000L;
                        long extractedCityCodeFromVillage = village.getCode() / 1000000L;

                        assertEquals(province.getCode(), extractedProvinceCodeFromVillage);
                        assertEquals(city.getCode(), extractedCityCodeFromVillage);
                    }
                }
            }
        }
    }

    @Test
    void testFindNonExistentEntity() {
        Optional<Province> province = service.findProvince(999999L);
        assertFalse(province.isPresent());

        Optional<City> city = service.findCity(999999L);
        assertFalse(city.isPresent());

        Optional<District> district = service.findDistrict(999999L);
        assertFalse(district.isPresent());

        Optional<Village> village = service.findVillage(9999999999L);
        assertFalse(village.isPresent());
    }

    @Test
    void testEmptySearchReturnsAll() {
        List<Province> allProvinces = service.getAllProvinces();
        List<Province> emptySearch = service.searchProvinces("");
        List<Province> nullSearch = service.searchProvinces(null);

        assertEquals(allProvinces.size(), emptySearch.size());
        assertEquals(allProvinces.size(), nullSearch.size());
    }

    @Test
    void testServiceCreatedWithDirectImplementation() {
        InMemoryIndonesiaCache cache = new InMemoryIndonesiaCache();
        CsvIndonesiaDataLoader loader = new CsvIndonesiaDataLoader();
        IndonesiaService customService = new DefaultIndonesiaService(cache, loader);

        assertNotNull(customService);
        assertTrue(customService.isDataLoaded());

        List<Province> provinces = customService.getAllProvinces();
        assertFalse(provinces.isEmpty());
    }
}
