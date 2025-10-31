package id.xtramile.indonesia.constant;

/**
 * Constants for Indonesia administrative code extraction.
 * 
 * Code structure:
 * - Province code: 2 digits (e.g., 11)
 * - City code: 4 digits (e.g., 1122)
 * - District code: 6 digits (e.g., 112233)
 * - Village code: 10 digits (e.g., 1122334444)
 */
public class Constant {

    /**
     * Divisor to extract province code from district code.
     * District code format: XXXXXX where first 2 digits represent province code.
     */
    public static final int DIVISOR_PROVINCE_FROM_DISTRICT = 10000;

    /**
     * Divisor to extract province code from village code.
     * Village code format: XXXXXXXXXX where first 2 digits represent province code.
     */
    public static final int DIVISOR_PROVINCE_FROM_VILLAGE = 100000000;

    /**
     * Divisor to extract city code from village code.
     * Village code format: XXXXXXXXXX where first 4 digits represent city code.
     */
    public static final int DIVISOR_CITY_FROM_VILLAGE = 1000000;

    /**
     * Divisor to extract district code from village code.
     * Village code format: XXXXXXXXXX where first 6 digits represent district code.
     */
    public static final int DIVISOR_DISTRICT_FROM_VILLAGE = 10000;

    private Constant() {
        // Utility class - prevent instantiation
    }
}

