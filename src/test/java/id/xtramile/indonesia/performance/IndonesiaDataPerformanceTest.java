package id.xtramile.indonesia.performance;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.cache.InMemoryIndonesiaCache;
import id.xtramile.indonesia.loader.CsvIndonesiaDataLoader;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.util.IndonesiaServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Performance tests for Indonesia data operations.
 * Note: These tests verify basic performance characteristics but may vary based on system resources.
 */
class IndonesiaDataPerformanceTest {

    private IndonesiaService service;
    private IndonesiaDataLoader loader;
    private IndonesiaDataCache cache;

    @BeforeEach
    void setUp() {
        service = IndonesiaServiceFactory.createDefault();
        loader = new CsvIndonesiaDataLoader();
        cache = new InMemoryIndonesiaCache();
    }

    @Test
    void testLoadTimeForAllData() {
        long startTime = System.currentTimeMillis();
        
        assertDoesNotThrow(() -> {
            loader.loadProvinces();
            loader.loadCities();
            loader.loadDistricts();
            loader.loadVillages();
        });
        
        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;
        
        assertTrue(loadTime < 10000, "Data loading should complete within 10 seconds, took: " + loadTime + "ms");
        System.out.println("Data loading took: " + loadTime + "ms");
    }

    @Test
    void testCachePutPerformance() {
        Map<Long, Province> provinces = createLargeProvincesMap(1000);
        
        long startTime = System.currentTimeMillis();
        cache.putProvinces(provinces);
        long endTime = System.currentTimeMillis();
        
        long putTime = endTime - startTime;
        assertTrue(putTime < 1000, "Cache put should complete within 1 second, took: " + putTime + "ms");
        System.out.println("Cache put (1000 provinces) took: " + putTime + "ms");
    }

    @Test
    void testCacheGetPerformance() {
        Map<Long, Province> provinces = createLargeProvincesMap(1000);
        cache.putProvinces(provinces);
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Map<Long, Province> result = cache.getProvinces();
            assertNotNull(result);
        }
        long endTime = System.currentTimeMillis();
        
        long getTime = endTime - startTime;
        assertTrue(getTime < 1000, "100 cache get operations should complete within 1 second, took: " + getTime + "ms");
        System.out.println("100 cache get operations took: " + getTime + "ms");
    }

    @Test
    void testSearchPerformance() {
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 50; i++) {
            service.searchProvinces("Jakarta");
            service.searchCities("Jakarta");
            service.searchDistricts("Jakarta");
            service.searchVillages("Jakarta");
        }
        
        long endTime = System.currentTimeMillis();
        long searchTime = endTime - startTime;
        
        assertTrue(searchTime < 5000, "200 search operations should complete within 5 seconds, took: " + searchTime + "ms");
        System.out.println("200 search operations took: " + searchTime + "ms");
    }

    @Test
    void testFindOperationPerformance() {
        List<Province> provinces = service.getAllProvinces();
        if (!provinces.isEmpty()) {
            long provinceCode = provinces.get(0).getCode();
            
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                service.findProvince(provinceCode);
            }
            long endTime = System.currentTimeMillis();
            
            long findTime = endTime - startTime;
            assertTrue(findTime < 1000, "1000 find operations should complete within 1 second, took: " + findTime + "ms");
            System.out.println("1000 find operations took: " + findTime + "ms");
        }
    }

    @Test
    void testHierarchicalQueryPerformance() {
        List<Province> provinces = service.getAllProvinces();
        if (!provinces.isEmpty()) {
            long provinceCode = provinces.get(0).getCode();
            List<City> cities = service.getCitiesByProvince(provinceCode);
            
            if (!cities.isEmpty()) {
                long cityCode = cities.get(0).getCode();
                
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < 100; i++) {
                    service.getDistrictsByCity(cityCode);
                    service.getVillagesByCity(cityCode);
                    service.getVillagesByProvince(provinceCode);
                }
                long endTime = System.currentTimeMillis();
                
                long queryTime = endTime - startTime;
                assertTrue(queryTime < 2000, "300 hierarchical queries should complete within 2 seconds, took: " + queryTime + "ms");
                System.out.println("300 hierarchical queries took: " + queryTime + "ms");
            }
        }
    }

    @Test
    void testBuildFromPerformance() {
        List<Province> provinces = service.getAllProvinces();
        if (!provinces.isEmpty()) {
            Province province = provinces.get(0);
            List<City> cities = service.getCitiesByProvince(province.getCode());
            
            if (!cities.isEmpty()) {
                City city = cities.get(0);
                
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < 500; i++) {
                    service.buildFrom(province);
                    service.buildFrom(city);
                }
                long endTime = System.currentTimeMillis();
                
                long buildTime = endTime - startTime;
                assertTrue(buildTime < 2000, "1000 buildFrom operations should complete within 2 seconds, took: " + buildTime + "ms");
                System.out.println("1000 buildFrom operations took: " + buildTime + "ms");
            }
        }
    }

    @Test
    void testCacheStatsPerformance() {
        Map<Long, Province> provinces = createLargeProvincesMap(500);
        cache.putProvinces(provinces);
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            cache.getStats();
        }
        long endTime = System.currentTimeMillis();
        
        long statsTime = endTime - startTime;
        assertTrue(statsTime < 500, "1000 stats operations should complete within 500ms, took: " + statsTime + "ms");
        System.out.println("1000 stats operations took: " + statsTime + "ms");
    }

    @Test
    void testIndexedMapAccessPerformance() {
        Map<Long, City> cities = createLargeCitiesMap(1000);
        cache.putCities(cities);
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Map<Long, List<City>> citiesByProvince = cache.getCitiesByProvince();
            assertNotNull(citiesByProvince);
        }
        long endTime = System.currentTimeMillis();
        
        long indexTime = endTime - startTime;
        assertTrue(indexTime < 1000, "100 indexed map accesses should complete within 1 second, took: " + indexTime + "ms");
        System.out.println("100 indexed map accesses took: " + indexTime + "ms");
    }

    @Test
    void testFullServiceInitializationPerformance() {
        long startTime = System.currentTimeMillis();
        IndonesiaService newService = IndonesiaServiceFactory.createDefault();
        long endTime = System.currentTimeMillis();
        
        long initTime = endTime - startTime;
        assertTrue(initTime < 10000, "Service initialization should complete within 10 seconds, took: " + initTime + "ms");
        assertTrue(newService.isDataLoaded());
        System.out.println("Service initialization took: " + initTime + "ms");
    }

    @Test
    void testRefreshPerformance() {
        Map<Long, Province> provinces = createLargeProvincesMap(500);
        cache.putProvinces(provinces);
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            cache.refresh();
            cache.putProvinces(provinces);
        }
        long endTime = System.currentTimeMillis();
        
        long refreshTime = endTime - startTime;
        assertTrue(refreshTime < 2000, "50 refresh cycles should complete within 2 seconds, took: " + refreshTime + "ms");
        System.out.println("50 refresh cycles took: " + refreshTime + "ms");
    }

    private Map<Long, Province> createLargeProvincesMap(int count) {
        Map<Long, Province> provinces = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            long code = i;
            provinces.put(code, new Province(code, "Province " + i, -6.0 + (i * 0.01), 106.0 + (i * 0.01)));
        }
        return provinces;
    }

    private Map<Long, City> createLargeCitiesMap(int count) {
        Map<Long, City> cities = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            long code = 1000L + i;
            long provinceCode = (i % 10) + 1;
            cities.put(code, new City(code, provinceCode, "City " + i, -6.0 + (i * 0.001), 106.0 + (i * 0.001)));
        }
        return cities;
    }
}
