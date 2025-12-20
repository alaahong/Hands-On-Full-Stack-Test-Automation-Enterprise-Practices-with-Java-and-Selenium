package cn.ianzhang.extents;

import cn.ianzhang.WebDriverGetter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @Author: Ian
 */

@Disabled
public class ExtentReportsChromeBase implements WebDriverGetter, ExtentReportsGetter {
    protected static ExtentReports extent;
    protected WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver()
                .setup();
        extent = new ExtentReports();
        extent.attachReporter(
                new ExtentSparkReporter("target/report/extentreports.html")
        );
    }

    @BeforeEach
    public void beforeEach() {
        driver = new ChromeDriver(new ChromeOptions());
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Override
    public WebDriver getWebDriver() {
        return this.driver;
    }

    @Override
    public ExtentReports getExtentReports() {
        return extent;
    }
}
