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
     * @param provinceCode province code
     * @return Optional containing the Province if found
     */
    Optional<Province> findProvince(Long provinceCode);

    /**
     * @return list of all provinces
     */
    List<Province> getAllProvinces();

    /**
     * Case-insensitive partial match by name.
     *
     * @param query search query string
     * @return list of matching provinces, or all provinces if query is empty
     */
    List<Province> searchProvinces(String query);

    /**
     * @param cityCode city code
     * @return Optional containing the City if found
     */
    Optional<City> findCity(Long cityCode);

    /**
     * @param provinceCode province code
     * @return list of cities in the specified province
     */
    List<City> getCitiesByProvince(Long provinceCode);

    /**
     * @return list of all cities
     */
    List<City> getAllCities();

    /**
     * Case-insensitive partial match by name.
     *
     * @param query search query string
     * @return list of matching cities, or all cities if query is empty
     */
    List<City> searchCities(String query);

    /**
     * @param districtCode district code
     * @return Optional containing the District if found
     */
    Optional<District> findDistrict(Long districtCode);

    /**
     * @param cityCode city code
     * @return list of districts in the specified city
     */
    List<District> getDistrictsByCity(Long cityCode);

    /**
     * @return list of all districts
     */
    List<District> getAllDistricts();

    /**
     * Case-insensitive partial match by name.
     *
     * @param query search query string
     * @return list of matching districts, or all districts if query is empty
     */
    List<District> searchDistricts(String query);

    /**
     * @param villageCode village code
     * @return Optional containing the Village if found
     */
    Optional<Village> findVillage(Long villageCode);

    /**
     * @param districtCode district code
     * @return list of villages in the specified district
     */
    List<Village> getVillagesByDistrict(Long districtCode);

    /**
     * @return list of all villages
     */
    List<Village> getAllVillages();

    /**
     * Case-insensitive partial match by name.
     *
     * @param query search query string
     * @return list of matching villages, or all villages if query is empty
     */
    List<Village> searchVillages(String query);

    /**
     * @param provinceCode province code
     * @return list of villages in the specified province
     */
    List<Village> getVillagesByProvince(Long provinceCode);

    /**
     * @param cityCode city code
     * @return list of villages in the specified city
     */
    List<Village> getVillagesByCity(Long cityCode);

    /**
     * @param province province to build from
     * @return Indonesia object with only the province populated
     */
    Indonesia buildFrom(Province province);

    /**
     * @param city city to build from
     * @return Indonesia object with province and city populated
     */
    Indonesia buildFrom(City city);

    /**
     * @param district district to build from
     * @return Indonesia object with province, city, and district populated
     */
    Indonesia buildFrom(District district);

    /**
     * @param village village to build from
     * @return Indonesia object with complete hierarchy (province, city, district, village)
     */
    Indonesia buildFrom(Village village);

    /**
     * Clears the cache and reloads all administrative data.
     */
    void refreshData();

    /**
     * @return true if data is loaded
     */
    boolean isDataLoaded();

    /**
     * @return CacheStats with counts and last refresh time
     */
    CacheStats getCacheStats();
}
