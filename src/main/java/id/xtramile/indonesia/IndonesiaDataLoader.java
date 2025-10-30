package id.xtramile.indonesia;

import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.*;

import java.util.Map;

public interface IndonesiaDataLoader {

    Map<String, Province> loadProvinces() throws DataLoadException;
    Map<String, City> loadCities() throws DataLoadException;
    Map<String, District> loadDistricts() throws DataLoadException;
    Map<String, Village> loadVillages() throws DataLoadException;

    IndonesiaData loadAllData() throws DataLoadException;
}
