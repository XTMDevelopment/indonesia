package id.xtramile.indonesia.loader;

import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.IndonesiaData;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CsvIndonesiaDataLoaderTest {

    private final CsvIndonesiaDataLoader loader = new CsvIndonesiaDataLoader();

    @Test
    void testLoadProvinces() throws DataLoadException {
        Map<Long, Province> provinces = loader.loadProvinces();

        assertNotNull(provinces);
        assertFalse(provinces.isEmpty());

        Province province = provinces.values().iterator().next();
        assertNotNull(province);
        assertTrue(province.getCode() > 0);
        assertNotNull(province.getName());
        assertNotNull(province.getLatitude());
        assertNotNull(province.getLongitude());
    }

    @Test
    void testLoadCities() throws DataLoadException {
        Map<Long, City> cities = loader.loadCities();

        assertNotNull(cities);
        assertFalse(cities.isEmpty());

        City city = cities.values().iterator().next();
        assertNotNull(city);
        assertTrue(city.getCode() > 0);
        assertTrue(city.getProvinceCode() > 0);
        assertNotNull(city.getName());
    }

    @Test
    void testLoadDistricts() throws DataLoadException {
        Map<Long, District> districts = loader.loadDistricts();

        assertNotNull(districts);
        assertFalse(districts.isEmpty());

        District district = districts.values().iterator().next();
        assertNotNull(district);
        assertTrue(district.getCode() > 0);
        assertTrue(district.getCityCode() > 0);
        assertNotNull(district.getName());
    }

    @Test
    void testLoadVillages() throws DataLoadException {
        Map<Long, Village> villages = loader.loadVillages();

        assertNotNull(villages);
        assertFalse(villages.isEmpty());

        Village village = villages.values().iterator().next();
        assertNotNull(village);
        assertTrue(village.getCode() > 0);
        assertTrue(village.getDistrictCode() > 0);
        assertNotNull(village.getName());
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
    }

    @Test
    void testLoadVillagesLoadsMultipleProvinceFiles() throws DataLoadException {
        Map<Long, Village> villages = loader.loadVillages();

        assertNotNull(villages);
        assertFalse(villages.isEmpty());

        int uniqueDistricts = (int) villages.values().stream()
                .map(Village::getDistrictCode)
                .distinct()
                .count();

        assertTrue(uniqueDistricts > 0);
    }
}

