package id.xtramile.indonesia.constant;

/**
 * Constants for Indonesia administrative code extraction and CSV processing.
 * <p>
 * Code structure:
 * <ul>
 *   <li>Province code: 2 digits (e.g., 11)</li>
 *   <li>City code: 4 digits (e.g., 1122)</li>
 *   <li>District code: 6 digits (e.g., 112233)</li>
 *   <li>Village code: 10 digits (e.g., 1122334444)</li>
 * </ul>
 *
 * @since 1.0
 */
public final class Constant {
    public static final String CSV_PATH_PROVINCES = "/csv/provinces.csv";
    public static final String CSV_PATH_CITIES = "/csv/cities.csv";
    public static final String CSV_PATH_DISTRICTS = "/csv/districts.csv";
    public static final String CSV_PATH_VILLAGES_PREFIX = "/csv/villages/";
    public static final String CSV_EXTENSION = ".csv";

    public static final int PROVINCE_CSV_COLUMN_COUNT = 4;
    public static final int CITY_CSV_COLUMN_COUNT = 5;
    public static final int DISTRICT_CSV_COLUMN_COUNT = 5;
    public static final int VILLAGE_CSV_COLUMN_COUNT = 5;

    public static final String CSV_DELIMITER = ",";

    public static final String ERROR_FAILED_TO_PARSE_PROVINCE = "Failed to parse province data: ";
    public static final String ERROR_FAILED_TO_PARSE_CITY = "Failed to parse city data: ";
    public static final String ERROR_FAILED_TO_PARSE_DISTRICT = "Failed to parse district data: ";
    public static final String ERROR_FAILED_TO_PARSE_VILLAGE = "Failed to parse village data: ";
    public static final String ERROR_FAILED_TO_LOAD_PROVINCES = "Failed to load provinces from CSV";
    public static final String ERROR_FAILED_TO_LOAD_CITIES = "Failed to load cities from CSV";
    public static final String ERROR_FAILED_TO_LOAD_DISTRICTS = "Failed to load districts from CSV";
    public static final String ERROR_FAILED_TO_LOAD_VILLAGES = "Failed to load villages from CSV";
    public static final String ERROR_FAILED_TO_REFRESH_DATA = "Failed to refresh data";

    public static final long DIVISOR_PROVINCE_FROM_DISTRICT = 10000L;
    public static final long DIVISOR_PROVINCE_FROM_VILLAGE = 100000000L;
    public static final long DIVISOR_CITY_FROM_VILLAGE = 1000000L;

    private Constant() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}

