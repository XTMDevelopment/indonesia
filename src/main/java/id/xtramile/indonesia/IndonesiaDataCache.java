package id.xtramile.indonesia;

import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;

import java.util.List;
import java.util.Map;

/**
 * Interface for caching Indonesia administrative data (provinces, cities, districts, and villages).
 * <p>
 * This cache provides thread-safe storage and retrieval of Indonesia's administrative hierarchy
 * with support for hierarchical relationships between administrative units.
 *
 * @author Rigsto
 * @since 1.1
 */
public interface IndonesiaDataCache {

    /**
     * Stores provinces in the cache, replacing any existing province data.
     *
     * @param provinces a map of province codes to Province objects
     */
    void putProvinces(Map<Long, Province> provinces);

    /**
     * Stores cities in the cache, replacing any existing city data.
     * Also updates the cities-by-province index.
     *
     * @param cities a map of city codes to City objects
     */
    void putCities(Map<Long, City> cities);

    /**
     * Stores districts in the cache, replacing any existing district data.
     * Also updates the districts-by-city index.
     *
     * @param districts a map of district codes to District objects
     */
    void putDistricts(Map<Long, District> districts);

    /**
     * Stores villages in the cache, replacing any existing village data.
     * Also updates the villages-by-district index.
     *
     * @param villages a map of village codes to Village objects
     */
    void putVillages(Map<Long, Village> villages);

    /**
     * Retrieves all provinces from the cache.
     *
     * @return a copy of the map containing all provinces, keyed by province code
     */
    Map<Long, Province> getProvinces();

    /**
     * Retrieves all cities from the cache.
     *
     * @return a copy of the map containing all cities, keyed by city code
     */
    Map<Long, City> getCities();

    /**
     * Retrieves all districts from the cache.
     *
     * @return a copy of the map containing all districts, keyed by district code
     */
    Map<Long, District> getDistricts();

    /**
     * Retrieves all villages from the cache.
     *
     * @return a copy of the map containing all villages, keyed by village code
     */
    Map<Long, Village> getVillages();

    /**
     * Retrieves cities grouped by province code.
     *
     * @return a copy of the map containing lists of cities, keyed by province code
     */
    Map<Long, List<City>> getCitiesByProvince();

    /**
     * Retrieves districts grouped by city code.
     *
     * @return a copy of the map containing lists of districts, keyed by city code
     */
    Map<Long, List<District>> getDistrictsByCity();

    /**
     * Retrieves villages grouped by district code.
     *
     * @return a copy of the map containing lists of villages, keyed by district code
     */
    Map<Long, List<Village>> getVillagesByDistrict();

    /**
     * Retrieves a province by code without creating a defensive copy.
     *
     * @param provinceCode the province code
     * @return the Province if found, null otherwise
     * @since 1.1
     */
    Province getProvince(Long provinceCode);

    /**
     * Retrieves a city by code without creating a defensive copy.
     *
     * @param cityCode the city code
     * @return the City if found, null otherwise
     * @since 1.1
     */
    City getCity(Long cityCode);

    /**
     * Retrieves a district by code without creating a defensive copy.
     *
     * @param districtCode the district code
     * @return the District if found, null otherwise
     * @since 1.1
     */
    District getDistrict(Long districtCode);

    /**
     * Retrieves a village by code without creating a defensive copy.
     *
     * @param villageCode the village code
     * @return the Village if found, null otherwise
     * @since 1.1
     */
    Village getVillage(Long villageCode);

    /**
     * Retrieves cities by province code without creating a defensive copy.
     *
     * @param provinceCode the province code
     * @return a list of cities, or empty list if not found
     * @since 1.1
     */
    List<City> getCitiesByProvinceCode(Long provinceCode);

    /**
     * Retrieves districts by city code without creating a defensive copy.
     *
     * @param cityCode the city code
     * @return a list of districts, or empty list if not found
     * @since 1.1
     */
    List<District> getDistrictsByCityCode(Long cityCode);

    /**
     * Retrieves villages by district code without creating a defensive copy.
     *
     * @param districtCode the district code
     * @return a list of villages, or empty list if not found
     * @since 1.1
     */
    List<Village> getVillagesByDistrictCode(Long districtCode);

    /**
     * Retrieves villages by province code using optimized index.
     *
     * @param provinceCode the province code
     * @return a list of villages, or empty list if not found
     * @since 1.1
     */
    List<Village> getVillagesByProvinceCode(Long provinceCode);

    /**
     * Retrieves villages by city code using optimized index.
     *
     * @param cityCode the city code
     * @return a list of villages, or empty list if not found
     * @since 1.1
     */
    List<Village> getVillagesByCityCode(Long cityCode);

    /**
     * Clears all cached data and resets the loaded state.
     */
    void refresh();

    /**
     * Checks if the cache has been loaded with data.
     *
     * @return true if data has been loaded and provinces are not empty, false otherwise
     */
    boolean isLoaded();

    /**
     * Retrieves statistics about the cached data.
     *
     * @return CacheStats object containing counts and last refresh time
     */
    CacheStats getStats();
}
