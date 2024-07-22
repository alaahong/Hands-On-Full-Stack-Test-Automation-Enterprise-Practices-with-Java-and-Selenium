package cn.ianzhang;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author Ian
 */
public class HelloSeleniumTest {
  @Test
  public void seleniumTest() {
    WebDriver driver = null;
    try {
      driver = new ChromeDriver();
      driver.get("https://www.ianzhang.cn");
    } finally {
      if (driver != null) {
        driver.quit();
      }
    }
  }
}
