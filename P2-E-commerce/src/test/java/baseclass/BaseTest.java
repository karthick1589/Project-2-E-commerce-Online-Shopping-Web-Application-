package baseclass;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import utilityclass.ConfigReader;

import java.io.File;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

@Listeners(baseclass.TestListener.class) 
public class BaseTest {
    
    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    public static ExtentSparkReporter spark;
    public static ExtentReports extent;

    @BeforeSuite
    public void setUpReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeMethod
    @Parameters("browser")
    public void setup(@Optional String browserFromXML) {
        String browser;
        if (browserFromXML != null && !browserFromXML.isEmpty()) {
            browser = browserFromXML;
        } else {
            String configBrowser = null;
            try { configBrowser = ConfigReader.getProperty("browser"); } catch(Exception e) {}
            browser = (configBrowser != null) ? configBrowser : "chrome";
        }

        System.out.println("Launching Browser: " + browser);
        WebDriver localDriver;

        if (browser.equalsIgnoreCase("edge")) {
            localDriver = new EdgeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            localDriver = new FirefoxDriver();
        } else {
            localDriver = new ChromeDriver();
        }
        
        driver.set(localDriver);
        getDriver().manage().window().maximize();
        
        long wait = 10;
        try {
            String waitTime = ConfigReader.getProperty("implicitWait");
            if(waitTime != null) wait = Long.parseLong(waitTime);
        } catch(Exception e) {}

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

        try {
            String appUrl = ConfigReader.getProperty("testurl");
            if (appUrl != null) getDriver().get(appUrl);
            else getDriver().get("https://www.demoblaze.com/");
        } catch (Exception e) {
            getDriver().get("https://www.demoblaze.com/");
        }
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {

            try {
                getDriver().switchTo().alert().accept();
                System.out.println("Handled unexpected alert in tearDown");
            } catch (Exception e) {
            }

            try {
                getDriver().quit();
            } catch (Exception e) {
                System.out.println("Error while quitting driver: " + e.getMessage());
            }
            driver.remove();
        }
    }

    @AfterSuite
    public void flushReport() {
        extent.flush();
    }
    
    public static WebDriver getDriver() {
        return driver.get();
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }

    public static String captureScreenshot(String testName) {
        if (getDriver() == null) {
            return null; 
        }

        try {
            File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            String timestamp = String.valueOf(System.currentTimeMillis());
            String path = System.getProperty("user.dir") + "/screenshots/" + testName + "_" + timestamp + ".png";
            
            File dest = new File(path);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            
            java.nio.file.Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return path;
        } catch (Exception e) {
            // If browser died, this catches it and returns null so Listener doesn't crash
            System.out.println("Screenshot Failed: " + e.getMessage());
            return null;
        }
    }
    
}