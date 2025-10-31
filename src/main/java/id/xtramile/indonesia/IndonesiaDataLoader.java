package id.xtramile.indonesia;

import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.*;

import java.util.Map;

public interface IndonesiaDataLoader {

    Map<Long, Province> loadProvinces() throws DataLoadException;
    Map<Long, City> loadCities() throws DataLoadException;
    Map<Long, District> loadDistricts() throws DataLoadException;
    Map<Long, Village> loadVillages() throws DataLoadException;

    IndonesiaData loadAllData() throws DataLoadException;
}
