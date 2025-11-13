package id.xtramile.indonesia.service;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.constant.Constant;
import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Default implementation of IndonesiaService.
 * <p>
 * This service provides query capabilities for Indonesia administrative data using
 * a cache and data loader. Data is automatically loaded during construction and can
 * be refreshed on demand. The service supports searching, finding, and hierarchical
 * queries across all administrative levels.
 *
 * @author Rigsto
 * @since 1.1
 */
public class DefaultIndonesiaService implements IndonesiaService {

    /**
     * The cache for storing administrative data.
     */
    private final IndonesiaDataCache cache;
    /**
     * The loader for loading administrative data.
     */
    private final IndonesiaDataLoader loader;
    /**
     * Cache for search results to improve performance on repeated queries.
     * Key format: "searchType:normalizedQuery" (e.g., "provinces:jakarta")
     */
    private final ConcurrentHashMap<String, List<?>> searchResultCache;

    /**
     * Constructs a new DefaultIndonesiaService with the specified cache and loader.
     * Data is automatically loaded during construction.
     *
     * @param cache  the cache implementation to use
     * @param loader the data loader implementation to use
     * @throws DataLoadException if data cannot be loaded during initialization
     */
    public DefaultIndonesiaService(IndonesiaDataCache cache, IndonesiaDataLoader loader) {
        this.cache = cache;
        this.loader = loader;
        this.searchResultCache = new ConcurrentHashMap<>();
        loadData();
    }

    @Override
    public Optional<Province> findProvince(Long provinceCode) {
        return Optional.ofNullable(cache.getProvince(provinceCode));
    }

    @Override
    public List<Province> getAllProvinces() {
        return new ArrayList<>(cache.getProvinces().values());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Province> searchProvinces(String query) {
        if (isQueryEmpty(query)) {
            return getAllProvinces();
        }

        String cacheKey = "provinces:" + normalizeQuery(query);
        List<?> cached = searchResultCache.get(cacheKey);
        if (cached != null) {
            return (List<Province>) cached;
        }

        String lowerQuery = query.toLowerCase();
        List<Province> results = cache.getProvinces().values().stream()
                .filter(province -> province.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
        
        searchResultCache.put(cacheKey, results);
        return results;
    }

    @Override
    public Optional<City> findCity(Long cityCode) {
        return Optional.ofNullable(cache.getCity(cityCode));
    }

    @Override
    public List<City> getCitiesByProvince(Long provinceCode) {
        return cache.getCitiesByProvinceCode(provinceCode);
    }

    @Override
    public List<City> getAllCities() {
        return new ArrayList<>(cache.getCities().values());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<City> searchCities(String query) {
        if (isQueryEmpty(query)) {
            return getAllCities();
        }

        String cacheKey = "cities:" + normalizeQuery(query);
        List<?> cached = searchResultCache.get(cacheKey);
        if (cached != null) {
            return (List<City>) cached;
        }

        String lowerQuery = query.toLowerCase();
        List<City> results = cache.getCities().values().stream()
                .filter(city -> city.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
        
        searchResultCache.put(cacheKey, results);
        return results;
    }

    @Override
    public Optional<District> findDistrict(Long districtCode) {
        return Optional.ofNullable(cache.getDistrict(districtCode));
    }

    @Override
    public List<District> getDistrictsByCity(Long cityCode) {
        return cache.getDistrictsByCityCode(cityCode);
    }

    @Override
    public List<District> getAllDistricts() {
        return new ArrayList<>(cache.getDistricts().values());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<District> searchDistricts(String query) {
        if (isQueryEmpty(query)) {
            return getAllDistricts();
        }

        String cacheKey = "districts:" + normalizeQuery(query);
        List<?> cached = searchResultCache.get(cacheKey);
        if (cached != null) {
            return (List<District>) cached;
        }

        String lowerQuery = query.toLowerCase();
        List<District> results = cache.getDistricts().values().stream()
                .filter(district -> district.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
        
        searchResultCache.put(cacheKey, results);
        return results;
    }

    @Override
    public Optional<Village> findVillage(Long villageCode) {
        return Optional.ofNullable(cache.getVillage(villageCode));
    }

    @Override
    public List<Village> getVillagesByDistrict(Long districtCode) {
        return cache.getVillagesByDistrictCode(districtCode);
    }

    @Override
    public List<Village> getAllVillages() {
        return new ArrayList<>(cache.getVillages().values());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Village> searchVillages(String query) {
        if (isQueryEmpty(query)) {
            return getAllVillages();
        }

        String cacheKey = "villages:" + normalizeQuery(query);
        List<?> cached = searchResultCache.get(cacheKey);
        if (cached != null) {
            return (List<Village>) cached;
        }

        String lowerQuery = query.toLowerCase();
        List<Village> results = cache.getVillages().values().stream()
                .filter(village -> village.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
        
        searchResultCache.put(cacheKey, results);
        return results;
    }

    @Override
    public List<Village> getVillagesByProvince(Long provinceCode) {
        return cache.getVillagesByProvinceCode(provinceCode);
    }

    @Override
    public List<Village> getVillagesByCity(Long cityCode) {
        return cache.getVillagesByCityCode(cityCode);
    }

    @Override
    public Indonesia buildFrom(Province province) {
        return new Indonesia(province, null, null, null);
    }

    @Override
    public Indonesia buildFrom(City city) {
        if (city == null) {
            return new Indonesia(null, null, null, null);
        }

        return new Indonesia(cache.getProvince(city.getProvinceCode()), city, null, null);
    }

    @Override
    public Indonesia buildFrom(District district) {
        if (district == null) {
            return new Indonesia(null, null, null, null);
        }

        long provinceCode = district.getCode() / Constant.DIVISOR_PROVINCE_FROM_DISTRICT;

        return new Indonesia(cache.getProvince(provinceCode), cache.getCity(district.getCityCode()), district, null);
    }

    @Override
    public Indonesia buildFrom(Village village) {
        if (village == null) {
            return new Indonesia(null, null, null, null);
        }

        long provinceCode = village.getCode() / Constant.DIVISOR_PROVINCE_FROM_VILLAGE;
        long cityCode = village.getCode() / Constant.DIVISOR_CITY_FROM_VILLAGE;

        return new Indonesia(
                cache.getProvince(provinceCode),
                cache.getCity(cityCode),
                cache.getDistrict(village.getDistrictCode()),
                village
        );
    }

    @Override
    public void refreshData() {
        try {
            cache.refresh();
            searchResultCache.clear(); // Clear search cache when data is refreshed
            loadData();

        } catch (DataLoadException e) {
            throw new RuntimeException(Constant.ERROR_FAILED_TO_REFRESH_DATA, e);
        }
    }

    @Override
    public boolean isDataLoaded() {
        return cache.isLoaded();
    }

    @Override
    public CacheStats getCacheStats() {
        return cache.getStats();
    }

    /**
     * Loads all administrative data into the cache.
     *
     * @throws DataLoadException if data cannot be loaded
     */
    private void loadData() throws DataLoadException {
        cache.putProvinces(loader.loadProvinces());
        cache.putCities(loader.loadCities());
        cache.putDistricts(loader.loadDistricts());
        cache.putVillages(loader.loadVillages());
    }

    /**
     * Checks if a search query is empty or null.
     *
     * @param query the query string to check
     * @return true if the query is null or empty after trimming
     */
    private boolean isQueryEmpty(String query) {
        return query == null || query.trim().isEmpty();
    }

    /**
     * Normalizes a query string for use as a cache key.
     * <p>
     * This method trims and converts the query to lowercase to ensure
     * consistent cache key generation.
     *
     * @param query the query string to normalize
     * @return the normalized query string
     */
    private String normalizeQuery(String query) {
        return query == null ? "" : query.trim().toLowerCase();
    }

}
