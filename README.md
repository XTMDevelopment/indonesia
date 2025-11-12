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

## Documentation

- **[HOW-TO-USE.md](HOW-TO-USE.md)** - Comprehensive guide on using the library, including:
  - Using the default service implementation
  - Creating custom cache implementations
  - Creating custom loader implementations
  - Creating custom service implementations
- **[CONTRIBUTING.md](CONTRIBUTING.md)** - Guidelines for contributing to the project

## Usage

Quick start example:

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

For more detailed usage examples and advanced features, see [HOW-TO-USE.md](HOW-TO-USE.md).

## License

MIT License

## Developer

Auriga Aristo - https://github.com/Rigsto

