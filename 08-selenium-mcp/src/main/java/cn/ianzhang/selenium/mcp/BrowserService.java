package cn.ianzhang.selenium.mcp;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Objects;

/**
 * Simple browser lifecycle manager used by the MCP tools.
 *
 * <p>This class is intentionally lightweight: it holds a single WebDriver instance
 * and exposes synchronized methods for starting, navigating and closing the browser.
 * Synchronization keeps the implementation safe to call from multiple MCP tool handlers
 * in a single JVM process.
 *
 * <p>Notes:
 * - Only Chrome is supported in the reference implementation. Extending to other
 *   browsers (Firefox/Edge) is straightforward by adding corresponding drivers.
 * - Errors are returned as plain text so the MCP tool can forward them directly to the
 *   caller. In a production system you might prefer structured error objects.
 */
@Slf4j
public class BrowserService {

    private WebDriver driver;

    /**
     * Start a browser by name (e.g. "chrome"). The method is synchronized to avoid
     * races when multiple MCP tool calls try to start the browser concurrently.
     *
     * @param browserName name of browser to start (case-insensitive). Currently supports "chrome".
     * @return a human readable result string (success or error message).
     */
    public synchronized String startBrowser(String browserName) {
        if (browserName == null || browserName.isBlank()) {
            return Constants.ERR_BROWSERNAME_REQUIRED;
        }

        String response;
        try {
            if (driver != null) {
                response = Constants.BROWSER_ALREADY_OPEN;
            } else if (Constants.BROWSER_CHROME.equalsIgnoreCase(browserName.trim())) {
                // Create a ChromeDriver using default system configuration. In many CI
                // environments you will want to set up ChromeDriver or Selenium Manager
                // before calling this code.
                driver = new ChromeDriver();
                response = Constants.BROWSER_STARTED;
                log.info("Started ChromeDriver instance");
            } else {
                response = Constants.ERR_UNSUPPORTED_BROWSER_PREFIX + browserName;
                log.warn(response);
            }

        } catch (Exception e) {
            log.error("Error starting browser", e);
            response = Constants.ERR_STARTING_BROWSER_PREFIX + e.getMessage();
        }
        return response;
    }

    /**
     * Navigate the current browser to a URL.
     *
     * @param url full URL to navigate to
     * @return result message
     */
    public synchronized String navigate(String url) {
        String response;
        if (driver == null) {
            response = Constants.BROWSER_NOT_STARTED;
        } else {
            try {
                Objects.requireNonNull(url, "url must not be null");
                driver.get(url);
                response = Constants.NAVIGATION_SUCCESS;
            } catch (Exception e) {
                log.warn("Navigation failed: " + url, e);
                response = Constants.ERR_NAVIGATING_PREFIX + e.getMessage();
            }
        }
        return response;
    }

    /**
     * Close and quit the browser. Ensures the underlying WebDriver reference is cleared
     * even when quit() throws an exception.
     *
     * @return result message
     */
    public synchronized String closeBrowser() {
        String response;
        if (driver == null) {
            response = Constants.BROWSER_NOT_STARTED;
        } else {
            try {
                driver.quit();
                response = Constants.BROWSER_CLOSED_SUCCESS;
                log.info("Browser quit successfully");
            } catch (Exception e) {
                log.warn("Error while closing browser", e);
                response = Constants.ERR_CLOSING_BROWSER_PREFIX + e.getMessage();
            } finally {
                // Always clear the reference to allow a new browser to be created later
                driver = null;
            }
        }
        return response;
    }

    /**
     * Return the page source of the current browser page. If no browser is started
     * an informative message is returned.
     *
     * @return page source or an error message
     */
    public synchronized String getPageSource() {
        String response;
        if (driver == null) {
            response = Constants.BROWSER_NOT_STARTED;
        } else {
            try {
                response = driver.getPageSource();
            } catch (Exception e) {
                log.warn("Failed to get page source", e);
                response = Constants.ERR_GET_PAGE_SOURCE_PREFIX + e.getMessage();
            }
        }
        return response;
    }

    /**
     * Convenience helper: whether a browser instance exists and is usable.
     */
    public synchronized boolean isBrowserStarted() {
        return driver != null;
    }

    /**
     * Convenience helper: get current URL or an informative message when unavailable.
     */
    public synchronized String getCurrentUrl() {
        if (driver == null) return Constants.BROWSER_NOT_STARTED;
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            log.debug("Failed to read current URL", e);
            return Constants.ERR_GET_CURRENT_URL_PREFIX + e.getMessage();
        }
    }

}
