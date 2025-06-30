package factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.ConfigReader;
import utils.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * DriverFactory - WebDriver Management Hub
 * 
 * Handles:
 * - Local and Remote WebDriver creation
 * - Browser-specific configurations
 * - Thread-safe driver management
 * - Automatic driver cleanup
 */
public class DriverFactory {
    
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    // Browser types
    public enum BrowserType {
        CHROME, FIREFOX, EDGE
    }
    
    /**
     * Initialize WebDriver based on configuration
     */
    public static void initializeDriver() {
        String browser = ConfigReader.getProperty("browser", "chrome").toLowerCase();
        String environment = ConfigReader.getProperty("environment", "local").toLowerCase();
        
        LoggerUtil.info("Initializing " + browser + " driver in " + environment + " environment");
        
        WebDriver driver;
        
        if ("docker".equals(environment) || "remote".equals(environment)) {
            driver = createRemoteDriver(browser);
        } else {
            driver = createLocalDriver(browser);
        }
        
        configureDriver(driver);
        driverThreadLocal.set(driver);
        
        LoggerUtil.info("WebDriver initialized successfully: " + browser);
    }
    
    /**
     * Create local WebDriver instance
     */
    private static WebDriver createLocalDriver(String browser) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> createFirefoxDriver();
            case "edge" -> createEdgeDriver();
            default -> {
                LoggerUtil.warn("Unknown browser: " + browser + ", defaulting to Chrome");
                yield createChromeDriver();
            }
        };
    }
    
    /**
     * Create Chrome driver with optimized options
     */
    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Performance optimizations
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-images");
        options.addArguments("--disable-web-security");
        
        // Notification and popup blocking
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-features=TranslateUI");
        options.addArguments("--disable-iframes-display-none-removal");
        
        // Window management
        if (ConfigReader.getBooleanProperty("headless", false)) {
            options.addArguments("--headless=new");
            LoggerUtil.info("Running Chrome in headless mode");
        }
        
        String windowSize = ConfigReader.getProperty("window.size", "1920,1080");
        options.addArguments("--window-size=" + windowSize);
        
        // Additional preferences
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        
        // Chrome preferences for notifications and popups
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2); // Block notifications
        prefs.put("profile.default_content_setting_values.popups", 0); // Block popups
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.managed_default_content_settings.popups", 0);
        prefs.put("profile.default_content_setting_values.geolocation", 2); // Block location
        prefs.put("profile.default_content_setting_values.media_stream", 2); // Block camera/mic
        options.setExperimentalOption("prefs", prefs);
        
        LoggerUtil.info("Chrome driver configured with optimized options (notifications blocked)");
        return new ChromeDriver(options);
    }
    
    /**
     * Create Firefox driver with optimized options
     */
    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        
        if (ConfigReader.getBooleanProperty("headless", false)) {
            options.addArguments("--headless");
            LoggerUtil.info("Running Firefox in headless mode");
        }
        
        // Performance preferences
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("media.volume_scale", "0.0");
        
        LoggerUtil.info("Firefox driver configured with optimized options");
        return new FirefoxDriver(options);
    }
    
    /**
     * Create Edge driver with optimized options
     */
    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        
        if (ConfigReader.getBooleanProperty("headless", false)) {
            options.addArguments("--headless=new");
            LoggerUtil.info("Running Edge in headless mode");
        }
        
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        
        LoggerUtil.info("Edge driver configured with optimized options");
        return new EdgeDriver(options);
    }
    
    /**
     * Create remote WebDriver for Docker/Grid
     */
    private static WebDriver createRemoteDriver(String browser) {
        try {
            String hubUrl = ConfigReader.getProperty("selenium.hub.url", "http://localhost:4444/wd/hub");
            URL gridUrl = URI.create(hubUrl).toURL();
            
            return switch (browser.toLowerCase()) {
                case "chrome" -> {
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--disable-dev-shm-usage", "--no-sandbox", "--disable-gpu");
                    LoggerUtil.info("Creating remote Chrome driver");
                    yield new RemoteWebDriver(gridUrl, chromeOptions);
                }
                case "firefox" -> {
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    LoggerUtil.info("Creating remote Firefox driver");
                    yield new RemoteWebDriver(gridUrl, firefoxOptions);
                }
                case "edge" -> {
                    EdgeOptions edgeOptions = new EdgeOptions();
                    LoggerUtil.info("Creating remote Edge driver");
                    yield new RemoteWebDriver(gridUrl, edgeOptions);
                }
                default -> {
                    LoggerUtil.warn("Unknown browser for remote: " + browser + ", defaulting to Chrome");
                    ChromeOptions defaultOptions = new ChromeOptions();
                    yield new RemoteWebDriver(gridUrl, defaultOptions);
                }
            };
            
        } catch (MalformedURLException e) {
            LoggerUtil.error("Invalid Selenium Grid URL", e);
            throw new RuntimeException("Failed to create remote driver", e);
        }
    }
    
    /**
     * Configure common driver settings
     */
    private static void configureDriver(WebDriver driver) {
        // Timeouts
        int implicitWait = ConfigReader.getIntProperty("timeout.implicit", 10);
        int pageLoadTimeout = ConfigReader.getIntProperty("timeout.pageLoad", 30);
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        
        // Window management
        if (!ConfigReader.getBooleanProperty("headless", false)) {
            if (ConfigReader.getBooleanProperty("maximize.window", true)) {
                driver.manage().window().maximize();
                LoggerUtil.info("Browser window maximized");
            }
        }
        
        LoggerUtil.info("Driver configured with timeouts - Implicit: " + implicitWait + "s, PageLoad: " + pageLoadTimeout + "s");
    }
    
    /**
     * Get current WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            LoggerUtil.error("WebDriver not initialized! Call initializeDriver() first.");
            throw new RuntimeException("WebDriver not initialized");
        }
        return driver;
    }
    
    /**
     * Check if driver is initialized
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }
    
    /**
     * Quit driver and clean up
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                LoggerUtil.info("WebDriver quit successfully");
            } catch (Exception e) {
                LoggerUtil.error("Error during driver quit", e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
    
    /**
     * Navigate to URL with error handling
     */
    public static void navigateToUrl(String url) {
        try {
            WebDriver driver = getDriver();
            driver.get(url);
            LoggerUtil.info("Navigated to: " + url);
            
            // Wait for page load
            Thread.sleep(2000);
            
        } catch (Exception e) {
            LoggerUtil.error("Failed to navigate to: " + url, e);
            throw new RuntimeException("Navigation failed", e);
        }
    }
    
    /**
     * Get current browser name
     */
    public static String getCurrentBrowser() {
        return ConfigReader.getProperty("browser", "chrome");
    }
    
    /**
     * Get current environment
     */
    public static String getCurrentEnvironment() {
        return ConfigReader.getProperty("environment", "local");
    }
} 