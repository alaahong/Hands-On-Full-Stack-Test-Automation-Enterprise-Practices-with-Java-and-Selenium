package cn.ianzhang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * @Author: Ian
 * @Date: 2024/9/2 22:30
 * @Description:
 */
public class AlertTest {
  @Test
  void noBidiChromeTest() {
    ChromeOptions options = new ChromeOptions();
    WebDriver driver = new ChromeDriver(options);
    try {
      driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/interaction.html");
      driver.findElement(By.id("alert-btn")).click();
      Assertions.assertEquals(driver.switchTo().alert().getText(), "I am an alert");
    } finally {
      if (driver != null) driver.quit();
    }
  }


  @Test
  void bidiTest() {
    ChromeOptions options = new ChromeOptions();
    options.enableBiDi();
    WebDriver driver = new ChromeDriver(options);
    try {
      driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/interaction.html");
      driver.findElement(By.id("alert-btn")).click();
      Assertions.assertThrows(NoAlertPresentException.class, () -> driver.switchTo().alert());
    } finally {
      if (driver != null) driver.quit();
    }
  }

  @Test
  void noBidiFirefoxTest() {
    FirefoxOptions options = new FirefoxOptions();
    WebDriver driver = new FirefoxDriver(options);
    try {
      driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/interaction.html");
      driver.findElement(By.id("alert-btn")).click();
      Assertions.assertEquals(driver.switchTo().alert().getText(), "I am an alert");
    } finally {
      if (driver != null) driver.quit();
    }
  }

  @Test
  void bidiFirefoxTest() {
    FirefoxOptions options = new FirefoxOptions();
    options.enableBiDi();
    WebDriver driver = new FirefoxDriver(options);
    try {
      driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/interaction.html");
      driver.findElement(By.id("alert-btn")).click();
      Assertions.assertEquals(driver.switchTo().alert().getText(), "I am an alert");
    } finally {
      if (driver != null) driver.quit();
    }
  }
}
