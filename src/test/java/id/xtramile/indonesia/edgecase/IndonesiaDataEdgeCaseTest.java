package id.xtramile.indonesia.edgecase;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.cache.InMemoryIndonesiaCache;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;
import id.xtramile.indonesia.service.DefaultIndonesiaService;
import id.xtramile.indonesia.util.IndonesiaServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IndonesiaDataEdgeCaseTest {

    private IndonesiaDataCache cache;

    @BeforeEach
    void setUp() {
        cache = new InMemoryIndonesiaCache();
    }

    @Test
    void testMaximumLongCode() {
        long maxCode = Long.MAX_VALUE;
        Province province = new Province(maxCode, "Max Province", -90.0, 180.0);
        
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(maxCode, province);
        
        cache.putProvinces(provinces);
        Optional<Province> found = Optional.ofNullable(cache.getProvinces().get(maxCode));
        
        assertTrue(found.isPresent());
        assertEquals(maxCode, found.get().getCode());
    }

    @Test
    void testMinimumLongCode() {
        long minCode = 1L;
        Province province = new Province(minCode, "Min Province", -90.0, 180.0);
        
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(minCode, province);
        
        cache.putProvinces(provinces);
        Optional<Province> found = Optional.ofNullable(cache.getProvinces().get(minCode));
        
        assertTrue(found.isPresent());
        assertEquals(minCode, found.get().getCode());
    }

    @Test
    void testVeryLargeCode() {
        long largeCode = 9999999999L;
        Village village = new Village(largeCode, 999999L, "Large Code Village", -6.2088, 106.8456);
        
        Map<Long, Village> villages = new HashMap<>();
        villages.put(largeCode, village);
        
        cache.putVillages(villages);
        Optional<Village> found = Optional.ofNullable(cache.getVillages().get(largeCode));
        
        assertTrue(found.isPresent());
        assertEquals(largeCode, found.get().getCode());
    }

    @Test
    void testEmptyDataSet() {
        Map<Long, Province> emptyProvinces = new HashMap<>();
        cache.putProvinces(emptyProvinces);
        
        assertFalse(cache.isLoaded());
        assertTrue(cache.getProvinces().isEmpty());
        assertEquals(0, cache.getStats().getProvinceCount());
    }

    @Test
    void testNullHandlingInService() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();
        
        assertNotNull(service.findProvince(null));
        assertFalse(service.findProvince(null).isPresent());
        
        assertNotNull(service.searchProvinces(null));
        assertTrue(service.searchProvinces(null).size() > 0);
        
        assertNotNull(service.searchProvinces(""));
        assertTrue(service.searchProvinces("").size() > 0);
    }

    @Test
    void testEmptyStringSearch() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();
        
        List<Province> allProvinces = service.getAllProvinces();
        List<Province> emptySearch = service.searchProvinces("");
        List<Province> whitespaceSearch = service.searchProvinces("   ");
        
        assertEquals(allProvinces.size(), emptySearch.size());
        assertEquals(allProvinces.size(), whitespaceSearch.size());
    }

    @Test
    void testVeryLongName() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("A");
        }
        String veryLongName = sb.toString();
        Province province = new Province(999L, veryLongName, -6.2088, 106.8456);
        
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(999L, province);
        
        cache.putProvinces(provinces);
        Province cached = cache.getProvinces().get(999L);
        
        assertNotNull(cached);
        assertEquals(veryLongName, cached.getName());
        assertEquals(1000, cached.getName().length());
    }

    @Test
    void testExtremeCoordinates() {
        Province northPole = new Province(1L, "North Pole", 90.0, 0.0);
        Province southPole = new Province(2L, "South Pole", -90.0, 0.0);
        Province dateLine = new Province(3L, "Date Line", 0.0, 180.0);
        Province negativeDateLine = new Province(4L, "Negative Date Line", 0.0, -180.0);
        
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(1L, northPole);
        provinces.put(2L, southPole);
        provinces.put(3L, dateLine);
        provinces.put(4L, negativeDateLine);
        
        cache.putProvinces(provinces);
        
        assertEquals(4, cache.getProvinces().size());
        assertEquals(90.0, cache.getProvinces().get(1L).getLatitude());
        assertEquals(-90.0, cache.getProvinces().get(2L).getLatitude());
        assertEquals(180.0, cache.getProvinces().get(3L).getLongitude());
        assertEquals(-180.0, cache.getProvinces().get(4L).getLongitude());
    }

    @Test
    void testSpecialCharactersInName() {
        String[] specialNames = {
            "Province & City",
            "Province-City",
            "Province_City",
            "Province.City",
            "Province/City",
            "Province@City",
            "Province#City",
            "Province$City",
            "Province%City",
            "Province*City",
            "Province+City",
            "Province=City",
            "Province?City",
            "Province!City",
            "Province,City",
            "Province;City",
            "Province:City"
        };
        
        Map<Long, Province> provinces = new HashMap<>();
        for (int i = 0; i < specialNames.length; i++) {
            provinces.put((long) (i + 1), new Province(i + 1L, specialNames[i], -6.0 + i, 106.0 + i));
        }
        
        cache.putProvinces(provinces);
        
        for (int i = 0; i < specialNames.length; i++) {
            Province cached = cache.getProvinces().get((long) (i + 1));
            assertNotNull(cached);
            assertEquals(specialNames[i], cached.getName());
        }
    }

    @Test
    void testUnicodeCharactersInName() {
        String[] unicodeNames = {
            "Provinsi Jawa",
            "Provinsi Sumatera",
            "Provinsi Sulawesi",
            "Provinsi Kalimantan",
            "Provinsi Papua",
            "Provinsi Nusa Tenggara",
            "Provinsi Maluku",
            "Kota Jakarta",
            "Kabupaten Bandung"
        };
        
        Map<Long, Province> provinces = new HashMap<>();
        for (int i = 0; i < unicodeNames.length; i++) {
            provinces.put((long) (i + 1), new Province(i + 1L, unicodeNames[i], -6.0 + i, 106.0 + i));
        }
        
        cache.putProvinces(provinces);
        
        for (int i = 0; i < unicodeNames.length; i++) {
            Province cached = cache.getProvinces().get((long) (i + 1));
            assertNotNull(cached);
            assertEquals(unicodeNames[i], cached.getName());
        }
    }

    @Test
    void testCodeExtractionWithMaxValues() {
        long maxVillageCode = Long.MAX_VALUE;
        long maxDistrictCode = Long.MAX_VALUE / 10000L;
        
        District district = new District(maxDistrictCode, 1000L, "Max District", -6.0, 106.0);
        Village village = new Village(maxVillageCode, maxDistrictCode, "Max Village", -6.0, 106.0);
        
        Map<Long, District> districts = new HashMap<>();
        districts.put(maxDistrictCode, district);
        
        Map<Long, Village> villages = new HashMap<>();
        villages.put(maxVillageCode, village);
        
        cache.putDistricts(districts);
        cache.putVillages(villages);
        
        District cachedDistrict = cache.getDistricts().get(maxDistrictCode);
        Village cachedVillage = cache.getVillages().get(maxVillageCode);
        
        assertNotNull(cachedDistrict);
        assertNotNull(cachedVillage);
        assertEquals(maxDistrictCode, cachedDistrict.getCode());
        assertEquals(maxVillageCode, cachedVillage.getCode());
    }

    @Test
    void testSingleEntityOperations() {
        Province singleProvince = new Province(1L, "Single Province", -6.2088, 106.8456);
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(1L, singleProvince);
        
        cache.putProvinces(provinces);
        
        assertEquals(1, cache.getProvinces().size());
        assertEquals(singleProvince, cache.getProvinces().get(1L));
        assertTrue(cache.isLoaded());
    }

    @Test
    void testVerySmallCode() {
        long smallCode = 11L;
        Province province = new Province(smallCode, "Small Code Province", -6.2088, 106.8456);
        
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(smallCode, province);
        
        cache.putProvinces(provinces);
        
        Province cached = cache.getProvinces().get(smallCode);
        assertNotNull(cached);
        assertEquals(smallCode, cached.getCode());
        
        long extractedProvinceCode = smallCode / 10000L;
        assertEquals(0L, extractedProvinceCode);
    }

    @Test
    void testZeroCoordinates() {
        Province zeroCoord = new Province(1L, "Zero Coordinates", 0.0, 0.0);
        
        Map<Long, Province> provinces = new HashMap<>();
        provinces.put(1L, zeroCoord);
        
        cache.putProvinces(provinces);
        
        Province cached = cache.getProvinces().get(1L);
        assertEquals(0.0, cached.getLatitude());
        assertEquals(0.0, cached.getLongitude());
    }

    @Test
    void testBuildFromWithNullValues() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();
        
        assertNotNull(service.buildFrom((Province) null));
        assertNotNull(service.buildFrom((City) null));
        assertNotNull(service.buildFrom((District) null));
        assertNotNull(service.buildFrom((Village) null));
    }

    @Test
    void testSearchWithVeryLongQuery() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("A");
        }
        String veryLongQuery = sb.toString();
        
        List<Province> results = service.searchProvinces(veryLongQuery);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testRefreshOnEmptyCache() {
        cache.refresh();
        
        assertFalse(cache.isLoaded());
        assertTrue(cache.getProvinces().isEmpty());
        assertTrue(cache.getCities().isEmpty());
        assertTrue(cache.getDistricts().isEmpty());
        assertTrue(cache.getVillages().isEmpty());
        assertEquals(0, cache.getStats().getProvinceCount());
    }
}
