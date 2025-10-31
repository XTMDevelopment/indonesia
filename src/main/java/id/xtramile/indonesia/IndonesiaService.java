package id.xtramile.indonesia;

import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.model.*;

import java.util.List;
import java.util.Optional;

public interface IndonesiaService {

    Optional<Province> findProvince(Integer provinceCode);
    List<Province> getAllProvinces();
    List<Province> searchProvinces(String query);

    Optional<City> findCity(Integer cityCode);
    List<City> getCitiesByProvince(Integer provinceCode);
    List<City> getAllCities();
    List<City> searchCities(String query);

    Optional<District> findDistrict(Integer districtCode);
    List<District> getDistrictsByCity(Integer cityCode);
    List<District> getAllDistricts();
    List<District> searchDistricts(String query);

    Optional<Village> findVillage(Integer villageCode);
    List<Village> getVillagesByDistrict(Integer districtCode);
    List<Village> getAllVillages();
    List<Village> searchVillages(String query);

    List<Village> getVillagesByProvince(Integer provinceCode);
    List<Village> getVillagesByCity(Integer cityCode);

    Indonesia buildFrom(Province province);
    Indonesia buildFrom(City city);
    Indonesia buildFrom(District district);
    Indonesia buildFrom(Village village);

    void refreshData();
    boolean isDataLoaded();
    CacheStats getCacheStats();
}
