package cn.ianzhang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;

public class ChromeRemoteBase {
    protected WebDriver driver;

    @BeforeEach
    void setupRemote() {
        // Default to Selenoid root path ('/') to avoid websocket path issues
        String remoteUrlStr = System.getProperty("selenium.remote.url", "http://localhost:4444/");
        String browserVersion = System.getProperty("browserVersion", "");

        ChromeOptions options = new ChromeOptions();
        // 无头与稳定性参数（兼容旧版 Chrome，例如 85）
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--start-maximized");
        options.addArguments("--lang=zh-CN");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-background-networking");
        options.setAcceptInsecureCerts(true);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        if (!browserVersion.isEmpty()) {
            options.setBrowserVersion(browserVersion);
        }

        try {
            URI address = new URL(remoteUrlStr).toURI();
            driver = RemoteWebDriver.builder()
                    .oneOf(options)
                    .address(address)
                    .build();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException("非法的远端 URL: " + remoteUrlStr, e);
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
