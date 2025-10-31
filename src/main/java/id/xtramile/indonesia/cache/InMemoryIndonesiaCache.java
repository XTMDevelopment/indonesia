package id.xtramile.indonesia.cache;

import id.xtramile.indonesia.IndonesiaDataCache;
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

public class InMemoryIndonesiaCache implements IndonesiaDataCache {

    private final Map<Integer, Province> provinces = new ConcurrentHashMap<>();
    private final Map<Integer, City> cities = new ConcurrentHashMap<>();
    private final Map<Integer, District> districts = new ConcurrentHashMap<>();
    private final Map<Integer, Village> villages = new ConcurrentHashMap<>();

    private final Map<Integer, List<City>> citiesByProvince = new ConcurrentHashMap<>();
    private final Map<Integer, List<District>> districtsByCity = new ConcurrentHashMap<>();
    private final Map<Integer, List<Village>> villagesByDistrict = new ConcurrentHashMap<>();

    private volatile boolean loaded = false;
    private final AtomicLong lastRefreshTime = new AtomicLong(0);

    @Override
    public void putProvinces(Map<Integer, Province> provinces) {
        this.provinces.clear();
        this.provinces.putAll(provinces);
        updateRefreshTime();
    }

    @Override
    public void putCities(Map<Integer, City> cities) {
        this.cities.clear();
        this.cities.putAll(cities);

        this.citiesByProvince.clear();
        cities.values().forEach(city ->
                this.citiesByProvince.computeIfAbsent(city.getProvinceCode(), k -> new ArrayList<>())
                        .add(city));
        updateRefreshTime();
    }

    @Override
    public void putDistricts(Map<Integer, District> districts) {
        this.districts.clear();
        this.districts.putAll(districts);

        this.districtsByCity.clear();
        districts.values().forEach(district ->
                this.districtsByCity.computeIfAbsent(district.getCityCode(), k -> new ArrayList<>())
                        .add(district));
        updateRefreshTime();
    }

    @Override
    public void putVillages(Map<Integer, Village> villages) {
        this.villages.clear();
        this.villages.putAll(villages);

        this.villagesByDistrict.clear();
        villages.values().forEach(village ->
                this.villagesByDistrict.computeIfAbsent(village.getDistrictCode(), k -> new ArrayList<>())
                        .add(village));
        updateRefreshTime();
    }

    @Override
    public Map<Integer, Province> getProvinces() {
        return new HashMap<>(provinces);
    }

    @Override
    public Map<Integer, City> getCities() {
        return new HashMap<>(cities);
    }

    @Override
    public Map<Integer, District> getDistricts() {
        return new HashMap<>(districts);
    }

    @Override
    public Map<Integer, Village> getVillages() {
        return new HashMap<>(villages);
    }

    @Override
    public Map<Integer, List<City>> getCitiesByProvince() {
        return new HashMap<>(citiesByProvince);
    }

    @Override
    public Map<Integer, List<District>> getDistrictsByCity() {
        return new HashMap<>(districtsByCity);
    }

    @Override
    public Map<Integer, List<Village>> getVillagesByDistrict() {
        return new HashMap<>(villagesByDistrict);
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
    }

    private void updateRefreshTime() {
        lastRefreshTime.set(System.currentTimeMillis());
        loaded = true;
    }
}
