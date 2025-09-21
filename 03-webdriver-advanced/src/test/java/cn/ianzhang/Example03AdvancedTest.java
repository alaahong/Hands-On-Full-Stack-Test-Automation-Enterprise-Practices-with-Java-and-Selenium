package cn.ianzhang;


import cn.ianzhang.annotation.Retry;
import cn.ianzhang.extension.RetryExtension;
import cn.ianzhang.watcher.JUnit5ScreenshotOnFailure;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.devtools.events.CdpEventTypes;
import org.openqa.selenium.devtools.events.ConsoleEvent;
import org.openqa.selenium.devtools.events.DomMutationEvent;
import org.openqa.selenium.devtools.idealized.log.Log;
import org.openqa.selenium.logging.HasLogEvents;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.http.Contents;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.Route;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @Author: Ian
 */

@ExtendWith({RetryExtension.class, JUnit5ScreenshotOnFailure.class})
@Retry(3)
public class Example03AdvancedTest extends EdgeBase {
    /**
     * 例程3.24  HasLogEvents完成事件监控
     */
    @Test
    public void mutationObservationTest() throws InterruptedException {
        //    定义原子引用记录事件
        AtomicReference<DomMutationEvent> event = new AtomicReference<>();
        //    定义线程等待数量
        CountDownLatch latch = new CountDownLatch(1);
        //    调用实现了HasLogEvents接口的WebDriver类的onLogEvent方法
        ((HasLogEvents) driver).onLogEvent(CdpEventTypes.domMutation(mutation -> {
            event.set(mutation);
            latch.countDown();
        }));
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/bidi.html");
        //    获取目标元素
        WebElement moInput = driver.findElement(By.id("mo-input"));
        //    设置目标元素为只读元素
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].setAttribute('readonly', 'readonly');", moInput);
        //    提取事件集合
        Assertions.assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        //    期望事件返回变动属性名为"readonly"
        Assertions.assertThat(event.get().getAttributeName())
                .isEqualTo("readonly");
        //    期望事件返回变动当前值为"readonly"
        Assertions.assertThat(event.get().getCurrentValue())
                .isEqualTo("readonly");
        //    期望事件返回变动原始值为"null"
        Assertions.assertThat(event.get().getOldValue())
                .isEqualTo("null");
    }

    /**
     * 例程3.25  通过LogEntries获取日志
     */
    @Test
    public void consoleLogWithoutDevToolsTest() {
        //    打开目标网站
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/bidi.html");
        //    点击页面所有log按钮
        driver.findElements(By.name("console-name"))
                .forEach(WebElement::click);
        //    动态设置日志类型,获取浏览器记录
        LogEntries entries = driver.manage().logs().get(LogType.BROWSER);
        //    进行断言
        Assertions.assertThat(entries)
                //            以"WARNING"类型获取到"console.warn"的记录
                .anyMatch(e -> e.getLevel().getName().equals("WARNING")
                        && e.getMessage().contains("console.warn"))
                //            以"SEVERE"类型获取到"console.warn"的记录
                .anyMatch(e -> e.getLevel().getName().equals("SEVERE")
                        && e.getMessage().contains("console.error"))
                //            无法获取"console.trace"的记录
                .noneMatch(e -> e.getMessage().contains("console.trace"))
                //            无法获取"console.log"的记录
                .noneMatch(e -> e.getMessage().contains("console.log"))
                //            无法获取"console.info的记录
                .noneMatch(e -> e.getMessage().contains("console.info"))
                //            无法获取"console.debug"的记录
                .noneMatch(e -> e.getMessage().contains("console.debug"));
    }

    /**
     * 例程3.26  通过ConsoleEvent获取日志
     */
    @Test
    public void consoleLogDevToolsTest() {
        //    定义临时队列存储命令行事件
        List<ConsoleEvent> consoleEventList = new LinkedList<>();
        //    通过HasDevTools接口获取DevTools实例
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        //    创建会话
        devTools.createSession();
        //    获取日志对象
        Log log = devTools.getDomains().log();
        //    发送日志开启命令
        devTools.send(log.enable());
        //    定义日志事件消费者
        Consumer<ConsoleEvent> consoleEventConsumer = consoleEventList::add;
        //    监听ConsoleEvent并加入存储队列
        devTools.getDomains().events().addConsoleListener(consoleEventConsumer);
        //    打开目标网站
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/bidi.html");
        //    点击页面所有log按钮
        driver.findElements(By.name("console-name"))
                .forEach(WebElement::click);
        //    进行断言
        Assertions.assertThat(consoleEventList)
                //            getMessages方法无法获取占位符实际值,
                //            占位符参数的实际值,需要通过getArgs另行获取
                .anyMatch(e ->
                        e.getType().equals("log")
                                && e.getMessages().stream()
                                .anyMatch(ele -> ele.equals("format %s"))
                )
                //            getMessages方法可以获取参数替换后的记录值,
                //            也可以通过getArgs获取全部参数
                .anyMatch(e ->
                        e.getType().equals("log")
                                && e.getMessages().stream()
                                .anyMatch(ele -> ele.equals("%s log"))
                )
                //            getMessages方法可以获取参数替换后的记录值,
                //            也可以通过getArgs获取全部参数
                .anyMatch(e ->
                        e.getType().equals("log")
                                && e.getMessages().stream()
                                .anyMatch(ele -> ele.equals("format log"))
                )
                //            以"info"类型获取到"console.info"的记录
                .anyMatch(e ->
                        e.getType().equals("info")
                                && e.getMessages().stream()
                                .anyMatch(ele -> ele.equals("console.info")))
                //            以"error"类型获取到"console.error"的记录
                .anyMatch(e ->
                        e.getType().equals("error")
                                && e.getMessages().stream()
                                .anyMatch(ele -> ele.equals("console.error")))
                //            以"debug"类型获取到"console.debug"的记录
                .anyMatch(e ->
                        e.getType().equals("debug")
                                && e.getMessages().stream()
                                .anyMatch(ele -> ele.equals("console.debug")))
                //            以"trace"类型获取到"console.trace"的记录
                .anyMatch(e ->
                        e.getType().equals("trace")
                                && e.getMessages().stream()
                                .anyMatch(ele -> ele.equals("console.trace")))
                //            以"warning"类型获取到"console.warn"的记录
                .anyMatch(e ->
                        e.getType().equals("warning")
                                && e.getMessages().stream()
                                .anyMatch(ele -> ele.equals("console.warn")));
    }

    /**
     * 例程3.27  JavaScriptException
     */
    @Test
    public void javascriptExceptionTest() {
        //    定义临时队列存储JavascriptException
        List<JavascriptException> jsExceptionsList = new LinkedList<>();
        //    通过HasDevTools接口获取DevTools实例
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        //    创建会话
        devTools.createSession();
        //    定义JavascriptException消费者
        Consumer<JavascriptException> addEntry = jsExceptionsList::add;
        //    监听JavascriptException并加入存储队列
        devTools.getDomains().events().addJavascriptExceptionListener(addEntry);
        //    打开目标网站
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/bidi.html");
        List<WebElement> exceptionList = driver.findElements(By.name("exception-name"));
        //   点击所有按钮
        exceptionList.forEach(WebElement::click);
        //    进行断言
        Assertions.assertThat(jsExceptionsList)
                //            期望异常信息包括"Test For JavascriptException 1"
                .anyMatch(e -> e.getMessage().contains("Test For JavascriptException 1"))
                //            期望异常信息包括"Test For JavascriptException 2"
                .anyMatch(e -> e.getMessage().contains("Test For JavascriptException 2"));
    }

    /**
     * 例程3.28  NetworkInterceptor
     */
    @Test
    public void networkInterceptionTest() {
        //    创建网络拦截器
        try (NetworkInterceptor networkInterceptor = new NetworkInterceptor(
                driver,
                //    设置拦截目标站点
                Route.matching(req -> req.getUri().contains("google.com"))
                        //    强制返回指定文本
                        .to(() -> req -> new HttpResponse()
                                .setStatus(200)
                                .setContent(Contents.utf8String("Hello, Ian!"))));) {


            //    打开目标站点
            driver.get("https://google.com");
            //    获取页面源码
            String source = driver.getPageSource();
            //    期望包含指定文本
            Assertions.assertThat(source).contains("Ian!");
        }
        //    再次创建网络拦截器
        try (NetworkInterceptor networkInterceptor = new NetworkInterceptor(
                driver,
                //            设置拦截目标站点
                Route.matching(req -> req.getUri().contains("github.com"))
                        //    强制返回指定内容
                        .to(() -> req -> new HttpResponse()
                                //  设置响应状态
                                .setStatus(200)
                                //  设置相应content-type
                                .setHeader("content-type", "application/json; charset=utf-8")
                                //  设置相应内容
                                .setContent(Contents.utf8String("Hello, API!"))))) {
            //    打开目标接口
            driver.get("https://api.github.com/users?since=1");
            //    获取页面源码
            String sourceApi = driver.getPageSource();
            //    期望包含指定文本
            Assertions.assertThat(sourceApi).contains("API!");
        }
    }
}
