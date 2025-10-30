package id.xtramile.indonesia.model;

import java.util.Map;

public class IndonesiaData {
    private final Map<String, Province> provinces;
    private final Map<String, City> cities;
    private final Map<String, District> districts;
    private final Map<String, Village> villages;

    public IndonesiaData(Map<String, Province> provinces, Map<String, City> cities, Map<String, District> districts, Map<String, Village> villages) {
        this.provinces = provinces;
        this.cities = cities;
        this.districts = districts;
        this.villages = villages;
    }

    public Map<String, Province> getProvinces() {
        return provinces;
    }

    public Map<String, City> getCities() {
        return cities;
    }

    public Map<String, District> getDistricts() {
        return districts;
    }

    public Map<String, Village> getVillages() {
        return villages;
    }
}
