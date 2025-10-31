package id.xtramile.indonesia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void testConstructorAndGetters() {
        City city = new City(1101L, 11L, "Jakarta Pusat", -6.1818, 106.8318);

        assertEquals(1101L, city.getCode());
        assertEquals(11L, city.getProvinceCode());
        assertEquals("Jakarta Pusat", city.getName());
        assertEquals(-6.1818, city.getLatitude(), 0.0001);
        assertEquals(106.8318, city.getLongitude(), 0.0001);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    void testEquals() {
        City city1 = new City(1101L, 11L, "Jakarta Pusat", -6.1818, 106.8318);
        City city2 = new City(1101L, 11L, "Jakarta Pusat", -6.1818, 106.8318);
        City city3 = new City(1201L, 12L, "Medan", 3.5833, 98.6667);

        assertEquals(city1, city2);
        assertNotEquals(city1, city3);
        assertEquals(city1, city1);
        assertNotEquals(null, city1);
    }

    @Test
    void testHashCode() {
        City city1 = new City(1101L, 11L, "Jakarta Pusat", -6.1818, 106.8318);
        City city2 = new City(1101L, 11L, "Jakarta Pusat", -6.1818, 106.8318);

        assertEquals(city1.hashCode(), city2.hashCode());
    }

    @Test
    void testToString() {
        City city = new City(1101L, 11L, "Jakarta Pusat", -6.1818, 106.8318);
        String result = city.toString();

        assertNotNull(result);
        assertTrue(result.contains("City"));
        assertTrue(result.contains("code=1101"));
        assertTrue(result.contains("provinceCode=11"));
    }
}

