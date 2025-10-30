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
    private static final String DISTRICT_CSV = "/csv/district.csv";

    @Override
    public Map<String, Province> loadProvinces() throws DataLoadException {
        Map<String, Province> provinces = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(PROVINCE_CSV))
        ))) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length >= 2) {
                    Province province = new Province(line[0], line[1]);
                    provinces.put(province.getId(), province);
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
                if (line.length >= 3) {
                    City city = new City(line[0], line[1], line[2]);
                    cities.put(city.getId(), city);
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
                if (line.length >= 4) {
                    District district = new District(line[0], line[1], line[2], line[3]);
                    districts.put(district.getId(), district);
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
