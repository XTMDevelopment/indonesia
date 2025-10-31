package id.xtramile.indonesia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProvinceTest {

    @Test
    void testConstructorAndGetters() {
        Province province = new Province(11L, "Jakarta", -6.2088, 106.8456);

        assertEquals(11L, province.getCode());
        assertEquals("Jakarta", province.getName());
        assertEquals(-6.2088, province.getLatitude(), 0.0001);
        assertEquals(106.8456, province.getLongitude(), 0.0001);
    }

    @Test
    void testEquals() {
        Province province1 = new Province(11L, "Jakarta", -6.2088, 106.8456);
        Province province2 = new Province(11L, "Jakarta", -6.2088, 106.8456);
        Province province3 = new Province(12L, "Sumatera Utara", 3.5952, 98.6722);

        assertEquals(province1, province2);
        assertNotEquals(province1, province3);
        assertEquals(province1, province1);
        assertNotEquals(province1, null);
        assertNotEquals(province1, "Not a Province");
    }

    @Test
    void testHashCode() {
        Province province1 = new Province(11L, "Jakarta", -6.2088, 106.8456);
        Province province2 = new Province(11L, "Jakarta", -6.2088, 106.8456);
        Province province3 = new Province(12L, "Sumatera Utara", 3.5952, 98.6722);

        assertEquals(province1.hashCode(), province2.hashCode());
        assertNotEquals(province1.hashCode(), province3.hashCode());
    }

    @Test
    void testToString() {
        Province province = new Province(11L, "Jakarta", -6.2088, 106.8456);
        String result = province.toString();

        assertNotNull(result);
        assertTrue(result.contains("Province"));
        assertTrue(result.contains("code=11"));
        assertTrue(result.contains("name='Jakarta'"));
        assertTrue(result.contains("latitude=-6.2088"));
        assertTrue(result.contains("longitude=106.8456"));
    }
}

