package cn.ianzhang;

import cn.ianzhang.annotation.Retry;
import cn.ianzhang.extension.RetryExtension;
import cn.ianzhang.watcher.JUnit5ScreenshotOnFailure;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.NoSuchDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.time.Duration;
import java.util.Date;

/**
 * @Author: Ian
 */

@ExtendWith({RetryExtension.class, JUnit5ScreenshotOnFailure.class})
@Retry(3)
public class Example03ExceptionTest extends WebDriverBase {

    /**
     * 例程3.21  Selenium异常模拟
     */
    /***
     * 异步执行JavaScript脚本时,超时会抛出的异常
     */
    @Test
    public void testScriptTimeoutException() {
        //    获取执行脚本的默认超时时间
        Duration defaultDuration = driver.manage().timeouts().getScriptTimeout();
        //    设置执行脚本的超时时间为1秒
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(1));
        //    期望抛出ScriptTimeoutException异常
        Assertions.assertThatExceptionOfType(ScriptTimeoutException.class)
                .isThrownBy(() -> ((JavascriptExecutor) driver)
                        .executeAsyncScript("window.setTimeout(function(){},5000);"));
        //   设置执行脚本的默认超时时间
        driver.manage().timeouts().scriptTimeout(defaultDuration);
    }

    /***
     * 无法正确执行点击操作时抛出的异常,一般是由于目标元素被遮挡
     * Indicates that a click could not be properly executed because the target element was obscured in some way
     */
    @Test
    public void testElementClickInterceptedException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    获取显示点击来源的元素
        WebElement ecie = driver.findElement(By.id("ecie-status"));
        //    点击元素 "ecie-div-1"
        driver.findElement(By.id("ecie-div-1")).click();
        //    期望点击来源得元素显示 "1"
        Assertions.assertThat(ecie.getText())
                .isEqualTo("1");
        //    当点击 "ecie-div-2" 时,因为被可视区域内的重叠元素遮挡,
        //    期望抛出ElementClickInterceptedException异常
        Assertions.assertThatExceptionOfType(ElementClickInterceptedException.class)
                .isThrownBy(() -> driver.findElement(By.id("ecie-div-2")).click());
    }

    /**
     * 元素不可交互时抛出此异常
     */
    @Test
    public void testElementNotInteractableException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    期望抛出异常,因为div无法进行键入操作
        Assertions.assertThatExceptionOfType(ElementNotInteractableException.class)
                .isThrownBy(() -> driver
                        .findElement(By.id("enie-div")).sendKeys("test"));
        //    获取input元素
        WebElement enieInput = driver.findElement(By.id("enie-input"));
        //    尝试键入操作
        enieInput.sendKeys("test");
        //    期望input值为预期值
        Assertions.assertThat(enieInput.getDomProperty("value"))
                .isEqualTo("test");
    }

    /**
     * 无效参数则抛出此异常
     */
    @Test
    public void testInvalidArgumentException() {
        //    期望抛出异常,因为传递了无效的参数
        Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
                .isThrownBy(() -> driver.get("test"));
    }

    /**
     * 跨域操作Cookie时抛出此异常
     */
    @Test
    public void testInvalidCookieDomainException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    期望抛出异常,因为添加了无效的Cookie
        Assertions.assertThatExceptionOfType(InvalidCookieDomainException.class)
                .isThrownBy(() -> driver
                        .manage()
                        .addCookie(new Cookie("n", "v", "d", "p", new Date()))
                );
    }

    /**
     * 针对不能键入或者清除的元素,
     * 既不可编辑的元素,
     * 进行相应操作时抛出此异常。
     */
    @Test
    public void testInvalidElementStateException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    期望抛出异常,因为元素只读
        Assertions.assertThatExceptionOfType(InvalidElementStateException.class)
                .isThrownBy(() -> driver.findElement(By.id("iese-input-1")).clear());
    }

    /**
     * 执行JavaScript脚本的过程中抛出的异常,
     * 可被WebDriver捕获并铺抛出此异常
     */
    @Test
    public void testJavascriptException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    期望抛出异常,因为页面报出JavaScriptException
        Assertions.assertThatExceptionOfType(JavascriptException.class)
                .isThrownBy(() -> ((JavascriptExecutor) driver).executeScript("throw 'error'"));
    }

    /**
     * 由于Selenium客户端与服务端版本不一致,
     * 造成发送了服务端不支持的命令,
     * 即可导致该异常。
     */
    @Test
    public void testUnsupportedCommandException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    获取sessionId
        SessionId sessionId = ((RemoteWebDriver) driver).getSessionId();
        //    期望抛出异常,因为发送了无效命令
        Assertions.assertThatExceptionOfType(UnsupportedCommandException.class)
                .isThrownBy(() -> ((RemoteWebDriver) driver)
                        .getCommandExecutor()
                        .execute(new Command(sessionId, "UnsupportedCommand")));
    }

    /**
     * 移动超出窗口时抛出异常
     */
    @Test
    public void testMoveTargetOutOfBoundsException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    获取窗口尺寸
        Dimension dimension = driver.manage().window().getSize();
        //    获取目标元素
        WebElement targetElement = driver.findElement(By.id("mtobe-p"));
        //    期望抛出异常,因为移动超出窗口
        Assertions.assertThatExceptionOfType(MoveTargetOutOfBoundsException.class)
                .isThrownBy(() -> new Actions(driver).dragAndDropBy(targetElement
                                , dimension.getWidth() + 100
                                , dimension.getHeight() + 100)
                        .perform());
    }

    /**
     * 没有Alert可以被切换时抛出异常
     */
    @Test
    public void testNoAlertPresentException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    期望抛出异常,因为没有Alert可以被切换
        Assertions.assertThatExceptionOfType(NoAlertPresentException.class)
                .isThrownBy(() -> driver.switchTo().alert());

    }

    /**
     * 当元素不存在时抛出此异常
     */
    @Test
    public void testNoSuchElementException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    期望抛出异常,因为没有目标元素
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> driver.findElement(By.id("I'm not here")));
    }

    /**
     * 当frame不存在时抛出此异常
     */
    @Test
    public void testNoSuchFrameException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    期望抛出异常,因为没有目标frame
        Assertions.assertThatExceptionOfType(NoSuchFrameException.class)
                .isThrownBy(() -> driver.switchTo().frame("I'm not here"));
    }

    /**
     * 目标Session不存在时抛出此异常
     */
    @Test
    public void testNoSuchSessionException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    期望抛出异常,因为没有目标session
        Assertions.assertThatExceptionOfType(NoSuchSessionException.class)
                .isThrownBy(() -> {
                    driver.quit();
                    driver.manage().window().getSize();
                });
    }

    /**
     * Shadow Root不存在时抛出此异常
     */
    @Test
    public void testNoSuchShadowRootException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    期望抛出异常,因为没有目标ShadowRoot元素
        Assertions.assertThatExceptionOfType(NoSuchShadowRootException.class)
                .isThrownBy(() -> {
                    driver.findElement(By.id("nssre-p")).getShadowRoot();
                });
    }

    /**
     * Window不存在时抛出此异常
     */
    @Test
    public void testNoSuchWindowException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    期望抛出异常,因为没有目标window
        Assertions.assertThatExceptionOfType(NoSuchWindowException.class)
                .isThrownBy(() -> driver.switchTo().window("I'm not here"));
    }

    /**
     * 无法创建Driver实例时抛出此异常
     */
    @Test
    public void testNoSuchDriverException() {
        //    新建ChromeOptions实例
        ChromeOptions options = new ChromeOptions();
        //    设置无效可执行文件路径
        options.setBinary("I'm not here");
        //    期望抛出异常
        Assertions.assertThatExceptionOfType(NoSuchDriverException.class)
                .isThrownBy(() -> new ChromeDriver(options));
    }

    /**
     * 当操作的目标元素不再留存于当前页面的DOM树时抛出此异常
     */
    @Test
    public void testStaleElementReferenceException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    获取目标元素
        WebElement targetElement = driver.findElement(By.id("sere-p"));
        //    刷新页面
        driver.navigate().refresh();
        //    期望抛出异常,因为之前获取的元素引用已经不存在于当前页面
        Assertions.assertThatExceptionOfType(StaleElementReferenceException.class)
                .isThrownBy(targetElement::getText);

    }

    /**
     * 操作超时时抛出此异常
     */
    @Test
    public void testTimeoutException() {
        //    获取页面加载超时时间
        Duration defaultDuration = driver.manage().timeouts().getPageLoadTimeout();
        //    设置极短的页面加载超时时间
        driver.manage().timeouts().pageLoadTimeout(Duration.ofNanos(1L));
        //    期望抛出异常,因为页面无法在预期时间内完成加载
        Assertions.assertThatExceptionOfType(TimeoutException.class)
                .isThrownBy(() -> driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html"));
        //    设置页面加载超时时间
        driver.manage().timeouts().pageLoadTimeout(defaultDuration);
    }

    /**
     * 当解析到无效的元素选择器时抛出此异常
     */
    @Test
    public void testInvalidSelectorException() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
        //    期望抛出异常,因为无效的元素选择器
        Assertions.assertThatExceptionOfType(InvalidSelectorException.class)
                .isThrownBy(() -> driver.findElement(By.xpath("abs(-1)")));
    }

    /**
     * 可以经由修改Alert策略引起
     */
    @Test
    public void testUnhandledAlertException() {
        //    强制退出当前driver
        if (driver != null) driver.quit();
        //    新建ChromeOptions实例
        ChromeOptions options = new ChromeOptions();
        //    设置默认Alert策略
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        //    初始化ChromeDriver实例
        driver = new ChromeDriver(options);
        //    手工发起弹窗
        ((JavascriptExecutor) driver).executeScript("alert()");
        //    期望抛出异常,因为存在未处理的Alert
        Assertions.assertThatExceptionOfType(UnhandledAlertException.class)
                .isThrownBy(() -> ((JavascriptExecutor) driver).executeScript("alert()"));
    }

  /**
   * 默认的异常类型
   */
  @Test
  public void testWebDriverException() {
    //    打开目标页面
    driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html");
    //    获取目标元素
    driver.findElement(By.id("se-btn")).click();
    //    期望抛出异常,因为元素不可见
    Assertions.assertThatExceptionOfType(WebDriverException.class)
            .isThrownBy(() -> driver.findElement(By.id("se-input"))
                    .getScreenshotAs(OutputType.BASE64));
  }

}
