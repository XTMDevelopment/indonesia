# Indonesia Java Library

A Java library for Indonesian administrative data (Provinces, Cities, Districts, Villages).

## Features

- Load Indonesian administrative data from CSV files
- In-memory caching for fast data access
- Hierarchical data queries (Province → City → District → Village)
- Search functionality across all administrative levels
- Thread-safe cache implementation
- Support for Java 8 and above

## Requirements

- Java 8 or higher
- Maven 3.6+

## Supported Java Versions

This library is tested and compatible with:
- Java 8 (minimum supported version)
- Java 11
- Java 17
- Java 21

## Building

```bash
mvn clean install
```

## Running Tests

```bash
mvn test
```

## Usage

```java
import id.xtramile.indonesia.util.IndonesiaServiceFactory;
import id.xtramile.indonesia.IndonesiaService;

// Create service instance
IndonesiaService service = IndonesiaServiceFactory.createDefault();

// Find a province
Optional<Province> province = service.findProvince(11L);

// Get all cities in a province
List<City> cities = service.getCitiesByProvince(11L);

// Search for provinces
List<Province> results = service.searchProvinces("Jakarta");

// Build hierarchical data from any entity
Indonesia indonesia = service.buildFrom(province.get());
```

## License

MIT License

## Developer

Auriga Aristo - https://github.com/Rigsto

