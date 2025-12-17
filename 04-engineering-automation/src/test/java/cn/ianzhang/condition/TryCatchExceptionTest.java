package cn.ianzhang.condition;

import cn.ianzhang.ScreenshotUtil;
import cn.ianzhang.WebDriverBase;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TakesScreenshot;

/**
 * @Author: Ian
 * @Description:
 */
public class TryCatchExceptionTest extends WebDriverBase {

    @Test
    public void tryCatchExceptionTest() {
        try {
            driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/exception.html");
            driver.findElement(By.id("nobody"));
        } catch (NoSuchElementException ex) {
            ScreenshotUtil.takeScreenshot((TakesScreenshot) driver);
        }
    }
}
