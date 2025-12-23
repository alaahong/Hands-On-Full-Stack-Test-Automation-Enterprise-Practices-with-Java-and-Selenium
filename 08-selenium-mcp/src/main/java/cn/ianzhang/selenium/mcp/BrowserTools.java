package cn.ianzhang.selenium.mcp;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import reactor.core.publisher.Mono;

/**
 * MCP tool definitions that wrap browser operations provided by {@link BrowserService}.
 *
 * <p>Each tool exposes a small JSON input schema and returns a textual JSON result
 * with the shape: { success: boolean, message: string, data?: object }.
 */
@Slf4j
public class BrowserTools {

    private final BrowserService browserService;
    private final ObjectMapper mapper = new ObjectMapper();

    public BrowserTools(BrowserService browserService) {
        this.browserService = browserService;
    }

    /**
     * Define a tool that starts a browser.
     * Input: { "browser_name": "chrome" }
     */
    public McpServerFeatures.AsyncToolSpecification browserStart() {
        String browserStartArgument = Constants.TOOL_OPEN_BROWSER;
        String browserNameArgument = Constants.ARG_BROWSER_NAME;

        McpSchema.JsonSchema inputSchema = new McpSchema.JsonSchema("object",
                Map.of(browserNameArgument, Map.of("type", "string",
                        "description",
                        Constants.INPUT_DESC_BROWSER_NAME)),
                List.of(browserNameArgument), null, null, null);
        Tool tool = McpSchema.Tool.builder().name(browserStartArgument)
                .description(
                        Constants.DESCRIPTION_BROWSER_START)
                .inputSchema(inputSchema).build();

        return McpServerFeatures.AsyncToolSpecification.builder().tool(tool)
                .callHandler((exchange, args) -> {
                    // Log exchange and args to avoid unused-parameter compiler warnings
                    log.debug("browserStart called; exchange={}, args={}", exchange, args);

                    String browserName = (String) args.arguments()
                            .get(browserNameArgument);
                    log.info("tool: start browser request for: {}", browserName);
                    String result = browserService.startBrowser(browserName);
                    boolean isError = result != null && result.startsWith("Error");

                    ObjectNode resp = mapper.createObjectNode();
                    resp.put(Constants.KEY_SUCCESS, !isError);
                    resp.put(Constants.KEY_MESSAGE, result == null ? "" : result);
                    ObjectNode data = mapper.createObjectNode();
                    data.put(Constants.KEY_BROWSER, browserName == null ? "" : browserName);
                    resp.set(Constants.KEY_DATA, data);

                    String payload;
                    try {
                        payload = mapper.writeValueAsString(resp);
                    } catch (Exception e) {
                        log.warn("Failed to serialize response", e);
                        payload = Constants.INTERNAL_SERIALIZATION_ERROR;
                    }

                    return Mono.just(McpSchema.CallToolResult.builder()
                            .addTextContent(payload)
                            .isError(isError).build());
                }).build();
    }

    /**
     * Tool to navigate the active browser to a given URL.
     * Input: { "url": "example.com" }
     */
    public McpServerFeatures.AsyncToolSpecification browserNavigate() {
        McpSchema.JsonSchema inputSchema = new McpSchema.JsonSchema("object",
                Map.of(Constants.ARG_URL, Map.of("type", "string", "description",
                        Constants.INPUT_DESC_URL)),
                List.of(Constants.ARG_URL), null, null, null);

        Tool tool = McpSchema.Tool.builder().name(Constants.TOOL_NAVIGATE_URL).description(
                        Constants.DESCRIPTION_NAVIGATE)
                .inputSchema(inputSchema).build();

        return McpServerFeatures.AsyncToolSpecification.builder().tool(tool)
                .callHandler((exchange, args) -> {
                    // Log exchange and args to avoid unused-parameter compiler warnings
                    log.debug("browserNavigate called; exchange={}, args={}", exchange, args);

                    String url = (String) args.arguments().get(Constants.ARG_URL);
                    log.info("tool: navigate to {}", url);
                    String result = browserService.navigate(url);
                    boolean isError = result != null && result.startsWith("Error");

                    ObjectNode resp = mapper.createObjectNode();
                    resp.put(Constants.KEY_SUCCESS, !isError);
                    resp.put(Constants.KEY_MESSAGE, result == null ? "" : result);
                    ObjectNode data = mapper.createObjectNode();
                    data.put(Constants.ARG_URL, url == null ? "" : url);
                    resp.set(Constants.KEY_DATA, data);

                    String payload;
                    try {
                        payload = mapper.writeValueAsString(resp);
                    } catch (Exception e) {
                        log.warn("Failed to serialize response", e);
                        payload = Constants.INTERNAL_SERIALIZATION_ERROR;
                    }

                    return Mono.just(McpSchema.CallToolResult.builder()
                            .addTextContent(payload)
                            .isError(isError).build());
                }).build();
    }

    /**
     * Tool to close the browser instance.
     * Input: {}
     */
    public McpServerFeatures.AsyncToolSpecification browserClose() {
        McpSchema.JsonSchema inputSchema = new McpSchema.JsonSchema("object",
                Map.of(), List.of(), null, null, null);

        Tool tool = McpSchema.Tool.builder().name(Constants.TOOL_CLOSE_BROWSER)
                .description(Constants.DESCRIPTION_CLOSE).inputSchema(inputSchema)
                .build();

        return McpServerFeatures.AsyncToolSpecification.builder().tool(tool)
                .callHandler((exchange, args) -> {
                    // Log exchange and args to avoid unused-parameter compiler warnings
                    log.debug("browserClose called; exchange={}, args={}", exchange, args);

                    log.info("tool: close browser");
                    String result = browserService.closeBrowser();
                    boolean isError = result != null && result.startsWith("Error");

                    ObjectNode resp = mapper.createObjectNode();
                    resp.put(Constants.KEY_SUCCESS, !isError);
                    resp.put(Constants.KEY_MESSAGE, result == null ? "" : result);

                    String payload;
                    try {
                        payload = mapper.writeValueAsString(resp);
                    } catch (Exception e) {
                        log.warn("Failed to serialize response", e);
                        payload = Constants.INTERNAL_SERIALIZATION_ERROR;
                    }

                    return Mono.just(McpSchema.CallToolResult.builder()
                            .addTextContent(payload)
                            .isError(isError).build());
                }).build();
    }

    /**
     * Tool to fetch the current page source from the browser.
     * Input: {}
     */
    public McpServerFeatures.AsyncToolSpecification getPageSource() {
        McpSchema.JsonSchema inputSchema = new McpSchema.JsonSchema("object",
                Map.of(), List.of(), null, null, null);

        Tool tool = McpSchema.Tool.builder().name(Constants.TOOL_GET_PAGE_SOURCE)
                .description(Constants.DESCRIPTION_GET_PAGE_SOURCE)
                .inputSchema(inputSchema).build();

        return McpServerFeatures.AsyncToolSpecification.builder().tool(tool)
                .callHandler((exchange, args) -> {
                    // Log exchange and args to avoid unused-parameter compiler warnings
                    log.debug("getPageSource called; exchange={}, args={}", exchange, args);

                    log.info("tool: get page source");
                    String result = browserService.getPageSource();
                    boolean isError = result != null && result.startsWith("Error");

                    ObjectNode resp = mapper.createObjectNode();
                    resp.put(Constants.KEY_SUCCESS, !isError);
                    resp.put(Constants.KEY_MESSAGE, isError ? result : Constants.PAGE_SOURCE_RETURNED);
                    if (!isError) {
                        // Put page source into data field; consumers should be aware this can be large
                        ObjectNode data = mapper.createObjectNode();
                        data.put(Constants.KEY_PAGE_SOURCE, result == null ? "" : result);
                        resp.set(Constants.KEY_DATA, data);
                    }

                    String payload;
                    try {
                        payload = mapper.writeValueAsString(resp);
                    } catch (Exception e) {
                        log.warn("Failed to serialize response", e);
                        payload = Constants.INTERNAL_SERIALIZATION_ERROR;
                    }

                    return Mono.just(McpSchema.CallToolResult.builder()
                            .addTextContent(payload)
                            .isError(isError).build());
                }).build();
    }

}
