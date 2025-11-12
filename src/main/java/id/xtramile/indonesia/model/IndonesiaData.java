package id.xtramile.indonesia.model;

import java.util.Map;

/**
 * Container class for all Indonesia administrative data.
 * <p>
 * This class holds all loaded administrative data including provinces, cities,
 * districts, and villages in a single object for convenient data transfer.
 *
 * @author Rigsto
 */
public class IndonesiaData {
    /**
     * Map of province codes to Province objects.
     */
    private final Map<Long, Province> provinces;
    /**
     * Map of city codes to City objects.
     */
    private final Map<Long, City> cities;
    /**
     * Map of district codes to District objects.
     */
    private final Map<Long, District> districts;
    /**
     * Map of village codes to Village objects.
     */
    private final Map<Long, Village> villages;

    /**
     * @param provinces map of province codes to Province objects
     * @param cities    map of city codes to City objects
     * @param districts map of district codes to District objects
     * @param villages  map of village codes to Village objects
     */
    public IndonesiaData(Map<Long, Province> provinces, Map<Long, City> cities, Map<Long, District> districts, Map<Long, Village> villages) {
        this.provinces = provinces;
        this.cities = cities;
        this.districts = districts;
        this.villages = villages;
    }

    public Map<Long, Province> getProvinces() {
        return provinces;
    }

    public Map<Long, City> getCities() {
        return cities;
    }

    public Map<Long, District> getDistricts() {
        return districts;
    }

    public Map<Long, Village> getVillages() {
        return villages;
    }
}
