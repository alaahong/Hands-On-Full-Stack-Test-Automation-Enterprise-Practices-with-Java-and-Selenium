package cn.ianzhang.selenium.mcp;

/**
 * 集中存放项目常量，便于统一管理与本地化。
 */
public final class Constants {

    private Constants() {
        // utility class
    }

    // --- BrowserService related ---
    public static final String BROWSER_CHROME = "chrome";

    public static final String BROWSER_NOT_STARTED = "Browser not started. Please start a browser first.";
    public static final String BROWSER_STARTED = "Browser started successfully.";
    public static final String ERR_BROWSERNAME_REQUIRED = "Error: browserName is required";
    public static final String BROWSER_ALREADY_OPEN = "Browser is already open.";
    public static final String ERR_UNSUPPORTED_BROWSER_PREFIX = "Error unsupported browser: ";
    public static final String ERR_STARTING_BROWSER_PREFIX = "Error starting browser: ";
    public static final String NAVIGATION_SUCCESS = "Navigation successful.";
    public static final String ERR_NAVIGATING_PREFIX = "Error navigating to url: ";
    public static final String BROWSER_CLOSED_SUCCESS = "Browser closed successfully.";
    public static final String ERR_CLOSING_BROWSER_PREFIX = "Error closing browser: ";
    public static final String ERR_GET_PAGE_SOURCE_PREFIX = "Error getting page source: ";
    public static final String PAGE_SOURCE_RETURNED = "Page source returned";
    public static final String ERR_GET_CURRENT_URL_PREFIX = "Error getting current URL: ";

    // --- BrowserTools related ---
    public static final String TOOL_OPEN_BROWSER = "open_browser";
    public static final String ARG_BROWSER_NAME = "browser_name";

    public static final String TOOL_NAVIGATE_URL = "navigate_url";
    public static final String ARG_URL = "url";

    public static final String TOOL_CLOSE_BROWSER = "close_browser";
    public static final String TOOL_GET_PAGE_SOURCE = "get_page_source";

    public static final String KEY_SUCCESS = "success";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_DATA = "data";

    public static final String KEY_BROWSER = "browser";
    public static final String KEY_PAGE_SOURCE = "pageSource";

    public static final String DESCRIPTION_BROWSER_START = "Launches a new browser instance. Supports Chrome and Firefox browsers";
    public static final String DESCRIPTION_NAVIGATE = "Navigate the browser to a specified URL. The browser must be started first";
    public static final String DESCRIPTION_CLOSE = "Close the browser";
    public static final String DESCRIPTION_GET_PAGE_SOURCE = "Get page source of the current page in the browser";
    public static final String INPUT_DESC_URL = "The complete URL to navigate to (e.g., a fully qualified URL)";
    public static final String INPUT_DESC_BROWSER_NAME = "The name of the browser to open. Supported values: 'chrome', 'firefox'";

    public static final String INTERNAL_SERIALIZATION_ERROR = "{\"success\":false,\"message\":\"internal serialization error\"}";

    // --- SeleniumMcpServer related ---
    public static final String SERVER_NAME = "selenium-mcp-server";
    public static final String SERVER_VERSION = "1.0.0";
    public static final String LOG_MSG_STARTING = "Starting Selenium MCP server, args={}";
    public static final String LOG_MSG_INITIALIZED = "Selenium MCP server initialized";
    public static final String LOG_MSG_SHUTDOWN = "Shutting down Selenium MCP server";
}

