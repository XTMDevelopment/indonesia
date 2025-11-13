package id.xtramile.indonesia.util;

import id.xtramile.indonesia.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DistanceCalculator utility class.
 *
 * @author Rigsto
 * @since 1.1
 */
class DistanceCalculatorTest {

    // Jakarta coordinates (approximately)
    private static final double JAKARTA_LAT = -6.2088;
    private static final double JAKARTA_LON = 106.8456;

    // Bandung coordinates (approximately)
    private static final double BANDUNG_LAT = -6.9175;
    private static final double BANDUNG_LON = 107.6191;

    // Surabaya coordinates (approximately)
    private static final double SURABAYA_LAT = -7.2575;
    private static final double SURABAYA_LON = 112.7521;

    @Test
    void testDistanceBetweenProvinces_Valid() {
        Province jakarta = new Province(31L, "DKI Jakarta", JAKARTA_LAT, JAKARTA_LON);
        Province bandung = new Province(32L, "Jawa Barat", BANDUNG_LAT, BANDUNG_LON);

        double distance = DistanceCalculator.distanceBetweenProvinces(jakarta, bandung);
        assertTrue(distance > 0);
        assertTrue(distance > 100 && distance < 300);
    }

    @Test
    void testDistanceBetweenProvinces_Null() {
        Province jakarta = new Province(31L, "DKI Jakarta", JAKARTA_LAT, JAKARTA_LON);
        assertEquals(-1.0, DistanceCalculator.distanceBetweenProvinces(jakarta, null));
        assertEquals(-1.0, DistanceCalculator.distanceBetweenProvinces(null, jakarta));
        assertEquals(-1.0, DistanceCalculator.distanceBetweenProvinces(null, null));
    }

    @Test
    void testDistanceBetweenCities_Valid() {
        City jakarta = new City(3174L, 31L, "Jakarta", JAKARTA_LAT, JAKARTA_LON);
        City bandung = new City(3273L, 32L, "Bandung", BANDUNG_LAT, BANDUNG_LON);

        double distance = DistanceCalculator.distanceBetweenCities(jakarta, bandung);
        assertTrue(distance > 0);
        assertTrue(distance > 100 && distance < 300);
    }

    @Test
    void testDistanceBetweenCities_Null() {
        City jakarta = new City(3174L, 31L, "Jakarta", JAKARTA_LAT, JAKARTA_LON);
        assertEquals(-1.0, DistanceCalculator.distanceBetweenCities(jakarta, null));
        assertEquals(-1.0, DistanceCalculator.distanceBetweenCities(null, jakarta));
    }

    @Test
    void testDistanceBetweenDistricts_Valid() {
        District district1 = new District(317401L, 3174L, "District 1", JAKARTA_LAT, JAKARTA_LON);
        District district2 = new District(327301L, 3273L, "District 2", BANDUNG_LAT, BANDUNG_LON);

        double distance = DistanceCalculator.distanceBetweenDistricts(district1, district2);
        assertTrue(distance > 0);
    }

    @Test
    void testDistanceBetweenVillages_Valid() {
        Village village1 = new Village(3174011001L, 317401L, "Village 1", JAKARTA_LAT, JAKARTA_LON);
        Village village2 = new Village(3273011001L, 327301L, "Village 2", BANDUNG_LAT, BANDUNG_LON);

        double distance = DistanceCalculator.distanceBetweenVillages(village1, village2);
        assertTrue(distance > 0);
    }

    @Test
    void testDistanceBetweenCoordinates() {
        double distance = DistanceCalculator.distanceBetweenCoordinates(
                JAKARTA_LAT, JAKARTA_LON,
                BANDUNG_LAT, BANDUNG_LON
        );

        assertTrue(distance > 0);
        assertTrue(distance > 100 && distance < 300);
    }

    @Test
    void testDistanceBetweenCoordinatesInMeters() {
        double distanceKm = DistanceCalculator.distanceBetweenCoordinates(
                JAKARTA_LAT, JAKARTA_LON,
                BANDUNG_LAT, BANDUNG_LON
        );

        double distanceM = DistanceCalculator.distanceBetweenCoordinatesInMeters(
                JAKARTA_LAT, JAKARTA_LON,
                BANDUNG_LAT, BANDUNG_LON
        );

        assertEquals(distanceKm * 1000.0, distanceM, 1.0); // Allow 1 meter tolerance
    }

    @Test
    void testFindNearestVillage_Valid() {
        Village village1 = new Village(3174011001L, 317401L, "Village 1", JAKARTA_LAT, JAKARTA_LON);
        Village village2 = new Village(3273011001L, 327301L, "Village 2", BANDUNG_LAT, BANDUNG_LON);
        Village village3 = new Village(3578011001L, 357801L, "Village 3", SURABAYA_LAT, SURABAYA_LON);

        List<Village> villages = Arrays.asList(village1, village2, village3);

        Village nearest = DistanceCalculator.findNearestVillage(JAKARTA_LAT, JAKARTA_LON, villages);
        assertNotNull(nearest);
        assertEquals(village1, nearest);
    }

    @Test
    void testFindNearestVillage_EmptyList() {
        assertNull(DistanceCalculator.findNearestVillage(JAKARTA_LAT, JAKARTA_LON, Collections.emptyList()));
        assertNull(DistanceCalculator.findNearestVillage(JAKARTA_LAT, JAKARTA_LON, null));
    }

    @Test
    void testFindNearestDistrict_Valid() {
        District district1 = new District(317401L, 3174L, "District 1", JAKARTA_LAT, JAKARTA_LON);
        District district2 = new District(327301L, 3273L, "District 2", BANDUNG_LAT, BANDUNG_LON);

        List<District> districts = Arrays.asList(district1, district2);

        District nearest = DistanceCalculator.findNearestDistrict(JAKARTA_LAT, JAKARTA_LON, districts);
        assertNotNull(nearest);
        assertEquals(district1, nearest);
    }

    @Test
    void testFindNearestCity_Valid() {
        City city1 = new City(3174L, 31L, "Jakarta", JAKARTA_LAT, JAKARTA_LON);
        City city2 = new City(3273L, 32L, "Bandung", BANDUNG_LAT, BANDUNG_LON);

        List<City> cities = Arrays.asList(city1, city2);

        City nearest = DistanceCalculator.findNearestCity(JAKARTA_LAT, JAKARTA_LON, cities);
        assertNotNull(nearest);
        assertEquals(city1, nearest);
    }

    @Test
    void testFindNearestVillage_WithNulls() {
        Village village1 = new Village(3174011001L, 317401L, "Village 1", JAKARTA_LAT, JAKARTA_LON);
        Village village2 = new Village(3273011001L, 327301L, "Village 2", BANDUNG_LAT, BANDUNG_LON);

        List<Village> villages = new ArrayList<>();
        villages.add(null);
        villages.add(village1);
        villages.add(null);
        villages.add(village2);

        Village nearest = DistanceCalculator.findNearestVillage(JAKARTA_LAT, JAKARTA_LON, villages);
        assertNotNull(nearest);
        assertEquals(village1, nearest);
    }

    @Test
    void testDistanceBetweenSameLocation() {
        double distance = DistanceCalculator.distanceBetweenCoordinates(
                JAKARTA_LAT, JAKARTA_LON,
                JAKARTA_LAT, JAKARTA_LON
        );

        assertEquals(0.0, distance, 0.1);
    }
}

