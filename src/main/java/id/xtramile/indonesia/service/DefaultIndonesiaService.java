package id.xtramile.indonesia.service;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.constant.Constant;
import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.Indonesia;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;

import java.util.ArrayList;
import java.util.Collections;
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
        if (isQueryEmpty(query)) {
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
        if (isQueryEmpty(query)) {
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
        if (isQueryEmpty(query)) {
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
        if (isQueryEmpty(query)) {
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
        List<District> provinceDistricts = districts.values().stream()
                .filter(district -> Objects.equals(district.getCode() / Constant.DIVISOR_PROVINCE_FROM_DISTRICT, provinceCode))
                .collect(Collectors.toList());

        return collectVillagesFromDistricts(provinceDistricts);
    }

    @Override
    public List<Village> getVillagesByCity(Integer cityCode) {
        Map<Integer, District> districts = cache.getDistricts();
        List<District> cityDistricts = districts.values().stream()
                .filter(district -> Objects.equals(district.getCityCode(), cityCode))
                .collect(Collectors.toList());

        return collectVillagesFromDistricts(cityDistricts);
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

        Map<Integer, Province> provinces = cache.getProvinces();

        return new Indonesia(provinces.get(city.getProvinceCode()), city, null, null);
    }

    @Override
    public Indonesia buildFrom(District district) {
        if (district == null) {
            return new Indonesia(null, null, null, null);
        }

        Map<Integer, Province> provinces = cache.getProvinces();
        Map<Integer, City> cities = cache.getCities();

        int provinceCode = district.getCode() / Constant.DIVISOR_PROVINCE_FROM_DISTRICT;

        return new Indonesia(provinces.get(provinceCode), cities.get(district.getCityCode()), district, null);
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

        return new Indonesia(
                provinces.get(provinceCode),
                cities.get(cityCode),
                districts.get(village.getDistrictCode()),
                village
        );
    }

    @Override
    public void refreshData() {
        try {
            cache.refresh();
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

    private void loadData() throws DataLoadException {
        cache.putProvinces(loader.loadProvinces());
        cache.putCities(loader.loadCities());
        cache.putDistricts(loader.loadDistricts());
        cache.putVillages(loader.loadVillages());
    }

    private boolean isQueryEmpty(String query) {
        return query == null || query.trim().isEmpty();
    }

    private List<Village> collectVillagesFromDistricts(List<District> districts) {
        if (districts.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Integer, List<Village>> villagesByDistrict = cache.getVillagesByDistrict();
        List<Village> villages = new ArrayList<>();

        for (District district : districts) {
            List<Village> districtVillages = villagesByDistrict.getOrDefault(district.getCode(), Collections.emptyList());
            villages.addAll(districtVillages);
        }

        return villages;
    }
}
