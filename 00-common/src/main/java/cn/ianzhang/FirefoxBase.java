package cn.ianzhang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * @Author: Ian
 */
public class FirefoxBase {
    protected WebDriver driver;

    @BeforeEach
    void setupTest() {
        System.setProperty("webdriver.firefox.logfile", "NUL"); // Windows下屏蔽geckodriver日志
        System.setProperty("webdriver.firefox.driver.loglevel", "warn"); // 只输出警告
        FirefoxOptions options = new FirefoxOptions();
        //开启无头模式
        //            options.addArguments("--headless");
        //禁止gpu渲染
        options.addArguments("--disable-gpu");
        //关闭沙盒模式
        options.addArguments("--no-sandbox");
        //disable specifying the browser options
        options.addArguments("--disable-search-engine-choice-screen");
        //disable language download
        options.addArguments("--disable-features=OptimizationGuideModelDownloading,OptimizationHintsFetching,OptimizationTargetPrediction,OptimizationHints");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--start-maximized");
        options.addArguments("--lang=zh-CN");
        options.setAcceptInsecureCerts(Boolean.TRUE);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        options.enableBiDi();
        driver = new FirefoxDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // 推荐使用显式等待
        driver.manage().window().maximize();
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
