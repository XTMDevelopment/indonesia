# Contributing to Indonesia Java Library

Thank you for your interest in contributing to the Indonesia Java Library! This document provides guidelines and instructions for contributing to the project.

## Table of Contents

1. [Code of Conduct](#code-of-conduct)
2. [Getting Started](#getting-started)
3. [Development Setup](#development-setup)
4. [Project Structure](#project-structure)
5. [Coding Standards](#coding-standards)
6. [Testing](#testing)
7. [Submitting Changes](#submitting-changes)
8. [Pull Request Process](#pull-request-process)

---

## Code of Conduct

- Be respectful and considerate of others
- Welcome newcomers and help them get started
- Focus on constructive feedback
- Respect different viewpoints and experiences

## Getting Started

1. **Fork the repository** on GitHub
2. **Clone your fork** to your local machine:
   ```bash
   git clone https://github.com/XTMDevelopment/indonesia.git
   cd indonesia
   ```
3. **Add the upstream repository**:
   ```bash
   git remote add upstream https://github.com/Rigsto/indonesia.git
   ```

## Development Setup

### Prerequisites

- **Java 8 or higher** (Java 8, 11, 17, or 21 recommended)
- **Maven 3.6+**
- **Git**

### Building the Project

```bash
# Clean and build the project
mvn clean install

# Skip tests during build (not recommended)
mvn clean install -DskipTests
```

### Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn test jacoco:report

# Run a specific test class
mvn test -Dtest=IndonesiaServiceIntegrationTest
```

### IDE Setup

The project uses standard Maven structure. Most IDEs (IntelliJ IDEA, Eclipse, VS Code) will automatically detect the Maven project.

**IntelliJ IDEA:**
1. Open the project directory
2. Maven will be automatically imported
3. Ensure Project SDK is set to Java 8 or higher

**Eclipse:**
1. Import as Maven project
2. Ensure Java compiler compliance level is 1.8 or higher

## Project Structure

```
indonesia/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── id/xtramile/indonesia/
│   │   │       ├── cache/              # Cache implementations
│   │   │       ├── constant/           # Constants
│   │   │       ├── exception/          # Custom exceptions
│   │   │       ├── loader/             # Data loader implementations
│   │   │       ├── model/              # Domain models (Province, City, etc.)
│   │   │       ├── service/            # Service implementations
│   │   │       └── util/               # Utility classes (Factory, etc.)
│   │   └── resources/
│   │       └── csv/                    # CSV data files
│   └── test/
│       └── java/
│           └── id/xtramile/indonesia/
│               ├── concurrency/        # Concurrency tests
│               ├── error/              # Error handling tests
│               ├── integration/        # Integration tests
│               └── performance/       # Performance tests
├── pom.xml                             # Maven configuration
├── README.md                           # Project documentation
├── HOW-TO-USE.md                       # Usage guide
└── CONTRIBUTING.md                     # This file
```

### Key Components

- **`IndonesiaService`**: Main service interface for querying administrative data
- **`DefaultIndonesiaService`**: Default service implementation
- **`IndonesiaDataCache`**: Cache interface for storing administrative data
- **`InMemoryIndonesiaCache`**: Thread-safe in-memory cache implementation
- **`IndonesiaDataLoader`**: Interface for loading data from various sources
- **`CsvIndonesiaDataLoader`**: CSV-based data loader implementation
- **`IndonesiaServiceFactory`**: Factory for creating service instances

## Coding Standards

### Java Code Style

1. **Indentation**: Use 4 spaces (no tabs)
2. **Line Length**: Maximum 120 characters per line
3. **Naming Conventions**:
   - Classes: `PascalCase` (e.g., `IndonesiaService`)
   - Methods/Variables: `camelCase` (e.g., `findProvince`)
   - Constants: `UPPER_SNAKE_CASE` (e.g., `CSV_PATH_PROVINCES`)
   - Packages: `lowercase` (e.g., `id.xtramile.indonesia`)

4. **JavaDoc**: All public classes and methods must have JavaDoc comments:
   ```java
   /**
    * Finds a province by its code.
    *
    * @param provinceCode the province code to search for
    * @return Optional containing the Province if found, empty otherwise
    */
   Optional<Province> findProvince(Long provinceCode);
   ```

5. **Access Modifiers**: Use the most restrictive access modifier possible (private > package-private > protected > public)

6. **Imports**: Organize imports (IDE can do this automatically):
   - Java standard library imports
   - Third-party library imports
   - Project imports

### Code Quality Guidelines

1. **Thread Safety**: 
   - Cache implementations must be thread-safe
   - Use `ConcurrentHashMap` for concurrent collections
   - Document thread-safety guarantees

2. **Defensive Programming**:
   - Return defensive copies of collections
   - Validate input parameters
   - Handle null values appropriately

3. **Error Handling**:
   - Use custom exceptions (`DataLoadException`) for data loading errors
   - Provide meaningful error messages
   - Preserve exception chains when re-throwing

4. **Immutability**: 
   - Prefer immutable objects where possible
   - Use `final` for fields that shouldn't change

5. **Method Design**:
   - Keep methods focused and single-purpose
   - Avoid methods longer than 50 lines
   - Extract complex logic into private helper methods

### Example: Good Code Style

```java
package id.xtramile.indonesia.service;

import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.model.Province;

import java.util.List;
import java.util.Optional;

/**
 * Service for querying Indonesia administrative data.
 *
 * @author YourName
 */
public class MyService implements IndonesiaService {
    
    private final IndonesiaDataCache cache;
    
    public MyService(IndonesiaDataCache cache) {
        this.cache = cache;
    }
    
    @Override
    public Optional<Province> findProvince(Long provinceCode) {
        if (provinceCode == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(cache.getProvinces().get(provinceCode));
    }
    
    // ... other methods
}
```

## Testing

### Test Structure

- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions
- **Concurrency Tests**: Test thread-safety
- **Performance Tests**: Test performance characteristics

### Writing Tests

1. **Test Naming**: Use descriptive test method names:
   ```java
   @Test
   void testFindProvinceWithValidCode() {
       // test implementation
   }
   
   @Test
   void testFindProvinceWithInvalidCode() {
       // test implementation
   }
   ```

2. **Test Coverage**: Aim for high test coverage (80%+)
   - Test happy paths
   - Test error cases
   - Test edge cases (null, empty, boundary values)

3. **Test Organization**: Use `@BeforeEach` and `@AfterEach` for setup/teardown:
   ```java
   class MyServiceTest {
       private IndonesiaService service;
       
       @BeforeEach
       void setUp() {
           service = IndonesiaServiceFactory.createDefault();
       }
       
       @Test
       void testSomething() {
           // test implementation
       }
   }
   ```

4. **Assertions**: Use JUnit 5 assertions:
   ```java
   assertEquals(expected, actual);
   assertTrue(condition);
   assertFalse(condition);
   assertNotNull(object);
   assertThrows(ExceptionClass.class, () -> method());
   ```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=IndonesiaServiceTest

# Run specific test method
mvn test -Dtest=IndonesiaServiceTest#testFindProvince
```

## Submitting Changes

### Before You Start

1. **Check existing issues**: Look for existing issues or pull requests that address your change
2. **Create an issue**: For significant changes, create an issue first to discuss the approach
3. **Keep changes focused**: Make small, focused changes rather than large refactorings

### Making Changes

1. **Create a branch** from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b fix/your-bug-fix
   ```

2. **Make your changes**:
   - Write code following the coding standards
   - Add tests for new functionality
   - Update documentation if needed

3. **Commit your changes**:
   ```bash
   git add .
   git commit -m "Add feature: description of your change"
   ```

   **Commit Message Guidelines**:
   - Use present tense ("Add feature" not "Added feature")
   - Be descriptive but concise
   - Reference issue numbers if applicable: "Fix #123: description"

4. **Keep your branch updated**:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

5. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

## Pull Request Process

### Before Submitting

1. **Ensure tests pass**: Run `mvn test` and fix any failures
2. **Check code style**: Ensure your code follows the project's coding standards
3. **Update documentation**: Update README.md, HOW-TO-USE.md, or JavaDoc as needed
4. **Squash commits**: Consider squashing related commits for cleaner history

### Creating a Pull Request

1. **Go to GitHub** and create a pull request from your fork to the main repository
2. **Fill out the PR template**:
   - **Title**: Clear, descriptive title
   - **Description**: 
     - What changes were made
     - Why the changes were made
     - How to test the changes
     - Related issues (if any)

3. **Wait for review**: Maintainers will review your PR
4. **Address feedback**: Make requested changes and push updates

### PR Checklist

- [ ] Code follows the project's coding standards
- [ ] All tests pass (`mvn test`)
- [ ] New tests added for new functionality
- [ ] Documentation updated (if needed)
- [ ] JavaDoc comments added for public APIs
- [ ] No linter warnings or errors
- [ ] Commit messages are clear and descriptive

### Review Process

1. **Automated Checks**: CI will run tests and checks
2. **Code Review**: Maintainers will review your code
3. **Feedback**: Address any requested changes
4. **Approval**: Once approved, your PR will be merged

## Areas for Contribution

We welcome contributions in the following areas:

1. **Bug Fixes**: Fix reported bugs
2. **New Features**: Add new functionality (discuss first via issue)
3. **Performance Improvements**: Optimize existing code
4. **Documentation**: Improve documentation and examples
5. **Test Coverage**: Add missing tests
6. **Code Quality**: Refactor and improve code quality

## Questions?

If you have questions about contributing:

1. **Check existing issues**: Your question might already be answered
2. **Create an issue**: Ask your question in a new issue
3. **Review existing code**: Look at similar implementations in the codebase

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

Thank you for contributing to Indonesia Java Library! 🎉

