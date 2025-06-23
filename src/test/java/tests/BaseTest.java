package tests;

import factory.DriverFactory;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.LoggerUtil;

/**
 * BaseTest - Foundation for all test classes
 * 
 * Provides:
 * - WebDriver setup and teardown
 * - Test lifecycle management
 * - Screenshot capture on failure
 * - Allure reporting integration
 * - Configuration management
 */
public abstract class BaseTest {
    
    // ===================================
    // TEST SETUP AND TEARDOWN
    // ===================================
    
    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "headless"})
    public void setUp(@Optional("chrome") String browser, 
                      @Optional("false") String headless) {
        
        LoggerUtil.testStart(this.getClass().getSimpleName());
        
        try {
            // Override config with parameters if provided
            System.setProperty("browser", browser);
            System.setProperty("headless", headless);
            
            // Initialize WebDriver
            DriverFactory.initializeDriver();
            
            // Navigate to base URL
            String baseUrl = ConfigReader.getBaseUrl();
            DriverFactory.navigateToUrl(baseUrl);
            
            LoggerUtil.info("Test setup completed successfully");
            LoggerUtil.info("Browser: " + DriverFactory.getCurrentBrowser());
            LoggerUtil.info("Environment: " + DriverFactory.getCurrentEnvironment());
            LoggerUtil.info("Base URL: " + baseUrl);
            
        } catch (Exception e) {
            LoggerUtil.error("Test setup failed", e);
            throw new RuntimeException("Cannot initialize test environment", e);
        }
    }
    
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        
        try {
            // Take screenshot on failure
            if (result.getStatus() == ITestResult.FAILURE) {
                LoggerUtil.error("Test failed: " + result.getName());
                takeScreenshotOnFailure();
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                LoggerUtil.testEnd(result.getName(), "PASSED");
            } else if (result.getStatus() == ITestResult.SKIP) {
                LoggerUtil.testEnd(result.getName(), "SKIPPED");
            }
            
        } catch (Exception e) {
            LoggerUtil.error("Error during test teardown", e);
        } finally {
            // Always quit driver
            if (DriverFactory.isDriverInitialized()) {
                DriverFactory.quitDriver();
                LoggerUtil.info("WebDriver closed successfully");
            }
        }
    }
    
    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        LoggerUtil.info("=".repeat(80));
        LoggerUtil.info("🚀 STARTING TEST SUITE: " + this.getClass().getSimpleName());
        LoggerUtil.info("=".repeat(80));
        
        // Print configuration
        printTestConfiguration();
    }
    
    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        LoggerUtil.info("=".repeat(80));
        LoggerUtil.info("🏁 TEST SUITE COMPLETED: " + this.getClass().getSimpleName());
        LoggerUtil.info("=".repeat(80));
    }
    
    // ===================================
    // UTILITY METHODS
    // ===================================
    
    /**
     * Take screenshot and attach to Allure report
     */
    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] takeScreenshotOnFailure() {
        try {
            if (DriverFactory.isDriverInitialized()) {
                TakesScreenshot screenshot = (TakesScreenshot) DriverFactory.getDriver();
                byte[] screenshotData = screenshot.getScreenshotAs(OutputType.BYTES);
                LoggerUtil.info("Screenshot captured and attached to report");
                return screenshotData;
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to take screenshot", e);
        }
        return new byte[0];
    }
    
    /**
     * Take screenshot manually (for debugging)
     */
    @Attachment(value = "Manual Screenshot", type = "image/png")
    public byte[] takeScreenshot(String description) {
        try {
            if (DriverFactory.isDriverInitialized()) {
                TakesScreenshot screenshot = (TakesScreenshot) DriverFactory.getDriver();
                byte[] screenshotData = screenshot.getScreenshotAs(OutputType.BYTES);
                LoggerUtil.info("Manual screenshot taken: " + description);
                return screenshotData;
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to take manual screenshot", e);
        }
        return new byte[0];
    }
    
    /**
     * Print test configuration for debugging
     */
    private void printTestConfiguration() {
        LoggerUtil.info("📋 TEST CONFIGURATION:");
        LoggerUtil.info("  Browser: " + ConfigReader.getBrowser());
        LoggerUtil.info("  Environment: " + ConfigReader.getEnvironment());
        LoggerUtil.info("  Base URL: " + ConfigReader.getBaseUrl());
        LoggerUtil.info("  Headless: " + ConfigReader.isHeadless());
        LoggerUtil.info("  Default Timeout: " + ConfigReader.getDefaultTimeout() + "s");
        LoggerUtil.info("  Page Load Timeout: " + ConfigReader.getPageLoadTimeout() + "s");
        
        if (ConfigReader.getEnvironment().equals("docker")) {
            LoggerUtil.info("  Selenium Hub URL: " + ConfigReader.getSeleniumHubUrl());
        }
    }
    
    /**
     * Wait for a specified time (use sparingly)
     */
    protected void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            LoggerUtil.info("Waited for " + seconds + " seconds");
        } catch (InterruptedException e) {
            LoggerUtil.error("Wait interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Get current test method name
     */
    protected String getCurrentTestMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
    
    /**
     * Log test step (for better readability)
     */
    protected void logTestStep(String stepDescription) {
        LoggerUtil.stepStart(stepDescription);
    }
    
    /**
     * Log test verification
     */
    protected void logTestVerification(String description, boolean result) {
        LoggerUtil.verification(description, result);
    }
    
    /**
     * Assert with logging
     */
    protected void assertWithLogging(boolean condition, String message) {
        if (condition) {
            LoggerUtil.assertion(message, true);
        } else {
            LoggerUtil.assertion(message, false);
            takeScreenshot("Assertion Failed: " + message);
            throw new AssertionError("Assertion failed: " + message);
        }
    }
    
    /**
     * Soft assertion with logging (doesn't stop test execution)
     */
    protected boolean softAssertWithLogging(boolean condition, String message) {
        if (condition) {
            LoggerUtil.assertion(message, true);
            return true;
        } else {
            LoggerUtil.assertion(message, false);
            return false;
        }
    }
} 