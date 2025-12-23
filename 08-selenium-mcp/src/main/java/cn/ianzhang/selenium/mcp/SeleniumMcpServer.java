package cn.ianzhang.selenium.mcp;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;

/**
 * Entrypoint for a minimal MCP server that exposes a small set of browser tools.
 *
 * <p>The server speaks the Model Context Protocol (MCP) over stdio, which makes it
 * suitable for being launched as a subprocess by models or other MCP-capable hosts.
 * The implementation intentionally keeps the surface area small to focus on the
 * interaction pattern rather than production-grade features.
 */
@Slf4j
public class SeleniumMcpServer {

    public static void main(String[] args) throws Exception {
        log.info(Constants.LOG_MSG_STARTING, (Object) args);

        // Create the tool wrappers backed by a single BrowserService instance.
        BrowserTools tools = new BrowserTools(new BrowserService());

        // Use a JSON mapper compatible with the MCP implementation and stdio transport.
        var transportProvider = new StdioServerTransportProvider(
                McpJsonMapper.createDefault());

        // Build an async MCP server and register the browser-related tools.
        McpAsyncServer server = McpServer.async(transportProvider)
                .serverInfo(Constants.SERVER_NAME, Constants.SERVER_VERSION)
                .capabilities(McpSchema.ServerCapabilities
                        .builder()
                        .tools(true)
                        .logging()
                        .build())
                .tools(tools.browserStart(), tools.browserNavigate(),
                        tools.getPageSource(), tools.browserClose())
                .build();
        log.info(Constants.LOG_MSG_INITIALIZED);
        // Keep the process alive until terminated. Add a shutdown hook to close the server
        // cleanly when the JVM exits (Ctrl+C or SIGTERM).
        CountDownLatch keepAlive = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                log.info(Constants.LOG_MSG_SHUTDOWN);
                server.close();
            } finally {
                keepAlive.countDown();
            }
        }));

        // Block forever until the Java process is terminated
        keepAlive.await();
    }
}
