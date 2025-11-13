package id.xtramile.indonesia.util;

/**
 * Utility class for validating Indonesia administrative codes.
 * <p>
 * This class provides static methods to validate administrative codes
 * for provinces, cities, districts, and villages based on their expected
 * digit length and format.
 * <p>
 * Administrative code structure:
 * <ul>
 *   <li>Province code: 2 digits (range: 11-99)</li>
 *   <li>City code: 4 digits (range: 1101-9999)</li>
 *   <li>District code: 6 digits (range: 110101-999999)</li>
 *   <li>Village code: 10 digits (range: 1101011001-9999999999)</li>
 * </ul>
 *
 * @author Rigsto
 * @since 1.1
 */
public final class CodeValidator {

    /**
     * Minimum province code value (11).
     */
    private static final long MIN_PROVINCE_CODE = 11L;
    /**
     * Maximum province code value (99).
     */
    private static final long MAX_PROVINCE_CODE = 99L;
    /**
     * Minimum city code value (1101).
     */
    private static final long MIN_CITY_CODE = 1101L;
    /**
     * Maximum city code value (9999).
     */
    private static final long MAX_CITY_CODE = 9999L;
    /**
     * Minimum district code value (110101).
     */
    private static final long MIN_DISTRICT_CODE = 110101L;
    /**
     * Maximum district code value (999999).
     */
    private static final long MAX_DISTRICT_CODE = 999999L;
    /**
     * Minimum village code value (1101011001).
     */
    private static final long MIN_VILLAGE_CODE = 1101011001L;
    /**
     * Maximum village code value (9999999999L).
     */
    private static final long MAX_VILLAGE_CODE = 9999999999L;

    private CodeValidator() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Validates a province code.
     * <p>
     * A valid province code must be:
     * <ul>
     *   <li>Between 11 and 99 (inclusive)</li>
     *   <li>Exactly 2 digits when represented as a number</li>
     * </ul>
     *
     * @param provinceCode the province code to validate
     * @return true if the code is valid, false otherwise
     */
    public static boolean isValidProvinceCode(Long provinceCode) {
        if (provinceCode == null) {
            return false;
        }

        return provinceCode >= MIN_PROVINCE_CODE && provinceCode <= MAX_PROVINCE_CODE
                && getDigitCount(provinceCode) == 2;
    }

    /**
     * Validates a city code.
     * <p>
     * A valid city code must be:
     * <ul>
     *   <li>Between 1101 and 9999 (inclusive)</li>
     *   <li>Exactly 4 digits when represented as a number</li>
     * </ul>
     *
     * @param cityCode the city code to validate
     * @return true if the code is valid, false otherwise
     */
    public static boolean isValidCityCode(Long cityCode) {
        if (cityCode == null) {
            return false;
        }

        return cityCode >= MIN_CITY_CODE && cityCode <= MAX_CITY_CODE
                && getDigitCount(cityCode) == 4;
    }

    /**
     * Validates a district code.
     * <p>
     * A valid district code must be:
     * <ul>
     *   <li>Between 110101 and 999999 (inclusive)</li>
     *   <li>Exactly 6 digits when represented as a number</li>
     * </ul>
     *
     * @param districtCode the district code to validate
     * @return true if the code is valid, false otherwise
     */
    public static boolean isValidDistrictCode(Long districtCode) {
        if (districtCode == null) {
            return false;
        }

        return districtCode >= MIN_DISTRICT_CODE && districtCode <= MAX_DISTRICT_CODE
                && getDigitCount(districtCode) == 6;
    }

    /**
     * Validates a village code.
     * <p>
     * A valid village code must be:
     * <ul>
     *   <li>Between 1101011001 and 9999999999 (inclusive)</li>
     *   <li>Exactly 10 digits when represented as a number</li>
     * </ul>
     *
     * @param villageCode the village code to validate
     * @return true if the code is valid, false otherwise
     */
    public static boolean isValidVillageCode(Long villageCode) {
        if (villageCode == null) {
            return false;
        }

        return villageCode >= MIN_VILLAGE_CODE && villageCode <= MAX_VILLAGE_CODE
                && getDigitCount(villageCode) == 10;
    }

    /**
     * Validates that a city code belongs to a specific province.
     * <p>
     * This checks if the first 2 digits of the city code match the province code.
     *
     * @param cityCode    the city code to validate
     * @param provinceCode the province code to check against
     * @return true if the city code belongs to the province, false otherwise
     */
    public static boolean isCityCodeBelongsToProvince(Long cityCode, Long provinceCode) {
        if (cityCode == null || provinceCode == null) {
            return false;
        }

        if (!isValidCityCode(cityCode) || !isValidProvinceCode(provinceCode)) {
            return false;
        }

        long extractedProvinceCode = cityCode / 100L;
        return extractedProvinceCode == provinceCode;
    }

    /**
     * Validates that a district code belongs to a specific city.
     * <p>
     * This checks if the first 4 digits of the district code match the city code.
     *
     * @param districtCode the district code to validate
     * @param cityCode     the city code to check against
     * @return true if the district code belongs to the city, false otherwise
     */
    public static boolean isDistrictCodeBelongsToCity(Long districtCode, Long cityCode) {
        if (districtCode == null || cityCode == null) {
            return false;
        }

        if (!isValidDistrictCode(districtCode) || !isValidCityCode(cityCode)) {
            return false;
        }

        long extractedCityCode = districtCode / 100L;
        return extractedCityCode == cityCode;
    }

    /**
     * Validates that a village code belongs to a specific district.
     * <p>
     * This checks if the first 6 digits of the village code match the district code.
     *
     * @param villageCode  the village code to validate
     * @param districtCode the district code to check against
     * @return true if the village code belongs to the district, false otherwise
     */
    public static boolean isVillageCodeBelongsToDistrict(Long villageCode, Long districtCode) {
        if (villageCode == null || districtCode == null) {
            return false;
        }
        
        if (!isValidVillageCode(villageCode) || !isValidDistrictCode(districtCode)) {
            return false;
        }

        long extractedDistrictCode = villageCode / 10000L;
        return extractedDistrictCode == districtCode;
    }

    /**
     * Counts the number of digits in a number.
     *
     * @param number the number to count digits for
     * @return the number of digits
     */
    private static int getDigitCount(long number) {
        if (number == 0) {
            return 1;
        }
        return (int) Math.log10(Math.abs(number)) + 1;
    }
}

