package id.xtramile.indonesia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VillageTest {

    @Test
    void testConstructorAndGetters() {
        Village village = new Village(1101011001L, 110101L, "Gambir", -6.1751, 106.8208);

        assertEquals(1101011001L, village.getCode());
        assertEquals(110101L, village.getDistrictCode());
        assertEquals("Gambir", village.getName());
        assertEquals(-6.1751, village.getLatitude(), 0.0001);
        assertEquals(106.8208, village.getLongitude(), 0.0001);
    }

    @Test
    void testEquals() {
        Village village1 = new Village(1101011001L, 110101L, "Gambir", -6.1751, 106.8208);
        Village village2 = new Village(1101011001L, 110101L, "Gambir", -6.1751, 106.8208);
        Village village3 = new Village(1201011001L, 120101L, "Medan Barat", 3.5842, 98.6756);

        assertEquals(village1, village2);
        assertNotEquals(village1, village3);
        assertEquals(village1, village1);
        assertNotEquals(village1, null);
    }

    @Test
    void testHashCode() {
        Village village1 = new Village(1101011001L, 110101L, "Gambir", -6.1751, 106.8208);
        Village village2 = new Village(1101011001L, 110101L, "Gambir", -6.1751, 106.8208);

        assertEquals(village1.hashCode(), village2.hashCode());
    }

    @Test
    void testToString() {
        Village village = new Village(1101011001L, 110101L, "Gambir", -6.1751, 106.8208);
        String result = village.toString();

        assertNotNull(result);
        assertTrue(result.contains("Village"));
        assertTrue(result.contains("code=1101011001"));
        assertTrue(result.contains("districtCode=110101"));
    }
}

