/**
 * Author: Xebia | Appcino
 * Framework: SNAP Appian Test Framework (https://github.com/arjitappcino/Snap-Appian-Test-Framework)
 * Class Description: This is the base class of the framework via which everthing is started and ended. Feel free to get in touch to understand any of the frameworks objects.
 */
package base;

import com.appiancorp.ps.automatedtest.common.PropertiesUtilities;
import com.appiancorp.ps.automatedtest.fixture.BaseFixture;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import manager.DriverManager;
import com.appiancorp.ps.automatedtest.fixture.SitesFixture;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pageObjects.AppianObjects;
import pageObjects.ProjectSpecificObjects;
import utils.Report;
import utils.TestListener;
import utils.Utility;

import java.io.IOException;
import java.util.Properties;

@ExtendWith(TestListener.class)
public class BaseClass extends BaseFixture {

    public static SitesFixture fixture;
    public static WebDriver driver;
    public static ProjectSpecificObjects projectObject;
    public static AppianObjects appianObject;
    public static Utility util;
    public static ExtentSparkReporter reporter;
    public static ExtentReports extent;
    public static ExtentTest logger;
    public static Report report;
    public static String artifactsFolder;
    public static Properties props;
    public static String currentDirectory = System.getProperty("user.dir");
    public static int passedTests = 0;
    public static int failedTests = 0;
    public static int skippedTests = 0;

    @BeforeAll
    public static void reporting() throws IOException {
        Utility.deleteDirectoryContents(".\\testResults");
        report = new Report();
        artifactsFolder = Utility.currentTimestamp();
        extent = report.startReporting();
        util = new Utility();
    }

    @BeforeEach
    public void setUp() {
        fixture = new SitesFixture();
        this.setUpBrowser();
        projectObject = new ProjectSpecificObjects(driver);
        appianObject = new AppianObjects(driver);
    }

    @AfterAll
    public static void extentFlush() throws IOException {
        extent.flush();
        driver.quit();
        Utility.openReportInBrowser("testResults");
//        Report.sendEmail();
    }

    public void setUpBrowser() {
        PropertiesUtilities.loadProperties();
        props = PropertiesUtilities.getProps();
        driver = DriverManager.setBrowser(props.getProperty("TEST_BROWSER"));
        DriverManager.setZoom(props.getProperty("TEST_BROWSER_ZOOM"));
        fixture.setWebDriver(driver);
        fixture.setAppianUrlTo(props.getProperty("TEST_SITE_URL"));
        fixture.setAppianLocaleTo(props.getProperty("TEST_SITE_LOCALE"));
        fixture.setAppianVersionTo(props.getProperty("TEST_SITE_VERSION"));
        fixture.setTimeoutSecondsTo(Integer.valueOf(props.getProperty("TEST_TIMEOUT")));
    }
}
