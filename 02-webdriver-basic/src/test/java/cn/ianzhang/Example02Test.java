package cn.ianzhang;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.locators.RelativeLocator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Ian
 */
public class Example02Test extends ChromeBase {
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
            .isEqualTo("chrome://settings/");
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
    Assertions.assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> driver
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
                    .findElements(By.cssSelector("p[id*='locator8']，p[name*='locator8']")))
            .hasSize(2);
  }

  /**
   * 例程2.22  above相对定位
   */
  @Test
  public void relativeLocatorsAboveTest() {
    //    打开目标页面
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/relative_locators.html");
    //    获取单元格8上方的元素，期望依次获取2 1 3三个单元格的元素
    Assertions.assertThat(
            driver
                    .findElements(RelativeLocator
                            .with(By.tagName("td"))
                            .above(By.id("td8")))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining(" "))
    ).isEqualTo("2 1 3");
    //    获取单元格9上方的元素，期望依次获取3 2 1三个单元格的元素
    Assertions.assertThat(
            driver
                    .findElements(RelativeLocator
                            .with(By.tagName("td"))
                            .above(By.id("td9")))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining(" "))
    ).isEqualTo("3 2 1");
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
    //    获取单元格6左侧的元素，期望依次获取4 1 7三个单元格的元素
    Assertions.assertThat(
            driver
                    .findElements(RelativeLocator
                            .with(By.tagName("td"))
                            .toLeftOf(By.id("td6")))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining(" "))
    ).isEqualTo("4 1 7");
    //    获取单元格9左侧的元素，期望依次获取4 1 7三个单元格的元素
    Assertions.assertThat(
            driver
                    .findElements(RelativeLocator
                            .with(By.tagName("td"))
                            .toLeftOf(By.id("td9")))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining(" "))
    ).isEqualTo("7 4 1");
  }

  /**
   * 例程2.25  toRightOf相对定位
   */
  @Test
  public void relativeLocatorsRightTest() {
    //    打开目标页面
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/relative_locators.html");
    //    获取单元格4右侧的元素，期望依次获取6 3 9三个单元格的元素
    Assertions.assertThat(
            driver
                    .findElements(RelativeLocator
                            .with(By.tagName("td"))
                            .toRightOf(By.id("td4")))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining(" "))
    ).isEqualTo("6 3 9");
    //    获取单元格7右侧的元素，期望依次获取9 6 3三个单元格的元素
    Assertions.assertThat(
            driver
                    .findElements(RelativeLocator
                            .with(By.tagName("td"))
                            .toRightOf(By.id("td7")))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining(" "))
    ).isEqualTo("9 6 3");
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
    //    获取单元格1附近的元素，期望依次获取4 7 2 5 8 3 6 9共八个单元格的元素
    Assertions.assertThat(
            driver
                    .findElements(RelativeLocator
                            .with(By.tagName("td"))
                            .near(By.id("td1")))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining(" "))
    ).isEqualTo("4 7 2 5 8 3 6 9");
    //    获取单元格9附近的元素，期望依次获取6 3 8 5 2 7 4 1共八个单元格的元素
    Assertions.assertThat(
            driver
                    .findElements(RelativeLocator
                            .with(By.tagName("td"))
                            .near(By.id("td9")))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining(" "))
    ).isEqualTo("6 3 8 5 2 7 4 1");
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
                    .findElement(RelativeLocator.with(By.tagName("div"))
                            .above(elementYellow)));
    //    获取黄色元素色的下面元素，期望抛出异常
    Assertions.assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(() -> driver
                    .findElement(RelativeLocator.with(By.tagName("div"))
                            .below(elementYellow)));
    //    获取黄色元素的左侧元素，期望抛出异常
    Assertions.assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(() -> driver
                    .findElement(RelativeLocator.with(By.tagName("div"))
                            .toLeftOf(elementYellow)));
    //    获取黄色元素的右侧元素，期望抛出异常
    Assertions.assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(() -> driver
                    .findElement(RelativeLocator.with(By.tagName("div"))
                            .toRightOf(elementYellow)));
    //    获取黄色元素的周围元素
    List<WebElement> elementListNearYellow = driver
            .findElements(RelativeLocator.with(By.tagName("div"))
                    .near(elementYellow));
    //    期望依次获得红色，蓝色以及红黄蓝元素
    Assertions.assertThat(
            elementListNearYellow
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.joining(" "))
    ).isEqualTo("red blue red\n" +
            "yellow\n" +
            "blue");
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

}
