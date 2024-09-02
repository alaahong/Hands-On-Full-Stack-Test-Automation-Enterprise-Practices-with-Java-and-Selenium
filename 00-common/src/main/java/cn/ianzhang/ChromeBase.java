package cn.ianzhang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;

import java.util.Collections;

/**
 * @Author: Ian
 */
public class ChromeBase {
  protected WebDriver driver;

  @BeforeEach
  void setupTest() {
    ChromeDriverService service = new ChromeDriverService.Builder()
            .withLogLevel(ChromiumDriverLogLevel.DEBUG)
            .withLogOutput(System.out)
            .build();
    ChromeOptions options = new ChromeOptions();
    //开启无头模式
    //            options.addArguments("--headless");
    //禁止gpu渲染
    options.addArguments("--disable-gpu");
    //关闭沙盒模式
    options.addArguments("–-no-sandbox");
    //disable specifying the browser options
    options.addArguments("--disable-search-engine-choice-screen");
    //disable language download
    options.addArguments("--disable-features=OptimizationGuideModelDownloading,OptimizationHintsFetching,OptimizationTargetPrediction,OptimizationHints");
    options.addArguments("--no-default-browser-check");
    options.addArguments("--start-maximized");
    options.addArguments("--lang=zh-CN");
    options.setAcceptInsecureCerts(Boolean.TRUE);
    options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
    //    options.addArguments("blink-settings=imagesEnabled=false");//禁用图片
    //        options.addArguments("--disable-dev-shm-usage");
    options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
    //    driver = new ChromeDriver(options);
    options.enableBiDi();
    driver = new ChromeDriver(service, options);
  }

  @AfterEach
  void teardown() {
    if (driver != null) {
      driver.quit();
    }
  }

}
