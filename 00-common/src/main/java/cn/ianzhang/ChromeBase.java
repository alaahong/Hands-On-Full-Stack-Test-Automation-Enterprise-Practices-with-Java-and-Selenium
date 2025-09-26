package cn.ianzhang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;

import java.time.Duration;
import java.util.Collections;

/**
 * @Author: Ian
 */
public class ChromeBase {
    protected WebDriver driver;

    @BeforeEach
    void setupTest() {
        ChromeDriverService service = new ChromeDriverService.Builder()
                .withLogLevel(ChromiumDriverLogLevel.WARNING)
                .withLogOutput(System.out)
                .build();
        ChromeOptions options = new ChromeOptions();
        //开启无头模式
        //            options.addArguments("--headless");
        //禁止gpu渲染
        options.addArguments("--disable-gpu");
        //关闭沙盒模式
        options.addArguments("--no-sandbox");
        //disable specifying the browser options
        options.addArguments("--disable-search-engine-choice-screen");
        //disable language download
        options.addArguments("--disable-features=NetworkService,NetworkServiceInProcess,OptimizationGuideModelDownloading,OptimizationHintsFetching,OptimizationTargetPrediction,OptimizationHints");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--start-maximized");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--lang=zh-CN");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-background-networking");
        options.setAcceptInsecureCerts(Boolean.TRUE);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        //    options.addArguments("blink-settings=imagesEnabled=false");//禁用图片
        //        options.addArguments("--disable-dev-shm-usage");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        //    driver = new ChromeDriver(options);
//        options.setCapability("se:bidiEnabled", false);
//        options.setCapability("se:cdpEnabled", false);
//    options.setCapability("goog:loggingPrefs", java.util.Map.of("browser", "WARNING"));
        options.enableBiDi();
        driver = new ChromeDriver(service, options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // 推荐使用显式等待
        // 再次显式设置窗口尺寸以覆盖无头或平台差异
        driver.manage().window().setSize(new Dimension(1920, 1080));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
