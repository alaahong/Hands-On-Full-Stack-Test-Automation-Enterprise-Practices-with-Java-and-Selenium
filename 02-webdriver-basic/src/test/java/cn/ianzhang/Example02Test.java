package cn.ianzhang;

import cn.ianzhang.annotation.Retry;
import cn.ianzhang.extension.RetryExtension;
import cn.ianzhang.watcher.JUnit5ScreenshotOnFailure;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.Colors;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Ian
 */
@ExtendWith({RetryExtension.class, JUnit5ScreenshotOnFailure.class})
@Retry(3)
public class Example02Test extends WebDriverBase {
    /**
     * 例程2.1  打开百度网站首页
     */
    @Test
    public void openWebPageTest() {
        //    两种方式分别打开https://www.baidu.com
        driver.get("https://www.baidu.com");
        driver.navigate().to("https://www.baidu.com");
    }

    /**
     * 例程2.2  打开重定向页面
     */
    @Test
    public void getTest() {
        //    打开第一个页面get.html
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get.html");
        //    当打开页面操作结束时，期望当前页面为最终重定向的get3.html
        Assertions.assertThat(driver.getCurrentUrl())
                .endsWith("get3.html");
        //    同时判断当前页面不为get.html或get2.html
        Assertions.assertThat(driver.getCurrentUrl())
                .doesNotEndWith("get.html")
                .doesNotEndWith("get2.html");
    }

    /**
     * 例程2.3  完全限定URL
     */
    @Test
    public void fullyQualifiedUrlTest() {
        //    传入正常的网址，但是不符合完全限定URL的要求，期望抛出异常InvalidArgumentException
        Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
                .isThrownBy(() -> driver.get("www.baidu.com"));
        //    传入完全限定URL后，可以正常访问
        driver.get("https://www.baidu.com/");
        //    满足格式要求的参数都可以正常传入
        //    例如Chrome下可以访问chrome://version/
        //    // EDGE浏览器可以访问edge://version/
        driver.get("chrome://version/");
        //    浏览器有自动补全机制，例如以下传入参数被自动追加了斜杠
        driver.get("https://www.baidu.com");
        Assertions.assertThat(driver.getCurrentUrl())
                .isEqualTo("https://www.baidu.com/");
        //    localhost:port这样的参数需特别注意，即使资源不存在，也不会抛出异常
        driver.get("localhost:1234");
        //    但是其他相近情况依然会抛出异常
        Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
                .isThrownBy(() -> driver.get("127.0.0.1:1234"));
        Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
                .isThrownBy(() -> driver.get("localhost"));
    }

    /**
     * 例程2.4  获取当前URL
     */
    @Test
    public void getCurrentUrlTest() {
        //    期望返回值包括浏览器地址栏可见的Get参数
        driver.get("https://www.baidu.com/s?wd=selenium");
        Assertions.assertThat(driver.getCurrentUrl())
                .isEqualTo("https://www.baidu.com/s?wd=selenium");
        //    期望返回值包括浏览器自动补全的斜杠
        driver.get("chrome://settings");
        Assertions.assertThat(driver.getCurrentUrl())
                .contains("settings");
    }

    /**
     * 例程2.5  后退与前进
     */
    @Test
    public void testBackAndForward() {
        //    依次打开三个地址不同的页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#first");
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#second");
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#third");
        //    执行后退操作
        driver.navigate().back();
        //    期望页面后退到第二个地址
        Assertions.assertThat(driver.getCurrentUrl())
                .isEqualTo("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#second");
        //    执行前进操作
        driver.navigate().forward();
        //    期望页面前进到第三个地址
        Assertions.assertThat(driver.getCurrentUrl())
                .isEqualTo("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#third");
        //    点击链接再次跳转到第一个地址
        driver.findElement(By.xpath("//*[@id='firstLink']")).click();
        //    点击链接再次跳转到第二个地址
        driver.findElement(By.xpath("//*[@id='secondLink']")).click();
        //    期望页面为第二个地址
        Assertions.assertThat(driver.getCurrentUrl())
                .isEqualTo("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#second");
        //    执行前进操作
        driver.navigate().forward();
        //    因为当前页面没有执行过后退操作
        //    所以前进操作没有实际行为
        //    期望页面无变化
        Assertions.assertThat(driver.getCurrentUrl())
                .isEqualTo("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#second");
    }

    /**
     * 例程2.6  刷新当前页面
     */
    @Test
    public void refreshTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/refresh.pdf");
        //    刷新当前页面
        driver.navigate().refresh();
    }

    /**
     * 例程2.7  获取页面标题
     */
    @Test
    public void titleTest() {
        //    获取页面的title属性，期望等于“标题”
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/normal_title.html");
        Assertions.assertThat(driver.getTitle()).isEqualTo("标题");
        //    获取页面的title属性，期望为空
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/no_title.html");
        Assertions.assertThat(driver.getTitle()).isEqualTo("");
        Assertions.assertThat(driver.getTitle()).isEmpty();
        //    获取页面的title属性，期望不包含前后的空格，等于“标 题”
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/space_title.html");
        Assertions.assertThat(driver.getTitle()).isEqualTo("标 题");
    }

    /**
     * 例程2.8  获取页面源码
     */
    @Test
    public void pageSourceTest() {
        //    打开目标页面后，执行获取页面源码方法
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/refresh.pdf");
        driver.getPageSource();
    }


    /**
     * 例程2.9  切换窗口
     */
    @Test
    public void windowHandleTest() {
        //    打开目标页面1
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/window-handle-1.html");
        //    获取当前窗口句柄，记为page1WindowHandle
        String page1WindowHandle = driver.getWindowHandle();
        //    期望当前页面标题为"window-handle-1"
        Assertions.assertThat(driver.getTitle()).isEqualTo("window-handle-1");
        //    点击链接打开新窗口
        driver.findElement(By.id("link")).click();
        //    获取全部窗口句柄，依次遍历
        for (String handleValue : driver.getWindowHandles()) {
            //    若句柄不等于page1WindowHandle的值，则根据该句柄值切换至新窗口
            if (!Objects.equals(page1WindowHandle, handleValue)) {
                driver.switchTo().window(handleValue);
                break;
            }
        }
        //    期望当前窗口标题为"window-handle-2"
        Assertions.assertThat(driver.getTitle()).isEqualTo("window-handle-2");
    }

    /**
     * 例程2.10  创建新窗口或标签并切换
     */
    @Test
    public void newWindowTest() {
        //    打开Chrome的设置窗口
        driver.get("chrome://settings");
        //    打开新标签页
        driver.switchTo().newWindow(WindowType.TAB);
        //    打开新窗口
        driver.switchTo().newWindow(WindowType.WINDOW);
    }

    /**
     * 例程2.11 管理窗口
     */
    @Test
    public void windowTest() {
        //打开chrome的设置窗口
        driver.get("chrome://settings");
        //获取当前Window对象
        WebDriver.Window window = driver.manage().window();
        //全屏窗口，相当于在大多数浏览器中按下F11，但是再次执行并不会使浏览器回到非全屏的状态
        window.fullscreen();
        //获取当前坐标，如果有多个屏幕，则以主屏幕的左上角为坐标原点计算当前窗口左上角的位置
        Point point = window.getPosition();
        //获取当前的具体坐标值
        int x = point.getX();
        int y = point.getY();
        //将当前窗口移动至主窗口的左上角
        window.setPosition(new Point(0, 0));
        //最大化窗口， 此时如果getPosition会得到值为0，0的点
        window.maximize();
        //获取当前窗口尺寸
        Dimension dimension = window.getSize();
        //获取当前窗口的高宽值
        int height = dimension.getHeight();
        int width = dimension.getWidth();
        //设置当前窗口的尺寸
        window.setSize(new Dimension(width - 128, height - 128));
    }

    /**
     * 例程2.12  切换iframe
     */
    @Test
    public void frameTest() {
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/frame.html");
        //    坐标从0开始计算，切换至第0个frame
        driver.switchTo().frame(0);
        //    切换回父frame
        driver.switchTo().parentFrame();
        //    切换至id为id的frame
        driver.switchTo().frame("id");
        //    切换回父frame
        driver.switchTo().parentFrame();
        //    切换至name为name的frame
        driver.switchTo().frame("name");
        //    切换回父frame
        driver.switchTo().parentFrame();
        //    切换至XPath值为//iframe[1]的frame
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[1]")));
        //    切换回父frame
        driver.switchTo().parentFrame();
        //    如果当前位置处于最顶层，意味着无父frame，切换无效果
        driver.switchTo().parentFrame();
        //    切换至页面的主内容，一般是第一个frame
        driver.switchTo().defaultContent();
    }


    /**
     * 例程2.13  关闭窗口
     */
    @Test
    public void closeTest() {
        //    打开默认页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/default.html");
        //    创建新窗口
        driver.switchTo().newWindow(WindowType.WINDOW);
        //    创建新标签页
        driver.switchTo().newWindow(WindowType.TAB);
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/default.html");
        //    获取窗口句柄
        String windowHandle = driver.getWindowHandle();
        //    期望当前窗口标题为Default
        Assertions.assertThat(driver.getTitle()).isEqualTo("Default");
        //    关闭当前窗口或标签
        driver.close();
        //    期望抛出NoSuchWindowException异常
        Assertions.assertThatExceptionOfType(NoSuchWindowException.class)
                .isThrownBy(() -> driver.switchTo().window(windowHandle));
        //    退出当前驱动
        driver.quit();
        //    期望抛出NoSuchSessionException异常
        Assertions.assertThatExceptionOfType(NoSuchSessionException.class)
                .isThrownBy(() -> driver.switchTo().window(windowHandle));
    }

    /**
     * 例程2.14  id的定位
     */
    @Test
    public void locatorIdTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/absolute_locators.html");
        //        期望通过id获取到文本值为locator1_1的元素
        Assertions.assertThat(driver
                        .findElement(By.id("locator11"))
                        .getText())
                .isEqualTo("locator1_1");
        //        期望通过id获取到文本值为locator1_2的元素
        Assertions.assertThat(driver
                        .findElement(By.id("locator12"))
                        .getText())
                .isEqualTo("locator1_2");
    }

    /**
     * 例程2.15  link text的定位
     */
    @Test
    public void locatorLinkTextTest() {
        //        打开页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/absolute_locators.html");
        //        期望通过link text获取到文本值为locator2_1的元素，忽略页面源码中元素文本前后的空格
        Assertions.assertThat(driver
                        .findElement(By.linkText("locator2_1"))
                        .getText())
                .isEqualTo("locator2_1");
        //        期望通过link text获取到文本值为locator2_2的元素
        Assertions.assertThat(driver
                        .findElement(By.linkText("locator2_2"))
                        .getText())
                .isEqualTo("locator2_2");
        //        采用大小写不相等的文本进行link text匹配，期望抛出异常
        Assertions.assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> driver
                .findElement(By.linkText("Locator2_2")));
    }

    /**
     * 例程2.16  partial link text的定位
     */
    @Test
    public void locatorPartialLinkTextTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/absolute_locators.html");
        //        通过locator3进行匹配，期望获得两个元素
        Assertions.assertThat(driver
                        .findElements(By.partialLinkText("locator3")))
                .hasSize(2);
        //        通过locator3进行匹配，期望第一个返回元素的文本等于locator3_1
        Assertions.assertThat(driver
                        .findElement(By.partialLinkText("locator3"))
                        .getText())
                .isEqualTo("locator3_1");
        //        通过3_2进行匹配，期望返回本文值为locator3_2的元素
        Assertions.assertThat(driver
                        .findElement(By.partialLinkText("3_2"))
                        .getText())
                .isEqualTo("locator3_2");
    }

    /**
     * 例程2.17  name的定位
     */
    @Test
    public void locatorNameTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/absolute_locators.html");
        //        通过locator4进行匹配，期望返回两个元素
        Assertions.assertThat(driver
                        .findElements(By.name("locator4")))
                .hasSize(2);
        //        通过locator4进行匹配，期望第一个返回元素的文本等于locator4_1
        Assertions.assertThat(driver
                        .findElement(By.name("locator4"))
                        .getText())
                .isEqualTo("locator4_1");
    }

    /**
     * 例程2.18  tag name的定位
     */
    @Test
    public void locatorTagNameTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/absolute_locators.html");
        //        通过locator5进行匹配，期望获取两个元素，浏览器会统一将标签名转换为小写
        Assertions.assertThat(driver
                        .findElements(By.tagName("locator5")))
                .hasSize(2);
        //        采用大写标签名进行匹配，期望获取两个元素，Selenium会忽略大小写进行匹配
        Assertions.assertThat(driver
                        .findElements(By.tagName("LOCATOR5")))
                .hasSize(2);
        //        通过locator5进行匹配，期望第一个返回元素的文本等于locator5_1
        Assertions.assertThat(driver
                        .findElement(By.tagName("locator5"))
                        .getText())
                .isEqualTo("locator5_1");
    }

    /**
     * 例程2.19  XPath的定位
     */
    @Test
    public void locatorXpathTest() {
        //      打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/absolute_locators.html");
        //    期望获取文本值为locator6_1的元素
        Assertions.assertThat(driver
                        .findElement(By.xpath("//*[@id='locator61']"))
                        .getText())
                .isEqualTo("locator6_1");
        //      期望获取文本值为locator6_2的元素
        Assertions.assertThat(driver
                        .findElement(By.xpath("//*[@name='locator62']"))
                        .getText())
                .isEqualTo("locator6_2");
        //    定义XPath表达式，规则为先定位文本值为xpath的h1元素，然后通过轴寻找父元素的父元素，最后返回后代元素中全部p标签元素。期望找到两个
        Assertions.assertThat(driver
                        .findElements(By.xpath("//h1[text()='xpath']/../../descendant::p")))
                .hasSize(2);
    }

    /**
     * 例程2.20  class name的定位
     */
    @Test
    public void locatorClassNameTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/absolute_locators.html");
        //    通过locator71进行定位，期望获取本文值为locator7_1的元素
        Assertions.assertThat(driver
                        .findElement(By.className("locator71"))
                        .getText())
                .isEqualTo("locator7_1");
        //    通过locator72进行定位，期望获取本文值为locator7_2的元素
        Assertions.assertThat(driver
                        .findElement(By.className("locator72"))
                        .getText())
                .isEqualTo("locator7_2");
        //    通过locator7进行定位，期望同时获取两个元素
        Assertions.assertThat(driver
                        .findElements(By.className("locator7")))
                .hasSize(2);
        //    同时通过两个class name进行定位，期望抛出异常
        Assertions.assertThatExceptionOfType(InvalidSelectorException.class).isThrownBy(() -> driver
                .findElement(By.className("locator7 locator71")));
    }

    /**
     * 例程2.21  CSS Selector的定位
     */
    @Test
    public void locatorCssSelectorTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/absolute_locators.html");
        //    通过#locator81进行定位，期望获取文本值为locator8_1的元素
        Assertions.assertThat(driver
                        .findElement(By.cssSelector("#locator81"))
                        .getText())
                .isEqualTo("locator8_1");
        //    通过p[name='locator82']进行定位，期望获取文本值为locator8_2的元素
        Assertions.assertThat(driver
                        .findElement(By.cssSelector("p[name='locator82']"))
                        .getText())
                .isEqualTo("locator8_2");
        //    通过CSS Selector进行定位，表达式为id包含locator8的p标签或者name包含locator8的p标签
        Assertions.assertThat(driver
                        .findElements(By.cssSelector("p[id*='locator8'], p[name*='locator8']")))
                .hasSize(2);
    }

    /**
     * 例程2.22  above相对定位
     */
    @Test
    public void relativeLocatorsAboveTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/relative_locators.html");
        //    获取单元格8上方的元素，期望依次获取5 2 4 6 1 3等单元格的元素
        Assertions.assertThat(
                driver
                        .findElements(RelativeLocator
                                .with(By.tagName("td"))
                                .above(By.id("td8")))
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("5 2 4 6 1 3");
        //    获取单元格9上方的元素，期望依次获取6 3 5 2 4 1等单元格的元素
        Assertions.assertThat(
                driver
                        .findElements(RelativeLocator
                                .with(By.tagName("td"))
                                .above(By.id("td9")))
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("6 3 5 2 4 1");
    }

    /**
     * 例程2.23  below相对定位
     */
    @Test
    public void relativeLocatorsBelowTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/relative_locators.html");
        //    获取单元格2下面的元素，期望依次获取8 7 9三个单元格的元素
        Assertions.assertThat(
                driver
                        .findElements(RelativeLocator
                                .with(By.tagName("td"))
                                .below(By.id("td2")))
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("8 7 9");
        //    获取单元格3下面的元素，期望依次获取9 8 7三个单元格的元素
        Assertions.assertThat(
                driver
                        .findElements(RelativeLocator
                                .with(By.tagName("td"))
                                .below(By.id("td3")))
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("9 8 7");
    }

    /**
     * 例程2.24  toLeftOf相对定位
     */
    @Test
    public void relativeLocatorsLeftTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/relative_locators.html");
        //    获取单元格6左侧的元素，期望依次获取5 2 8 4 1 7等单元格的元素
        Assertions.assertThat(
                driver
                        .findElements(RelativeLocator
                                .with(By.tagName("td"))
                                .toLeftOf(By.id("td6")))
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("5 2 8 4 1 7");
        //    获取单元格9左侧的元素，期望依次获取8 5 2 7 4 1等单元格的元素
        Assertions.assertThat(
                driver
                        .findElements(RelativeLocator
                                .with(By.tagName("td"))
                                .toLeftOf(By.id("td9")))
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("8 5 2 7 4 1");
    }

    /**
     * 例程2.25  toRightOf相对定位
     */
    @Test
    public void relativeLocatorsRightTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/relative_locators.html");
        //    获取单元格4右侧的元素，期望依次获取5 2 8 6 3 9等单元格的元素
        Assertions.assertThat(
                driver
                        .findElements(RelativeLocator
                                .with(By.tagName("td"))
                                .toRightOf(By.id("td4")))
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("5 2 8 6 3 9");
        //    获取单元格7右侧的元素，期望依次获取8 5 2 9 6 3等单元格的元素
        Assertions.assertThat(
                driver
                        .findElements(RelativeLocator
                                .with(By.tagName("td"))
                                .toRightOf(By.id("td7")))
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("8 5 2 9 6 3");
    }

    /**
     * 例程2.26  near相对定位
     */
    @Test
    public void relativeLocatorsNearTest() {
        //    打开目标元素
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/relative_locators.html");
        //    获取单元格5附近的元素，期望依次获取2 8 4 6 1 7 3 9共八个单元格的元素
        Assertions.assertThat(
                driver
                        .findElements(RelativeLocator
                                .with(By.tagName("td"))
                                .near(By.id("td5")))
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("2 8 4 6 1 7 3 9");
        //    获取单元格1附近的元素，期望依次获取4 7 2 5 8共五个单元格的元素
        Assertions.assertThat(
                driver
                        .findElements(RelativeLocator
                                .with(By.tagName("td"))
                                .near(By.id("td1")))
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("4 7 2 5 8");
        //    获取单元格9附近的元素，期望依次获取6 3 8 5 2共五个单元格的元素
        Assertions.assertThat(
                driver
                        .findElements(RelativeLocator
                                .with(By.tagName("td"))
                                .near(By.id("td9")))
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("6 3 8 5 2");
    }

    /**
     * 例程2.27  重叠元素相对定位
     */
    @Test
    public void relativeLocatorsYellowTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/relative_locators_position.html");
        //    获取黄色元素
        WebElement elementYellow = driver.findElement(By.id("yellow"));
        //    获取黄色元素的上方元素，期望抛出异常
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> driver
                        .findElement(RelativeLocator.with(By.id("yellow"))
                                .above(elementYellow)));
        //    获取黄色元素色的下面元素，期望抛出异常
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> driver
                        .findElement(RelativeLocator.with(By.id("yellow"))
                                .below(elementYellow)));
        //    获取黄色元素的左侧元素，期望抛出异常
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> driver
                        .findElement(RelativeLocator.with(By.id("yellow"))
                                .toLeftOf(elementYellow)));
        //    获取黄色元素的右侧元素，期望抛出异常
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> driver
                        .findElement(RelativeLocator.with(By.id("yellow"))
                                .toRightOf(elementYellow)));
        //    获取黄色元素的周围元素
        List<WebElement> elementListNearYellow = driver
                .findElements(RelativeLocator.with(By.id("yellow"))
                        .near(elementYellow));
        //    期望依次获得红色，蓝色以及红黄蓝元素
        Assertions.assertThat(
                elementListNearYellow
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.joining(" "))
        ).isEqualTo("");
    }

    /**
     * 例程2.28  获取全部元素
     */
    @Test
    public void findElementsTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/find_elements.html");
        //    获取包含全className包含vegetable的List<WebElement>
        Assertions.assertThat(driver
                        .findElements(By.className("vegetable"))
                        .stream().map(WebElement::getText)
                        .collect(Collectors.joining(" "))
                )
                .isEqualTo("potato eggplant pepper");
        //    获取id为fruit的全部元素，若只找到一个元素，也会返回List<WebElement>
        Assertions.assertThat(driver
                        .findElements(By.id("fruit"))
                )
                .isInstanceOf(List.class)
                .hasSize(1);
        //    获取id为none的全部元素，因为当前页面不存在这样的元素，所以返回空List
        Assertions.assertThat(driver
                .findElements(By.id("none"))
        ).hasSize(0);
    }

    /**
     * 例程2.29  获取元素
     */
    @Test
    public void findElementTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/find_elements.html");
        //    通过XPath定位直接获取apple元素
        Assertions.assertThat(driver
                .findElement(By.xpath("//ol[@id='fruit']/li[1]"))
                .getText()
        ).isEqualTo("apple");
        //    以下为通过元素获取元素，通过链式调用的方式，直接获取元素的后台元素。
        //    获取id为fruit的元素后，执行二次子查询以获vegetable元素。
        //    因为fruit的后代元素不包括vegetable，所以期望抛出异常。
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> driver
                        .findElement(By.id("fruit"))
                        .findElement(By.id("vegetable"))
                );
        //    获取id为vegetable的元素后，执行二次子查询以获vegetable元素。
        //    因为vegetable的后代元素不包括vegetable自身，所以期望抛出异常。
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> driver
                        .findElement(By.id("vegetable"))
                        .findElement(By.id("vegetable"))
                );
        //    获取id为main的元素后，执行二次子查询以获取vegetable元素。
        //    因为main的后代元素包括vegetable，所以期望获取结果不为空。
        Assertions.assertThat(driver
                .findElement(By.id("main"))
                .findElement(By.id("vegetable"))
        ).isNotNull();
    }

    /**
     * 例程2.30  获取活跃元素
     */
    @Test
    public void findActiveElementTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/find_elements.html");
        //    页面初始化时，默认的活跃元素为body标签元素
        Assertions.assertThat(driver
                        .switchTo()
                        .activeElement()
                        .getTagName())
                .isEqualTo("body");
        //    通过Tab键使当前活跃元素为input标签元素
        driver
                .switchTo()
                .activeElement()
                .sendKeys(Keys.TAB);
        //    期望通过获取活跃元素定位到input标签元素
        Assertions.assertThat(driver
                        .switchTo()
                        .activeElement()
                        .getTagName())
                //Chrome运行为"input"， IE下运行则等于"html"
                .isEqualTo("input");
    }

    /**
     * 例程2.31  操作失效元素
     */
    @Test
    public void findStaleElementTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/find_elements.html");
        //    获取页面所有的li元素
        List<WebElement> webElements = driver
                .findElements(By.tagName("li"));
        //    判断获取到的第一个元素文本为potato
        Assertions.assertThat(webElements
                        .get(0)
                        .getText())
                .isEqualTo("potato");
        //    执行刷新页面
        driver.navigate().refresh();
        //    第二次尝试判断获取到的第一个元素文本为potato，
        //    因为之前获取的元素对象已经不存在与当前页面，
        //    抛出StaleElementReferenceException异常
        Assertions.assertThatExceptionOfType(StaleElementReferenceException.class)
                .isThrownBy(() ->
                        webElements.get(0).getText()
                );
    }

    /**
     * 例程2.32  getTagName
     */
    @Test
    public void getTagNameTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //        页面初始化时，默认的活跃元素的tagName为"body"
        Assertions.assertThat(driver
                        .switchTo()
                        .activeElement()
                        .getTagName())
                .isEqualTo("body");
        //    通过name获取待测元素
        List<WebElement> elementList = driver
                .findElements(By.name("get-tag-name"));
        //        期望通过第一个元素的tagName为"div"
        Assertions.assertThat(elementList.get(0).getTagName())
                .isEqualTo("div");
        //        期望通过第二个元素的tagName为"i-am-a-tag"
        Assertions.assertThat(elementList.get(1).getTagName())
                .isEqualTo("i-am-a-tag");
    }

    /**
     * 例程 2.33  getAccessibleName
     */
    @Test
    public void getAccessibleNameTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //        期望可以获取AccessibleName为label的值
        Assertions.assertThat(
                        driver.findElement(By.id("get-accessible-name-1"))
                                .getAccessibleName())
                .isEqualTo("from label");
        //        期望可以获取AccessibleName为title的值
        Assertions.assertThat(
                        driver.findElement(By.id("get-accessible-name-2"))
                                .getAccessibleName())
                .isEqualTo("from title");
        //       期望可以获取AccessibleName为placeholder的值
        Assertions.assertThat(
                        driver.findElement(By.id("get-accessible-name-3"))
                                .getAccessibleName())
                .isEqualTo("from placeholder");
        //        期望可以获取AccessibleName为aria-placeholder的值
        Assertions.assertThat(
                        driver.findElement(By.id("get-accessible-name-4"))
                                .getAccessibleName())
                .isEqualTo("from aria-placeholder");
        //        期望可以获取AccessibleName为aria-label的值
        Assertions.assertThat(
                        driver.findElement(By.id("get-accessible-name-5"))
                                .getAccessibleName())
                .isEqualTo("from aria-label");
        //        当同时具备多个匹配AccessibleName的条件时，期望获取aria-label的值
        Assertions.assertThat(
                        driver.findElement(By.id("get-accessible-name-6"))
                                .getAccessibleName())
                .isEqualTo("from aria-label");
    }

    /**
     * 例程2.34  getAriaRole
     */
    @Test
    public void getAriaRoleTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //        期望获取AriaRole的值为"alert"
        Assertions.assertThat(
                        driver.findElement(By.id("get-aria-role-1"))
                                .getAriaRole())
                .isEqualTo("alert");
        //        期望获取AriaRole的值为"paragraph"
        Assertions.assertThat(
                        driver.findElement(By.id("get-aria-role-2"))
                                .getAriaRole())
                .isEqualTo("paragraph");
        //        期望获取AriaRole的值为"link"
        Assertions.assertThat(
                        driver.findElement(By.id("get-aria-role-3"))
                                .getAriaRole())
                .isEqualTo("link");
        //        期望获取AriaRole的值为"generic"
        Assertions.assertThat(
                        driver.findElement(By.id("get-aria-role-4"))
                                .getAriaRole())
                .isEqualTo("generic");
    }

    /**
     * 例程2.35  getSize
     */
    @Test
    public void getSizeTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //    获取尺寸等信息
        Dimension dimension = driver.findElement(By.id("get-size-1")).getSize();
        //    打印该矩形的属性值
        System.out.println("Width: " + dimension.getWidth());
        System.out.println("Height: " + dimension.getHeight());
    }

    /**
     * 例程2.36  getLocation
     */
    @Test
    public void getLocationTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //    获取元素的位置，由相对于页面左上角的距离构成
        Point point = driver.findElement(By.id("get-location-1")).getLocation();
        //    打印距离
        System.out.println("X: " + point.getX());
        System.out.println("Y: " + point.getY());
    }

    /**
     * 例程2.37  getRect
     */
    @Test
    public void getRectTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //    获取元素的位置和尺寸等信息
        Rectangle rectangle = driver.findElement(By.id("get-rect-1")).getRect();
        //    打印该矩形的属性值
        System.out.println("X: " + rectangle.getX());
        System.out.println("Y: " + rectangle.getY());
        System.out.println("Width: " + rectangle.getWidth());
        System.out.println("Height: " + rectangle.getHeight());
        //    元素的Rectangle可以视作是Size和Location的集合
        Assertions.assertThat(driver.findElement(By.id("get-rect-1")).getSize())
                .isEqualTo(rectangle.getDimension());
        Assertions.assertThat(driver.findElement(By.id("get-rect-1")).getLocation())
                .isEqualTo(rectangle.getPoint());
    }


    /**
     * 例程2.38  getCssValue
     */
    @Test
    public void getCssValueTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        // 设置当前窗口的宽高分别为1024和768
        driver.manage().window().setSize(new Dimension(1024, 768));
        //        依次获取当前目标元素的宽度和高度
        String width1 = driver.findElement(By.id("get-css-value-1")).getCssValue("width");
        String height1 = driver.findElement(By.id("get-css-value-1")).getCssValue("height");
        //    设置当前窗口的宽高分别为640和480
        driver.manage().window().setSize(new Dimension(640, 480));
        //        再次获取当前目标元素的宽度和高度
        String width2 = driver.findElement(By.id("get-css-value-1")).getCssValue("width");
        String height2 = driver.findElement(By.id("get-css-value-1")).getCssValue("height");
        //    预期当前元素的宽度的值width2小于width1，但是高度的值height1等于height2
        Assertions.assertThat(width1).isGreaterThan(width2);
        Assertions.assertThat(height1).isEqualTo(height2);
        //    预期获取shorthand属性值时，返回值包括"Arial， sans-serif"，但是还包含其他内容
        String shorthandValue = driver.findElement(By.id("get-css-value-1")).getCssValue("font");
        Assertions.assertThat(shorthandValue).contains("Arial, sans-serif").isNotEqualTo("Arial， sans-serif");
        //    预期获取longhand属性值时，返回实际的CSS值为"Arial， sans-serif"
        String longhandValue = driver.findElement(By.id("get-css-value-1")).getCssValue("font-family");
        Assertions.assertThat(longhandValue).isEqualTo("Arial, sans-serif");
    }


    /**
     * 例程2.40  Selenium 4 中实现getShadowRoot
     */
    @Test
    public void getShadowRootTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //    获取open状态的shadowDom
        SearchContext searchContext = driver.findElement(By.id("get-shadow-root-1")).getShadowRoot();
        //    通过SearchContext可以进行针对ShadowDom的二次搜索，因为ChromeDriver实现的缘故，此处尽量采用By.cssSelector进行定位
        Assertions.assertThat(searchContext
                        .findElement(By.cssSelector("#shadow-content-1"))
                        .getText())
                .isEqualTo("shadow-content-1");
        //    Selenium之前通过driver无法获取closed的shadowDom，https://chromium-review.googlesource.com/c/chromium/src/+/5923865
        //    131版本后增加了对于close的支持
        Assertions.assertThatCode(() -> driver
                .findElement(By.id("get-shadow-root-2"))
                .getShadowRoot()
        ).doesNotThrowAnyException();
//    Assertions.assertThatExceptionOfType(NoSuchShadowRootException.class)
//            .isThrownBy(() -> driver
//                    .findElement(By.id("get-shadow-root-2"))
//                    .getShadowRoot()
//            );
        //    如果想通过JavaScript进行获取的话，需要对返回结果进行SearchContext类型的强制转换
        SearchContext jsSearchContext = ((SearchContext) ((JavascriptExecutor) driver).executeScript("return document.getElementById('get-shadow-root-1').shadowRoot"));
        Assertions.assertThat(jsSearchContext
                        .findElement(By.cssSelector("#shadow-content-1"))
                        .getText())
                .isEqualTo("shadow-content-1");
    }

    /**
     * 例程2.41  获取文本
     */
    @Test
    public void getTextTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //        期望获取第一个元素的text为"text1"
        Assertions.assertThat(driver.findElement(By.id("get-text-1")).getText()).isEqualTo("text1");
        //        期望获取第二个元素的text为"text2"，包括该元素内的所有元素的可见内容
        Assertions.assertThat(driver.findElement(By.id("get-text-2")).getText()).isEqualTo("text2");
        //        期望获取第三个元素的text为空""，该元素的value属性的值并不被视为text的内容
        Assertions.assertThat(driver.findElement(By.id("get-text-3")).getText()).isEqualTo("");
        //        期望获取第四个元素的text为空""，label的值并不会被视为该元素text的内容
        Assertions.assertThat(driver.findElement(By.id("get-text-4")).getText()).isEqualTo("");
        //        期望获取第五个元素的每个选项的值以及占位符号的合集
        Assertions.assertThat(driver.findElement(By.id("get-text-5")).getText()).matches("(\\s*)(option1)(\\s*)(option2)(\\s*)");
        //        Select是非闭合标签，若是源码强行闭合，浏览器可能会有特殊的渲染。例如Chrome97版本运行时会得到非空的值，但是Firefox96会得到空""
        Assertions.assertThat(driver.findElement(By.id("get-text-6")).getText()).isNotEmpty();
        //        无option的Select标签为无效标签，可能会被浏览器忽略。例如Chrome97版本运行时该标签不可见
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> driver.findElement(
                        By.id("get-text-7"))
                );
        //        期望获取第八个元素的每个选项的值以及之间占位符号的合集，
        //        与Select内的option标签不同的是，input为自闭合标签，所以文本值并不包含额外的非内容字段，
        //        只是每个选项之间会自动保留换行或者增加空格用以区分内容
        Assertions.assertThat(driver.findElement(By.id("get-text-8"))
                        .getText())
                .isEqualTo("radio1 radio2\nradio3\nradio4");
        //    checkbox采用与上述radio相同的input标签，所以getText的逻辑一致
        Assertions.assertThat(driver.findElement(By.id("get-text-9"))
                        .getText())
                .isEqualTo("checkbox1 checkbox2\ncheckbox3\ncheckbox4");
        //    期望获取按钮的text为"button1"
        Assertions.assertThat(driver.findElement(By.id("get-text-10"))
                        .getText())
                .isEqualTo("button1");
        //    期望获取按钮的text为空""
        Assertions.assertThat(driver.findElement(By.id("get-text-11"))
                        .getText())
                .isEqualTo("");
    }


    /**
     * 例程2.42  获取元素特性
     */
    @Test
    public void getDomAttributeTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //        期望获取readonly的DomAttribute值为"true"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-attribute-1"))
                                .getDomAttribute("readonly"))
                .isEqualTo("true");
        //        期望获取checked的DomAttribute值为"true"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-attribute-2"))
                                .getDomAttribute("checked"))
                .isEqualTo("true");
        //        期望获取required的DomAttribute值为null
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-attribute-2"))
                                .getDomAttribute("required"))
                .isEqualTo(null);
        //        期望获取class的DomAttribute值为"test attribute"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-attribute-3"))
                                .getDomAttribute("class"))
                .isEqualTo("test dom attribute");
        //        期望获取value的DomAttribute值为"default value"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-attribute-4"))
                                .getDomAttribute("value"))
                .isEqualTo("default value");
        //    期望获取defaultValue的DomAttribute值为null，因为不存在此属性
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-attribute-4"))
                                .getDomAttribute("defaultValue"))
                .isEqualTo(null);
        //    模拟用户清除该元素的值并输入"post value"
        driver.findElement(By.id("get-dom-attribute-4")).clear();
        driver.findElement(By.id("get-dom-attribute-4")).sendKeys("post value");
        //        此时期望获取value的DomAttribute值为"default value"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-attribute-4"))
                                .getDomAttribute("value"))
                .isEqualTo("default value");
        //        期望获取自定义属性的值为"data-test-dom-attribute"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-attribute-5"))
                                .getDomAttribute("data-test-dom-attribute"))
                .isEqualTo("data-test-dom-attribute");
        //        期望获取href属性的值为"#"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-attribute-6"))
                                .getDomAttribute("href"))
                .isEqualTo("#");
        //    期望getDomAttribute获取的值等同于执行JavaScript对象的getAttribute方法
        Assertions.assertThat(
                        ((JavascriptExecutor) driver)
                                .executeScript(
                                        "return document.querySelector('#get-dom-attribute-4').getAttribute('value')"
                                ))
                .isEqualTo("default value");
    }

    /**
     * 例程2.43  获取元素属性
     */
    @Test
    public void getDomPropertyTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //        不存在名为readonly的DomProperty，对应的名为readOnly
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-property-1"))
                                .getDomProperty("readonly"))
                .isEqualTo(null);
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-property-1"))
                                .getDomProperty("readOnly"))
                .isEqualTo("true");
        //        期望获取checked属性的值为"true"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-property-2"))
                                .getDomProperty("checked"))
                .isEqualTo("true");
        //        期望获取required属性的值为"false"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-property-2"))
                                .getDomProperty("required"))
                .isEqualTo("false");
        //        不存在名为class的DomProperty，对应的名为className
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-property-3"))
                                .getDomProperty("class"))
                .isEqualTo(null);
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-property-3"))
                                .getDomProperty("className"))
                .isEqualTo("test dom property");
        //        期望获取value以及defaultValue属性的值皆为"default value"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-property-4"))
                                .getDomProperty("value"))
                .isEqualTo("default value");
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-property-4"))
                                .getDomProperty("defaultValue"))
                .isEqualTo("default value");
        //    模拟用户清除该元素的值并输入"post value"
        driver.findElement(By.id("get-dom-property-4")).clear();
        driver.findElement(By.id("get-dom-property-4")).sendKeys("post value");
        //        此时期望获取value为"post value"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-property-4"))
                                .getDomProperty("value"))
                .isEqualTo("post value");
        //        此时期望获取defaultValue属性的值仍为"default value"
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-property-4"))
                                .getDomProperty("defaultValue"))
                .isEqualTo("default value");
        //        期望获取href属性的值为解析网址后的值
        Assertions.assertThat(
                        driver.findElement(By.id("get-dom-property-5"))
                                .getDomProperty("href"))
                .isEqualTo("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html#");
        //    期望getDomProperty获取的值等同于执行JavaScript对象.同名属性
        Assertions.assertThat(
                        ((JavascriptExecutor) driver)
                                .executeScript(
                                        "return document.querySelector('#get-dom-property-3').className"
                                ))
                .isEqualTo("test dom property");
    }

    /**
     * 例程2.44  获取属性
     */
    @Test
    public void getAttributeTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //        期望获取readonly属性的值为"true"
        Assertions.assertThat(
                        driver.findElement(By.id("get-attribute-1"))
                                .getAttribute("readonly"))
                .isEqualTo("true");
        //        期望获取checked属性的值为"true"
        Assertions.assertThat(
                        driver.findElement(By.id("get-attribute-2"))
                                .getAttribute("checked"))
                .isEqualTo("true");
        //        期望获取required属性的值为null
        Assertions.assertThat(
                        driver.findElement(By.id("get-attribute-2"))
                                .getAttribute("required"))
                .isEqualTo(null);
        //        期望获取class属性的值为"test attribute"
        Assertions.assertThat(
                        driver.findElement(By.id("get-attribute-3"))
                                .getAttribute("class"))
                .isEqualTo("test attribute");
        //        期望获取value以及defaultValue属性的值皆为"default value"
        Assertions.assertThat(
                        driver.findElement(By.id("get-attribute-4"))
                                .getAttribute("value"))
                .isEqualTo("default value");
        Assertions.assertThat(
                        driver.findElement(By.id("get-attribute-4"))
                                .getAttribute("defaultValue"))
                .isEqualTo("default value");
        //    模拟用户清除该元素的值并输入"post value"
        driver.findElement(By.id("get-attribute-4")).clear();
        driver.findElement(By.id("get-attribute-4")).sendKeys("post value");
        //        此时期望获取value为"post value"
        Assertions.assertThat(
                        driver.findElement(By.id("get-attribute-4"))
                                .getAttribute("value"))
                .isEqualTo("post value");
        //        期望获取href属性的值为解析网址后的值
        Assertions.assertThat(
                        driver.findElement(By.id("get-attribute-5"))
                                .getAttribute("href"))
                .isEqualTo("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html#");

        //        此时期望获取defaultValue属性的值仍为"default value"
        Assertions.assertThat(
                        driver.findElement(By.id("get-attribute-4"))
                                .getAttribute("defaultValue"))
                .isEqualTo("default value");
    }

    /**
     * 例程2.45  元素是否可见
     */
    @Test
    public void isDisplayedTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //        正常显示的元素执行isDisplayed期望返回true
        Assertions.assertThat(
                        driver.findElement(By.id("is-displayed-1"))
                                .isDisplayed())
                .isTrue();
        //        设置了hidden属性的元素执行isDisplayed期望返回false
        Assertions.assertThat(
                        driver.findElement(By.id("is-displayed-2"))
                                .isDisplayed())
                .isFalse();
        //        设置了hidden属性父元素会导致子元素执行isDisplayed期望返回false
        Assertions.assertThat(
                        driver.findElement(By.id("is-displayed-3"))
                                .isDisplayed())
                .isFalse();
        //        设置了"visibility: hidden"样式的元素执行isDisplayed期望返回false
        Assertions.assertThat(
                        driver.findElement(By.id("is-displayed-4"))
                                .isDisplayed())
                .isFalse();
        //       设置了"display: none"样式的元素执行isDisplayed期望返回false
        Assertions.assertThat(
                        driver.findElement(By.id("is-displayed-5"))
                                .isDisplayed())
                .isFalse();
    }

    /**
     * 例程2.46  元素是否启用
     */
    @Test
    public void isEnabledTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //        正常的input执行isEnabled期望返回true
        Assertions.assertThat(
                        driver.findElement(By.id("is-enabled-1"))
                                .isEnabled())
                .isTrue();
        //        设置disabled的input执行isEnabled期望返回false
        Assertions.assertThat(
                        driver.findElement(By.id("is-enabled-2"))
                                .isEnabled())
                .isFalse();
        //        设置disabled的select执行isEnabled期望返回false
        Assertions.assertThat(
                        driver.findElement(By.id("is-enabled-3"))
                                .isEnabled())
                .isFalse();
        //        设置disabled的textarea执行isEnabled期望返回false
        Assertions.assertThat(
                        driver.findElement(By.id("is-enabled-4"))
                                .isEnabled())
                .isFalse();
        //        因为p标签不可编辑，即使强行设置disabled，该p元素执行isEnabled期望返回true
        Assertions.assertThat(
                        driver.findElement(By.id("is-enabled-5"))
                                .isEnabled())
                .isTrue();
    }

    /**
     * 例程2.47  元素是否被选择
     */
    @Test
    public void isSelectedTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html");
        //        期望未选中的checkbox返回false
        Assertions.assertThat(
                        driver.findElement(By.id("is-selected-1"))
                                .isSelected())
                .isFalse();
        //        期望选中的checkbox返回true
        Assertions.assertThat(
                        driver.findElement(By.id("is-selected-2"))
                                .isSelected())
                .isTrue();
        //        针对有选项选中的Select元素进行判断，期望返回false
        Assertions.assertThat(
                        driver.findElement(By.id("is-selected-3"))
                                .isSelected())
                .isFalse();
        //        期望未选中的option返回false
        Assertions.assertThat(
                        driver
                                .findElement(By.xpath("//select[@id='is-selected-3']/option[1]"))
                                .isSelected())
                .isFalse();
        //        期望选中的option返回true
        Assertions.assertThat(
                        driver
                                .findElement(By.xpath("//select[@id='is-selected-3']/option[2]"))
                                .isSelected())
                .isTrue();
        //        期望多选框中可以存在多个被选中的选项
        Assertions.assertThat(
                        driver
                                .findElement(By.xpath("//select[@id='is-selected-4']/option[2]"))
                                .isSelected())
                .isTrue();
        Assertions.assertThat(
                        driver
                                .findElement(By.xpath("//select[@id='is-selected-4']/option[3]"))
                                .isSelected())
                .isTrue();
        //        期望未选中的radio返回false
        Assertions.assertThat(
                        driver
                                .findElement(By.id("is-selected-5"))
                                .isSelected())
                .isFalse();
        //        期望选中的radio返回true
        Assertions.assertThat(
                        driver
                                .findElement(By.id("is-selected-6"))
                                .isSelected())
                .isTrue();
        //        针对非checkbox、select中的option以及radio按钮以外的元素，
        //        即使有强行设置checked，也会返回false
        //    p标签不具备选中的状态，期望返回false
        Assertions.assertThat(
                        driver
                                .findElement(By.id("is-selected-7"))
                                .isSelected())
                .isFalse();
        //    option不在select内，期望返回false
        Assertions.assertThat(
                        driver
                                .findElement(By.id("is-selected-8"))
                                .isSelected())
                .isFalse();
    }

    /**
     * 例程2.48  单选
     */
    @Test
    public void operationSingleWithoutSelectTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/select.html");
        //    获取目标元素
        WebElement selectElement = driver.findElement(By.id("select-1"));
        //    单选下拉列表，预期返回null
        Assertions.assertThat(selectElement
                        .getDomAttribute("multiple"))
                .isNull();
        //    获取选项option列表
        List<WebElement> optionList = selectElement.findElements(By.tagName("option"));
        //    打印每个option的value
        //    value-select-1-1
        //    value-select-1-2
        //    value-select-1-3
        optionList.forEach(element -> System.out.println(element.getDomAttribute("value")));
        //    打印每个option的text
        //    text-select-1-1
        //    text-select-1-2
        //    text-select-1-3
        optionList.forEach(element -> System.out.println(element.getText()));
        //    打印已选择的选项option的序号以及文本
        //    option 1 is selected with text text-select-1-2
        for (int i = 0; i < optionList.size(); i++) {
            if (optionList.get(i).isSelected()) {
                System.out.println(
                        "option "
                                + i
                                + " is selected with text "
                                + optionList.get(i).getText()
                );
            }
        }
        //选中最后一个选项option
        optionList.get(optionList.size() - 1).click();
        //因为是单选下拉列表，所以期望点击后，最后一个选项被选，之前的倒数第二个选项不再被选
        Assertions.assertThat(optionList.get(optionList.size() - 1)
                        .isSelected())
                .isTrue();
        Assertions.assertThat(optionList.get(optionList.size() - 2)
                        .isSelected())
                .isFalse();
    }


    /**
     * 例程2.49  多选
     */
    @Test
    public void operationMultipleWithoutSelectTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/select.html");
        //    获取目标元素
        WebElement selectElement = driver.findElement(By.id("select-2"));
        //     多选下拉列表，预期返回"true"
        Assertions.assertThat(selectElement
                        .getDomAttribute("multiple"))
                .isEqualTo("true");
        //    获取选项option列表
        List<WebElement> optionList = selectElement.findElements(By.tagName("option"));
        //    打印每个option的value
        //    value-select-2-1
        //    value-select-2-2
        //    value-select-2-3
        optionList.forEach(element -> System.out.println(element.getDomAttribute("value")));
        //    打印每个option的text
        //    text-select-2-1
        //    text-select-2-2
        //    text-select-2-3
        optionList.forEach(element -> System.out.println(element.getText()));
        //    打印已选择的选项option的序号以及文本
        //    option 1 is selected with text text-select-2-1
        for (int i = 0; i < optionList.size(); i++) {
            if (optionList.get(i).isSelected()) {
                System.out.println(
                        "option "
                                + i
                                + " is selected with text "
                                + optionList.get(i).getText()
                );
            }
        }
        //选中最后一个选项option
        optionList.get(optionList.size() - 1).click();
        //因为是多选下拉列表，所以期望点击后，最后一个选项被选，页面初始时已经被选中的选项保持不变
        Assertions.assertThat(optionList.get(optionList.size() - 1)
                        .isSelected())
                .isTrue();
        Assertions.assertThat(optionList.get(0)
                        .isSelected())
                .isTrue();
        //同时选中全部选项，如果已经被选中，则不重复点击
        Actions action = new Actions(driver);
        action.keyDown(Keys.CONTROL).perform();
        optionList.stream().filter(element -> !element.isSelected()).forEach(WebElement::click);
        action.keyUp(Keys.CONTROL).perform();
        //预期全部选项option都被选中
        Assertions.assertThat(optionList).allMatch(WebElement::isSelected);
    }

    /**
     * 例程2.50  借助Selenium单选
     */
    @Test
    public void operationSingleWithSelectTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/select.html");
        //    获取目标元素
        WebElement selectElement = driver.findElement(By.id("select-1"));
        //    转换为select元素
        Select select = new Select(selectElement);
        //    期望单选下拉列表返回false
        Assertions.assertThat(select.isMultiple())
                .isFalse();
        //    获取选项option列表
        List<WebElement> elementList = select.getOptions();
        //    期望通过getOptions方法获取的列表等同于通过标签或取得值
        Assertions.assertThat(elementList)
                .isEqualTo(selectElement.findElements(By.tagName("option")));
        //    获取第一个被选中的选项option
        Assertions.assertThat(select.getFirstSelectedOption())
                .isEqualTo(elementList
                        .stream()
                        .filter(WebElement::isSelected)
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("No options are selected")));
        //    获取全部被选中的选项option
        Assertions.assertThat(select.getAllSelectedOptions())
                .isEqualTo(elementList
                        .stream()
                        .filter(WebElement::isSelected)
                        .collect(Collectors.toList()));
        //    按照序号选中选项option
        select.selectByIndex(0);
        Assertions.assertThat(elementList
                        .get(0)
                        .isSelected())
                .isTrue();
        //    按照可见文本值选中选项option
        select.selectByVisibleText("text-select-1-2");
        Assertions.assertThat(elementList
                        .get(1)
                        .isSelected())
                .isTrue();
        //    按照value选中选项option
        select.selectByValue("value-select-1-3");
        Assertions.assertThat(elementList
                        .get(2)
                        .isSelected())
                .isTrue();
        //    反选选项的各种方法均不支持单选下拉列表的使用
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(select::deselectAll);
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> select.deselectByVisibleText("VisibleText"));
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> select.deselectByIndex(0));
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> select.deselectByValue("Value"));
        //    期望返回初始构建的WebElement元素
        Assertions.assertThat(select.getWrappedElement())
                .isEqualTo(selectElement);
    }

    /**
     * 例程2.51  借助Selenium多选
     */
    @Test
    public void operationMultipleWithSelectTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/select.html");
        //    获取目标元素
        WebElement selectElement = driver.findElement(By.id("select-2"));
        //    转换为select元素
        Select select = new Select(selectElement);
        //    期望多选下拉列表返回true
        Assertions.assertThat(select.isMultiple())
                .isTrue();
        //    获取选项option列表
        List<WebElement> elementList = select.getOptions();
        //    期望通过getOptions方法获取的列表等同于通过标签或取得值
        Assertions.assertThat(elementList)
                .isEqualTo(selectElement.findElements(By.tagName("option")));
        //    取消全部选中的选项option
        select.deselectAll();
        //     没有选中的选项option，期望getAllSelectedOptions返回空列表
        Assertions.assertThat(select.getAllSelectedOptions())
                .isEmpty();
        //    按照序号选中选项option
        select.selectByIndex(0);
        Assertions.assertThat(elementList
                        .get(0)
                        .isSelected())
                .isTrue();
        //    按照序号取消选中选项option
        select.deselectByIndex(0);
        //    按照可见文本值选中选项option
        select.selectByVisibleText("text-select-2-2");
        Assertions.assertThat(elementList
                        .get(1)
                        .isSelected())
                .isTrue();
        //    按照可见文本值取消选中选项option
        select.deselectByVisibleText("text-select-2-2");
        //    按照value选中选项option
        select.selectByValue("value-select-2-3");
        Assertions.assertThat(elementList
                        .get(2)
                        .isSelected())
                .isTrue();
        //    按照value取消选中选项option
        select.deselectByValue("value-select-2-3");
        //    没有选中的选项option，期望getFirstSelectedOption方法报错
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(select::getFirstSelectedOption);
        //    期望选中全部选项
        select.getOptions()
                .stream()
                .filter(option -> !option.isSelected())
                .forEach(WebElement::click);
        Assertions.assertThat(select.getAllSelectedOptions())
                .isEqualTo(select.getOptions());
        //    期望返回初始构建的WebElement元素
        Assertions.assertThat(select.getWrappedElement())
                .isEqualTo(selectElement);
    }

    /**
     * 例程2.52  操作非select元素抛出异常
     */
    @Test
    public void nonSelectTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/select.html");
        //    获取目标元素
        WebElement selectElement = driver.findElement(By.id("select-3"));
        //    对于非select元素，期望初始化时抛出异常
        Assertions.assertThatExceptionOfType(UnexpectedTagNameException.class)
                .isThrownBy(() -> new Select(selectElement));
    }

    /**
     * 例程2.53  操作Color
     */
    @Test
    public void colorTest() {
        //        打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/color.html");
        //  通过CssValue获取元素的背景色
        String rgba = driver.findElement(By.xpath("//td[normalize-space()='#FF0000']/preceding-sibling::td[4]"))
                .getCssValue("background-color");
        //    期望红色的背景色为"rgba(255, 0, 0, 1)", 注意,某些浏览器下可能获取的值为"rgb(255, 0, 0)"
        Assertions.assertThat(rgba).isEqualTo("rgba(255, 0, 0, 1)");
        //    通过颜色的构造方法获取Color对象
        Color red = Color.fromString(rgba);
        //    期望获取十六进制的颜色值
        Assertions.assertThat(red.asHex())
                .isEqualTo("#ff0000");
        //    期望获取RGB的颜色值
        Assertions.assertThat(red.asRgb())
                .isEqualTo("rgb(255, 0, 0)");
        //    期望获取RGBA的颜色值
        Assertions.assertThat(red.asRgba())
                .isEqualTo("rgba(255, 0, 0, 1)");
        //    通过数值构造颜色
        Assertions.assertThat(new Color(255, 0, 0, 1))
                .isEqualTo(red);
        //    通过名称构造颜色
        Assertions.assertThat(Color.fromString("red"))
                .isEqualTo(red);
        //    通过HSL构造颜色
        Assertions.assertThat(Color.fromString("hsl(0, 100%, 50%)"))
                .isEqualTo(red);
        //    通过HSLA构造颜色
        Assertions.assertThat(Color.fromString("hsla(0, 100%, 50%, 1)"))
                .isEqualTo(red);
        //获取awt的颜色类
        Assertions.assertThat(red.getColor())
                .isInstanceOf(java.awt.Color.class);
        //    可以通过Colors类获取颜色的枚举值
        Assertions.assertThat(red)
                .isEqualTo(Colors.RED.getColorValue());
        //    如果颜色转换过程中缺失了透明度,则可能导致断言有误
        Assertions.assertThat(Color.fromString(
                        Colors.TRANSPARENT.getColorValue().asHex()))
                .isEqualTo(Colors.BLACK.getColorValue());
        Assertions.assertThat(Color.fromString(
                        Colors.TRANSPARENT.getColorValue().asRgba()))
                .isEqualTo(Colors.TRANSPARENT.getColorValue());
    }


}
