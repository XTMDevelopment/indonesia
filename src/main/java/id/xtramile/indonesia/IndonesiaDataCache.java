package id.xtramile.indonesia;

import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;

import java.util.List;
import java.util.Map;

public interface IndonesiaDataCache {

    void putProvinces(Map<String, Province> provinces);
    void putCities(Map<String, City> cities);
    void putDistricts(Map<String, District> districts);
    void putVillages(Map<String, Village> villages);

    Map<String, Province> getProvinces();
    Map<String, City> getCities();
    Map<String, District> getDistricts();
    Map<String, Village> getVillages();

    Map<String, List<City>> getCitiesByProvince();
    Map<String, List<District>> getDistrictsByCity();
    Map<String, List<Village>> getVillagesByDistrict();

    void refresh();
    boolean isLoaded();
    CacheStats getStats();
}
