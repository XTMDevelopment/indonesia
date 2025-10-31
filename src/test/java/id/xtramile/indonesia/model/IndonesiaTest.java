package id.xtramile.indonesia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IndonesiaTest {

    @Test
    void testConstructorAndGetters() {
        Province province = new Province(11L, "Jakarta", -6.2088, 106.8456);
        City city = new City(1101L, 11L, "Jakarta Pusat", -6.1818, 106.8318);
        District district = new District(110101L, 1101L, "Gambir", -6.1751, 106.8208);
        Village village = new Village(1101011001L, 110101L, "Gambir", -6.1751, 106.8208);

        Indonesia indonesia = new Indonesia(province, city, district, village);

        assertEquals(province, indonesia.getProvince());
        assertEquals(city, indonesia.getCity());
        assertEquals(district, indonesia.getDistrict());
        assertEquals(village, indonesia.getVillage());
    }

    @Test
    void testWithNullValues() {
        Province province = new Province(11L, "Jakarta", -6.2088, 106.8456);
        Indonesia indonesia = new Indonesia(province, null, null, null);

        assertEquals(province, indonesia.getProvince());
        assertNull(indonesia.getCity());
        assertNull(indonesia.getDistrict());
        assertNull(indonesia.getVillage());
    }

    @Test
    void testToString() {
        Province province = new Province(11L, "Jakarta", -6.2088, 106.8456);
        City city = new City(1101L, 11L, "Jakarta Pusat", -6.1818, 106.8318);
        Indonesia indonesia = new Indonesia(province, city, null, null);

        String result = indonesia.toString();
        assertNotNull(result);
        assertTrue(result.contains("Indonesia"));
        assertTrue(result.contains("province="));
        assertTrue(result.contains("city="));
    }
}

