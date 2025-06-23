package base;

import factory.DriverFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LoggerUtil;

import java.time.Duration;

/**
 * BasePage - Simple foundation for all Page Object classes
 * 
 * Features:
 * - Basic WebDriver operations
 * - PageFactory initialization
 * - Simple wait and interaction methods
 * - Smart scroll method for finding elements
 * - Professional logging for all actions
 */
public abstract class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected JavascriptExecutor js;
    protected final String pageName;
    
    /**
     * Constructor for all page objects
     */
    public BasePage(String pageName) {
        this.pageName = pageName;
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
        LoggerUtil.info("Initialized page: " + pageName);
    }

    // ===================================
    // BASIC OPERATIONS WITH LOGGING
    // ===================================
    
    @Step("Click element")
    public void click(WebElement element) {
        String elementInfo = getElementInfo(element);
        LoggerUtil.info("Attempting to click on element: " + elementInfo);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            LoggerUtil.info("Successfully clicked on element: " + elementInfo);
        } catch (Exception e) {
            LoggerUtil.error("Failed to click on element: " + elementInfo + " - Error: " + e.getMessage());
            throw e;
        }
    }
    
    @Step("Fast click element")
    public void fastClick(WebElement element) {
        String elementInfo = getElementInfo(element);
        LoggerUtil.info("Fast clicking on element: " + elementInfo);
        try {
            element.click();
            LoggerUtil.info("Successfully fast clicked on element: " + elementInfo);
        } catch (Exception e) {
            LoggerUtil.error("Failed to fast click on element: " + elementInfo + " - Error: " + e.getMessage());
            throw e;
        }
    }
    
    @Step("Instant click element")
    public void instantClick(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            // Silent fail for ultra-fast operations
        }
    }
    
    @Step("Enter text: {text}")
    public void sendKeys(WebElement element, String text) {
        String elementInfo = getElementInfo(element);
        LoggerUtil.info("Attempting to enter text '" + text + "' into element: " + elementInfo);
        try {
            WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));
            visibleElement.clear();
            visibleElement.sendKeys(text);
            LoggerUtil.info("Successfully entered text '" + text + "' into element: " + elementInfo);
        } catch (Exception e) {
            LoggerUtil.error("Failed to enter text into element: " + elementInfo + " - Error: " + e.getMessage());
            throw e;
        }
    }
    
    @Step("Hover over element")
    public void hover(WebElement element) {
        String elementInfo = getElementInfo(element);
        LoggerUtil.info("Attempting to hover over element: " + elementInfo);
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            actions.moveToElement(element).perform();
            LoggerUtil.info("Successfully hovered over element: " + elementInfo);
        } catch (Exception e) {
            LoggerUtil.error("Failed to hover over element: " + elementInfo + " - Error: " + e.getMessage());
            throw e;
        }
    }
    
    @Step("Wait for element to be visible")
    public WebElement waitForVisible(WebElement element) {
        String elementInfo = getElementInfo(element);
        LoggerUtil.info("Waiting for element to be visible: " + elementInfo);
        try {
            WebElement result = wait.until(ExpectedConditions.visibilityOf(element));
            LoggerUtil.info("Element is now visible: " + elementInfo);
            return result;
        } catch (Exception e) {
            LoggerUtil.error("Element did not become visible: " + elementInfo + " - Error: " + e.getMessage());
            throw e;
        }
    }
    
    @Step("Wait for element to be clickable")
    public WebElement waitForClickable(WebElement element) {
        String elementInfo = getElementInfo(element);
        LoggerUtil.info("Waiting for element to be clickable: " + elementInfo);
        try {
            WebElement result = wait.until(ExpectedConditions.elementToBeClickable(element));
            LoggerUtil.info("Element is now clickable: " + elementInfo);
            return result;
        } catch (Exception e) {
            LoggerUtil.error("Element did not become clickable: " + elementInfo + " - Error: " + e.getMessage());
            throw e;
        }
    }

    // ===================================
    // SMART SCROLL METHOD WITH LOGGING
    // ===================================
    
    /**
     * Scrolls down the page until element becomes visible
     * Optimized for faster execution with larger scroll amounts
     */
    @Step("Scroll until element is visible")
    public boolean scrollUntilVisible(WebElement element) {
        String elementInfo = getElementInfo(element);
        LoggerUtil.info("Starting smart scroll to find element: " + elementInfo);
        
        int maxAttempts = 5;  // Reduced attempts
        int scrollAmount = 800;  // Larger scroll amount for faster movement
        
        for (int i = 0; i < maxAttempts; i++) {
            LoggerUtil.info("Scroll attempt " + (i + 1) + "/" + maxAttempts + " for element: " + elementInfo);
            
            try {
                // Check if element is visible
                if (element.isDisplayed() && element.getLocation().getY() > 0) {
                    LoggerUtil.info("Element found and visible on attempt " + (i + 1) + ": " + elementInfo);
                    // Scroll element into center view
                    js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", element);
                    LoggerUtil.info("Element scrolled into view: " + elementInfo);
                    return true;
                }
            } catch (Exception e) {
                LoggerUtil.debug("Element not yet in DOM on attempt " + (i + 1) + ": " + elementInfo);
            }
            
            // Fast scroll down
            LoggerUtil.debug("Scrolling down " + scrollAmount + "px on attempt " + (i + 1));
            js.executeScript("window.scrollBy(0, " + scrollAmount + ");");
            // Minimal wait for DOM update
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        LoggerUtil.warn("Element not found after " + maxAttempts + " scroll attempts: " + elementInfo);
        return false; // Element not found
    }
    
    // ===================================
    // UTILITY METHODS
    // ===================================
    
    /**
     * Gets element field name for logging purposes
     */
    private String getElementInfo(WebElement element) {
        try {
            // Try to find field name using reflection
            String fieldName = findFieldName(element);
            if (fieldName != null) {
                return fieldName;
            }
            
            // Try to get element info from its attributes for better logging
            String elementClass = element.getAttribute("class");
            String elementId = element.getAttribute("id");
            String elementTag = element.getTagName();
            
            if (elementClass != null && elementClass.contains("position-list-item")) {
                return "jobListingItem";
            } else if (elementClass != null && elementClass.contains("btn-navy")) {
                return "viewRoleButton";
            } else if (elementId != null && !elementId.isEmpty()) {
                return "element-" + elementId;
            } else if (elementClass != null && !elementClass.isEmpty()) {
                // Use first class name for better identification
                String firstClass = elementClass.split(" ")[0];
                return "element-" + firstClass;
            } else {
                return elementTag + "-element";
            }
        } catch (Exception e) {
            return "unknown-element";
        }
    }
    
    /**
     * Tries to find the field name of WebElement using reflection
     */
    private String findFieldName(WebElement element) {
        try {
            Class<?> pageClass = this.getClass();
            java.lang.reflect.Field[] fields = pageClass.getDeclaredFields();
            
            for (java.lang.reflect.Field field : fields) {
                if (field.getType() == WebElement.class || field.getType().getName().contains("WebElement")) {
                    field.setAccessible(true);
                    try {
                        Object fieldElement = field.get(this);
                        if (fieldElement != null) {
                            // For proxy elements, compare string representations
                            if (fieldElement.toString().equals(element.toString()) || 
                                fieldElement == element) {
                                return field.getName();
                            }
                        }
                    } catch (Exception fieldException) {
                        // Skip this field if access fails
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            // Reflection failed completely
        }
        return null;
    }
    
    // ===================================
    // PAGE UTILITIES WITH LOGGING
    // ===================================
    
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        LoggerUtil.debug("Current URL: " + url);
        return url;
    }
    
    public String getPageTitle() {
        String title = driver.getTitle();
        LoggerUtil.debug("Current page title: " + title);
        return title;
    }
    
    public void switchToNewWindow() {
        LoggerUtil.info("Switching to new window/tab");
        String originalWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                LoggerUtil.info("Successfully switched to new window: " + getCurrentUrl());
                break;
            }
        }
    }
    
    /**
     * Force wait - use sparingly
     */
    public void forceWait(int seconds) {
        LoggerUtil.debug("Force waiting for " + seconds + " seconds");
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    


    @Step("Navigate to URL: {url}")
    public void navigateToUrl(String url) {
        LoggerUtil.info("Navigating to URL: " + url);
        try {
            driver.get(url);
            LoggerUtil.info("Successfully navigated to: " + getCurrentUrl());
        } catch (Exception e) {
            LoggerUtil.error("Failed to navigate to URL: " + url + " - Error: " + e.getMessage());
            throw e;
        }
    }
} 