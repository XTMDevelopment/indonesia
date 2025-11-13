package id.xtramile.indonesia.util;

import id.xtramile.indonesia.model.*;

/**
 * Utility class for calculating distances between Indonesia administrative locations.
 * <p>
 * This class provides methods to calculate distances between administrative entities
 * (provinces, cities, districts, villages) using their geographic coordinates.
 * <p>
 * All distance calculations use the Haversine formula to compute the great-circle
 * distance between two points on Earth, which accounts for the Earth's spherical shape.
 * <p>
 * The Earth's radius used in calculations is approximately 6,371 kilometers.
 *
 * @author Rigsto
 * @since 1.1
 */
public final class DistanceCalculator {

    /**
     * Earth's radius in kilometers (mean radius).
     */
    private static final double EARTH_RADIUS_KM = 6371.0;

    private DistanceCalculator() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Calculates the distance between two provinces in kilometers.
     *
     * @param province1 the first province
     * @param province2 the second province
     * @return the distance in kilometers, or -1 if either province is null
     */
    public static double distanceBetweenProvinces(Province province1, Province province2) {
        if (province1 == null || province2 == null) {
            return -1.0;
        }

        return haversineDistance(
                province1.getLatitude(), province1.getLongitude(),
                province2.getLatitude(), province2.getLongitude()
        );
    }

    /**
     * Calculates the distance between two cities in kilometers.
     *
     * @param city1 the first city
     * @param city2 the second city
     * @return the distance in kilometers, or -1 if either city is null
     */
    public static double distanceBetweenCities(City city1, City city2) {
        if (city1 == null || city2 == null) {
            return -1.0;
        }

        return haversineDistance(
                city1.getLatitude(), city1.getLongitude(),
                city2.getLatitude(), city2.getLongitude()
        );
    }

    /**
     * Calculates the distance between two districts in kilometers.
     *
     * @param district1 the first district
     * @param district2 the second district
     * @return the distance in kilometers, or -1 if either district is null
     */
    public static double distanceBetweenDistricts(District district1, District district2) {
        if (district1 == null || district2 == null) {
            return -1.0;
        }

        return haversineDistance(
                district1.getLatitude(), district1.getLongitude(),
                district2.getLatitude(), district2.getLongitude()
        );
    }

    /**
     * Calculates the distance between two villages in kilometers.
     *
     * @param village1 the first village
     * @param village2 the second village
     * @return the distance in kilometers, or -1 if either village is null
     */
    public static double distanceBetweenVillages(Village village1, Village village2) {
        if (village1 == null || village2 == null) {
            return -1.0;
        }

        return haversineDistance(
                village1.getLatitude(), village1.getLongitude(),
                village2.getLatitude(), village2.getLongitude()
        );
    }

    /**
     * Calculates the distance between two geographic coordinates in kilometers.
     *
     * @param lat1 latitude of the first point
     * @param lon1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param lon2 longitude of the second point
     * @return the distance in kilometers
     */
    public static double distanceBetweenCoordinates(double lat1, double lon1, double lat2, double lon2) {
        return haversineDistance(lat1, lon1, lat2, lon2);
    }

    /**
     * Calculates the distance between two geographic coordinates in meters.
     *
     * @param lat1 latitude of the first point
     * @param lon1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param lon2 longitude of the second point
     * @return the distance in meters
     */
    public static double distanceBetweenCoordinatesInMeters(double lat1, double lon1, double lat2, double lon2) {
        return haversineDistance(lat1, lon1, lat2, lon2) * 1000.0;
    }

    /**
     * Finds the nearest village to a given location from a list of villages.
     *
     * @param latitude  the latitude of the target location
     * @param longitude the longitude of the target location
     * @param villages  the list of villages to search
     * @return the nearest village, or null if the list is empty or null
     */
    public static Village findNearestVillage(double latitude, double longitude, java.util.List<Village> villages) {
        if (villages == null || villages.isEmpty()) {
            return null;
        }

        Village nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Village village : villages) {
            if (village == null) {
                continue;
            }

            double distance = haversineDistance(
                    latitude, longitude,
                    village.getLatitude(), village.getLongitude()
            );

            if (distance < minDistance) {
                minDistance = distance;
                nearest = village;
            }
        }

        return nearest;
    }

    /**
     * Finds the nearest district to a given location from a list of districts.
     *
     * @param latitude  the latitude of the target location
     * @param longitude the longitude of the target location
     * @param districts the list of districts to search
     * @return the nearest district, or null if the list is empty or null
     */
    public static District findNearestDistrict(double latitude, double longitude, java.util.List<District> districts) {
        if (districts == null || districts.isEmpty()) {
            return null;
        }

        District nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (District district : districts) {
            if (district == null) {
                continue;
            }

            double distance = haversineDistance(
                    latitude, longitude,
                    district.getLatitude(), district.getLongitude()
            );
            
            if (distance < minDistance) {
                minDistance = distance;
                nearest = district;
            }
        }

        return nearest;
    }

    /**
     * Finds the nearest city to a given location from a list of cities.
     *
     * @param latitude  the latitude of the target location
     * @param longitude the longitude of the target location
     * @param cities    the list of cities to search
     * @return the nearest city, or null if the list is empty or null
     */
    public static City findNearestCity(double latitude, double longitude, java.util.List<City> cities) {
        if (cities == null || cities.isEmpty()) {
            return null;
        }

        City nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (City city : cities) {
            if (city == null) {
                continue;
            }

            double distance = haversineDistance(
                    latitude, longitude,
                    city.getLatitude(), city.getLongitude()
            );
            
            if (distance < minDistance) {
                minDistance = distance;
                nearest = city;
            }
        }

        return nearest;
    }

    /**
     * Calculates the great-circle distance between two points on Earth using the Haversine formula.
     * <p>
     * The Haversine formula determines the great-circle distance between two points
     * on a sphere given their longitudes and latitudes.
     *
     * @param lat1 latitude of the first point in degrees
     * @param lon1 longitude of the first point in degrees
     * @param lat2 latitude of the second point in degrees
     * @param lon2 longitude of the second point in degrees
     * @return the distance in kilometers
     */
    private static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLatRad = Math.toRadians(lat2 - lat1);
        double deltaLonRad = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}

