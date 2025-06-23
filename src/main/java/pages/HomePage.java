package pages;

import base.BasePage;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.text.Normalizer;
import java.util.Locale;

/**
 * HomePage - Insider Home Page Object
 * 
 * Simple page object with basic operations and validations
 */
public class HomePage extends BasePage {
    
    // ===================================
    // PAGE ELEMENTS
    // ===================================
    
    @FindBy(css = "a.wt-cli-accept-all-btn[data-cli_action='accept_all']")
    public WebElement btnAcceptCookies;
    
    @FindBy(xpath = "//a[@id='navbarDropdownMenuLink' and normalize-space(text())='Company']")
    public WebElement menuCompany;
    
    @FindBy(css = "a[href='https://useinsider.com/careers/']")
    public WebElement linkCareers;
    
    @FindBy(css = "img[alt='insider_logo'][src*='logo-old.png']")
    public WebElement logoInsider;
    
    @FindBy(css = "span.ins-close-button")
    public WebElement btnCloseTour;
    
    // ===================================
    // CONSTRUCTOR
    // ===================================
    
    public HomePage() {
        super("Home Page");
    }
    
    // ===================================
    // PAGE ACTIONS
    // ===================================
    
    @Step("Accept all cookies")
    public void acceptCookies() {
        try {
            if (btnAcceptCookies.isDisplayed()) {
                click(btnAcceptCookies);
            }
        } catch (Exception e) {
            // Cookie popup not present, continue normally
        }
    }
    
    @Step("Close tour popup if present")
    public void closeTourIfPresent() {
        try {
            forceWait(2);
            if (btnCloseTour.isDisplayed()) {
                click(btnCloseTour);
            }
        } catch (Exception e) {
            // Tour popup not present, continue normally
        }
    }
    
    @Step("Navigate to Careers page")
    public void navigateToCareers() {
        closeTourIfPresent();
        acceptCookies();
        hover(menuCompany);
        waitForVisible(linkCareers);
        click(linkCareers);
    }
    
    // ===================================
    // PAGE VALIDATIONS
    // ===================================
    
    @Step("Check home page is loaded")
    public void checkHomePageLoaded() {
        Assert.assertTrue(logoInsider.isDisplayed(), "Logo görüntülenemedi!");

        String cleanedTitle = Normalizer.normalize(getPageTitle(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase(Locale.ENGLISH);
        Assert.assertTrue(cleanedTitle.contains("individualized"), "Page title is incorrect!");

        Assert.assertTrue(getCurrentUrl().contains("useinsider.com"), "URL is incorrect!");
    }
    
    @Step("Check company menu is accessible")
    public void checkCompanyMenuAccessible() {
        Assert.assertTrue(menuCompany.isDisplayed(), "Company menü görüntülenemedi!");
        Assert.assertTrue(menuCompany.isEnabled(), "Company menü tıklanamıyor!");
    }
    
    @Override
    public boolean isPageLoaded() {
        try {
            return logoInsider.isDisplayed() && 
                   getPageTitle().toLowerCase().contains("insider") &&
                   getCurrentUrl().contains("useinsider.com");
        } catch (Exception e) {
            return false;
        }
    }
} 