package cn.ianzhang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Collections;

/**
 * @Author: Ian
 */
public class FirefoxBase {
  protected WebDriver driver;

  @BeforeEach
  void setupTest() {
    FirefoxOptions options = new FirefoxOptions();
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
    options.enableBiDi();
    driver = new FirefoxDriver(options);
  }

  @AfterEach
  void teardown() {
    if (driver != null) {
      driver.quit();
    }
  }

}
