package id.xtramile.indonesia.loader;

import com.opencsv.CSVReader;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.*;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CsvIndonesiaDataLoader implements IndonesiaDataLoader {

    private static final String PROVINCE_CSV = "/csv/provinces.csv";
    private static final String CITY_CSV = "/csv/cities.csv";
    private static final String DISTRICT_CSV = "/csv/districts.csv";

    @Override
    public Map<String, Province> loadProvinces() throws DataLoadException {
        Map<String, Province> provinces = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(PROVINCE_CSV))
        ))) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 4) {
                    try {
                        int code = Integer.parseInt(line[0]);
                        String name = line[1];
                        double latitude = Double.parseDouble(line[2]);
                        double longitude = Double.parseDouble(line[3]);
                        Province province = new Province(code, name, latitude, longitude);
                        provinces.put(String.valueOf(province.getCode()), province);
                    } catch (NumberFormatException e) {
                        throw new DataLoadException("Failed to parse province data: " + String.join(",", line), e);
                    }
                }
            }

        } catch (Exception e) {
            throw new DataLoadException("Failed to load provinces from CSV", e);
        }

        return provinces;
    }

    @Override
    public Map<String, City> loadCities() throws DataLoadException {
        Map<String, City> cities = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(CITY_CSV))
        ))) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 5) {
                    try {
                        int code = Integer.parseInt(line[0]);
                        int provinceCode = Integer.parseInt(line[1]);
                        String name = line[2];
                        double latitude = Double.parseDouble(line[3]);
                        double longitude = Double.parseDouble(line[4]);
                        City city = new City(code, provinceCode, name, latitude, longitude);
                        cities.put(String.valueOf(city.getCode()), city);
                    } catch (NumberFormatException e) {
                        throw new DataLoadException("Failed to parse city data: " + String.join(",", line), e);
                    }
                }
            }

        } catch (Exception e) {
            throw new DataLoadException("Failed to load cities from CSV", e);
        }

        return cities;
    }

    @Override
    public Map<String, District> loadDistricts() throws DataLoadException {
        Map<String, District> districts = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(DISTRICT_CSV))
        ))) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 5) {
                    try {
                        int code = Integer.parseInt(line[0]);
                        int cityCode = Integer.parseInt(line[1]);
                        String name = line[2];
                        double latitude = Double.parseDouble(line[3]);
                        double longitude = Double.parseDouble(line[4]);
                        District district = new District(code, cityCode, name, latitude, longitude);
                        districts.put(String.valueOf(district.getCode()), district);
                    } catch (NumberFormatException e) {
                        throw new DataLoadException("Failed to parse district data: " + String.join(",", line), e);
                    }
                }
            }

        } catch (Exception e) {
            throw new DataLoadException("Failed to load districts from CSV", e);
        }

        return districts;
    }

    @Override
    public Map<String, Village> loadVillages() throws DataLoadException {
        Map<String, Village> villages = new HashMap<>();
        
        try {
            // Load provinces to get province codes dynamically
            Map<String, Province> provinces = loadProvinces();
            
            // Iterate over province codes to load corresponding village files
            for (String provinceCode : provinces.keySet()) {
                String villageCsvPath = "/csv/villages/" + provinceCode + ".csv";
                
                try (CSVReader reader = new CSVReader(new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream(villageCsvPath))
                ))) {
                    String[] line;
                    while ((line = reader.readNext()) != null) {
                        if (line.length >= 5) {
                            try {
                                int code = Integer.parseInt(line[0]);
                                int districtCode = Integer.parseInt(line[1]);
                                String name = line[2];
                                double latitude = Double.parseDouble(line[3]);
                                double longitude = Double.parseDouble(line[4]);
                                Village village = new Village(code, districtCode, name, latitude, longitude);
                                villages.put(String.valueOf(village.getCode()), village);
                            } catch (NumberFormatException e) {
                                throw new DataLoadException("Failed to parse village data: " + String.join(",", line), e);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new DataLoadException("Failed to load villages from CSV", e);
        }
        
        return villages;
    }

    @Override
    public IndonesiaData loadAllData() throws DataLoadException {
        return new IndonesiaData(
                loadProvinces(),
                loadCities(),
                loadDistricts(),
                loadVillages()
        );
    }
}
