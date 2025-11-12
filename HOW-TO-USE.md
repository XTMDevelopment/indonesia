# How to Use Indonesia Java Library

This guide explains how to use the Indonesia Java Library in your projects. You can either use the default service implementation or create custom implementations to suit your specific needs.

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
        
        // Build cities-by-province index
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
        
        // Build districts-by-city index
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
        
        // Build villages-by-district index
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

import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.cache.CacheStats;
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
        // Custom implementation with additional logic
        return Optional.ofNullable(cache.getProvinces().get(provinceCode));
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

3. **Index Maintenance**: When implementing `putCities()`, `putDistricts()`, and `putVillages()`, you must maintain the hierarchical indexes (`citiesByProvince`, `districtsByCity`, `villagesByDistrict`).

4. **Error Handling**: Custom loaders should throw `DataLoadException` when errors occur during data loading.

5. **Complete Implementation**: When implementing `IndonesiaService`, you must implement all methods defined in the interface.

---

## Summary

- **Default Usage**: Use `IndonesiaServiceFactory.createDefault()` for quick setup with CSV data and in-memory caching.
- **Custom Cache**: Implement `IndonesiaDataCache` for custom caching strategies.
- **Custom Loader**: Implement `IndonesiaDataLoader` for loading from different data sources.
- **Custom Service**: Implement `IndonesiaService` for completely custom behavior.

For more examples and advanced usage, refer to the test files in the source code repository.

