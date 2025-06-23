package pages;

import base.BasePage;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * CareersPage - Insider Careers Page Object
 * 
 * Simple page object for careers page operations and validations
 * Uses smart scroll functionality to find elements on the page
 */
public class CareersPage extends BasePage {
    
    // ===================================
    // PAGE ELEMENTS
    // ===================================
    
    @FindBy(css = "section.elementor-section[data-id='a8e7b90']")
    public WebElement blockLocations;
    
    @FindBy(css = "section.elementor-section[data-id='21cea83']") 
    public WebElement blockTeams;
    
    @FindBy(css = "section.elementor-section[data-id='6c45e81']")
    public WebElement blockLifeAtInsider;
    
    @FindBy(css = ".career-find-our-calling h2")
    public WebElement titleFindOurCalling;
    
    @FindBy(css = ".career-our-location h2") 
    public WebElement titleOurLocation;
    
    // Alternative locators - if main ones don't work
    @FindBy(xpath = "//h3[contains(text(), 'Our Locations')]")
    public WebElement locationsTitle;
    
    @FindBy(xpath = "//h3[contains(text(), 'Find your calling')]")
    public WebElement teamsTitle;
    
    @FindBy(xpath = "//h2[contains(text(), 'Life at Insider')]")
    public WebElement lifeAtInsiderTitle;
    
    // Page header element (at top of page)
    @FindBy(xpath = "//h1[normalize-space(text())='Ready to disrupt?']")
    public WebElement headerReadyToDisrupt;
    
    // ===================================
    // CONSTRUCTOR
    // ===================================
    
    public CareersPage() {
        super("Careers Page");
    }
    
    // ===================================
    // PAGE ACTIONS
    // ===================================
    
    @Step("Navigate to QA jobs page")
    public void navigateToQAJobs() {
        navigateToUrl("https://useinsider.com/careers/quality-assurance");
    }
    
    // ===================================
    // PAGE VALIDATIONS
    // ===================================
    
    @Step("Check careers page is loaded")
    public void checkCareersPageLoaded() {
        Assert.assertTrue(getCurrentUrl().toLowerCase().contains("careers"), "URL should contain careers!");
        Assert.assertTrue(getPageTitle().toLowerCase().contains("ready") || 
                         getPageTitle().toLowerCase().contains("insider"), "Page title is incorrect!");
        
        // Check header element at top of page (no scroll needed)
        Assert.assertTrue(headerReadyToDisrupt.isDisplayed(), "Header 'Ready to disrupt?' not displayed!");
    }
    
    @Step("Check all career page blocks")
    public void checkAllBlocks() {
        checkTeamsBlock();
        checkLocationsBlock();
        checkLifeAtInsiderBlock();
    }
    
    @Step("Check Locations block")
    public void checkLocationsBlock() {
        boolean locationsFound = scrollUntilVisible(locationsTitle) || 
                               scrollUntilVisible(blockLocations);
        
        Assert.assertTrue(locationsFound, "Locations block could not be found after scrolling!");
    }
    
    @Step("Check Teams block")
    public void checkTeamsBlock() {
        boolean teamsFound = scrollUntilVisible(teamsTitle) || 
                           scrollUntilVisible(blockTeams);
        
        Assert.assertTrue(teamsFound, "Teams block could not be found after scrolling!");
    }
    
    @Step("Check Life at Insider block")
    public void checkLifeAtInsiderBlock() {
        boolean lifeAtInsiderFound = scrollUntilVisible(lifeAtInsiderTitle) || 
                                   scrollUntilVisible(blockLifeAtInsider);
        
        Assert.assertTrue(lifeAtInsiderFound, "Life at Insider block could not be found after scrolling!");
    }
    
    @Override
    public boolean isPageLoaded() {
        try {
            return getCurrentUrl().toLowerCase().contains("careers") &&
                   headerReadyToDisrupt.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
} 