package id.xtramile.indonesia;

import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.*;

import java.util.Map;

/**
 * Interface for loading Indonesia administrative data from various sources.
 * <p>
 * Implementations of this interface are responsible for loading provinces, cities,
 * districts, and villages from data sources such as CSV files, databases, or APIs.
 *
 * @author Rigsto
 */
public interface IndonesiaDataLoader {

    /**
     * @return map of province codes to Province objects
     * @throws DataLoadException if an error occurs while loading
     */
    Map<Long, Province> loadProvinces() throws DataLoadException;

    /**
     * @return map of city codes to City objects
     * @throws DataLoadException if an error occurs while loading
     */
    Map<Long, City> loadCities() throws DataLoadException;

    /**
     * @return map of district codes to District objects
     * @throws DataLoadException if an error occurs while loading
     */
    Map<Long, District> loadDistricts() throws DataLoadException;

    /**
     * @return map of village codes to Village objects
     * @throws DataLoadException if an error occurs while loading
     */
    Map<Long, Village> loadVillages() throws DataLoadException;

    /**
     * @return IndonesiaData containing all loaded data
     * @throws DataLoadException if an error occurs while loading
     */
    IndonesiaData loadAllData() throws DataLoadException;
}
