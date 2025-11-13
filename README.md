# Indonesia Java Library

A Java library for Indonesian administrative data (Provinces, Cities, Districts, Villages).

## Features

- Load Indonesian administrative data from CSV files
- In-memory caching for fast data access
- Hierarchical data queries (Province → City → District → Village)
- Search functionality across all administrative levels
- Thread-safe cache implementation
- Support for Java 8 and above
- **Version 1.1**: Performance optimizations with direct lookup methods and optimized indexes

## Requirements

- Java 8 or higher
- Maven 3.6+

## Supported Java Versions

This library is tested and compatible with:
- Java 8 (minimum supported version)
- Java 11
- Java 17
- Java 21

## Installation

Add the following dependency to your project:

### Maven

```xml
<dependency>
    <groupId>id.xtramile</groupId>
    <artifactId>indonesia</artifactId>
    <version>1.1</version>
</dependency>
```

### Gradle

```gradle
implementation 'id.xtramile:indonesia:1.1'
```

### SBT

```scala
libraryDependencies += "id.xtramile" % "indonesia" % "1.1"
```

### Ivy

```xml
<dependency org="id.xtramile" name="indonesia" rev="1.1" />
```

## Building

```bash
mvn clean install
```

## Running Tests

```bash
mvn test
```

## What's New in Version 1.1

- **Performance Optimizations**: Direct lookup methods that avoid defensive copying, providing up to 90% faster lookups
- **Optimized Indexes**: Additional indexes for villages by province and city, eliminating full scans
- **Reduced Memory Usage**: Direct lookup methods reduce unnecessary object creation
- **Search Result Caching**: Automatic caching of search results for improved performance on repeated queries
- **Code Validation Utilities**: `CodeValidator` class for validating administrative codes and their hierarchical relationships
- **Distance Calculation Utilities**: `DistanceCalculator` class for calculating distances between locations and finding nearest entities

See [HOW-TO-USE.md](HOW-TO-USE.md) for details on using the new performance features.

## Version History

| Version | Changes | Date |
|---------|---------|------|
| **1.1** | **New Features:** Performance optimizations with direct lookup methods (up to 90% faster), optimized indexes for villages by province and city, search result caching, code validation utilities (`CodeValidator`), distance calculation utilities (`DistanceCalculator`). **Improvements:** Reduced memory usage through direct lookups, enhanced hierarchical query performance. | 2025-11-13 |
| **1.0** | **Initial Release:** Load Indonesian administrative data from CSV files, in-memory caching for fast data access, hierarchical data queries (Province → City → District → Village), search functionality across all administrative levels, thread-safe cache implementation, support for Java 8 and above. | 2025-11-12 |

## Documentation

- **[HOW-TO-USE.md](HOW-TO-USE.md)** - Comprehensive guide on using the library, including:
  - Using the default service implementation
  - Creating custom cache implementations
  - Creating custom loader implementations
  - Creating custom service implementations
  - Performance tips for version 1.1+
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

