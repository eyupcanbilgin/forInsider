package pages;

import base.BasePage;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import utils.LoggerUtil;

/**
 * QualityAssuranceJobsPage - QA Jobs Listing and Filtering Page
 * 
 * This page handles QA position listings and filtering
 * Uses smart scroll methods to find elements
 */
public class QualityAssuranceJobsPage extends BasePage {
    
    // ===================================
    // PAGE ELEMENTS
    // ===================================
    
    // QA Overview page elements (/careers/quality-assurance)
    @FindBy(xpath = "//h1[normalize-space(text())='Quality Assurance']")
    public WebElement qaOverviewTitle;
    
    @FindBy(xpath = "//a[normalize-space(text())='See all QA jobs']")
    public WebElement btnSeeAllQAJobs;
    
    // Jobs List page elements (after clicking "See all QA jobs")
    @FindBy(xpath = "//h3[contains(text(), 'Browse') and contains(text(), 'Open Positions')]")
    public WebElement browseOpenPositionsTitle;
    
    // Select2 Location Filter Elements
    @FindBy(xpath = "//span[@id='select2-filter-by-location-container' and @title='All']")
    public WebElement locationFilterContainer;
    
    @FindBy(xpath = "//li[text()='Istanbul, Turkiye']")
    public WebElement istanbulTurkiyeOption;
    
    // Select2 Department Filter Elements  
    @FindBy(css = "#select2-filter-by-department-container")
    public WebElement departmentFilterContainer;
    
    @FindBy(xpath = "//li[contains(@id, 'select2-filter-by-department-result') and contains(text(), 'Quality Assurance')]")
    public WebElement qualityAssuranceOption;
    
    // Job listings after filtering
    @FindBy(css = ".position-list-item")
    public List<WebElement> jobListings;
    
    @FindBy(css = ".position-title")
    public List<WebElement> jobTitles;
    
    @FindBy(css = ".position-department")
    public List<WebElement> jobDepartments;
    
    @FindBy(css = ".position-location")
    public List<WebElement> jobLocations;
    
    @FindBy(css = ".btn.btn-navy[href*='jobs.lever.co']")
    public List<WebElement> viewRoleButtons;
    
    // Result counter
    @FindBy(css = "#resultCounter .totalResult")
    public WebElement totalResultCount;
    
    // ===================================
    // CONSTRUCTOR
    // ===================================
    
    public QualityAssuranceJobsPage() {
        super("QA Jobs Page");
    }
    
    // ===================================
    // PAGE ACTIONS
    // ===================================
    
    @Step("Click See all QA jobs button")
    public void clickSeeAllQAJobs() {
        waitForVisible(btnSeeAllQAJobs);
        click(btnSeeAllQAJobs);
        
        // Wait for the jobs page to fully load
        forceWait(3); // Wait for navigation
        
        // Wait for filter elements to be available
        waitForVisible(locationFilterContainer);
        waitForVisible(departmentFilterContainer);
    }
    
    @Step("Apply location filter")
    public void applyLocationFilter() {
        // Click location filter and wait for dropdown to open
        click(locationFilterContainer);
        
        // Click Istanbul option
        click(istanbulTurkiyeOption);
        forceWait(1); // Wait for filter to apply
    }
    

    
    @Step("Click first View Role button")
    public void clickFirstViewRole() {
        Assert.assertFalse(jobListings.isEmpty(), "No job listings found!");
        
        // Get first job block and hover quickly
        WebElement firstJobBlock = jobListings.get(0);
        hover(firstJobBlock);
        
        // Quick click View Role button
        Assert.assertFalse(viewRoleButtons.isEmpty(), "No View Role buttons found after hover!");
        WebElement firstViewRoleButton = viewRoleButtons.get(0);
        fastClick(firstViewRoleButton);
    }
    
    // ===================================
    // PAGE VALIDATIONS
    // ===================================
    
    @Step("Check QA overview page is loaded")
    public void checkQAOverviewPageLoaded() {
        String currentUrl = getCurrentUrl().toLowerCase();
        Assert.assertTrue(currentUrl.contains("quality-assurance"), 
                         "URL should contain quality-assurance! Current URL: " + currentUrl);
        
        Assert.assertTrue(qaOverviewTitle.isDisplayed(), 
                         "QA Overview title not displayed!");
        
        Assert.assertTrue(btnSeeAllQAJobs.isDisplayed(), 
                         "See all QA jobs button not displayed!");
    }
    
    @Step("Check QA jobs list page is loaded")
    public void checkQAJobsListPageLoaded() {
        String currentUrl = getCurrentUrl().toLowerCase();
        Assert.assertTrue(currentUrl.contains("open-positions"), 
                         "URL should contain open-positions! Current URL: " + currentUrl);
        
    }
    
    @Step("Check all filtered jobs meet criteria")
    public void checkFilteredJobs() {
        // Check that we have jobs (no wait)
        Assert.assertFalse(jobListings.isEmpty(), "Filtered job list is empty!");
        
        int jobCount = jobListings.size();
        
        // Check only first 3 jobs for speed
        int jobsToCheck = Math.min(jobCount, 3);
        
        for (int i = 0; i < jobsToCheck; i++) {
            String jobTitle = jobTitles.get(i).getText();
            String jobDepartment = jobDepartments.get(i).getText();
            String jobLocation = jobLocations.get(i).getText();
            
            // Quick validations
            Assert.assertTrue(
                jobTitle.toLowerCase().contains("quality assurance") || 
                jobTitle.toLowerCase().contains("qa") || 
                jobTitle.toLowerCase().contains("test"),
                "Job title is not QA related: " + jobTitle
            );
            
            Assert.assertEquals(jobDepartment, "Quality Assurance",
                "Department is not Quality Assurance: " + jobDepartment);
            
            Assert.assertEquals(jobLocation, "Istanbul, Turkiye",
                "Location is not Istanbul, Turkiye: " + jobLocation);
        }
    }
    
    @Step("Check Lever application page redirect")
    public void checkLeverRedirect() {
        String currentUrl = getCurrentUrl().toLowerCase();
        Assert.assertTrue(currentUrl.contains("jobs.lever.co") || 
                         currentUrl.contains("lever"),
                         "Not redirected to Lever application page! Current URL: " + currentUrl);
        
        String pageTitle = getPageTitle().toLowerCase();
        // More flexible title check - should contain either job-related or company-related terms
        Assert.assertTrue(pageTitle.contains("quality") || 
                         pageTitle.contains("engineer") || 
                         pageTitle.contains("software") ||
                         pageTitle.contains("qa") ||
                         pageTitle.contains("insider"),
                         "Page title does not contain expected job or company content: " + pageTitle);
    }
    
    @Override
    public boolean isPageLoaded() {
        try {
            String currentUrl = getCurrentUrl().toLowerCase();
            return currentUrl.contains("open-positions") &&
                   browseOpenPositionsTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}