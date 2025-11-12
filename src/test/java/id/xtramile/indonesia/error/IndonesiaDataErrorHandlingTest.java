package id.xtramile.indonesia.error;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.cache.InMemoryIndonesiaCache;
import id.xtramile.indonesia.loader.CsvIndonesiaDataLoader;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.util.IndonesiaServiceFactory;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IndonesiaDataErrorHandlingTest {

    @Test
    void testServiceFactoryWithNullCache() {
        IndonesiaDataLoader loader = new CsvIndonesiaDataLoader();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> IndonesiaServiceFactory.create(null, loader)
        );

        assertEquals("Cache cannot be null", exception.getMessage());
    }

    @Test
    void testServiceFactoryWithNullLoader() {
        IndonesiaDataCache cache = new InMemoryIndonesiaCache();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> IndonesiaServiceFactory.create(cache, null)
        );

        assertEquals("Loader cannot be null", exception.getMessage());
    }

    @Test
    void testFindNonExistentProvince() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertFalse(service.findProvince(-1L).isPresent());
        assertFalse(service.findProvince(0L).isPresent());
        assertFalse(service.findProvince(999999L).isPresent());
        assertFalse(service.findProvince(Long.MAX_VALUE).isPresent());
    }

    @Test
    void testFindNonExistentCity() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertFalse(service.findCity(-1L).isPresent());
        assertFalse(service.findCity(0L).isPresent());
        assertFalse(service.findCity(999999L).isPresent());
    }

    @Test
    void testFindNonExistentDistrict() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertFalse(service.findDistrict(-1L).isPresent());
        assertFalse(service.findDistrict(0L).isPresent());
        assertFalse(service.findDistrict(999999L).isPresent());
    }

    @Test
    void testFindNonExistentVillage() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertFalse(service.findVillage(-1L).isPresent());
        assertFalse(service.findVillage(0L).isPresent());
        assertFalse(service.findVillage(9999999999L).isPresent());
    }

    @Test
    void testGetCitiesByNonExistentProvince() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertNotNull(service.getCitiesByProvince(-1L));
        assertTrue(service.getCitiesByProvince(-1L).isEmpty());
        assertTrue(service.getCitiesByProvince(999999L).isEmpty());
    }

    @Test
    void testGetDistrictsByNonExistentCity() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertNotNull(service.getDistrictsByCity(-1L));
        assertTrue(service.getDistrictsByCity(-1L).isEmpty());
        assertTrue(service.getDistrictsByCity(999999L).isEmpty());
    }

    @Test
    void testGetVillagesByNonExistentDistrict() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertNotNull(service.getVillagesByDistrict(-1L));
        assertTrue(service.getVillagesByDistrict(-1L).isEmpty());
        assertTrue(service.getVillagesByDistrict(999999L).isEmpty());
    }

    @Test
    void testGetVillagesByNonExistentProvince() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertNotNull(service.getVillagesByProvince(-1L));
        assertTrue(service.getVillagesByProvince(-1L).isEmpty());
        assertTrue(service.getVillagesByProvince(999999L).isEmpty());
    }

    @Test
    void testGetVillagesByNonExistentCity() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertNotNull(service.getVillagesByCity(-1L));
        assertTrue(service.getVillagesByCity(-1L).isEmpty());
        assertTrue(service.getVillagesByCity(999999L).isEmpty());
    }

    @Test
    void testSearchWithNullQuery() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertNotNull(service.searchProvinces(null));
        assertNotNull(service.searchCities(null));
        assertNotNull(service.searchDistricts(null));
        assertNotNull(service.searchVillages(null));

        assertFalse(service.searchProvinces(null).isEmpty());
    }

    @Test
    void testSearchWithEmptyQuery() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertNotNull(service.searchProvinces(""));
        assertNotNull(service.searchCities(""));
        assertNotNull(service.searchDistricts(""));
        assertNotNull(service.searchVillages(""));

        assertFalse(service.searchProvinces("").isEmpty());
    }

    @Test
    void testSearchWithWhitespaceOnly() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        List<Province> allProvinces = service.getAllProvinces();
        List<Province> whitespaceSearch = service.searchProvinces("   ");

        assertEquals(allProvinces.size(), whitespaceSearch.size());
    }

    @Test
    void testCacheWithEmptyMaps() {
        IndonesiaDataCache cache = new InMemoryIndonesiaCache();

        cache.putProvinces(Collections.emptyMap());
        cache.putCities(Collections.emptyMap());
        cache.putDistricts(Collections.emptyMap());
        cache.putVillages(Collections.emptyMap());

        assertFalse(cache.isLoaded());
        assertTrue(cache.getProvinces().isEmpty());
        assertTrue(cache.getCities().isEmpty());
    }

    @Test
    void testCacheWithNullMapShouldThrowException() {
        IndonesiaDataCache cache = new InMemoryIndonesiaCache();

        assertThrows(Exception.class, () -> cache.putProvinces(null));
    }

    @Test
    void testRefreshAfterPutOperations() {
        IndonesiaDataCache cache = new InMemoryIndonesiaCache();
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(1L, new Province(1L, "Test", -6.0, 106.0));

        cache.putProvinces(provinces);
        assertTrue(cache.isLoaded());

        cache.refresh();
        assertFalse(cache.isLoaded());
        assertTrue(cache.getProvinces().isEmpty());

        cache.putProvinces(provinces);
        assertTrue(cache.isLoaded());
    }

    @Test
    void testStatsOnEmptyCache() {
        IndonesiaDataCache cache = new InMemoryIndonesiaCache();

        assertEquals(0, cache.getStats().getProvinceCount());
        assertEquals(0, cache.getStats().getCityCount());
        assertEquals(0, cache.getStats().getDistrictCount());
        assertEquals(0, cache.getStats().getVillageCount());
        assertEquals(0, cache.getStats().getLastRefreshTime());
    }

    @Test
    void testMultipleRefreshCalls() {
        IndonesiaDataCache cache = new InMemoryIndonesiaCache();
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(1L, new Province(1L, "Test", -6.0, 106.0));

        cache.putProvinces(provinces);
        cache.refresh();
        cache.refresh();
        cache.refresh();

        assertFalse(cache.isLoaded());
        assertTrue(cache.getProvinces().isEmpty());
    }

    @Test
    void testBuildFromWithNullEntities() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertNotNull(service.buildFrom((Province) null));
        assertNull(service.buildFrom((Province) null).getProvince());

        assertNotNull(service.buildFrom((id.xtramile.indonesia.model.City) null));
        assertNull(service.buildFrom((id.xtramile.indonesia.model.City) null).getCity());

        assertNotNull(service.buildFrom((id.xtramile.indonesia.model.District) null));
        assertNull(service.buildFrom((id.xtramile.indonesia.model.District) null).getDistrict());

        assertNotNull(service.buildFrom((id.xtramile.indonesia.model.Village) null));
        assertNull(service.buildFrom((id.xtramile.indonesia.model.Village) null).getVillage());
    }

    @Test
    void testCsvLoaderHandlesRealFiles() {
        IndonesiaDataLoader loader = new CsvIndonesiaDataLoader();

        assertDoesNotThrow(() -> {
            loader.loadProvinces();
            loader.loadCities();
            loader.loadDistricts();
            loader.loadVillages();
            loader.loadAllData();
        });
    }

    @Test
    void testServiceWithInvalidCodeValues() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();

        assertFalse(service.findProvince(Long.MIN_VALUE).isPresent());
        assertFalse(service.findCity(Long.MIN_VALUE).isPresent());
        assertFalse(service.findDistrict(Long.MIN_VALUE).isPresent());
        assertFalse(service.findVillage(Long.MIN_VALUE).isPresent());
    }

    @Test
    void testCacheStatsAfterMultipleOperations() {
        IndonesiaDataCache cache = new InMemoryIndonesiaCache();

        Map<Long, Province> provinces1 = new HashMap<>();
        provinces1.put(1L, new Province(1L, "Test1", -6.0, 106.0));
        cache.putProvinces(provinces1);
        long stats1 = cache.getStats().getLastRefreshTime();

        Map<Long, Province> provinces2 = new HashMap<>();
        provinces2.put(2L, new Province(2L, "Test2", -7.0, 107.0));
        cache.putProvinces(provinces2);
        long stats2 = cache.getStats().getLastRefreshTime();

        assertTrue(stats2 >= stats1);
        assertEquals(1, cache.getStats().getProvinceCount());
    }
}
