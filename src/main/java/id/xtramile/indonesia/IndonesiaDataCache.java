package id.xtramile.indonesia;

import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;

import java.util.List;
import java.util.Map;

public interface IndonesiaDataCache {

    void putProvinces(Map<Integer, Province> provinces);
    void putCities(Map<Integer, City> cities);
    void putDistricts(Map<Integer, District> districts);
    void putVillages(Map<Integer, Village> villages);

    Map<Integer, Province> getProvinces();
    Map<Integer, City> getCities();
    Map<Integer, District> getDistricts();
    Map<Integer, Village> getVillages();

    Map<Integer, List<City>> getCitiesByProvince();
    Map<Integer, List<District>> getDistrictsByCity();
    Map<Integer, List<Village>> getVillagesByDistrict();

    void refresh();
    boolean isLoaded();
    CacheStats getStats();
}
