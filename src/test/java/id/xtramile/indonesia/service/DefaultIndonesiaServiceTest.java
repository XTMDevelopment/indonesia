package id.xtramile.indonesia.service;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultIndonesiaServiceTest {

    @Mock
    private IndonesiaDataCache cache;

    @Mock
    private IndonesiaDataLoader loader;

    private DefaultIndonesiaService service;

    private Province province1;
    private Province province2;
    private City city1;
    private City city2;
    private District district1;
    private District district2;
    private Village village1;
    private Village village2;

    @BeforeEach
    void setUp() throws DataLoadException {
        province1 = new Province(11L, "Jakarta", -6.2088, 106.8456);
        province2 = new Province(12L, "Sumatera Utara", 3.5952, 98.6722);

        city1 = new City(1101L, 11L, "Jakarta Pusat", -6.1818, 106.8318);
        city2 = new City(1201L, 12L, "Medan", 3.5833, 98.6667);

        district1 = new District(110101L, 1101L, "Gambir", -6.1751, 106.8208);
        district2 = new District(120101L, 1201L, "Medan Barat", 3.5842, 98.6756);

        village1 = new Village(1101011001L, 110101L, "Gambir", -6.1751, 106.8208);
        village2 = new Village(1201011001L, 120101L, "Medan Barat", 3.5842, 98.6756);

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

        Map<Long, List<City>> citiesByProvince = new HashMap<>();
        citiesByProvince.put(11L, Collections.singletonList(city1));
        citiesByProvince.put(12L, Collections.singletonList(city2));

        Map<Long, List<District>> districtsByCity = new HashMap<>();
        districtsByCity.put(1101L, Collections.singletonList(district1));
        districtsByCity.put(1201L, Collections.singletonList(district2));

        Map<Long, List<Village>> villagesByDistrict = new HashMap<>();
        villagesByDistrict.put(110101L, Collections.singletonList(village1));
        villagesByDistrict.put(120101L, Collections.singletonList(village2));

        Map<Long, List<Village>> villagesByProvince = new HashMap<>();
        villagesByProvince.put(11L, Collections.singletonList(village1));
        villagesByProvince.put(12L, Collections.singletonList(village2));

        Map<Long, List<Village>> villagesByCity = new HashMap<>();
        villagesByCity.put(1101L, Collections.singletonList(village1));
        villagesByCity.put(1201L, Collections.singletonList(village2));

        when(cache.getProvinces()).thenReturn(provinces);
        when(cache.getCities()).thenReturn(cities);
        when(cache.getDistricts()).thenReturn(districts);
        when(cache.getVillages()).thenReturn(villages);
        when(cache.getCitiesByProvince()).thenReturn(citiesByProvince);
        when(cache.getDistrictsByCity()).thenReturn(districtsByCity);
        when(cache.getVillagesByDistrict()).thenReturn(villagesByDistrict);
        
        when(cache.getProvince(11L)).thenReturn(province1);
        when(cache.getProvince(12L)).thenReturn(province2);
        when(cache.getCity(1101L)).thenReturn(city1);
        when(cache.getCity(1201L)).thenReturn(city2);
        when(cache.getDistrict(110101L)).thenReturn(district1);
        when(cache.getDistrict(120101L)).thenReturn(district2);
        when(cache.getVillage(1101011001L)).thenReturn(village1);
        when(cache.getVillage(1201011001L)).thenReturn(village2);
        
        when(cache.getCitiesByProvinceCode(11L)).thenReturn(Collections.singletonList(city1));
        when(cache.getCitiesByProvinceCode(12L)).thenReturn(Collections.singletonList(city2));
        when(cache.getDistrictsByCityCode(1101L)).thenReturn(Collections.singletonList(district1));
        when(cache.getDistrictsByCityCode(1201L)).thenReturn(Collections.singletonList(district2));
        when(cache.getVillagesByDistrictCode(110101L)).thenReturn(Collections.singletonList(village1));
        when(cache.getVillagesByDistrictCode(120101L)).thenReturn(Collections.singletonList(village2));
        when(cache.getVillagesByProvinceCode(11L)).thenReturn(Collections.singletonList(village1));
        when(cache.getVillagesByProvinceCode(12L)).thenReturn(Collections.singletonList(village2));
        when(cache.getVillagesByCityCode(1101L)).thenReturn(Collections.singletonList(village1));
        when(cache.getVillagesByCityCode(1201L)).thenReturn(Collections.singletonList(village2));
        
        when(cache.isLoaded()).thenReturn(true);
        when(cache.getStats()).thenReturn(new CacheStats(2, 2, 2, 2, System.currentTimeMillis()));

        when(loader.loadProvinces()).thenReturn(provinces);
        when(loader.loadCities()).thenReturn(cities);
        when(loader.loadDistricts()).thenReturn(districts);
        when(loader.loadVillages()).thenReturn(villages);

        service = new DefaultIndonesiaService(cache, loader);
    }

    @Test
    void testConstructorLoadsData() throws DataLoadException {
        verify(loader, times(1)).loadProvinces();
        verify(loader, times(1)).loadCities();
        verify(loader, times(1)).loadDistricts();
        verify(loader, times(1)).loadVillages();
        verify(cache, times(1)).putProvinces(any());
        verify(cache, times(1)).putCities(any());
        verify(cache, times(1)).putDistricts(any());
        verify(cache, times(1)).putVillages(any());
    }

    @Test
    void testFindProvince() {
        Optional<Province> result = service.findProvince(11L);
        assertTrue(result.isPresent());
        assertEquals(province1, result.get());

        Optional<Province> notFound = service.findProvince(99L);
        assertFalse(notFound.isPresent());
    }

    @Test
    void testGetAllProvinces() {
        List<Province> result = service.getAllProvinces();
        assertEquals(2, result.size());
        assertTrue(result.contains(province1));
        assertTrue(result.contains(province2));
    }

    @Test
    void testSearchProvinces() {
        List<Province> result = service.searchProvinces("Jakarta");
        assertEquals(1, result.size());
        assertEquals(province1, result.get(0));

        List<Province> emptyResult = service.searchProvinces("NonExistent");
        assertTrue(emptyResult.isEmpty());

        List<Province> allWhenEmptyQuery = service.searchProvinces("");
        assertEquals(2, allWhenEmptyQuery.size());

        List<Province> allWhenNullQuery = service.searchProvinces(null);
        assertEquals(2, allWhenNullQuery.size());

        List<Province> caseInsensitive = service.searchProvinces("jakarta");
        assertEquals(1, caseInsensitive.size());
    }

    @Test
    void testFindCity() {
        Optional<City> result = service.findCity(1101L);
        assertTrue(result.isPresent());
        assertEquals(city1, result.get());

        Optional<City> notFound = service.findCity(9999L);
        assertFalse(notFound.isPresent());
    }

    @Test
    void testGetCitiesByProvince() {
        List<City> result = service.getCitiesByProvince(11L);
        assertEquals(1, result.size());
        assertEquals(city1, result.get(0));

        List<City> emptyResult = service.getCitiesByProvince(99L);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void testGetAllCities() {
        List<City> result = service.getAllCities();
        assertEquals(2, result.size());
        assertTrue(result.contains(city1));
        assertTrue(result.contains(city2));
    }

    @Test
    void testSearchCities() {
        List<City> result = service.searchCities("Jakarta");
        assertEquals(1, result.size());
        assertEquals(city1, result.get(0));

        List<City> emptyResult = service.searchCities("NonExistent");
        assertTrue(emptyResult.isEmpty());

        List<City> allWhenEmptyQuery = service.searchCities("");
        assertEquals(2, allWhenEmptyQuery.size());
    }

    @Test
    void testFindDistrict() {
        Optional<District> result = service.findDistrict(110101L);
        assertTrue(result.isPresent());
        assertEquals(district1, result.get());
    }

    @Test
    void testGetDistrictsByCity() {
        List<District> result = service.getDistrictsByCity(1101L);
        assertEquals(1, result.size());
        assertEquals(district1, result.get(0));
    }

    @Test
    void testGetAllDistricts() {
        List<District> result = service.getAllDistricts();
        assertEquals(2, result.size());
        assertTrue(result.contains(district1));
        assertTrue(result.contains(district2));
    }

    @Test
    void testSearchDistricts() {
        List<District> result = service.searchDistricts("Gambir");
        assertEquals(1, result.size());
        assertEquals(district1, result.get(0));

        List<District> allWhenEmptyQuery = service.searchDistricts("");
        assertEquals(2, allWhenEmptyQuery.size());
    }

    @Test
    void testFindVillage() {
        Optional<Village> result = service.findVillage(1101011001L);
        assertTrue(result.isPresent());
        assertEquals(village1, result.get());
    }

    @Test
    void testGetVillagesByDistrict() {
        List<Village> result = service.getVillagesByDistrict(110101L);
        assertEquals(1, result.size());
        assertEquals(village1, result.get(0));
    }

    @Test
    void testGetAllVillages() {
        List<Village> result = service.getAllVillages();
        assertEquals(2, result.size());
        assertTrue(result.contains(village1));
        assertTrue(result.contains(village2));
    }

    @Test
    void testSearchVillages() {
        List<Village> result = service.searchVillages("Gambir");
        assertEquals(1, result.size());
        assertEquals(village1, result.get(0));

        List<Village> allWhenEmptyQuery = service.searchVillages("");
        assertEquals(2, allWhenEmptyQuery.size());
    }

    @Test
    void testGetVillagesByProvince() {
        List<Village> result = service.getVillagesByProvince(11L);
        assertEquals(1, result.size());
        assertEquals(village1, result.get(0));

        Map<Long, District> emptyDistricts = new HashMap<>();
        when(cache.getDistricts()).thenReturn(emptyDistricts);
        List<Village> emptyResult = service.getVillagesByProvince(99L);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void testGetVillagesByCity() {
        List<Village> result = service.getVillagesByCity(1101L);
        assertEquals(1, result.size());
        assertEquals(village1, result.get(0));

        Map<Long, District> emptyDistricts = new HashMap<>();
        when(cache.getDistricts()).thenReturn(emptyDistricts);
        List<Village> emptyResult = service.getVillagesByCity(9999L);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void testBuildFromProvince() {
        Indonesia result = service.buildFrom(province1);
        assertNotNull(result);
        assertEquals(province1, result.getProvince());
        assertNull(result.getCity());
        assertNull(result.getDistrict());
        assertNull(result.getVillage());
    }

    @Test
    void testBuildFromCity() {
        Indonesia result = service.buildFrom(city1);
        assertNotNull(result);
        assertEquals(province1, result.getProvince());
        assertEquals(city1, result.getCity());
        assertNull(result.getDistrict());
        assertNull(result.getVillage());

        Indonesia nullResult = service.buildFrom((City) null);
        assertNotNull(nullResult);
        assertNull(nullResult.getProvince());
        assertNull(nullResult.getCity());
    }

    @Test
    void testBuildFromDistrict() {
        Indonesia result = service.buildFrom(district1);
        assertNotNull(result);
        assertEquals(province1, result.getProvince());
        assertEquals(city1, result.getCity());
        assertEquals(district1, result.getDistrict());
        assertNull(result.getVillage());

        Indonesia nullResult = service.buildFrom((District) null);
        assertNotNull(nullResult);
        assertNull(nullResult.getProvince());
    }

    @Test
    void testBuildFromVillage() {
        Indonesia result = service.buildFrom(village1);
        assertNotNull(result);
        assertEquals(province1, result.getProvince());
        assertEquals(city1, result.getCity());
        assertEquals(district1, result.getDistrict());
        assertEquals(village1, result.getVillage());

        Indonesia nullResult = service.buildFrom((Village) null);
        assertNotNull(nullResult);
        assertNull(nullResult.getVillage());
    }

    @Test
    void testRefreshData() throws DataLoadException {
        service.refreshData();
        verify(cache, times(1)).refresh();
        verify(loader, times(2)).loadProvinces(); // Once in constructor, once in refreshData
    }

    @Test
    void testRefreshDataThrowsException() throws DataLoadException {
        doThrow(new DataLoadException("Error")).when(cache).refresh();
        assertThrows(RuntimeException.class, () -> {
            try {
                service.refreshData();
            } catch (RuntimeException e) {
                assertTrue(e.getMessage().contains("Failed to refresh data"));
                throw e;
            }
        });
    }

    @Test
    void testIsDataLoaded() {
        assertTrue(service.isDataLoaded());
        verify(cache).isLoaded();
    }

    @Test
    void testGetCacheStats() {
        CacheStats stats = service.getCacheStats();
        assertNotNull(stats);
        assertEquals(2, stats.getProvinceCount());
        verify(cache).getStats();
    }
}

