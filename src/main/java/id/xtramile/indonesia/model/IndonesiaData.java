package id.xtramile.indonesia.model;

import java.util.Map;

public class IndonesiaData {
    private final Map<Integer, Province> provinces;
    private final Map<Integer, City> cities;
    private final Map<Integer, District> districts;
    private final Map<Integer, Village> villages;

    public IndonesiaData(Map<Integer, Province> provinces, Map<Integer, City> cities, Map<Integer, District> districts, Map<Integer, Village> villages) {
        this.provinces = provinces;
        this.cities = cities;
        this.districts = districts;
        this.villages = villages;
    }

    public Map<Integer, Province> getProvinces() {
        return provinces;
    }

    public Map<Integer, City> getCities() {
        return cities;
    }

    public Map<Integer, District> getDistricts() {
        return districts;
    }

    public Map<Integer, Village> getVillages() {
        return villages;
    }
}
