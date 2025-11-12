package id.xtramile.indonesia.loader;

import com.opencsv.CSVReader;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.constant.Constant;
import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.*;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * CSV-based implementation of IndonesiaDataLoader.
 * <p>
 * Loads Indonesia administrative data from CSV files located in the classpath.
 * The CSV files are expected to be in the /csv directory with specific naming conventions:
 * <ul>
 *   <li>provinces.csv - contains province data</li>
 *   <li>cities.csv - contains city data</li>
 *   <li>districts.csv - contains district data</li>
 *   <li>villages/{provinceCode}.csv - contains village data per province</li>
 * </ul>
 *
 * @author Rigsto
 */
public class CsvIndonesiaDataLoader implements IndonesiaDataLoader {

    @Override
    public Map<Long, Province> loadProvinces() throws DataLoadException {
        Map<Long, Province> provinces = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(Constant.CSV_PATH_PROVINCES))
        ))) {
            String[] line;

            while ((line = reader.readNext()) != null) {
                if (line.length >= Constant.PROVINCE_CSV_COLUMN_COUNT) {
                    try {
                        long code = Long.parseLong(line[0]);
                        String name = line[1];

                        double[] coordinates = parseCoordinatesForProvince(line);
                        if (coordinates == null) {
                            continue;
                        }

                        Province province = new Province(code, name, coordinates[0], coordinates[1]);
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
    public Map<Long, City> loadCities() throws DataLoadException {
        Map<Long, City> cities = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(Constant.CSV_PATH_CITIES))
        ))) {
            String[] line;

            while ((line = reader.readNext()) != null) {
                if (line.length >= Constant.CITY_CSV_COLUMN_COUNT) {
                    try {
                        long code = Long.parseLong(line[0]);
                        long provinceCode = Long.parseLong(line[1]);
                        String name = line[2];

                        double[] coordinates = parseCoordinates(line);
                        if (coordinates == null) {
                            continue;
                        }

                        City city = new City(code, provinceCode, name, coordinates[0], coordinates[1]);
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
    public Map<Long, District> loadDistricts() throws DataLoadException {
        Map<Long, District> districts = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(Constant.CSV_PATH_DISTRICTS))
        ))) {
            String[] line;

            while ((line = reader.readNext()) != null) {
                if (line.length >= Constant.DISTRICT_CSV_COLUMN_COUNT) {
                    try {
                        long code = Long.parseLong(line[0]);
                        long cityCode = Long.parseLong(line[1]);
                        String name = line[2];

                        double[] coordinates = parseCoordinates(line);
                        if (coordinates == null) {
                            continue;
                        }

                        District district = new District(code, cityCode, name, coordinates[0], coordinates[1]);
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
    public Map<Long, Village> loadVillages() throws DataLoadException {
        Map<Long, Village> villages = new HashMap<>();

        try {
            Map<Long, Province> provinces = loadProvinces();

            for (Long provinceCode : provinces.keySet()) {
                String villageCsvPath = Constant.CSV_PATH_VILLAGES_PREFIX + provinceCode + Constant.CSV_EXTENSION;

                try (CSVReader reader = new CSVReader(new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream(villageCsvPath))
                ))) {
                    String[] line;

                    while ((line = reader.readNext()) != null) {
                        if (line.length >= Constant.VILLAGE_CSV_COLUMN_COUNT) {
                            try {
                                long code = Long.parseLong(line[0]);
                                long districtCode = Long.parseLong(line[1]);
                                String name = line[2];

                                double[] coordinates = parseCoordinates(line);
                                if (coordinates == null) {
                                    continue;
                                }

                                Village village = new Village(code, districtCode, name, coordinates[0], coordinates[1]);
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

    /**
     * Parses latitude and longitude coordinates from a CSV line.
     *
     * @param line the CSV line array
     * @return an array containing [latitude, longitude], or null if parsing fails
     */
    private double[] parseCoordinates(String[] line) {
        if (line.length < Constant.CITY_CSV_COLUMN_COUNT) {
            return null;
        }

        if (line[3] == null || line[3].trim().isEmpty() || line[4] == null || line[4].trim().isEmpty()) {
            return null;
        }

        try {
            double latitude = Double.parseDouble(line[3]);
            double longitude = Double.parseDouble(line[4]);

            return new double[]{latitude, longitude};

        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parses latitude and longitude coordinates from a province CSV line.
     * Province CSV format has coordinates at different positions than other entities.
     *
     * @param line the CSV line array
     * @return an array containing [latitude, longitude], or null if parsing fails
     */
    private double[] parseCoordinatesForProvince(String[] line) {
        if (line.length < Constant.PROVINCE_CSV_COLUMN_COUNT) {
            return null;
        }

        if (line[2] == null || line[2].trim().isEmpty() || line[3] == null || line[3].trim().isEmpty()) {
            return null;
        }

        try {
            double latitude = Double.parseDouble(line[2]);
            double longitude = Double.parseDouble(line[3]);

            return new double[]{latitude, longitude};

        } catch (NumberFormatException e) {
            return null;
        }
    }
}
