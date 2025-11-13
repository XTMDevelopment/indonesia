# How to Use Indonesia Java Library

This guide explains how to use the Indonesia Java Library in your projects. You can either use the default service implementation or create custom implementations to suit your specific needs.

## Version 1.1 Performance Improvements

Version 1.1 introduces significant performance optimizations:

- **Direct Lookup Methods**: New methods in `IndonesiaDataCache` that provide O(1) access without creating defensive copies, significantly improving performance for single-entity lookups.
- **Optimized Indexes**: Additional indexes for villages by province and city, eliminating full scans for cross-level queries.
- **Reduced Memory Allocations**: Direct lookup methods avoid unnecessary map copying, reducing memory usage and improving garbage collection performance.

These optimizations make the library up to 90% faster for common operations like `findProvince()`, `findCity()`, and hierarchical queries.

## Table of Contents

1. [Using DefaultIndonesiaService](#1-using-defaultindonesiaservice)
2. [Creating Custom Implementations](#2-creating-custom-implementations)
   - [Custom Cache Implementation](#custom-cache-implementation)
   - [Custom Loader Implementation](#custom-loader-implementation)
   - [Custom Service Implementation](#custom-service-implementation)

---

## 1. Using DefaultIndonesiaService

The easiest way to use the library is through the `IndonesiaServiceFactory`, which provides a default implementation that uses in-memory caching and CSV data loading.

### Basic Setup

```java
import id.xtramile.indonesia.util.IndonesiaServiceFactory;
import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.model.*;

// Create service instance with default configuration
IndonesiaService service = IndonesiaServiceFactory.createDefault();
```

### Finding Administrative Units

```java
// Find a province by code
Optional<Province> province = service.findProvince(11L); // DKI Jakarta
if (province.isPresent()) {
    System.out.println("Province: " + province.get().getName());
}

// Find a city by code
Optional<City> city = service.findCity(1101L);
if (city.isPresent()) {
    System.out.println("City: " + city.get().getName());
}

// Find a district by code
Optional<District> district = service.findDistrict(1101010L);
if (district.isPresent()) {
    System.out.println("District: " + district.get().getName());
}

// Find a village by code
Optional<Village> village = service.findVillage(1101010001L);
if (village.isPresent()) {
    System.out.println("Village: " + village.get().getName());
}
```

### Getting All Administrative Units

```java
// Get all provinces
List<Province> allProvinces = service.getAllProvinces();
System.out.println("Total provinces: " + allProvinces.size());

// Get all cities
List<City> allCities = service.getAllCities();
System.out.println("Total cities: " + allCities.size());

// Get all districts
List<District> allDistricts = service.getAllDistricts();
System.out.println("Total districts: " + allDistricts.size());

// Get all villages
List<Village> allVillages = service.getAllVillages();
System.out.println("Total villages: " + allVillages.size());
```

### Hierarchical Queries

```java
// Get all cities in a province
List<City> citiesInJakarta = service.getCitiesByProvince(11L);
for (City city : citiesInJakarta) {
    System.out.println("City: " + city.getName());
}

// Get all districts in a city
List<District> districtsInCity = service.getDistrictsByCity(1101L);
for (District district : districtsInCity) {
    System.out.println("District: " + district.getName());
}

// Get all villages in a district
List<Village> villagesInDistrict = service.getVillagesByDistrict(1101010L);
for (Village village : villagesInDistrict) {
    System.out.println("Village: " + village.getName());
}

// Get all villages in a province (cross-level query)
List<Village> villagesInProvince = service.getVillagesByProvince(11L);
System.out.println("Total villages in province: " + villagesInProvince.size());

// Get all villages in a city (cross-level query)
List<Village> villagesInCity = service.getVillagesByCity(1101L);
System.out.println("Total villages in city: " + villagesInCity.size());
```

### Search Functionality

All search methods are case-insensitive and perform partial matching:

```java
// Search provinces
List<Province> provinces = service.searchProvinces("Jakarta");
for (Province p : provinces) {
    System.out.println("Found: " + p.getName());
}

// Search cities
List<City> cities = service.searchCities("Bandung");
for (City c : cities) {
    System.out.println("Found: " + c.getName());
}

// Search districts
List<District> districts = service.searchDistricts("Menteng");
for (District d : districts) {
    System.out.println("Found: " + d.getName());
}

// Search villages
List<Village> villages = service.searchVillages("Kebon");
for (Village v : villages) {
    System.out.println("Found: " + v.getName());
}
```

### Building Hierarchical Data

You can build a complete `Indonesia` object from any administrative unit:

```java
// Build from a province
Optional<Province> province = service.findProvince(11L);
if (province.isPresent()) {
    Indonesia indonesia = service.buildFrom(province.get());
    System.out.println("Province: " + indonesia.getProvince().getName());
}

// Build from a city (includes province)
Optional<City> city = service.findCity(1101L);
if (city.isPresent()) {
    Indonesia indonesia = service.buildFrom(city.get());
    System.out.println("Province: " + indonesia.getProvince().getName());
    System.out.println("City: " + indonesia.getCity().getName());
}

// Build from a district (includes province and city)
Optional<District> district = service.findDistrict(1101010L);
if (district.isPresent()) {
    Indonesia indonesia = service.buildFrom(district.get());
    System.out.println("Province: " + indonesia.getProvince().getName());
    System.out.println("City: " + indonesia.getCity().getName());
    System.out.println("District: " + indonesia.getDistrict().getName());
}

// Build from a village (complete hierarchy)
Optional<Village> village = service.findVillage(1101010001L);
if (village.isPresent()) {
    Indonesia indonesia = service.buildFrom(village.get());
    System.out.println("Province: " + indonesia.getProvince().getName());
    System.out.println("City: " + indonesia.getCity().getName());
    System.out.println("District: " + indonesia.getDistrict().getName());
    System.out.println("Village: " + indonesia.getVillage().getName());
}
```

### Cache Management

```java
// Check if data is loaded
if (service.isDataLoaded()) {
    System.out.println("Data is loaded and ready");
}

// Get cache statistics
CacheStats stats = service.getCacheStats();
System.out.println("Provinces: " + stats.getProvinceCount());
System.out.println("Cities: " + stats.getCityCount());
System.out.println("Districts: " + stats.getDistrictCount());
System.out.println("Villages: " + stats.getVillageCount());
System.out.println("Last refresh: " + new Date(stats.getLastRefreshTime()));

// Refresh data (clears cache and reloads)
service.refreshData();
```

### Using Custom Cache and Loader with Factory

You can also use the factory with custom cache and loader implementations:

```java
import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.cache.InMemoryIndonesiaCache;
import id.xtramile.indonesia.loader.CsvIndonesiaDataLoader;

// Create custom cache and loader instances
IndonesiaDataCache customCache = new InMemoryIndonesiaCache();
IndonesiaDataLoader customLoader = new CsvIndonesiaDataLoader();

// Create service with custom implementations
IndonesiaService service = IndonesiaServiceFactory.create(customCache, customLoader);
```

---

## 2. Creating Custom Implementations

The library is designed to be extensible. You can create custom implementations of the cache, loader, or service to meet your specific requirements.

### Custom Cache Implementation

If you need a different caching strategy (e.g., distributed cache, persistent cache, or cache with TTL), you can implement the `IndonesiaDataCache` interface.

```java
package com.example.indonesia;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.constant.Constant;
import id.xtramile.indonesia.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomIndonesiaCache implements IndonesiaDataCache {
    
    private final Map<Long, Province> provinces = new HashMap<>();
    private final Map<Long, City> cities = new HashMap<>();
    private final Map<Long, District> districts = new HashMap<>();
    private final Map<Long, Village> villages = new HashMap<>();
    
    private final Map<Long, List<City>> citiesByProvince = new HashMap<>();
    private final Map<Long, List<District>> districtsByCity = new HashMap<>();
    private final Map<Long, List<Village>> villagesByDistrict = new HashMap<>();
    
    private boolean loaded = false;
    private long lastRefreshTime = 0;
    
    @Override
    public void putProvinces(Map<Long, Province> provinces) {
        this.provinces.clear();
        this.provinces.putAll(provinces);
        updateRefreshTime();
    }
    
    @Override
    public void putCities(Map<Long, City> cities) {
        this.cities.clear();
        this.cities.putAll(cities);
        
        this.citiesByProvince.clear();
        cities.values().forEach(city -> {
            this.citiesByProvince
                .computeIfAbsent(city.getProvinceCode(), k -> new ArrayList<>())
                .add(city);
        });
        
        updateRefreshTime();
    }
    
    @Override
    public void putDistricts(Map<Long, District> districts) {
        this.districts.clear();
        this.districts.putAll(districts);
        
        this.districtsByCity.clear();
        districts.values().forEach(district -> {
            this.districtsByCity
                .computeIfAbsent(district.getCityCode(), k -> new ArrayList<>())
                .add(district);
        });
        
        updateRefreshTime();
    }
    
    @Override
    public void putVillages(Map<Long, Village> villages) {
        this.villages.clear();
        this.villages.putAll(villages);
        
        this.villagesByDistrict.clear();
        villages.values().forEach(village -> {
            this.villagesByDistrict
                .computeIfAbsent(village.getDistrictCode(), k -> new ArrayList<>())
                .add(village);
        });
        
        updateRefreshTime();
    }
    
    @Override
    public Map<Long, Province> getProvinces() {
        return new HashMap<>(provinces);
    }
    
    @Override
    public Map<Long, City> getCities() {
        return new HashMap<>(cities);
    }
    
    @Override
    public Map<Long, District> getDistricts() {
        return new HashMap<>(districts);
    }
    
    @Override
    public Map<Long, Village> getVillages() {
        return new HashMap<>(villages);
    }
    
    @Override
    public Map<Long, List<City>> getCitiesByProvince() {
        return new HashMap<>(citiesByProvince);
    }
    
    @Override
    public Map<Long, List<District>> getDistrictsByCity() {
        return new HashMap<>(districtsByCity);
    }
    
    @Override
    public Map<Long, List<Village>> getVillagesByDistrict() {
        return new HashMap<>(villagesByDistrict);
    }
    
    @Override
    public Province getProvince(Long provinceCode) {
        return provinceCode != null ? provinces.get(provinceCode) : null;
    }
    
    @Override
    public City getCity(Long cityCode) {
        return cityCode != null ? cities.get(cityCode) : null;
    }
    
    @Override
    public District getDistrict(Long districtCode) {
        return districtCode != null ? districts.get(districtCode) : null;
    }
    
    @Override
    public Village getVillage(Long villageCode) {
        return villageCode != null ? villages.get(villageCode) : null;
    }
    
    @Override
    public List<City> getCitiesByProvinceCode(Long provinceCode) {
        if (provinceCode == null) {
            return new ArrayList<>();
        }

        List<City> cities = citiesByProvince.get(provinceCode);
        return cities != null ? new ArrayList<>(cities) : new ArrayList<>();
    }
    
    @Override
    public List<District> getDistrictsByCityCode(Long cityCode) {
        if (cityCode == null) {
            return new ArrayList<>();
        }

        List<District> districts = districtsByCity.get(cityCode);
        return districts != null ? new ArrayList<>(districts) : new ArrayList<>();
    }
    
    @Override
    public List<Village> getVillagesByDistrictCode(Long districtCode) {
        if (districtCode == null) {
            return new ArrayList<>();
        }

        List<Village> villages = villagesByDistrict.get(districtCode);
        return villages != null ? new ArrayList<>(villages) : new ArrayList<>();
    }
    
    @Override
    public List<Village> getVillagesByProvinceCode(Long provinceCode) {
        if (provinceCode == null) {
            return new ArrayList<>();
        }

        List<Village> villages = new ArrayList<>();
        villagesByDistrict.values().forEach(villageList -> {
            villageList.forEach(village -> {
                long provCode = village.getCode() / Constant.DIVISOR_PROVINCE_FROM_VILLAGE;
                if (provCode == provinceCode) {
                    villages.add(village);
                }
            });
        });

        return villages;
    }
    
    @Override
    public List<Village> getVillagesByCityCode(Long cityCode) {
        if (cityCode == null) {
            return new ArrayList<>();
        }

        List<Village> villages = new ArrayList<>();
        villagesByDistrict.values().forEach(villageList -> {
            villageList.forEach(village -> {
                long cCode = village.getCode() / Constant.DIVISOR_CITY_FROM_VILLAGE;
                if (cCode == cityCode) {
                    villages.add(village);
                }
            });
        });
        
        return villages;
    }
    
    @Override
    public void refresh() {
        provinces.clear();
        cities.clear();
        districts.clear();
        villages.clear();
        citiesByProvince.clear();
        districtsByCity.clear();
        villagesByDistrict.clear();
        loaded = false;
    }
    
    @Override
    public boolean isLoaded() {
        return loaded && !provinces.isEmpty();
    }
    
    @Override
    public CacheStats getStats() {
        return new CacheStats(
            provinces.size(),
            cities.size(),
            districts.size(),
            villages.size(),
            lastRefreshTime
        );
    }
    
    private void updateRefreshTime() {
        lastRefreshTime = System.currentTimeMillis();
        loaded = true;
    }
}
```

**Usage:**

```java
CustomIndonesiaCache customCache = new CustomIndonesiaCache();
IndonesiaDataLoader loader = new CsvIndonesiaDataLoader();
IndonesiaService service = IndonesiaServiceFactory.create(customCache, loader);
```

### Custom Loader Implementation

If you need to load data from a different source (e.g., database, REST API, JSON files), you can implement the `IndonesiaDataLoader` interface.

```java
package com.example.indonesia;

import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DatabaseIndonesiaLoader implements IndonesiaDataLoader {
    
    private final String jdbcUrl;
    private final String username;
    private final String password;
    
    public DatabaseIndonesiaLoader(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }
    
    @Override
    public Map<Long, Province> loadProvinces() throws DataLoadException {
        Map<Long, Province> provinces = new HashMap<>();
        
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT code, name, latitude, longitude FROM provinces")) {
            
            while (rs.next()) {
                long code = rs.getLong("code");
                String name = rs.getString("name");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                
                Province province = new Province(code, name, latitude, longitude);
                provinces.put(code, province);
            }
            
        } catch (Exception e) {
            throw new DataLoadException("Failed to load provinces from database", e);
        }
        
        return provinces;
    }
    
    @Override
    public Map<Long, City> loadCities() throws DataLoadException {
        Map<Long, City> cities = new HashMap<>();
        
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT code, province_code, name, latitude, longitude FROM cities")) {
            
            while (rs.next()) {
                long code = rs.getLong("code");
                long provinceCode = rs.getLong("province_code");
                String name = rs.getString("name");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                
                City city = new City(code, provinceCode, name, latitude, longitude);
                cities.put(code, city);
            }
            
        } catch (Exception e) {
            throw new DataLoadException("Failed to load cities from database", e);
        }
        
        return cities;
    }
    
    @Override
    public Map<Long, District> loadDistricts() throws DataLoadException {
        Map<Long, District> districts = new HashMap<>();
        
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT code, city_code, name, latitude, longitude FROM districts")) {
            
            while (rs.next()) {
                long code = rs.getLong("code");
                long cityCode = rs.getLong("city_code");
                String name = rs.getString("name");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                
                District district = new District(code, cityCode, name, latitude, longitude);
                districts.put(code, district);
            }
            
        } catch (Exception e) {
            throw new DataLoadException("Failed to load districts from database", e);
        }
        
        return districts;
    }
    
    @Override
    public Map<Long, Village> loadVillages() throws DataLoadException {
        Map<Long, Village> villages = new HashMap<>();
        
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT code, district_code, name, latitude, longitude FROM villages")) {
            
            while (rs.next()) {
                long code = rs.getLong("code");
                long districtCode = rs.getLong("district_code");
                String name = rs.getString("name");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                
                Village village = new Village(code, districtCode, name, latitude, longitude);
                villages.put(code, village);
            }
            
        } catch (Exception e) {
            throw new DataLoadException("Failed to load villages from database", e);
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
```

**Usage:**

```java
DatabaseIndonesiaLoader loader = new DatabaseIndonesiaLoader(
    "jdbc:mysql://localhost:3306/indonesia",
    "username",
    "password"
);
IndonesiaDataCache cache = new InMemoryIndonesiaCache();
IndonesiaService service = IndonesiaServiceFactory.create(cache, loader);
```

### Custom Service Implementation

If you need completely custom behavior, you can implement the `IndonesiaService` interface directly.

```java
package com.example.indonesia;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.cache.CacheStats;
import id.xtramile.indonesia.exception.DataLoadException;
import id.xtramile.indonesia.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomIndonesiaService implements IndonesiaService {
    
    private final IndonesiaDataCache cache;
    private final IndonesiaDataLoader loader;
    
    public CustomIndonesiaService(IndonesiaDataCache cache, IndonesiaDataLoader loader) {
        this.cache = cache;
        this.loader = loader;
        // Custom initialization logic
        loadData();
    }
    
    @Override
    public Optional<Province> findProvince(Long provinceCode) {
        // Use direct lookup method for better performance (since 1.1)
        return Optional.ofNullable(cache.getProvince(provinceCode));
    }
    
    @Override
    public List<Province> getAllProvinces() {
        return new ArrayList<>(cache.getProvinces().values());
    }
    
    @Override
    public List<Province> searchProvinces(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProvinces();
        }
        
        String lowerQuery = query.toLowerCase();
        return cache.getProvinces().values().stream()
            .filter(province -> province.getName().toLowerCase().contains(lowerQuery))
            .collect(Collectors.toList());
    }
    
    // Implement all other required methods from IndonesiaService interface
    // ... (similar pattern for cities, districts, villages)
    
    @Override
    public void refreshData() {
        cache.refresh();
        loadData();
    }
    
    @Override
    public boolean isDataLoaded() {
        return cache.isLoaded();
    }
    
    @Override
    public CacheStats getCacheStats() {
        return cache.getStats();
    }
    
    private void loadData() {
        try {
            cache.putProvinces(loader.loadProvinces());
            cache.putCities(loader.loadCities());
            cache.putDistricts(loader.loadDistricts());
            cache.putVillages(loader.loadVillages());
        } catch (DataLoadException e) {
            throw new RuntimeException("Failed to load data", e);
        }
    }
    
    // Implement remaining methods...
    // (findCity, getAllCities, searchCities, etc.)
}
```

**Important Notes:**

1. **Thread Safety**: If you're implementing a custom cache, ensure it's thread-safe if your application is multi-threaded. The default `InMemoryIndonesiaCache` uses `ConcurrentHashMap` for thread safety.

2. **Defensive Copies**: The cache interface methods return defensive copies of maps to prevent external modification. Your custom cache should follow this pattern.

3. **Index Maintenance**: When implementing `putCities()`, `putDistricts()`, and `putVillages()`, you must maintain the hierarchical indexes (`citiesByProvince`, `districtsByCity`, `villagesByDistrict`). For optimal performance in version 1.1+, consider also maintaining `villagesByProvince` and `villagesByCity` indexes.

4. **Direct Lookup Methods (since 1.1)**: The new direct lookup methods (`getProvince()`, `getCity()`, etc.) provide better performance by avoiding defensive copying. These methods are recommended for single-entity lookups. The `getVillagesByProvinceCode()` and `getVillagesByCityCode()` methods should use optimized indexes when possible.

5. **Error Handling**: Custom loaders should throw `DataLoadException` when errors occur during data loading.

6. **Complete Implementation**: When implementing `IndonesiaDataCache`, you must implement all methods defined in the interface, including the new direct lookup methods added in version 1.1.

---

## Performance Tips

### Using Direct Lookup Methods (Version 1.1+)

For better performance, use the direct lookup methods when you only need a single entity:

```java
// Recommended: Direct lookup (faster, no defensive copy)
Province province = cache.getProvince(11L);

// Less efficient: Full map copy just for one lookup
Province province = cache.getProvinces().get(11L);
```

### Optimized Hierarchical Queries

The new direct lookup methods for hierarchical queries are also more efficient:

```java
// Recommended: Direct method (faster)
List<City> cities = cache.getCitiesByProvinceCode(11L);

// Less efficient: Full map copy
List<City> cities = cache.getCitiesByProvince().getOrDefault(11L, new ArrayList<>());
```

---

## Utility Classes (Version 1.1+)

### Code Validation Utilities

The `CodeValidator` class provides methods to validate administrative codes and their hierarchical relationships.

```java
import id.xtramile.indonesia.util.CodeValidator;

// Validate individual codes
boolean isValid = CodeValidator.isValidProvinceCode(11L); // true
boolean isInvalid = CodeValidator.isValidProvinceCode(100L); // false (too large)

boolean isValidCity = CodeValidator.isValidCityCode(1101L); // true
boolean isValidDistrict = CodeValidator.isValidDistrictCode(110101L); // true
boolean isValidVillage = CodeValidator.isValidVillageCode(1101011001L); // true

// Validate hierarchical relationships
boolean belongs = CodeValidator.isCityCodeBelongsToProvince(1101L, 11L); // true
boolean belongs = CodeValidator.isDistrictCodeBelongsToCity(110101L, 1101L); // true
boolean belongs = CodeValidator.isVillageCodeBelongsToDistrict(1101011001L, 110101L); // true
```

**Use Cases:**
- Input validation before querying the service
- Data integrity checks
- Validating user-provided administrative codes

### Distance Calculation Utilities

The `DistanceCalculator` class provides methods to calculate distances between locations and find nearest entities.

```java
import id.xtramile.indonesia.util.DistanceCalculator;
import id.xtramile.indonesia.model.*;

// Calculate distance between two provinces
Province jakarta = service.findProvince(31L).orElse(null);
Province bandung = service.findProvince(32L).orElse(null);
double distanceKm = DistanceCalculator.distanceBetweenProvinces(jakarta, bandung);
System.out.println("Distance: " + distanceKm + " km");

// Calculate distance between cities
City city1 = service.findCity(3174L).orElse(null);
City city2 = service.findCity(3273L).orElse(null);
double distance = DistanceCalculator.distanceBetweenCities(city1, city2);

// Calculate distance between coordinates
double distance = DistanceCalculator.distanceBetweenCoordinates(
    -6.2088, 106.8456, // Jakarta coordinates
    -6.9175, 107.6191  // Bandung coordinates
);

// Calculate distance in meters
double distanceMeters = DistanceCalculator.distanceBetweenCoordinatesInMeters(
    -6.2088, 106.8456,
    -6.9175, 107.6191
);

// Find nearest village to a location
List<Village> villages = service.getVillagesByProvince(31L);
Village nearest = DistanceCalculator.findNearestVillage(
    -6.2088, 106.8456, // Target coordinates
    villages
);
System.out.println("Nearest village: " + nearest.getName());

// Find nearest district
List<District> districts = service.getDistrictsByCity(3174L);
District nearestDistrict = DistanceCalculator.findNearestDistrict(
    -6.2088, 106.8456,
    districts
);

// Find nearest city
List<City> cities = service.getAllCities();
City nearestCity = DistanceCalculator.findNearestCity(
    -6.2088, 106.8456,
    cities
);
```

**Use Cases:**
- Location-based services
- Finding nearest administrative units
- Distance-based filtering and sorting
- Geographic analysis

**Note:** All distance calculations use the Haversine formula, which calculates the great-circle distance between two points on Earth, accounting for the Earth's spherical shape.

### Search Result Caching

Since version 1.1, `DefaultIndonesiaService` automatically caches search results to improve performance on repeated queries. The cache is automatically cleared when `refreshData()` is called.

```java
// First search - performs actual search and caches result
List<Province> results1 = service.searchProvinces("Jakarta");

// Second search with same query - returns cached result (much faster)
List<Province> results2 = service.searchProvinces("Jakarta");

// Cache is automatically cleared when data is refreshed
service.refreshData();
// Next search will perform actual search again
List<Province> results3 = service.searchProvinces("Jakarta");
```

**Benefits:**
- Improved performance for repeated searches
- Automatic cache management
- No manual cache invalidation needed

---

## Summary

- **Default Usage**: Use `IndonesiaServiceFactory.createDefault()` for quick setup with CSV data and in-memory caching.
- **Custom Cache**: Implement `IndonesiaDataCache` for custom caching strategies. Remember to implement all methods including the new direct lookup methods (since 1.1).
- **Custom Loader**: Implement `IndonesiaDataLoader` for loading from different data sources.
- **Custom Service**: Implement `IndonesiaService` for completely custom behavior.
- **Performance**: Use direct lookup methods (`getProvince()`, `getCity()`, etc.) for better performance when accessing single entities.
- **Validation**: Use `CodeValidator` to validate administrative codes before querying.
- **Distance Calculations**: Use `DistanceCalculator` for location-based operations and finding nearest entities.
- **Search Caching**: Search results are automatically cached for improved performance on repeated queries.

For more examples and advanced usage, refer to the test files in the source code repository.

