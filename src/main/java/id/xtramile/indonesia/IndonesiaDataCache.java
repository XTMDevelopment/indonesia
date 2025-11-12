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
