package id.xtramile.indonesia;

import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.*;

import java.util.Map;

public interface IndonesiaDataLoader {

    Map<Integer, Province> loadProvinces() throws DataLoadException;
    Map<Integer, City> loadCities() throws DataLoadException;
    Map<Integer, District> loadDistricts() throws DataLoadException;
    Map<Integer, Village> loadVillages() throws DataLoadException;

    IndonesiaData loadAllData() throws DataLoadException;
}
