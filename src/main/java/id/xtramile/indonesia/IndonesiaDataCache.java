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
     * @param provinces map of province codes to Province objects
     */
    void putProvinces(Map<Long, Province> provinces);

    /**
     * Also updates the cities-by-province index.
     *
     * @param cities map of city codes to City objects
     */
    void putCities(Map<Long, City> cities);

    /**
     * Also updates the districts-by-city index.
     *
     * @param districts map of district codes to District objects
     */
    void putDistricts(Map<Long, District> districts);

    /**
     * Also updates the villages-by-district index.
     *
     * @param villages map of village codes to Village objects
     */
    void putVillages(Map<Long, Village> villages);

    /**
     * @return copy of the map containing all provinces, keyed by province code
     */
    Map<Long, Province> getProvinces();

    /**
     * @return copy of the map containing all cities, keyed by city code
     */
    Map<Long, City> getCities();

    /**
     * @return copy of the map containing all districts, keyed by district code
     */
    Map<Long, District> getDistricts();

    /**
     * @return copy of the map containing all villages, keyed by village code
     */
    Map<Long, Village> getVillages();

    /**
     * @return copy of the map containing lists of cities, keyed by province code
     */
    Map<Long, List<City>> getCitiesByProvince();

    /**
     * @return copy of the map containing lists of districts, keyed by city code
     */
    Map<Long, List<District>> getDistrictsByCity();

    /**
     * @return copy of the map containing lists of villages, keyed by district code
     */
    Map<Long, List<Village>> getVillagesByDistrict();

    /**
     * Clears all cached data and resets the loaded state.
     */
    void refresh();

    /**
     * @return true if data has been loaded and provinces are not empty
     */
    boolean isLoaded();

    /**
     * @return CacheStats containing counts and last refresh time
     */
    CacheStats getStats();
}
