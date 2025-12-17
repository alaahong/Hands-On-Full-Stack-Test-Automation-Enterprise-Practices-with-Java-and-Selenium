package cn.ianzhang;


import cn.ianzhang.annotation.Retry;
import cn.ianzhang.extension.RetryExtension;
import cn.ianzhang.watcher.JUnit5ScreenshotOnFailure;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.UsernameAndPassword;

import java.net.URI;
import java.util.Base64;
import java.util.function.Predicate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

/**
 * @Author: Ian
 */

@ExtendWith({RetryExtension.class, JUnit5ScreenshotOnFailure.class})
@Retry(3)
public class Example03BasicAuthTest extends WebDriverBase {
    private WireMockServer mockServer;
    private String expectedAuth;


    @BeforeEach
    void start() {
        mockServer = new WireMockServer(new com.github.tomakehurst.wiremock.core.WireMockConfiguration().dynamicPort());
        mockServer.start();
        String creds = "admin:123456";
        expectedAuth = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());
        // 优先匹配带 Authorization 的请求
        mockServer.stubFor(get(urlEqualTo("/")).withHeader("Authorization", equalTo(expectedAuth)).willReturn(aResponse().withStatus(200).withHeader("Content-Type", "text/plain").withBody("nginx")));
        // 无凭证返回 401
        mockServer.stubFor(get(urlEqualTo("/")).atPriority(10).willReturn(aResponse().withStatus(401).withHeader("WWW-Authenticate", "Basic realm=\"local-test\"").withBody("Unauthorized")));
    }

    @AfterEach
    void stop() {
        if (mockServer != null) mockServer.stop();
    }

    /**
     * 例程3.22  遗留版本的基本认证实现
     */
    @Test
    public void basicAuthWithoutBidiTest() {
        String base = "http://localhost:" + mockServer.port() + "/";
        String urlWithCreds = base.replaceFirst("http://", "http://admin:123456@");
        //    将用户名密码以 username:password@url 的方式显式声明于目标地址
//    driver.get("http://admin:123456@localhost");
        driver.get(urlWithCreds);
        //    认证通过,期望目标页面含有"nginx"
        Assertions.assertThat(driver.getPageSource()).contains("nginx");
    }

    /**
     * 例程3.23  新版基本认证实现
     */
    @Test
    public void basicAuthPassedTest() {
        //    利用Predicate预定义函数实现
        Predicate<URI> uriPredicate = uri -> uri.getHost().contains("localhost");
        //    调用实现了HasAuthentication接口的WebDriver类的register方法定义认证行为
        ((HasAuthentication) driver)
                //    此处传入期望用户名和密码
                .register(uriPredicate, UsernameAndPassword.of("admin", "123456"));
        //    打开测试页面
        driver.get(mockServer.baseUrl());
        //    认证通过,期望目标页面含有"nginx"
        Assertions.assertThat(driver.getPageSource()).contains("nginx");
    }

    @Test
    public void basicAuthFailedTest() {
        //    利用Predicate预定义函数实现
        Predicate<URI> uriPredicate = uri -> uri.getHost().contains("localhost");
        //    调用实现了HasAuthentication接口的WebDriver类的register方法定义认证行为
        ((HasAuthentication) driver)
                //    此处传入虚假用户名和密码
                .register(uriPredicate, UsernameAndPassword.of("admin", "invalid"));
        //    打开测试页面
        driver.get(mockServer.baseUrl());
        //    认证失败,期望目标页面未含有"nginx"
        Assertions.assertThat(driver.getPageSource()).doesNotContain("nginx");
    }

}
