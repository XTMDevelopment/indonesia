package id.xtramile.indonesia.loader;

import com.opencsv.CSVReader;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.constant.Constant;
import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.IndonesiaData;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CsvIndonesiaDataLoader implements IndonesiaDataLoader {

    @Override
    public Map<Integer, Province> loadProvinces() throws DataLoadException {
        Map<Integer, Province> provinces = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(Constant.CSV_PATH_PROVINCES))
        ))) {
            String[] line;

            while ((line = reader.readNext()) != null) {
                if (line.length >= Constant.PROVINCE_CSV_COLUMN_COUNT) {

                    try {
                        int code = Integer.parseInt(line[0]);
                        String name = line[1];
                        double latitude = Double.parseDouble(line[2]);
                        double longitude = Double.parseDouble(line[3]);

                        Province province = new Province(code, name, latitude, longitude);
                        provinces.put(province.getCode(), province);

                    } catch (NumberFormatException e) {
                        throw new DataLoadException(Constant.ERROR_FAILED_TO_PARSE_PROVINCE + String.join(Constant.CSV_DELIMITER, line), e);
                    }
                }
            }

        } catch (DataLoadException e) {
            throw e;

        } catch (Exception e) {
            throw new DataLoadException(Constant.ERROR_FAILED_TO_LOAD_PROVINCES, e);
        }

        return provinces;
    }

    @Override
    public Map<Integer, City> loadCities() throws DataLoadException {
        Map<Integer, City> cities = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(Constant.CSV_PATH_CITIES))
        ))) {
            String[] line;

            while ((line = reader.readNext()) != null) {
                if (line.length >= Constant.CITY_CSV_COLUMN_COUNT) {

                    try {
                        int code = Integer.parseInt(line[0]);
                        int provinceCode = Integer.parseInt(line[1]);
                        String name = line[2];
                        double latitude = Double.parseDouble(line[3]);
                        double longitude = Double.parseDouble(line[4]);

                        City city = new City(code, provinceCode, name, latitude, longitude);
                        cities.put(city.getCode(), city);

                    } catch (NumberFormatException e) {
                        throw new DataLoadException(Constant.ERROR_FAILED_TO_PARSE_CITY + String.join(Constant.CSV_DELIMITER, line), e);
                    }
                }
            }

        } catch (DataLoadException e) {
            throw e;

        } catch (Exception e) {
            throw new DataLoadException(Constant.ERROR_FAILED_TO_LOAD_CITIES, e);
        }

        return cities;
    }

    @Override
    public Map<Integer, District> loadDistricts() throws DataLoadException {
        Map<Integer, District> districts = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(Constant.CSV_PATH_DISTRICTS))
        ))) {
            String[] line;

            while ((line = reader.readNext()) != null) {
                if (line.length >= Constant.DISTRICT_CSV_COLUMN_COUNT) {

                    try {
                        int code = Integer.parseInt(line[0]);
                        int cityCode = Integer.parseInt(line[1]);
                        String name = line[2];
                        double latitude = Double.parseDouble(line[3]);
                        double longitude = Double.parseDouble(line[4]);

                        District district = new District(code, cityCode, name, latitude, longitude);
                        districts.put(district.getCode(), district);

                    } catch (NumberFormatException e) {
                        throw new DataLoadException(Constant.ERROR_FAILED_TO_PARSE_DISTRICT + String.join(Constant.CSV_DELIMITER, line), e);
                    }
                }
            }

        } catch (DataLoadException e) {
            throw e;

        } catch (Exception e) {
            throw new DataLoadException(Constant.ERROR_FAILED_TO_LOAD_DISTRICTS, e);
        }

        return districts;
    }

    @Override
    public Map<Integer, Village> loadVillages() throws DataLoadException {
        Map<Integer, Village> villages = new HashMap<>();

        try {
            Map<Integer, Province> provinces = loadProvinces();

            for (Integer provinceCode : provinces.keySet()) {
                String villageCsvPath = Constant.CSV_PATH_VILLAGES_PREFIX + provinceCode + Constant.CSV_EXTENSION;

                try (CSVReader reader = new CSVReader(new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream(villageCsvPath))
                ))) {
                    String[] line;

                    while ((line = reader.readNext()) != null) {
                        if (line.length >= Constant.VILLAGE_CSV_COLUMN_COUNT) {

                            try {
                                int code = Integer.parseInt(line[0]);
                                int districtCode = Integer.parseInt(line[1]);
                                String name = line[2];
                                double latitude = Double.parseDouble(line[3]);
                                double longitude = Double.parseDouble(line[4]);

                                Village village = new Village(code, districtCode, name, latitude, longitude);
                                villages.put(village.getCode(), village);

                            } catch (NumberFormatException e) {
                                throw new DataLoadException(Constant.ERROR_FAILED_TO_PARSE_VILLAGE + String.join(Constant.CSV_DELIMITER, line), e);
                            }
                        }
                    }
                }
            }
        } catch (DataLoadException e) {
            throw e;

        } catch (Exception e) {
            throw new DataLoadException(Constant.ERROR_FAILED_TO_LOAD_VILLAGES, e);
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
