package id.xtramile.indonesia.constant;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ConstantTest {

    @Test
    void testCsvPaths() {
        assertEquals("/csv/provinces.csv", Constant.CSV_PATH_PROVINCES);
        assertEquals("/csv/cities.csv", Constant.CSV_PATH_CITIES);
        assertEquals("/csv/districts.csv", Constant.CSV_PATH_DISTRICTS);
        assertEquals("/csv/villages/", Constant.CSV_PATH_VILLAGES_PREFIX);
        assertEquals(".csv", Constant.CSV_EXTENSION);
    }

    @Test
    void testCsvColumnCounts() {
        assertEquals(4, Constant.PROVINCE_CSV_COLUMN_COUNT);
        assertEquals(5, Constant.CITY_CSV_COLUMN_COUNT);
        assertEquals(5, Constant.DISTRICT_CSV_COLUMN_COUNT);
        assertEquals(5, Constant.VILLAGE_CSV_COLUMN_COUNT);
    }

    @Test
    void testCsvDelimiter() {
        assertEquals(",", Constant.CSV_DELIMITER);
    }

    @Test
    void testErrorMessages() {
        assertEquals("Failed to parse province data: ", Constant.ERROR_FAILED_TO_PARSE_PROVINCE);
        assertEquals("Failed to parse city data: ", Constant.ERROR_FAILED_TO_PARSE_CITY);
        assertEquals("Failed to parse district data: ", Constant.ERROR_FAILED_TO_PARSE_DISTRICT);
        assertEquals("Failed to parse village data: ", Constant.ERROR_FAILED_TO_PARSE_VILLAGE);
        assertEquals("Failed to load provinces from CSV", Constant.ERROR_FAILED_TO_LOAD_PROVINCES);
        assertEquals("Failed to load cities from CSV", Constant.ERROR_FAILED_TO_LOAD_CITIES);
        assertEquals("Failed to load districts from CSV", Constant.ERROR_FAILED_TO_LOAD_DISTRICTS);
        assertEquals("Failed to load villages from CSV", Constant.ERROR_FAILED_TO_LOAD_VILLAGES);
        assertEquals("Failed to refresh data", Constant.ERROR_FAILED_TO_REFRESH_DATA);
    }

    @Test
    void testDivisors() {
        assertEquals(10000L, Constant.DIVISOR_PROVINCE_FROM_DISTRICT);
        assertEquals(100000000L, Constant.DIVISOR_PROVINCE_FROM_VILLAGE);
        assertEquals(1000000L, Constant.DIVISOR_CITY_FROM_VILLAGE);
    }

    @Test
    void testPrivateConstructorThrowsException() throws Exception {
        Constructor<Constant> constructor = Constant.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertEquals(AssertionError.class, exception.getCause().getClass());
        assertEquals("Utility class should not be instantiated", exception.getCause().getMessage());
    }

    @Test
    void testDivisorsValuesForCodeExtraction() {
        long districtCode = 110101L;
        long expectedProvinceCode = 11L;
        assertEquals(expectedProvinceCode, districtCode / Constant.DIVISOR_PROVINCE_FROM_DISTRICT);

        long villageCode = 1101011001L;
        assertEquals(expectedProvinceCode, villageCode / Constant.DIVISOR_PROVINCE_FROM_VILLAGE);

        long expectedCityCode = 1101L;
        assertEquals(expectedCityCode, villageCode / Constant.DIVISOR_CITY_FROM_VILLAGE);
    }
}
