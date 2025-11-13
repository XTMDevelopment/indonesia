package id.xtramile.indonesia.cache;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.constant.Constant;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe in-memory implementation of IndonesiaDataCache.
 * <p>
 * This implementation uses ConcurrentHashMap for thread-safe storage and maintains
 * hierarchical indexes for efficient lookups. All returned maps are defensive copies
 * to prevent external modification of the cache.
 *
 * @author Rigsto
 * @since 1.1
 */
public class InMemoryIndonesiaCache implements IndonesiaDataCache {

    private final Map<Long, Province> provinces = new ConcurrentHashMap<>();
    private final Map<Long, City> cities = new ConcurrentHashMap<>();
    private final Map<Long, District> districts = new ConcurrentHashMap<>();
    private final Map<Long, Village> villages = new ConcurrentHashMap<>();

    private final Map<Long, List<City>> citiesByProvince = new ConcurrentHashMap<>();
    private final Map<Long, List<District>> districtsByCity = new ConcurrentHashMap<>();
    private final Map<Long, List<Village>> villagesByDistrict = new ConcurrentHashMap<>();
    private final Map<Long, List<Village>> villagesByProvince = new ConcurrentHashMap<>();
    private final Map<Long, List<Village>> villagesByCity = new ConcurrentHashMap<>();
    private final AtomicLong lastRefreshTime = new AtomicLong(0);
    private volatile boolean loaded = false;

    @Override
    public void putProvinces(Map<Long, Province> provinces) {
        this.provinces.clear();
        this.provinces.putAll(provinces);
        updateRefreshTime();
    }

    @Override
    public void putCities(Map<Long, City> cities) {
        this.cities.clear();
        this.cities.putAll(cities);

        this.citiesByProvince.clear();
        cities.values().forEach(city ->
                this.citiesByProvince.computeIfAbsent(city.getProvinceCode(), k -> new ArrayList<>())
                        .add(city));

        updateRefreshTime();
    }

    @Override
    public void putDistricts(Map<Long, District> districts) {
        this.districts.clear();
        this.districts.putAll(districts);

        this.districtsByCity.clear();
        districts.values().forEach(district ->
                this.districtsByCity.computeIfAbsent(district.getCityCode(), k -> new ArrayList<>())
                        .add(district));

        updateRefreshTime();
    }

    @Override
    public void putVillages(Map<Long, Village> villages) {
        this.villages.clear();
        this.villages.putAll(villages);

        this.villagesByDistrict.clear();
        this.villagesByProvince.clear();
        this.villagesByCity.clear();

        villages.values().forEach(village -> {
            this.villagesByDistrict.computeIfAbsent(village.getDistrictCode(), k -> new ArrayList<>())
                    .add(village);

            long provinceCode = village.getCode() / Constant.DIVISOR_PROVINCE_FROM_VILLAGE;
            this.villagesByProvince.computeIfAbsent(provinceCode, k -> new ArrayList<>())
                    .add(village);

            long cityCode = village.getCode() / Constant.DIVISOR_CITY_FROM_VILLAGE;
            this.villagesByCity.computeIfAbsent(cityCode, k -> new ArrayList<>())
                    .add(village);
        });

        updateRefreshTime();
    }

    @Override
    public Map<Long, Province> getProvinces() {
        return new HashMap<>(provinces);
    }

    @Override
    public Map<Long, City> getCities() {
        return new HashMap<>(cities);
    }

    @Override
    public Map<Long, District> getDistricts() {
        return new HashMap<>(districts);
    }

    @Override
    public Map<Long, Village> getVillages() {
        return new HashMap<>(villages);
    }

    @Override
    public Map<Long, List<City>> getCitiesByProvince() {
        Map<Long, List<City>> result = new HashMap<>();
        citiesByProvince.forEach((key, value) ->
                result.put(key, new ArrayList<>(value)));

        return result;
    }

    @Override
    public Map<Long, List<District>> getDistrictsByCity() {
        Map<Long, List<District>> result = new HashMap<>();
        districtsByCity.forEach((key, value) ->
                result.put(key, new ArrayList<>(value)));

        return result;
    }

    @Override
    public Map<Long, List<Village>> getVillagesByDistrict() {
        return new HashMap<>(villagesByDistrict);
    }

    @Override
    public Province getProvince(Long provinceCode) {
        return provinceCode != null ? provinces.get(provinceCode) : null;
    }

    @Override
    public City getCity(Long cityCode) {
        return cityCode != null ? cities.get(cityCode) : null;
    }

    @Override
    public District getDistrict(Long districtCode) {
        return districtCode != null ? districts.get(districtCode) : null;
    }

    @Override
    public Village getVillage(Long villageCode) {
        return villageCode != null ? villages.get(villageCode) : null;
    }

    @Override
    public List<City> getCitiesByProvinceCode(Long provinceCode) {
        if (provinceCode == null) {
            return new ArrayList<>();
        }

        List<City> cities = citiesByProvince.get(provinceCode);
        return cities != null ? new ArrayList<>(cities) : new ArrayList<>();
    }

    @Override
    public List<District> getDistrictsByCityCode(Long cityCode) {
        if (cityCode == null) {
            return new ArrayList<>();
        }

        List<District> districts = districtsByCity.get(cityCode);
        return districts != null ? new ArrayList<>(districts) : new ArrayList<>();
    }

    @Override
    public List<Village> getVillagesByDistrictCode(Long districtCode) {
        if (districtCode == null) {
            return new ArrayList<>();
        }

        List<Village> villages = villagesByDistrict.get(districtCode);
        return villages != null ? new ArrayList<>(villages) : new ArrayList<>();
    }

    @Override
    public List<Village> getVillagesByProvinceCode(Long provinceCode) {
        if (provinceCode == null) {
            return new ArrayList<>();
        }

        List<Village> villages = villagesByProvince.get(provinceCode);
        return villages != null ? new ArrayList<>(villages) : new ArrayList<>();
    }

    @Override
    public List<Village> getVillagesByCityCode(Long cityCode) {
        if (cityCode == null) {
            return new ArrayList<>();
        }

        List<Village> villages = villagesByCity.get(cityCode);
        return villages != null ? new ArrayList<>(villages) : new ArrayList<>();
    }

    @Override
    public void refresh() {
        clearAll();
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded && !provinces.isEmpty();
    }

    @Override
    public CacheStats getStats() {
        return new CacheStats(
                provinces.size(),
                cities.size(),
                districts.size(),
                villages.size(),
                lastRefreshTime.get()
        );
    }

    private void clearAll() {
        provinces.clear();
        cities.clear();
        districts.clear();
        villages.clear();
        citiesByProvince.clear();
        districtsByCity.clear();
        villagesByDistrict.clear();
        villagesByProvince.clear();
        villagesByCity.clear();
    }

    private void updateRefreshTime() {
        lastRefreshTime.set(System.currentTimeMillis());
        loaded = true;
    }
}
