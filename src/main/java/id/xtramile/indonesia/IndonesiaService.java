package id.xtramile.indonesia;

import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.model.*;

import java.util.List;
import java.util.Optional;

public interface IndonesiaService {

    Optional<Province> findProvince(Long provinceCode);
    List<Province> getAllProvinces();
    List<Province> searchProvinces(String query);

    Optional<City> findCity(Long cityCode);
    List<City> getCitiesByProvince(Long provinceCode);
    List<City> getAllCities();
    List<City> searchCities(String query);

    Optional<District> findDistrict(Long districtCode);
    List<District> getDistrictsByCity(Long cityCode);
    List<District> getAllDistricts();
    List<District> searchDistricts(String query);

    Optional<Village> findVillage(Long villageCode);
    List<Village> getVillagesByDistrict(Long districtCode);
    List<Village> getAllVillages();
    List<Village> searchVillages(String query);

    List<Village> getVillagesByProvince(Long provinceCode);
    List<Village> getVillagesByCity(Long cityCode);

    Indonesia buildFrom(Province province);
    Indonesia buildFrom(City city);
    Indonesia buildFrom(District district);
    Indonesia buildFrom(Village village);

    void refreshData();
    boolean isDataLoaded();
    CacheStats getCacheStats();
}
