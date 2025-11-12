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
     * Loads all provinces from the data source.
     *
     * @return a map of province codes to Province objects
     * @throws DataLoadException if an error occurs while loading the data
     */
    Map<Long, Province> loadProvinces() throws DataLoadException;

    /**
     * Loads all cities from the data source.
     *
     * @return a map of city codes to City objects
     * @throws DataLoadException if an error occurs while loading the data
     */
    Map<Long, City> loadCities() throws DataLoadException;

    /**
     * Loads all districts from the data source.
     *
     * @return a map of district codes to District objects
     * @throws DataLoadException if an error occurs while loading the data
     */
    Map<Long, District> loadDistricts() throws DataLoadException;

    /**
     * Loads all villages from the data source.
     *
     * @return a map of village codes to Village objects
     * @throws DataLoadException if an error occurs while loading the data
     */
    Map<Long, Village> loadVillages() throws DataLoadException;

    /**
     * Loads all administrative data (provinces, cities, districts, and villages) at once.
     *
     * @return an IndonesiaData object containing all loaded data
     * @throws DataLoadException if an error occurs while loading the data
     */
    IndonesiaData loadAllData() throws DataLoadException;
}
