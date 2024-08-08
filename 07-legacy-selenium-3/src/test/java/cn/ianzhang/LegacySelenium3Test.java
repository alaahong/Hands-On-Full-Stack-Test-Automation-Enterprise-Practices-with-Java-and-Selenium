package cn.ianzhang;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.nio.file.Path;
import java.util.Map;

public class LegacySelenium3Test {
  protected WebDriver driver;
  protected ChromeOptions chromeOptions;
  @TempDir
  private Path tempDir;

  @BeforeAll
  static void setupClass() {
    WebDriverManager.chromedriver()
            .setup();
  }

  @BeforeEach
  void init() {
    driver = WebDriverManager.chromedriver().create();
  }

  @AfterEach
  void teardown() {
    if (driver != null) {
      driver.quit();
    }
  }

  /**
   * 例程2.39  Selenium 3 中实现getShadowRoot
   */
  @Test
  public void shadowElementTest() {
    //        打开目标页面
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
    //    获取宿主元素
    WebElement shadowHost = driver.findElement(By.id("get-shadow-root-1"));
    WebElement shadowElement = null;
    //    利用JavaScript获取宿主元素对象
    Object shadowRoot = ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", shadowHost);
    if (shadowRoot instanceof WebElement) {
      // ChromeDriver 95
      shadowElement = (WebElement) shadowRoot;
    } else if (shadowRoot instanceof Map) {
      // ChromeDriver 96+
      // Based on https://github.com/SeleniumHQ/selenium/issues/10050#issuecomment-974231601
      Map<String, Object> shadowRootMap = (Map<String, Object>) shadowRoot;
      String shadowRootKey = (String) shadowRootMap.keySet().toArray()[0];
      String id = (String) shadowRootMap.get(shadowRootKey);
      RemoteWebElement remoteWebElement = new RemoteWebElement();
      remoteWebElement.setParent((RemoteWebDriver) driver);
      remoteWebElement.setId(id);
      shadowElement = remoteWebElement;
    } else {
      Assertions.fail("Unexpected return type for shadowRoot in expandRootElement()");
    }
    //    期望元素文本为"shadow-content-1"
    Assertions.assertThat(shadowElement
                    .findElement(By.cssSelector("#shadow-content-1"))
                    .getText())
            .isEqualTo("shadow-content-1");
  }

}
