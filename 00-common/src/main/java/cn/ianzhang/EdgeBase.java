package cn.ianzhang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;
import java.util.Collections;

/**
 * @Author: Ian
 */
public class EdgeBase {
    protected WebDriver driver;

    @BeforeEach
    void setupTest() {
        EdgeDriverService service = new EdgeDriverService.Builder()
                .withLoglevel(ChromiumDriverLogLevel.WARNING)
                .withLogOutput(System.out)
                .build();
        EdgeOptions options = new EdgeOptions();
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
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-background-networking");
        options.setAcceptInsecureCerts(Boolean.TRUE);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        //    options.addArguments("blink-settings=imagesEnabled=false");//禁用图片
        //        options.addArguments("--disable-dev-shm-usage");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        //    driver = new ChromeDriver(service, options);
        options.enableBiDi();
        driver = new EdgeDriver(service, options);
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
