package cn.ianzhang;

import cn.ianzhang.annotation.Retry;
import cn.ianzhang.extension.RetryExtension;
import cn.ianzhang.watcher.JUnit5ScreenshotOnFailure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * @Author: Ian
 * @Description:
 */

@ExtendWith({RetryExtension.class, JUnit5ScreenshotOnFailure.class})
@Retry(3)
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

    /**
     * 檢查當前環境是否支援 Firefox BiDi，
     * 如果不能正常啟動 BiDi 會話，則跳過依賴該能力的測試。
     */
    private void requireFirefoxBiDiOrSkip() {
        FirefoxOptions options = new FirefoxOptions();
        options.enableBiDi();           // 僅開啟 BiDi，不手動改 prefs
        WebDriver tmp = null;
        try {
            tmp = new FirefoxDriver(options);
        } catch (Exception e) {
            Assumptions.assumeTrue(false,
                    "當前環境無法啟動 Firefox BiDi 會話，跳過 BiDi 測試: " + e.getMessage());
        } finally {
            if (tmp != null) {
                tmp.quit();
            }
        }
    }

    @Disabled
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

    @Disabled
    @Test
    void bidiFirefoxTest() {
        // 如果 BiDi 不可用，這裡會直接標記為 skipped
        requireFirefoxBiDiOrSkip();
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


    @Test
    void bidiIgnoreTest() {
        ChromeOptions options = new ChromeOptions();
        options.enableBiDi();
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
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
    void bidiAcceptTest() {
        ChromeOptions options = new ChromeOptions();
        options.enableBiDi();
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
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
    void bidiDismissTest() {
        ChromeOptions options = new ChromeOptions();
        options.enableBiDi();
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
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
    void bidiDismissAndNotifyTest() {
        ChromeOptions options = new ChromeOptions();
        options.enableBiDi();
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS_AND_NOTIFY);
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
    void bidiAcceptAndNotifyTest() {
        ChromeOptions options = new ChromeOptions();
        options.enableBiDi();
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT_AND_NOTIFY);
        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/interaction.html");
            driver.findElement(By.id("alert-btn")).click();
            Assertions.assertThrows(NoAlertPresentException.class, () -> driver.switchTo().alert());
        } finally {
            if (driver != null) driver.quit();
        }
    }
}
