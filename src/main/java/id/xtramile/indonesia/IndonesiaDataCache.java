package id.xtramile.indonesia;

import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;

import java.util.List;
import java.util.Map;

public interface IndonesiaDataCache {

    void putProvinces(Map<Long, Province> provinces);
    void putCities(Map<Long, City> cities);
    void putDistricts(Map<Long, District> districts);
    void putVillages(Map<Long, Village> villages);

    Map<Long, Province> getProvinces();
    Map<Long, City> getCities();
    Map<Long, District> getDistricts();
    Map<Long, Village> getVillages();

    Map<Long, List<City>> getCitiesByProvince();
    Map<Long, List<District>> getDistrictsByCity();
    Map<Long, List<Village>> getVillagesByDistrict();

    void refresh();
    boolean isLoaded();
    CacheStats getStats();
}
