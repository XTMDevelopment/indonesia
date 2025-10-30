package id.xtramile.indonesia;

import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;

import java.util.List;
import java.util.Optional;

public interface IndonesiaService {

    Optional<Province> findProvince(String provinceId);
    List<Province> getAllProvinces();
    List<Province> searchProvinces(String query);

    Optional<City> findCity(String cityId);
    List<City> getCitiesByProvince(String provinceId);
    List<City> getAllCities();
    List<City> searchCities(String query);

    Optional<District> findDistrict(String districtId);
    List<District> getDistrictsByCity(String cityId);
    List<District> getAllDistricts();
    List<District> searchDistricts(String query);

    Optional<Village> findVillage(String villageId);
    List<Village> getVillagesByDistrict(String districtId);
    List<Village> getAllVillages();
    List<Village> searchVillages(String query);

    List<Village> getVillagesByProvince(String provinceId);
    List<Village> getVillagesByCity(String cityId);

    void refreshData();
    boolean isDataLoaded();
    CacheStats getCacheStats();
}
