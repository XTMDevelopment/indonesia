package id.xtramile.indonesia;

import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.model.*;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Indonesia administrative data.
 * <p>
 * Provides methods to search, find, and retrieve provinces, cities, districts, and villages
 * from the Indonesia administrative hierarchy. Supports hierarchical queries and building
 * complete Indonesia objects from individual administrative units.
 *
 * @author Rigsto
 */
public interface IndonesiaService {

    /**
     * Finds a province by its code.
     *
     * @param provinceCode the province code to search for
     * @return an Optional containing the Province if found, empty otherwise
     */
    Optional<Province> findProvince(Long provinceCode);

    /**
     * Retrieves all provinces.
     *
     * @return a list of all provinces
     */
    List<Province> getAllProvinces();

    /**
     * Searches for provinces by name (case-insensitive partial match).
     *
     * @param query the search query string
     * @return a list of provinces matching the query, or all provinces if query is empty
     */
    List<Province> searchProvinces(String query);

    /**
     * Finds a city by its code.
     *
     * @param cityCode the city code to search for
     * @return an Optional containing the City if found, empty otherwise
     */
    Optional<City> findCity(Long cityCode);

    /**
     * Retrieves all cities within a specific province.
     *
     * @param provinceCode the province code
     * @return a list of cities in the specified province
     */
    List<City> getCitiesByProvince(Long provinceCode);

    /**
     * Retrieves all cities.
     *
     * @return a list of all cities
     */
    List<City> getAllCities();

    /**
     * Searches for cities by name (case-insensitive partial match).
     *
     * @param query the search query string
     * @return a list of cities matching the query, or all cities if query is empty
     */
    List<City> searchCities(String query);

    /**
     * Finds a district by its code.
     *
     * @param districtCode the district code to search for
     * @return an Optional containing the District if found, empty otherwise
     */
    Optional<District> findDistrict(Long districtCode);

    /**
     * Retrieves all districts within a specific city.
     *
     * @param cityCode the city code
     * @return a list of districts in the specified city
     */
    List<District> getDistrictsByCity(Long cityCode);

    /**
     * Retrieves all districts.
     *
     * @return a list of all districts
     */
    List<District> getAllDistricts();

    /**
     * Searches for districts by name (case-insensitive partial match).
     *
     * @param query the search query string
     * @return a list of districts matching the query, or all districts if query is empty
     */
    List<District> searchDistricts(String query);

    /**
     * Finds a village by its code.
     *
     * @param villageCode the village code to search for
     * @return an Optional containing the Village if found, empty otherwise
     */
    Optional<Village> findVillage(Long villageCode);

    /**
     * Retrieves all villages within a specific district.
     *
     * @param districtCode the district code
     * @return a list of villages in the specified district
     */
    List<Village> getVillagesByDistrict(Long districtCode);

    /**
     * Retrieves all villages.
     *
     * @return a list of all villages
     */
    List<Village> getAllVillages();

    /**
     * Searches for villages by name (case-insensitive partial match).
     *
     * @param query the search query string
     * @return a list of villages matching the query, or all villages if query is empty
     */
    List<Village> searchVillages(String query);

    /**
     * Retrieves all villages within a specific province.
     *
     * @param provinceCode the province code
     * @return a list of villages in the specified province
     */
    List<Village> getVillagesByProvince(Long provinceCode);

    /**
     * Retrieves all villages within a specific city.
     *
     * @param cityCode the city code
     * @return a list of villages in the specified city
     */
    List<Village> getVillagesByCity(Long cityCode);

    /**
     * Builds an Indonesia object from a province, with only the province populated.
     *
     * @param province the province to build from
     * @return an Indonesia object containing the province
     */
    Indonesia buildFrom(Province province);

    /**
     * Builds an Indonesia object from a city, including its parent province.
     *
     * @param city the city to build from
     * @return an Indonesia object containing the province and city
     */
    Indonesia buildFrom(City city);

    /**
     * Builds an Indonesia object from a district, including its parent province and city.
     *
     * @param district the district to build from
     * @return an Indonesia object containing the province, city, and district
     */
    Indonesia buildFrom(District district);

    /**
     * Builds a complete Indonesia object from a village, including all parent administrative units.
     *
     * @param village the village to build from
     * @return an Indonesia object containing the complete hierarchy (province, city, district, village)
     */
    Indonesia buildFrom(Village village);

    /**
     * Refreshes the data by reloading from the data source.
     * This clears the cache and reloads all administrative data.
     */
    void refreshData();

    /**
     * Checks if data has been loaded into the service.
     *
     * @return true if data is loaded, false otherwise
     */
    boolean isDataLoaded();

    /**
     * Retrieves cache statistics including counts and last refresh time.
     *
     * @return CacheStats object with cache information
     */
    CacheStats getCacheStats();
}
