package id.xtramile.indonesia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistrictTest {

    @Test
    void testConstructorAndGetters() {
        District district = new District(110101L, 1101L, "Gambir", -6.1751, 106.8208);

        assertEquals(110101L, district.getCode());
        assertEquals(1101L, district.getCityCode());
        assertEquals("Gambir", district.getName());
        assertEquals(-6.1751, district.getLatitude(), 0.0001);
        assertEquals(106.8208, district.getLongitude(), 0.0001);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    void testEquals() {
        District district1 = new District(110101L, 1101L, "Gambir", -6.1751, 106.8208);
        District district2 = new District(110101L, 1101L, "Gambir", -6.1751, 106.8208);
        District district3 = new District(120101L, 1201L, "Medan Barat", 3.5842, 98.6756);

        assertEquals(district1, district2);
        assertNotEquals(district1, district3);
        assertEquals(district1, district1);
        assertNotEquals(null, district1);
    }

    @Test
    void testHashCode() {
        District district1 = new District(110101L, 1101L, "Gambir", -6.1751, 106.8208);
        District district2 = new District(110101L, 1101L, "Gambir", -6.1751, 106.8208);

        assertEquals(district1.hashCode(), district2.hashCode());
    }

    @Test
    void testToString() {
        District district = new District(110101L, 1101L, "Gambir", -6.1751, 106.8208);
        String result = district.toString();

        assertNotNull(result);
        assertTrue(result.contains("District"));
        assertTrue(result.contains("code=110101"));
        assertTrue(result.contains("cityCode=1101"));
    }
}

