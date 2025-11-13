package id.xtramile.indonesia.constant;

/**
 * Constants for Indonesia administrative code extraction and CSV processing.
 * <p>
 * This class contains all constants used throughout the Indonesia library for:
 * <ul>
 *   <li>CSV file paths and processing configuration</li>
 *   <li>Administrative code extraction using divisors</li>
 *   <li>Error messages for exceptions</li>
 * </ul>
 * <p>
 * Administrative code structure:
 * <ul>
 *   <li>Province code: 2 digits (e.g., 11)</li>
 *   <li>City code: 4 digits (e.g., 1122)</li>
 *   <li>District code: 6 digits (e.g., 112233)</li>
 *   <li>Village code: 10 digits (e.g., 1122334444)</li>
 * </ul>
 *
 * @author Rigsto
 * @since 1.0
 */
public final class Constant {
    /**
     * Classpath path to the provinces CSV file.
     * <p>
     * Expected format: code, name, latitude, longitude
     */
    public static final String CSV_PATH_PROVINCES = "/csv/provinces.csv";

    /**
     * Classpath path to the cities CSV file.
     * <p>
     * Expected format: code, province_code, name, latitude, longitude
     */
    public static final String CSV_PATH_CITIES = "/csv/cities.csv";

    /**
     * Classpath path to the districts CSV file.
     * <p>
     * Expected format: code, city_code, name, latitude, longitude
     */
    public static final String CSV_PATH_DISTRICTS = "/csv/districts.csv";

    /**
     * Classpath path prefix for village CSV files.
     * <p>
     * Village files are organized by province: /csv/villages/{provinceCode}.csv
     * Expected format: code, district_code, name, latitude, longitude
     */
    public static final String CSV_PATH_VILLAGES_PREFIX = "/csv/villages/";

    /**
     * File extension for CSV files.
     */
    public static final String CSV_EXTENSION = ".csv";

    /**
     * Expected number of columns in the provinces CSV file.
     * <p>
     * Format: code, name, latitude, longitude (4 columns)
     */
    public static final int PROVINCE_CSV_COLUMN_COUNT = 4;

    /**
     * Expected number of columns in the cities CSV file.
     * <p>
     * Format: code, province_code, name, latitude, longitude (5 columns)
     */
    public static final int CITY_CSV_COLUMN_COUNT = 5;

    /**
     * Expected number of columns in the districts CSV file.
     * <p>
     * Format: code, city_code, name, latitude, longitude (5 columns)
     */
    public static final int DISTRICT_CSV_COLUMN_COUNT = 5;

    /**
     * Expected number of columns in the villages CSV file.
     * <p>
     * Format: code, district_code, name, latitude, longitude (5 columns)
     */
    public static final int VILLAGE_CSV_COLUMN_COUNT = 5;

    /**
     * Delimiter used in CSV files.
     * <p>
     * Used for parsing and joining CSV data.
     */
    public static final String CSV_DELIMITER = ",";

    /**
     * Error message prefix when failing to parse province data from CSV.
     * <p>
     * The actual CSV line data is appended to this message.
     */
    public static final String ERROR_FAILED_TO_PARSE_PROVINCE = "Failed to parse province data: ";

    /**
     * Error message prefix when failing to parse city data from CSV.
     * <p>
     * The actual CSV line data is appended to this message.
     */
    public static final String ERROR_FAILED_TO_PARSE_CITY = "Failed to parse city data: ";

    /**
     * Error message prefix when failing to parse district data from CSV.
     * <p>
     * The actual CSV line data is appended to this message.
     */
    public static final String ERROR_FAILED_TO_PARSE_DISTRICT = "Failed to parse district data: ";

    /**
     * Error message prefix when failing to parse village data from CSV.
     * <p>
     * The actual CSV line data is appended to this message.
     */
    public static final String ERROR_FAILED_TO_PARSE_VILLAGE = "Failed to parse village data: ";

    /**
     * Error message when failing to load provinces from CSV file.
     * <p>
     * Used when the CSV file cannot be read or accessed.
     */
    public static final String ERROR_FAILED_TO_LOAD_PROVINCES = "Failed to load provinces from CSV";

    /**
     * Error message when failing to load cities from CSV file.
     * <p>
     * Used when the CSV file cannot be read or accessed.
     */
    public static final String ERROR_FAILED_TO_LOAD_CITIES = "Failed to load cities from CSV";

    /**
     * Error message when failing to load districts from CSV file.
     * <p>
     * Used when the CSV file cannot be read or accessed.
     */
    public static final String ERROR_FAILED_TO_LOAD_DISTRICTS = "Failed to load districts from CSV";

    /**
     * Error message when failing to load villages from CSV files.
     * <p>
     * Used when village CSV files cannot be read or accessed.
     */
    public static final String ERROR_FAILED_TO_LOAD_VILLAGES = "Failed to load villages from CSV";

    /**
     * Error message when failing to refresh data in the cache.
     * <p>
     * Used when data refresh operation fails.
     */
    public static final String ERROR_FAILED_TO_REFRESH_DATA = "Failed to refresh data";

    /**
     * Divisor for extracting province code from a district code.
     * <p>
     * Example: district code 110101 / 10000 = 11 (province code)
     * <p>
     * District codes are 6 digits, province codes are 2 digits.
     * Dividing by 10000 removes the last 4 digits (city + district).
     */
    public static final long DIVISOR_PROVINCE_FROM_DISTRICT = 10000L;

    /**
     * Divisor for extracting province code from a village code.
     * <p>
     * Example: village code 1101011001 / 100000000 = 11 (province code)
     * <p>
     * Village codes are 10 digits, province codes are 2 digits.
     * Dividing by 100000000 removes the last 8 digits (city + district + village).
     */
    public static final long DIVISOR_PROVINCE_FROM_VILLAGE = 100000000L;

    /**
     * Divisor for extracting city code from a village code.
     * <p>
     * Example: village code 1101011001 / 1000000 = 1101 (city code)
     * <p>
     * Village codes are 10 digits, city codes are 4 digits.
     * Dividing by 1000000 removes the last 6 digits (district + village).
     */
    public static final long DIVISOR_CITY_FROM_VILLAGE = 1000000L;

    private Constant() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}

