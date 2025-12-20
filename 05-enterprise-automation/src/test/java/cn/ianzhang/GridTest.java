package cn.ianzhang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author: Ian
 */
@Disabled
public class GridTest {
    private WebDriver driver;

    @BeforeEach
    void setupTest() throws MalformedURLException {

        ChromeOptions options = new ChromeOptions();
//        //禁止gpu渲染
//        options.addArguments("--disable-gpu");
//        //关闭沙盒模式
//        options.addArguments("–-no-sandbox");
//        //关闭默认浏览器检查
//        options.addArguments("--no-default-browser-check");
//        //打开窗口最大化
//        options.addArguments("--start-maximized");
//        //接受不安全的证书
//        options.setAcceptInsecureCerts(Boolean.TRUE);
//        //关闭自动化提示
//        options.setExperimentalOption("excludeSwitches"
//                , Collections.singletonList("enable-automation"));
        driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
    }

    @Test
    void gridTest() {
        driver.get("https://www.ianzhang.cn");
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
