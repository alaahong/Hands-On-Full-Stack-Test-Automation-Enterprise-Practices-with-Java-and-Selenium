package cn.ianzhang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;

/**
 * @Author: Ian
 */
public class ChromeHeadlessBase {
  protected WebDriver driver;

  @BeforeEach
  void setupTest() {
    //    ChromeDriverService service = new ChromeDriverService.Builder()
    //            .withLogLevel(ChromiumDriverLogLevel.DEBUG)
    ////            .withLogOutput(System.out)
    //            .build();
    ChromeOptions options = new ChromeOptions();
    //开启无头模式
    options.addArguments("--headless");
    //禁止gpu渲染
    options.addArguments("--disable-gpu");
    //关闭沙盒模式
    options.addArguments("–-no-sandbox");
    options.addArguments("--no-default-browser-check");
    options.addArguments("--start-maximized");
    options.addArguments("--lang=zh-CN");
    options.setAcceptInsecureCerts(Boolean.TRUE);
    options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
    //    options.addArguments("blink-settings=imagesEnabled=false");//禁用图片
    //        options.addArguments("--disable-dev-shm-usage");
    options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
    //    driver = new ChromeDriver(service, options);
    driver = new ChromeDriver(options);
  }

  @AfterEach
  void teardown() {
    if (driver != null) {
      driver.quit();
    }
  }

}
