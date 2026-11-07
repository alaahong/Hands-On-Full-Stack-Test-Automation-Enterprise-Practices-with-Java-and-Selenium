package cn.ianzhang.extents;

import cn.ianzhang.WebDriverBase;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * @Author: Ian
 */
public class ExtentReportsTest extends WebDriverBase {
    private static ExtentReports extent;

    @BeforeAll
    static void beforeAll() {
        extent = new ExtentReports();
        extent.attachReporter(
                new ExtentSparkReporter("target/report/extentreports.html")
        );
    }

    @AfterAll
    static void afterAll() {
        extent.flush();
    }

    @Test
    public void openThenAssertTitle() {
        ExtentTest test = extent.createTest("Open then assert title");
        driver.get("https://www.ianzhang.cn");
        test.log(Status.PASS, "Open https://www.ianzhang.cn");
        test.log(driver.getTitle().contains("手中执剑,方能保护所爱之人") ? Status.PASS : Status.FAIL,
                "Title should contain 手中执剑,方能保护所爱之人");
        test.addScreenCaptureFromBase64String(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BASE64), "Get screenshot");
        test.log(Status.PASS, "Get screenshot");
    }

    @Test
    public void throwFailure() {
        ExtentTest test = extent.createTest("Throw failure");
        driver.get("https://www.ianzhang.cn");
        test.log(Status.PASS, "Open https://www.ianzhang.cn");
        Throwable throwable = new RuntimeException("A runtime exception");
        test.log(Status.FAIL, throwable);
        test.addScreenCaptureFromBase64String(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BASE64), "Get screenshot");
    }
}
