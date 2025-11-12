package id.xtramile.indonesia.integration;

import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.loader.CsvIndonesiaDataLoader;
import id.xtramile.indonesia.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IndonesiaDataLoaderIntegrationTest {

    private final IndonesiaDataLoader loader = new CsvIndonesiaDataLoader();

    @Test
    void testLoadProvincesWithRealData() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();

        assertNotNull(provinces);
        assertFalse(provinces.isEmpty());

        for (Province province : provinces.values()) {
            assertTrue(province.getCode() > 0);
            assertNotNull(province.getName());
            assertFalse(province.getName().trim().isEmpty());
        }

        Set<Long> codes = provinces.keySet();
        assertEquals(codes.size(), provinces.size());
    }

    @Test
    void testLoadCitiesWithRealData() throws DataLoadException {
        Map<Long, City> cities = loader.loadCities();

        assertNotNull(cities);
        assertFalse(cities.isEmpty());

        Map<Long, Province> provinces = loader.loadProvinces();

        for (City city : cities.values()) {
            assertTrue(city.getCode() > 0);
            assertNotNull(city.getName());
            assertFalse(city.getName().trim().isEmpty());

            assertTrue(provinces.containsKey(city.getProvinceCode()),
                    "City " + city.getCode() + " references non-existent province " + city.getProvinceCode());
        }

        Set<Long> codes = cities.keySet();
        assertEquals(codes.size(), cities.size());
    }

    @Test
    void testLoadDistrictsWithRealData() throws DataLoadException {
        Map<Long, District> districts = loader.loadDistricts();

        assertNotNull(districts);
        assertFalse(districts.isEmpty());

        Map<Long, City> cities = loader.loadCities();

        for (District district : districts.values()) {
            assertTrue(district.getCode() > 0);
            assertNotNull(district.getName());
            assertFalse(district.getName().trim().isEmpty());

            assertTrue(cities.containsKey(district.getCityCode()),
                    "District " + district.getCode() + " references non-existent city " + district.getCityCode());
        }

        Set<Long> codes = districts.keySet();
        assertEquals(codes.size(), districts.size());
    }

    @Test
    void testLoadVillagesWithRealData() throws DataLoadException {
        Map<Long, Village> villages = loader.loadVillages();

        assertNotNull(villages);
        assertFalse(villages.isEmpty());

        Map<Long, District> districts = loader.loadDistricts();

        for (Village village : villages.values()) {
            assertTrue(village.getCode() > 0);
            assertNotNull(village.getName());
            assertFalse(village.getName().trim().isEmpty());

            assertTrue(districts.containsKey(village.getDistrictCode()),
                    "Village " + village.getCode() + " references non-existent district " + village.getDistrictCode());
        }

        Set<Long> codes = villages.keySet();
        assertEquals(codes.size(), villages.size());
    }

    @Test
    void testLoadAllData() throws DataLoadException {
        IndonesiaData data = loader.loadAllData();

        assertNotNull(data);
        assertNotNull(data.getProvinces());
        assertNotNull(data.getCities());
        assertNotNull(data.getDistricts());
        assertNotNull(data.getVillages());

        assertFalse(data.getProvinces().isEmpty());
        assertFalse(data.getCities().isEmpty());
        assertFalse(data.getDistricts().isEmpty());
        assertFalse(data.getVillages().isEmpty());
    }

    @Test
    void testDataConsistencyAcrossLoads() throws DataLoadException {
        Map<Long, Province> provinces1 = loader.loadProvinces();
        Map<Long, Province> provinces2 = loader.loadProvinces();

        assertEquals(provinces1.size(), provinces2.size());
        assertEquals(provinces1.keySet(), provinces2.keySet());

        for (Long code : provinces1.keySet()) {
            assertEquals(provinces1.get(code).getName(), provinces2.get(code).getName());
        }
    }

    @Test
    void testHierarchicalRelationshipConsistency() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();
        Map<Long, City> cities = loader.loadCities();
        Map<Long, District> districts = loader.loadDistricts();
        Map<Long, Village> villages = loader.loadVillages();

        for (City city : cities.values()) {
            assertTrue(provinces.containsKey(city.getProvinceCode()),
                    "City " + city.getCode() + " has invalid province code " + city.getProvinceCode());
        }

        for (District district : districts.values()) {
            assertTrue(cities.containsKey(district.getCityCode()),
                    "District " + district.getCode() + " has invalid city code " + district.getCityCode());
        }

        for (Village village : villages.values()) {
            assertTrue(districts.containsKey(village.getDistrictCode()),
                    "Village " + village.getCode() + " has invalid district code " + village.getDistrictCode());
        }
    }

    @Test
    void testCodeExtractionFromHierarchy() throws DataLoadException {
        Map<Long, District> districts = loader.loadDistricts();
        Map<Long, Village> villages = loader.loadVillages();

        if (!districts.isEmpty()) {
            District district = districts.values().iterator().next();
            long extractedProvinceCode = district.getCode() / 10000L;
            assertTrue(extractedProvinceCode > 0);
        }

        if (!villages.isEmpty()) {
            Village village = villages.values().iterator().next();
            long extractedProvinceCode = village.getCode() / 100000000L;
            long extractedCityCode = village.getCode() / 1000000L;

            assertTrue(extractedProvinceCode > 0);
            assertTrue(extractedCityCode > 0);
        }
    }

    @Test
    void testMultipleConsecutiveLoads() throws DataLoadException {
        for (int i = 0; i < 3; i++) {
            Map<Long, Province> provinces = loader.loadProvinces();
            Map<Long, City> cities = loader.loadCities();
            Map<Long, District> districts = loader.loadDistricts();
            Map<Long, Village> villages = loader.loadVillages();

            assertFalse(provinces.isEmpty());
            assertFalse(cities.isEmpty());
            assertFalse(districts.isEmpty());
            assertFalse(villages.isEmpty());
        }
    }
}
