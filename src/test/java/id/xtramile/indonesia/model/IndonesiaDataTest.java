package id.xtramile.indonesia.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndonesiaDataTest {

    @Test
    void testConstructorAndGetters() {
        Map<Long, Province> provinces = new HashMap<>();
        Map<Long, City> cities = new HashMap<>();
        Map<Long, District> districts = new HashMap<>();
        Map<Long, Village> villages = new HashMap<>();

        provinces.put(11L, new Province(11L, "Jakarta", -6.2088, 106.8456));
        cities.put(1101L, new City(1101L, 11L, "Jakarta Pusat", -6.1818, 106.8318));

        IndonesiaData data = new IndonesiaData(provinces, cities, districts, villages);

        assertEquals(provinces, data.getProvinces());
        assertEquals(cities, data.getCities());
        assertEquals(districts, data.getDistricts());
        assertEquals(villages, data.getVillages());
    }
}

