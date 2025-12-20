package cn.ianzhang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;

/**
 * @Author: Ian
 */
public class WebDriverBase implements WebDriverGetter {
    protected WebDriver driver;

    @BeforeEach
    void setupTest() throws IOException {
        String browser = System.getProperty("BROWSER", System.getenv("BROWSER"));
        if (browser == null || browser.isEmpty()) {
            browser = "chrome";
        }
        switch (browser.toLowerCase()) {
            case "firefox" -> setupFirefox();
            case "edge" -> setupEdge();
            case "chrome-remote" -> setupChromeRemote();
            case "chrome-headless" -> setupChromeHeadless();
//            case "chrome" -> setupChrome();
            default -> setupChrome();
        }
    }

    private void setupChrome() throws IOException {
        ChromeDriverService service = new ChromeDriverService.Builder().withLogLevel(ChromiumDriverLogLevel.WARNING).withLogOutput(System.out).build();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--disable-features=NetworkService,NetworkServiceInProcess,OptimizationGuideModelDownloading,OptimizationHintsFetching,OptimizationTargetPrediction,OptimizationHints");
        options.addArguments("--no-default-browser-check");
//        options.addArguments("--start-maximized");
        options.addArguments("--window-size=1024,768");
        options.addArguments("--lang=zh-CN");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-background-networking");
        options.setAcceptInsecureCerts(Boolean.TRUE);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.enableBiDi();
        Path tempUserDataDir = Files.createTempDirectory("chrome-user-data");
        options.addArguments("--user-data-dir=" + tempUserDataDir.toAbsolutePath());
        driver = new ChromeDriver(service, options);
        configureDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
    }

    private void setupChromeHeadless() {
        ChromeDriverService service = new ChromeDriverService.Builder()
                .withLogLevel(ChromiumDriverLogLevel.WARNING)
                .withLogOutput(System.out)
                .build();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("–-no-sandbox");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--allow-insecure-localhost");
//        options.addArguments("--start-maximized");
        options.addArguments("--window-size=1024,768");
        options.addArguments("--lang=zh-CN");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-background-networking");
        options.setAcceptInsecureCerts(Boolean.TRUE);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.enableBiDi();
        driver = new ChromeDriver(service, options);
        configureDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
    }

    private void setupFirefox() {
        System.setProperty("webdriver.firefox.logfile", "NUL");
        System.setProperty("webdriver.firefox.driver.loglevel", "warn");
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("–-no-sandbox");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--disable-features=OptimizationGuideModelDownloading,OptimizationHintsFetching,OptimizationTargetPrediction,OptimizationHints");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--start-maximized");
        options.addArguments("--lang=zh-CN");
        options.setAcceptInsecureCerts(Boolean.TRUE);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        options.enableBiDi();
        driver = new FirefoxDriver(options);
        configureDriver();
        driver.manage().window().maximize();
    }

    private void setupEdge() {
        EdgeDriverService service = new EdgeDriverService.Builder()
                .withLoglevel(ChromiumDriverLogLevel.WARNING)
                .withLogOutput(System.out)
                .build();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("–-no-sandbox");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--disable-features=OptimizationGuideModelDownloading,OptimizationHintsFetching,OptimizationTargetPrediction,OptimizationHints");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--start-maximized");
        options.addArguments("--lang=zh-CN");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-background-networking");
        options.setAcceptInsecureCerts(Boolean.TRUE);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.enableBiDi();
        driver = new EdgeDriver(service, options);
        configureDriver();
        driver.manage().window().maximize();
    }

    private void setupChromeRemote() throws IOException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
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

        String remoteUrl = System.getProperty("selenium.remote.url", "http://localhost:4444/wd/hub");
        String browserVersion = System.getProperty("browserVersion", "133.0");
        options.setBrowserVersion(browserVersion);
        driver = new RemoteWebDriver(URI.create(remoteUrl).toURL(), options);
        configureDriver();
    }


    private void setupChromeAndroid() throws IOException {
        ChromeOptions options = new ChromeOptions();
        //    https://chromedriver.chromium.org/getting-started/getting-started---android
        options.setExperimentalOption("androidPackage", "com.android.chrome");
        //    driver = new RemoteWebDriver(new URL("http://localhost:9515"), options);
        driver = new ChromeDriver(options);
    }

    private void configureDriver() {
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

    @Override
    public WebDriver getWebDriver() {
        return driver;
    }
}

