package cn.ianzhang;

import cn.ianzhang.annotation.Retry;
import cn.ianzhang.extension.RetryExtension;
import cn.ianzhang.watcher.JUnit5ScreenshotOnFailure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith({RetryExtension.class, JUnit5ScreenshotOnFailure.class})
@Retry(3)
public class HelloSeleniumTest {
    //  声明日志
    Logger log = LoggerFactory.getLogger(HelloSeleniumTest.class);

    /**
     * 测试打开目标网站
     */
    @Test
    public void seleniumTest() {
        //    定义WebDriver
        WebDriver driver = null;
        try {
            //      初始化ChromeDriver
            driver = new ChromeDriver();
            //      打开目标网站
            driver.get("https://www.ianzhang.cn");
        } catch (Exception e) {
            //      若有异常，则拦截并打印异常信息
            log.error(e.getMessage());
        } finally {
            //      若driver不为空则执行quit方法
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
