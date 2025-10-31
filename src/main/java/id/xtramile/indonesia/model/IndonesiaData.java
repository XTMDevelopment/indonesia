package id.xtramile.indonesia.model;

import java.util.Map;

public class IndonesiaData {
    private final Map<Long, Province> provinces;
    private final Map<Long, City> cities;
    private final Map<Long, District> districts;
    private final Map<Long, Village> villages;

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
