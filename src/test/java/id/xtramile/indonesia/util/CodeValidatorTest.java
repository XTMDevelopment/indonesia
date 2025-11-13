package id.xtramile.indonesia.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CodeValidator utility class.
 *
 * @author Rigsto
 * @since 1.1
 */
class CodeValidatorTest {

    @Test
    void testIsValidProvinceCode_ValidCodes() {
        assertTrue(CodeValidator.isValidProvinceCode(11L));
        assertTrue(CodeValidator.isValidProvinceCode(31L));
        assertTrue(CodeValidator.isValidProvinceCode(99L));
    }

    @Test
    void testIsValidProvinceCode_InvalidCodes() {
        assertFalse(CodeValidator.isValidProvinceCode(null));
        assertFalse(CodeValidator.isValidProvinceCode(10L)); // Too small
        assertFalse(CodeValidator.isValidProvinceCode(100L)); // Too large
        assertFalse(CodeValidator.isValidProvinceCode(1L)); // Wrong digit count
        assertFalse(CodeValidator.isValidProvinceCode(999L)); // Wrong digit count
    }

    @Test
    void testIsValidCityCode_ValidCodes() {
        assertTrue(CodeValidator.isValidCityCode(1101L));
        assertTrue(CodeValidator.isValidCityCode(3174L));
        assertTrue(CodeValidator.isValidCityCode(9999L));
    }

    @Test
    void testIsValidCityCode_InvalidCodes() {
        assertFalse(CodeValidator.isValidCityCode(null));
        assertFalse(CodeValidator.isValidCityCode(1100L)); // Too small
        assertFalse(CodeValidator.isValidCityCode(10000L)); // Too large
        assertFalse(CodeValidator.isValidCityCode(11L)); // Wrong digit count
        assertFalse(CodeValidator.isValidCityCode(99999L)); // Wrong digit count
    }

    @Test
    void testIsValidDistrictCode_ValidCodes() {
        assertTrue(CodeValidator.isValidDistrictCode(110101L));
        assertTrue(CodeValidator.isValidDistrictCode(317401L));
        assertTrue(CodeValidator.isValidDistrictCode(999999L));
    }

    @Test
    void testIsValidDistrictCode_InvalidCodes() {
        assertFalse(CodeValidator.isValidDistrictCode(null));
        assertFalse(CodeValidator.isValidDistrictCode(110100L)); // Too small
        assertFalse(CodeValidator.isValidDistrictCode(1000000L)); // Too large
        assertFalse(CodeValidator.isValidDistrictCode(1101L)); // Wrong digit count
        assertFalse(CodeValidator.isValidDistrictCode(9999999L)); // Wrong digit count
    }

    @Test
    void testIsValidVillageCode_ValidCodes() {
        assertTrue(CodeValidator.isValidVillageCode(1101011001L));
        assertTrue(CodeValidator.isValidVillageCode(3174011001L));
        assertTrue(CodeValidator.isValidVillageCode(9999999999L));
    }

    @Test
    void testIsValidVillageCode_InvalidCodes() {
        assertFalse(CodeValidator.isValidVillageCode(null));
        assertFalse(CodeValidator.isValidVillageCode(1101011000L)); // Too small
        assertFalse(CodeValidator.isValidVillageCode(10000000000L)); // Too large
        assertFalse(CodeValidator.isValidVillageCode(110101L)); // Wrong digit count
        assertFalse(CodeValidator.isValidVillageCode(99999999999L)); // Wrong digit count
    }

    @Test
    void testIsCityCodeBelongsToProvince_Valid() {
        assertTrue(CodeValidator.isCityCodeBelongsToProvince(1101L, 11L));
        assertTrue(CodeValidator.isCityCodeBelongsToProvince(3174L, 31L));
        assertTrue(CodeValidator.isCityCodeBelongsToProvince(9999L, 99L));
    }

    @Test
    void testIsCityCodeBelongsToProvince_Invalid() {
        assertFalse(CodeValidator.isCityCodeBelongsToProvince(null, 11L));
        assertFalse(CodeValidator.isCityCodeBelongsToProvince(1101L, null));
        assertFalse(CodeValidator.isCityCodeBelongsToProvince(1101L, 31L)); // Wrong province
        assertFalse(CodeValidator.isCityCodeBelongsToProvince(999L, 99L)); // Invalid city code
    }

    @Test
    void testIsDistrictCodeBelongsToCity_Valid() {
        assertTrue(CodeValidator.isDistrictCodeBelongsToCity(110101L, 1101L));
        assertTrue(CodeValidator.isDistrictCodeBelongsToCity(317401L, 3174L));
        assertTrue(CodeValidator.isDistrictCodeBelongsToCity(999999L, 9999L));
    }

    @Test
    void testIsDistrictCodeBelongsToCity_Invalid() {
        assertFalse(CodeValidator.isDistrictCodeBelongsToCity(null, 1101L));
        assertFalse(CodeValidator.isDistrictCodeBelongsToCity(110101L, null));
        assertFalse(CodeValidator.isDistrictCodeBelongsToCity(110101L, 3174L)); // Wrong city
        assertFalse(CodeValidator.isDistrictCodeBelongsToCity(1101L, 1101L)); // Invalid district code
    }

    @Test
    void testIsVillageCodeBelongsToDistrict_Valid() {
        assertTrue(CodeValidator.isVillageCodeBelongsToDistrict(1101011001L, 110101L));
        assertTrue(CodeValidator.isVillageCodeBelongsToDistrict(3174011001L, 317401L));
        assertTrue(CodeValidator.isVillageCodeBelongsToDistrict(9999999999L, 999999L));
    }

    @Test
    void testIsVillageCodeBelongsToDistrict_Invalid() {
        assertFalse(CodeValidator.isVillageCodeBelongsToDistrict(null, 110101L));
        assertFalse(CodeValidator.isVillageCodeBelongsToDistrict(1101011001L, null));
        assertFalse(CodeValidator.isVillageCodeBelongsToDistrict(1101011001L, 317401L)); // Wrong district
        assertFalse(CodeValidator.isVillageCodeBelongsToDistrict(110101L, 110101L)); // Invalid village code
    }
}

