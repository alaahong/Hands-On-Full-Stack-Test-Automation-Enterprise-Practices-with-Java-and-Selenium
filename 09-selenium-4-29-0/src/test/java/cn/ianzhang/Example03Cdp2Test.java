package cn.ianzhang;


import cn.ianzhang.annotation.Retry;
import cn.ianzhang.extension.RetryExtension;
import cn.ianzhang.watcher.JUnit5ScreenshotOnFailure;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * @Author: Ian
 */

@ExtendWith({RetryExtension.class, JUnit5ScreenshotOnFailure.class})
@Retry(3)
public class Example03Cdp2Test extends  ChromeRemoteBase {

    @Test
    public void test() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.setBrowserVersion("85.0");       // 或者具体版本，如 "128.0"
        //  本机上跑则是 localhost，远程则替换为主机地址
        URL url = URI.create("http://localhost:4444/wd/hub").toURL();
        WebDriver driver = new RemoteWebDriver(url, options);
        try {
            driver.get("https://www.bing.com");
            Assertions.assertThat(driver.getTitle()).contains("Bing");
        } finally {
            driver.quit();
        }
    }

}
