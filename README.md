# 🚀 Insider Test Automation Framework

Modern, scalable test automation framework for Insider website career flow testing.

## 🏗️ Architecture

- **Language**: Java 17
- **Build Tool**: Maven
- **Testing Framework**: TestNG
- **WebDriver Management**: Selenium 4.15.0 + WebDriverManager
- **Reporting**: Allure Reports 2.24.0
- **Logging**: Log4j2
- **Design Pattern**: Page Object Model (POM)
- **Containerization**: Docker + Docker Compose

## 📋 Test Scenario

Complete end-to-end test for Insider career journey:

1. **Homepage Validation** - Verify Insider homepage loads correctly
2. **Navigation** - Company → Careers page navigation
3. **Careers Page Validation** - Teams, Locations, Life at Insider blocks
4. **QA Jobs Navigation** - Navigate to Quality Assurance jobs
5. **Job Filtering** - Filter by location (Istanbul, Turkey) and department (QA)
6. **Job Validation** - Verify filtered jobs meet criteria
7. **Application Flow** - Click View Role and verify Lever redirect

## 🚀 Quick Start

### Local Execution

```bash
# Run tests locally
mvn clean test -Dtest=InsiderCareerFlowTest

# Generate and serve Allure report
mvn allure:serve
```

### Docker Execution

```bash
# Linux/Mac
chmod +x run-tests-docker.sh
./run-tests-docker.sh

# Windows PowerShell
.\run-tests-docker.ps1
```

## 📊 Reporting

### Allure Reports

**Local Development:**
```bash
mvn allure:serve
# Opens report at http://localhost:auto-port
```

**Docker Environment:**
- 📊 **Allure Report**: http://localhost:5050
- 📈 **Allure Dashboard**: http://localhost:5252

### Report Features
- ✅ Test execution timeline
- 📈 Test trends and statistics  
- 🔍 Detailed step-by-step execution
- 📸 Screenshots on failure
- 📝 Comprehensive logging
- 🏷️ Test categorization (Epic, Feature, Story)

## 🐳 Docker Services

| Service | Port | Description |
|---------|------|-------------|
| Selenium Hub | 4444 | Selenium Grid coordinator |
| Chrome Node | - | Chrome browser instances |
| Firefox Node | - | Firefox browser instances |
| Edge Node | - | Edge browser instances |
| Allure Report | 5050 | Allure report viewer |
| Allure Dashboard | 5252 | Real-time test dashboard |

## ⚙️ Configuration

### Environment Variables

```properties
# Browser Configuration
BROWSER=chrome|firefox|edge
HEADLESS=true|false
ENVIRONMENT=local|docker|remote

# Grid Configuration
SELENIUM_HUB_URL=http://selenium-hub:4444/wd/hub

# Timeouts (seconds)
IMPLICIT_WAIT=3
PAGE_LOAD_TIMEOUT=15
EXPLICIT_WAIT=8
```

### Configuration Files

- `src/main/resources/config.properties` - Main configuration
- `testng.xml` - TestNG suite configuration
- `docker-compose.yml` - Docker services
- `pom.xml` - Maven dependencies

## 🎯 Performance Optimizations

- ⚡ **Aggressive timeouts** - 3s implicit, 15s page load
- 🚀 **Fast click methods** - InstantClick, FastClick options
- 📦 **Minimal waits** - Optimized element interactions
- 🔄 **Smart scrolling** - Efficient element location
- 💾 **Containerized execution** - Consistent environments

## 🏭 CI/CD Integration

### GitHub Actions Example

```yaml
name: Test Automation
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      - run: chmod +x run-tests-docker.sh
      - run: ./run-tests-docker.sh
      - uses: actions/upload-artifact@v3
        with:
          name: allure-results
          path: allure-results/
```

## 📁 Project Structure

```
forInsider/
├── src/
│   ├── main/java/
│   │   ├── base/           # BasePage, BaseTest
│   │   ├── factory/        # DriverFactory
│   │   ├── pages/          # Page Object classes
│   │   └── utils/          # Utilities (Config, Logger)
│   └── resources/
│       ├── config.properties
│       └── log4j2.xml
├── src/test/java/tests/    # Test classes
├── target/                 # Build outputs
├── allure-results/         # Test results
├── docker-compose.yml      # Docker services
├── Dockerfile             # Test container
├── run-tests-docker.sh    # Linux/Mac runner
├── run-tests-docker.ps1   # Windows runner
└── README.md

```

## 🛠️ Development

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- Chrome/Firefox browser (for local execution)

### Adding New Tests

1. Create page object in `src/main/java/pages/`
2. Add test class in `src/test/java/tests/`
3. Update `testng.xml` if needed
4. Run tests: `mvn test -Dtest=YourTestClass`

### Page Object Example

```java
@Epic("Your Epic")
@Feature("Your Feature")
public class YourPage extends BasePage {
    
    @FindBy(css = "your-selector")
    public WebElement yourElement;
    
    public YourPage() {
        super("Your Page");
    }
    
    @Step("Your action description")
    public void yourAction() {
        click(yourElement);
    }
}
```

## 📞 Support

- 📧 **Email**: eyupcanbilgin01@gmail.com

---

**Made with ❤️ for reliable test automation** 
