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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultIndonesiaService implements IndonesiaService {

    private final IndonesiaDataCache cache;
    private final IndonesiaDataLoader loader;

    public DefaultIndonesiaService(IndonesiaDataCache cache, IndonesiaDataLoader loader) {
        this.cache = cache;
        this.loader = loader;
        loadData();
    }

    @Override
    public Optional<Province> findProvince(Integer provinceCode) {
        return Optional.ofNullable(cache.getProvinces().get(provinceCode));
    }

    @Override
    public List<Province> getAllProvinces() {
        return new ArrayList<>(cache.getProvinces().values());
    }

    @Override
    public List<Province> searchProvinces(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProvinces();
        }
        String lowerQuery = query.toLowerCase();
        return cache.getProvinces().values().stream()
                .filter(province -> province.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<City> findCity(Integer cityCode) {
        return Optional.ofNullable(cache.getCities().get(cityCode));
    }

    @Override
    public List<City> getCitiesByProvince(Integer provinceCode) {
        return new ArrayList<>(cache.getCitiesByProvince().getOrDefault(provinceCode, new ArrayList<>()));
    }

    @Override
    public List<City> getAllCities() {
        return new ArrayList<>(cache.getCities().values());
    }

    @Override
    public List<City> searchCities(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllCities();
        }
        String lowerQuery = query.toLowerCase();
        return cache.getCities().values().stream()
                .filter(city -> city.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<District> findDistrict(Integer districtCode) {
        return Optional.ofNullable(cache.getDistricts().get(districtCode));
    }

    @Override
    public List<District> getDistrictsByCity(Integer cityCode) {
        return new ArrayList<>(cache.getDistrictsByCity().getOrDefault(cityCode, new ArrayList<>()));
    }

    @Override
    public List<District> getAllDistricts() {
        return new ArrayList<>(cache.getDistricts().values());
    }

    @Override
    public List<District> searchDistricts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllDistricts();
        }
        String lowerQuery = query.toLowerCase();
        return cache.getDistricts().values().stream()
                .filter(district -> district.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Village> findVillage(Integer villageCode) {
        return Optional.ofNullable(cache.getVillages().get(villageCode));
    }

    @Override
    public List<Village> getVillagesByDistrict(Integer districtCode) {
        return new ArrayList<>(cache.getVillagesByDistrict().getOrDefault(districtCode, new ArrayList<>()));
    }

    @Override
    public List<Village> getAllVillages() {
        return new ArrayList<>(cache.getVillages().values());
    }

    @Override
    public List<Village> searchVillages(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllVillages();
        }
        String lowerQuery = query.toLowerCase();
        return cache.getVillages().values().stream()
                .filter(village -> village.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    @Override
    public List<Village> getVillagesByProvince(Integer provinceCode) {
        Map<Integer, District> districts = cache.getDistricts();
        List<Village> villages = new ArrayList<>();
        
        List<District> provinceDistricts = districts.values().stream()
                .filter(district -> Objects.equals(district.getCode() / Constant.DIVISOR_PROVINCE_FROM_DISTRICT, provinceCode))
                .collect(Collectors.toList());
        
        Map<Integer, List<Village>> villagesByDistrict = cache.getVillagesByDistrict();
        for (District district : provinceDistricts) {
            List<Village> districtVillages = villagesByDistrict.getOrDefault(district.getCode(), new ArrayList<>());
            villages.addAll(districtVillages);
        }
        
        return villages;
    }

    @Override
    public List<Village> getVillagesByCity(Integer cityCode) {
        Map<Integer, District> districts = cache.getDistricts();
        List<Village> villages = new ArrayList<>();
        
        // Get all districts in the city
        List<District> cityDistricts = districts.values().stream()
                .filter(district -> Objects.equals(district.getCityCode(), cityCode))
                .collect(Collectors.toList());
        
        // Get all villages in those districts
        Map<Integer, List<Village>> villagesByDistrict = cache.getVillagesByDistrict();
        for (District district : cityDistricts) {
            List<Village> districtVillages = villagesByDistrict.getOrDefault(district.getCode(), new ArrayList<>());
            villages.addAll(districtVillages);
        }
        
        return villages;
    }

    @Override
    public Indonesia buildFrom(Province province) {
        if (province == null) {
            return new Indonesia(null, null, null, null);
        }
        return new Indonesia(province, null, null, null);
    }

    @Override
    public Indonesia buildFrom(City city) {
        if (city == null) {
            return new Indonesia(null, null, null, null);
        }
        Map<Integer, Province> provinces = cache.getProvinces();
        Province province = provinces.get(city.getProvinceCode());
        return new Indonesia(province, city, null, null);
    }

    @Override
    public Indonesia buildFrom(District district) {
        if (district == null) {
            return new Indonesia(null, null, null, null);
        }
        Map<Integer, Province> provinces = cache.getProvinces();
        Map<Integer, City> cities = cache.getCities();
        int provinceCode = district.getCode() / Constant.DIVISOR_PROVINCE_FROM_DISTRICT;
        Province province = provinces.get(provinceCode);
        City city = cities.get(district.getCityCode());
        return new Indonesia(province, city, district, null);
    }

    @Override
    public Indonesia buildFrom(Village village) {
        if (village == null) {
            return new Indonesia(null, null, null, null);
        }
        Map<Integer, Province> provinces = cache.getProvinces();
        Map<Integer, City> cities = cache.getCities();
        Map<Integer, District> districts = cache.getDistricts();
        int provinceCode = village.getCode() / Constant.DIVISOR_PROVINCE_FROM_VILLAGE;
        int cityCode = village.getCode() / Constant.DIVISOR_CITY_FROM_VILLAGE;
        Province province = provinces.get(provinceCode);
        City city = cities.get(cityCode);
        District district = districts.get(village.getDistrictCode());
        return new Indonesia(province, city, district, village);
    }

    @Override
    public void refreshData() {
        try {
            cache.refresh();
            loadData();
        } catch (DataLoadException e) {
            throw new RuntimeException("Failed to refresh data", e);
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

    private void loadData() throws DataLoadException {
        try {
            cache.putProvinces(loader.loadProvinces());
            cache.putCities(loader.loadCities());
            cache.putDistricts(loader.loadDistricts());
            cache.putVillages(loader.loadVillages());
        } catch (DataLoadException e) {
            throw new RuntimeException("Failed to load data", e);
        }
    }
}
