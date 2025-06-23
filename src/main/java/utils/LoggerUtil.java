package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LoggerUtil - Centralized Logging Utility
 * 
 * Provides a clean interface for logging throughout the framework
 * Wraps Log4j2 functionality with convenience methods
 */
public class LoggerUtil {
    
    private static final Logger logger = LogManager.getLogger(LoggerUtil.class);
    
    /**
     * Log info message
     */
    public static void info(String message) {
        logger.info(message);
    }
    
    /**
     * Log info message with parameters
     */
    public static void info(String message, Object... params) {
        logger.info(message, params);
    }
    
    /**
     * Log debug message
     */
    public static void debug(String message) {
        logger.debug(message);
    }
    
    /**
     * Log debug message with parameters
     */
    public static void debug(String message, Object... params) {
        logger.debug(message, params);
    }
    
    /**
     * Log warning message
     */
    public static void warn(String message) {
        logger.warn(message);
    }
    
    /**
     * Log warning message with exception
     */
    public static void warn(String message, Throwable throwable) {
        logger.warn(message, throwable);
    }
    
    /**
     * Log error message
     */
    public static void error(String message) {
        logger.error(message);
    }
    
    /**
     * Log error message with exception
     */
    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
    
    /**
     * Log fatal message
     */
    public static void fatal(String message) {
        logger.fatal(message);
    }
    
    /**
     * Log fatal message with exception
     */
    public static void fatal(String message, Throwable throwable) {
        logger.fatal(message, throwable);
    }
    
    /**
     * Log step start - for test readability
     */
    public static void stepStart(String stepDescription) {
        logger.info("▶ STEP START: " + stepDescription);
    }
    
    /**
     * Log step completion - for test readability
     */
    public static void stepEnd(String stepDescription) {
        logger.info("✓ STEP COMPLETE: " + stepDescription);
    }
    
    /**
     * Log test start
     */
    public static void testStart(String testName) {
        logger.info("🧪 TEST STARTED: " + testName);
        logger.info("=" + "=".repeat(50 + testName.length()));
    }
    
    /**
     * Log test completion
     */
    public static void testEnd(String testName, String status) {
        logger.info("=" + "=".repeat(50 + testName.length()));
        logger.info("🧪 TEST " + status.toUpperCase() + ": " + testName);
    }
    
    /**
     * Log assertion
     */
    public static void assertion(String description, boolean passed) {
        if (passed) {
            logger.info("✓ ASSERTION PASSED: " + description);
        } else {
            logger.error("✗ ASSERTION FAILED: " + description);
        }
    }
    
    /**
     * Log browser action
     */
    public static void browserAction(String action) {
        logger.info("🌐 BROWSER: " + action);
    }
    
    /**
     * Log element interaction
     */
    public static void elementAction(String action, String element) {
        logger.info("🎯 ELEMENT: " + action + " on " + element);
    }
    
    /**
     * Log navigation
     */
    public static void navigation(String action) {
        logger.info("🧭 NAVIGATION: " + action);
    }
    
    /**
     * Log verification
     */
    public static void verification(String description, boolean result) {
        if (result) {
            logger.info("✅ VERIFICATION PASSED: " + description);
        } else {
            logger.error("❌ VERIFICATION FAILED: " + description);
        }
    }
    
    /**
     * Get logger for specific class
     */
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
    
    /**
     * Get logger with specific name
     */
    public static Logger getLogger(String name) {
        return LogManager.getLogger(name);
    }
} 