package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader - Configuration Management Utility
 * 
 * Handles reading configuration properties from config.properties file
 * Provides type-safe property access with default values
 */
public class ConfigReader {
    
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    
    static {
        loadProperties();
    }
    
    /**
     * Load properties from config file
     */
    private static void loadProperties() {
        properties = new Properties();
        
        try {
            // Try to load from file system first
            try (FileInputStream fileInput = new FileInputStream(CONFIG_FILE_PATH)) {
                properties.load(fileInput);
                LoggerUtil.info("Configuration loaded from: " + CONFIG_FILE_PATH);
            } catch (IOException e) {
                // If file system fails, try classpath
                try (InputStream resourceStream = ConfigReader.class.getClassLoader()
                        .getResourceAsStream("config.properties")) {
                    
                    if (resourceStream != null) {
                        properties.load(resourceStream);
                        LoggerUtil.info("Configuration loaded from classpath");
                    } else {
                        LoggerUtil.error("Configuration file not found in classpath");
                        throw new RuntimeException("config.properties not found");
                    }
                }
            }
            
        } catch (IOException e) {
            LoggerUtil.error("Failed to load configuration properties", e);
            throw new RuntimeException("Unable to load configuration", e);
        }
    }
    
    /**
     * Get string property with default value
     */
    public static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key); // Check system properties first
        if (value == null) {
            value = properties.getProperty(key, defaultValue);
        }
        return value;
    }
    
    /**
     * Get string property (required)
     */
    public static String getProperty(String key) {
        String value = getProperty(key, null);
        if (value == null) {
            throw new RuntimeException("Required property not found: " + key);
        }
        return value;
    }
    
    /**
     * Get integer property with default value
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LoggerUtil.warn("Invalid integer property: " + key + " = " + value + ", using default: " + defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Get boolean property with default value
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Get long property with default value
     */
    public static long getLongProperty(String key, long defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            LoggerUtil.warn("Invalid long property: " + key + " = " + value + ", using default: " + defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Get double property with default value
     */
    public static double getDoubleProperty(String key, double defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            LoggerUtil.warn("Invalid double property: " + key + " = " + value + ", using default: " + defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Check if property exists
     */
    public static boolean hasProperty(String key) {
        return System.getProperty(key) != null || properties.containsKey(key);
    }
    
    /**
     * Get all properties (for debugging)
     */
    public static Properties getAllProperties() {
        return (Properties) properties.clone();
    }
    
    /**
     * Reload properties from file
     */
    public static void reloadProperties() {
        loadProperties();
        LoggerUtil.info("Configuration properties reloaded");
    }
    
    // Common configuration getters for convenience
    
    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    public static String getBaseUrl() {
        return getProperty("base.url", "https://useinsider.com");
    }
    
    public static boolean isHeadless() {
        return getBooleanProperty("headless", false);
    }
    
    public static int getDefaultTimeout() {
        return getIntProperty("timeout.default", 10);
    }
    
    public static int getPageLoadTimeout() {
        return getIntProperty("timeout.pageLoad", 30);
    }
    
    public static String getEnvironment() {
        return getProperty("environment", "local");
    }
    
    public static String getSeleniumHubUrl() {
        return getProperty("selenium.hub.url", "http://localhost:4444/wd/hub");
    }
} 