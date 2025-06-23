package tests;

import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CareersPage;
import pages.HomePage;
import pages.QualityAssuranceJobsPage;

/**
 * InsiderCareerFlowTest - End-to-End Career Journey Test
 * 
 * Simple test class covering complete user journey from home to job application
 */
@Epic("Insider Career Journey")
@Feature("End-to-End User Flow")
public class InsiderCareerFlowTest extends BaseTest {
    
    // Page objects
    private HomePage homePage;
    private CareersPage careersPage;
    private QualityAssuranceJobsPage qaJobsPage;
    
    /**
     * Initialize page objects after WebDriver setup
     */
    @BeforeMethod(dependsOnMethods = "setUp")
    public void initializePageObjects() {
        homePage = new HomePage();
        careersPage = new CareersPage();
        qaJobsPage = new QualityAssuranceJobsPage();
    }
    
    @Test(description = "Complete Insider Career Journey - Single End-to-End Scenario")
    @Story("End-to-End Career Journey")
    @Description("Single comprehensive test: Home page → Company>Careers → QA Jobs → Filter → Job Details → Lever Application")
    @Severity(SeverityLevel.CRITICAL)
    public void testInsiderCareerJourney() {
        
        // ===========================================
        // STEP 1: HOME PAGE VALIDATION
        // ===========================================
        logTestStep("Step 1: Verify home page is loaded");
        homePage.checkHomePageLoaded();
        
        // ===========================================
        // STEP 2: NAVIGATE TO CAREERS PAGE
        // ===========================================
        logTestStep("Step 2: Navigate to Company > Careers page and verify");
        homePage.navigateToCareers();
        careersPage.checkCareersPageLoaded();
        careersPage.checkAllBlocks();
        
        // ===========================================
        // STEP 3: NAVIGATE TO QA OVERVIEW PAGE
        // ===========================================
        logTestStep("Step 3: Navigate to QA overview page");
        careersPage.navigateToQAJobs();
        qaJobsPage.checkQAOverviewPageLoaded();
        
        // ===========================================
        // STEP 4: CLICK "SEE ALL QA JOBS" BUTTON
        // ===========================================
        logTestStep("Step 4: Click See all QA jobs button");
        qaJobsPage.clickSeeAllQAJobs();
        qaJobsPage.checkQAJobsListPageLoaded();
        
        // ===========================================
        // STEP 5: APPLY LOCATION FILTER
        // ===========================================
        logTestStep("Step 5: Apply location filter (Istanbul, Turkiye)");
        qaJobsPage.applyLocationFilter();
        
        // ===========================================
        // STEP 6: VALIDATE FILTERED JOBS
        // ===========================================
        logTestStep("Step 6: Validate filtered jobs criteria");
        qaJobsPage.checkFilteredJobs();
        
        // ===========================================
        // STEP 7: LEVER REDIRECT VALIDATION
        // ===========================================
        logTestStep("Step 7: Click View Role button and verify Lever redirect");
        qaJobsPage.clickFirstViewRole();
        homePage.switchToNewWindow();
        qaJobsPage.checkLeverRedirect();
        
        // Test completed successfully
        logTestStep("✅ All steps completed successfully!");
    }
} 