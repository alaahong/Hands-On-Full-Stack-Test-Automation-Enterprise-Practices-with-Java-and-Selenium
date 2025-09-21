package cn.ianzhang;


import cn.ianzhang.annotation.Retry;
import cn.ianzhang.extension.RetryExtension;
import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Files;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.support.ui.Sleeper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Ian
 */
@ExtendWith(RetryExtension.class)
@Retry(3)
public class Example03Test extends EdgeBase {

    /**
     * 例程3.1  发送键位
     */
    @Test
    public void keyboardSampleTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/keyboard.html");
        //    获取id为"keyboard-input-text"的元素
        WebElement keyboardElement1 = driver.findElement(By.id("keyboard-input-text"));
        //    输入字符串 "a basic text input"
        keyboardElement1.sendKeys("a basic text input");
        //    sendKeys输入的是value并非text,所以期望获取元素的文本为空""
        Assertions.assertThat(keyboardElement1.getText())
                .isEmpty();
        //    期望获取的元素value为"a basic text input"
        Assertions.assertThat(keyboardElement1.getAttribute("value"))
                .isEqualTo("a basic text input");
        //    获取id为"keyboard-textarea"的元素
        WebElement keyboardElement2 = driver.findElement(By.id("keyboard-textarea"));
        //    输入字符串"textarea input"
        keyboardElement2.sendKeys("textarea input");
        //    sendKeys输入的是value并非text,所以期望获取元素的文本为空""
        Assertions.assertThat(keyboardElement2.getText())
                .isEmpty();
        //    期望获取的元素value为"textarea input"
        Assertions.assertThat(keyboardElement2.getAttribute("value"))
                .isEqualTo("textarea input");
        //    对于不可交互的元素,强行sendKeys,期望抛出异常
        Assertions.assertThatExceptionOfType(ElementNotInteractableException.class)
                .isThrownBy(() -> driver
                        .findElement(By.id("span"))
                        .sendKeys("span"));
        //    并非所有具有value属性的元素都可以sendKeys,例如针对progress执行sendKeys,期望抛出异常
        Assertions.assertThatExceptionOfType(ElementNotInteractableException.class)
                .isThrownBy(() -> driver
                        .findElement(By.id("progress"))
                        .sendKeys("100"));
        //    对于disabled的可交互元素,期望抛出异常
        Assertions.assertThatExceptionOfType(ElementNotInteractableException.class)
                .isThrownBy(() -> driver
                        .findElement(By.id("keyboard-input-text-disabled"))
                        .sendKeys("keyboard-input-text-disabled"));
        //    sendKeys不支持输入null,期望抛出异常
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> driver
                        .findElement(By.id("keyboard-input-text"))
                        .sendKeys(null));
    }

    /**
     * 例程3.2  录入元素
     */
    @Test
    public void keyboardInputTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/keyboard.html");
        //    期望获取元素的value为"text"
        driver.findElement(By.id("text")).sendKeys("text");
        Assertions.assertThat(driver.findElement(By.id("text"))
                        .getAttribute("value"))
                .isEqualTo("text");
        //    期望获取元素的value为"test@ianzhang.cn"
        driver.findElement(By.id("email")).sendKeys("test@ianzhang.cn");
        Assertions.assertThat(driver.findElement(By.id("email"))
                        .getAttribute("value"))
                .isEqualTo("test@ianzhang.cn");
        //    期望获取元素的value为"12345"
        driver.findElement(By.id("number")).sendKeys("12345");
        Assertions.assertThat(driver.findElement(By.id("number"))
                        .getAttribute("value"))
                .isEqualTo("12345");
        //    期望获取元素的value为"search"
        driver.findElement(By.id("search")).sendKeys("search");
        Assertions.assertThat(driver.findElement(By.id("search"))
                        .getAttribute("value"))
                .isEqualTo("search");
        //    期望获取元素的value为"tel"
        driver.findElement(By.id("tel")).sendKeys("tel");
        Assertions.assertThat(driver.findElement(By.id("tel"))
                        .getAttribute("value"))
                .isEqualTo("tel");
        //    期望获取元素的value为"url"
        driver.findElement(By.id("url")).sendKeys("url");
        Assertions.assertThat(driver.findElement(By.id("url"))
                        .getAttribute("value"))
                .isEqualTo("url");
        //    期望获取元素的value为"password"
        driver.findElement(By.id("password")).sendKeys("password");
        Assertions.assertThat(driver.findElement(By.id("password"))
                        .getAttribute("value"))
                .isEqualTo("password");
        //    期望获取元素的value为"#ff0000"
        driver.findElement(By.id("color")).sendKeys("#ff0000");
        Assertions.assertThat(driver.findElement(By.id("color"))
                        .getAttribute("value"))
                .isEqualTo("#ff0000");

        //    期望获取元素的value为不等于输入的值
        driver.findElement(By.id("date")).sendKeys("2022-02-01");
        Assertions.assertThat(driver.findElement(By.id("date"))
                        .getAttribute("value"))
                .isNotEqualTo("2022-02-01");
        //    期望获取元素的value为不等于输入的值
        driver.findElement(By.id("datetime-local")).sendKeys("2022-02-01T01:01");
        Assertions.assertThat(driver.findElement(By.id("datetime-local"))
                        .getAttribute("value"))
                .isNotEqualTo("2022-02-01T01:01");
        //    期望获取元素的value为不等于输入的值
        driver.findElement(By.id("month")).sendKeys("2022-07");
        Assertions.assertThat(driver.findElement(By.id("month"))
                        .getAttribute("value"))
                .isNotEqualTo("2022-07");
        //    期望获取元素的value为不等于输入的值
        driver.findElement(By.id("week")).sendKeys("2022-W01");
        Assertions.assertThat(driver.findElement(By.id("week"))
                        .getAttribute("value"))
                .isNotEqualTo("2022-W01");
        //    期望获取元素的value为等于输入的值,但是不建议采用这种方式输入
        driver.findElement(By.id("time")).sendKeys("01:02");
        Assertions.assertThat(driver.findElement(By.id("time"))
                        .getAttribute("value"))
                .isEqualTo("01:02");

        //    期望获取元素的value为空""
        driver.findElement(By.id("image")).sendKeys("image");
        Assertions.assertThat(driver.findElement(By.id("image"))
                        .getAttribute("value"))
                .isEmpty();
        //    期望获取元素的value为空""
        driver.findElement(By.id("reset")).sendKeys("reset");
        Assertions.assertThat(driver.findElement(By.id("reset"))
                        .getAttribute("value"))
                .isEmpty();
        //    期望获取元素的value为空""
        driver.findElement(By.id("submit")).sendKeys("submit");
        Assertions.assertThat(driver.findElement(By.id("submit"))
                        .getAttribute("value"))
                .isEmpty();
        //    期望获取元素的value为为空""
        driver.findElement(By.id("range")).sendKeys("99");
        Assertions.assertThat(driver.findElement(By.id("range"))
                        .getAttribute("value"))
                .isEqualTo("50");
        //    期望获取元素的value为空""
        driver.findElement(By.id("button")).sendKeys("button");
        Assertions.assertThat(driver.findElement(By.id("button"))
                        .getAttribute("value"))
                .isEmpty();
        //    期望获取元素的value为不等于输入的值
        driver.findElement(By.id("radio")).sendKeys("off");
        Assertions.assertThat(driver.findElement(By.id("radio"))
                        .getAttribute("value"))
                .isEqualTo("on");
        //    期望获取元素的value为不等于输入的值
        driver.findElement(By.id("checkbox")).sendKeys("off");
        Assertions.assertThat(driver.findElement(By.id("checkbox"))
                        .getAttribute("value"))
                .isEqualTo("on");

        //    文件元素可以通过sendKeys进行操作,但是针对错误的参数格式,期望抛出异常
        Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
                .isThrownBy(() -> driver.findElement(By.id("file")).sendKeys("file"));

        //    隐藏元素不可见,无法通过sendKeys进行操作,期望抛出异常
        Assertions.assertThatExceptionOfType(ElementNotInteractableException.class)
                .isThrownBy(() -> driver.findElement(By.id("hidden")).sendKeys("hidden"));
    }

    /**
     * 例程3.3  录入组合按键
     */
    @Test
    public void keyboardMultipleTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/keyboard.html");
        //    元素的sendKeys方法本身支持多个按键的同时输入,可以利用Keys对象调用修饰键
        driver.findElement(By.id("keyboard-multiple-1"))
                .sendKeys("a", "B"
                        , Keys.SHIFT, "c", "D"
                        , "\uE008", "e", "F");
        //    注意Shift按键是切换大小写输入的修饰键,期望获取元素的值为"aBCDeF"
        Assertions.assertThat(driver
                        .findElement(By.id("keyboard-multiple-1")).
                        getAttribute("value"))
                .isEqualTo("aBCDeF");
        //    利用Keys.chord实现组合按键的操作,因为会追加提示修饰键释放的Keys.NULL,所以不适合输入文本
        driver.findElement(By.id("keyboard-multiple-2"))
                .sendKeys(Keys.chord(Keys.SHIFT, "a", "B"));
        System.out.println(Keys.chord(Keys.SHIFT, "a", "B"));
        //    期望获取的value值为"AB"
        Assertions.assertThat(driver
                        .findElement(By.id("keyboard-multiple-2")).
                        getAttribute("value"))
                .isEqualTo("AB");
        //    某些特定版本对于字符录入可能会有Bug,
        //    例如在ChromeDriver 98.0.4758.48版本的实现中,
        //    Keys.chord会增加不可见的字符在输入框中
        //    如果遇到可以考虑版本号大于等于98.0.4758.102或者小于98的ChromeDriver
        //    详情可以参见以下两个链接
        //    https://github.com/SeleniumHQ/selenium/issues/10318    //
        //    https://bugs.chromium.org/p/chromedriver/issues/detail?id=3999
        //    Assertions.assertThat(driver
        //                    .findElement(By.id("keyboard-multiple-2")).
        //                    getAttribute("value"))
        //            .isEqualTo("AB\uE000");
        //    构造Actions对象
        Actions action = new Actions(driver);
        //    Actions默认是在当前焦点元素进行输入,也可以通过click或者sendKeys指定目标元素
        action.sendKeys(driver
                        .findElement(By.id("keyboard-multiple-3")), "abc")
                .perform();
        action.click(driver
                        .findElement(By.id("keyboard-multiple-3")))
                .sendKeys("def")
                .perform();
        //    期望元素的value值为"abcdef"
        Assertions.assertThat(driver
                        .findElement(By.id("keyboard-multiple-3")).
                        getAttribute("value"))
                .isEqualTo("abcdef");
        //如果提前按下修饰键,则会影响输入的值
        action.keyDown(Keys.SHIFT)
                .sendKeys(driver
                        .findElement(By.id("keyboard-multiple-4")), "abc")
                .keyUp(Keys.SHIFT)
                .perform();
        //    期望元素的value为空""
        Assertions.assertThat(driver
                        .findElement(By.id("keyboard-multiple-4")).
                        getAttribute("value"))
                .isEmpty();
        //提前通过点击操作将焦点定位到目标元素
        action.click(driver.findElement(By.id("keyboard-multiple-4")))
                .keyDown(Keys.SHIFT)
                .sendKeys(driver
                        .findElement(By.id("keyboard-multiple-4")), "abc")
                .keyUp(Keys.SHIFT)
                .perform();
        //    期望元素的value为"ABC"
        Assertions.assertThat(driver
                        .findElement(By.id("keyboard-multiple-4")).
                        getAttribute("value"))
                .isEqualTo("ABC");
        //期望keyUp和keyDown可以操作以下修饰键
        //    Keys.SHIFT,Keys.CONTROL,Keys.ALT,Keys.META,Keys.COMMAND,Keys.LEFT_ALT,Keys.LEFT_CONTROL,Keys.LEFT_SHIFT
        action.keyDown(Keys.SHIFT)
                .keyDown(Keys.CONTROL)
                .keyDown(Keys.ALT)
                .keyDown(Keys.META)
                .keyDown(Keys.COMMAND)
                .keyUp(Keys.LEFT_ALT)
                .keyUp(Keys.LEFT_CONTROL)
                .keyUp(Keys.LEFT_SHIFT)
                .perform();
        //    期望keyDown传入非指定修饰键时会抛出异常
        Assertions.assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> action.keyDown("code points"));
        //    期望keyUp传入非指定修饰键时会抛出异常
        Assertions.assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> action.keyUp("code points"));
        System.out.println();
    }

    /**
     * 例程3.4  录入快捷方式
     */
    @Test
    public void keyboardShortcutTest() throws AWTException, InterruptedException {
        //    获取当前窗口数量 windowSize1
        int windowSize1 = driver.getWindowHandles().size();
        //    利用Actions模拟输入Ctrl+T
        new Actions(driver)
                .keyDown(Keys.CONTROL)
                .sendKeys("n")
                .keyUp(Keys.CONTROL)
                .perform();
        //    获取当前窗口数量 windowSize2
        //    IE下虽然会看到新窗口弹出,但是获取的窗口句柄不变
        int windowSize2 = driver.getWindowHandles().size();
        //    期望 windowSize1 等于 windowSize2
        Assertions.assertThat(windowSize1)
                .isEqualTo(windowSize1);
        //    利用JavaScript执行window.open()打开新窗口
        ((JavascriptExecutor) driver)
                .executeScript("window.open()");
        //    获取当前窗口数量 windowSize3
        int windowSize3 = driver.getWindowHandles().size();
        //    期望 windowSize2 小于 windowSize3
        Assertions.assertThat(windowSize2)
                .isLessThan(windowSize3);
        //    利用Robot类模拟按键,受限于系统执行速度,可能需要添加延时
        //    但是因为焦点问题,不能保证按键操作一定作用于浏览器,有一定几率失败
        //    Robot robot = new Robot();
        //    robot.keyPress(KeyEvent.VK_CONTROL);
        //    robot.keyPress(KeyEvent.VK_N);
        //    robot.keyRelease(KeyEvent.VK_CONTROL);
        //    robot.keyRelease(KeyEvent.VK_N);
        //    Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(2));
        //    获取当前窗口数量 windowSize4
        //    int windowSize4 = driver.getWindowHandles().size();
        //    期望 windowSize3 小于 windowSize4
        //    Assertions.assertThat(windowSize3).isLessThan(windowSize4);
    }

    /**
     * 例程3.5  清除元素值
     */
    @Test
    public void keyboardClearTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/keyboard.html");
        //    获取目标元素
        WebElement element = driver.findElement(By.id("keyboard-clear"));
        //    期望元素值为"this is the default value"
        Assertions.assertThat(element.getAttribute("value"))
                .isEqualTo("this is the default value");
        //    调用clear方法清除元素值
        element.clear();
        //    期望元素值为空""
        Assertions.assertThat(element.getAttribute("value"))
                .isEmpty();
        //    发送目标文本
        element.sendKeys("send backspace while size is greater than 0");
        //    利用退格键清空文本,一般情况下应该设置超时时间,以避免死循环
        while (element.getAttribute("value").length() > 0) {
            element.sendKeys(Keys.BACK_SPACE);
        }
        //    利用Awaitility避免死循环
        Awaitility.await().atMost(Duration.ofMinutes(10L))
                .pollInterval(Duration.ofSeconds(1L))
                .until(() -> {
                    element.sendKeys(Keys.BACK_SPACE);
                    return element.getAttribute("value").isEmpty();
                });
        //    期望元素值为空""
        Assertions.assertThat(element.getAttribute("value"))
                .isEmpty();
        //    发送目标文本
        element.sendKeys("send ctrl a then press del");
        //    模拟按下ctrl+a执行全选操作,然后模拟按下删除键
        element.sendKeys(Keys.CONTROL + "A");
        element.sendKeys(Keys.DELETE);
        //    期望元素值为空""
        Assertions.assertThat(element.getAttribute("value"))
                .isEmpty();
    }

    /**
     * 例程3.6  鼠标交互
     */

    @Test
    public void clickSampleTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/mouse.html");
        //    获取hello按钮元素
        WebElement helloBtn = driver.findElement(By.id("hello-btn"));
        //    获取bye按钮元素
        WebElement byeBtn = driver.findElement(By.id("bye-btn"));
        //    获取点击事件记录元素
        WebElement clickEvent = driver.findElement(By.id("click-event"));
        //    创建Actions实例
        Actions actions = new Actions(driver);
        //    在hello按钮元素上执行单击操作
        actions.click(helloBtn).perform();
        //    期望点击事件记录的最后一个内容为"click"
        Assertions.assertThat(fetchLastP(clickEvent).getText())
                .isEqualTo("click");
        //    执行双击操作
        actions.doubleClick(helloBtn).perform();
        //    期望点击事件记录的最后一个内容为"dblclick"
        Assertions.assertThat(fetchLastP(clickEvent).getText())
                .isEqualTo("dblclick");
        //    执行右击操作
        actions.contextClick(helloBtn).perform();
        //    期望点击事件记录的最后一个内容为"contextmenu"
        Assertions.assertThat(fetchLastP(clickEvent).getText())
                .isEqualTo("contextmenu");
        //    将鼠标移动到bye按钮元素
        actions.moveToElement(byeBtn).perform();
        //    期望点击事件记录的最后一个内容为"mouseout"
        Assertions.assertThat(fetchLastP(clickEvent).getText())
                .isEqualTo("mouseout");
        //    将鼠标移动回hello按钮元素
        actions.moveToElement(helloBtn).perform();
        //    期望点击事件记录的最后一个内容为"mouseover"
        Assertions.assertThat(fetchLastP(clickEvent).getText())
                .isEqualTo("mouseover");
        //    执行点击且按住鼠标操作
        actions.clickAndHold(helloBtn).perform();
        //    期望点击事件记录的最后一个内容为"mousedown"
        Assertions.assertThat(fetchLastP(clickEvent).getText())
                .isEqualTo("mousedown");
        //    执行释放鼠标操作
        actions.release(helloBtn).perform();
        //    期望点击事件记录的最后一个内容为"click"
        Assertions.assertThat(fetchLastP(clickEvent).getText())
                .isEqualTo("click");
        //    设置输入操作类型为TOUCH,并执行单机操作
        actions.setActivePointer(PointerInput.Kind.TOUCH, "default touch")
                .click(helloBtn).perform();
        //    获取完整的事件列表
        List<String> actionList = clickEvent
                .findElements(By.tagName("p"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        //    期望事件列表包含 "touchstart" 和 "touchend"
        Assertions.assertThat(actionList)
                .contains("touchstart")
                .contains("touchend");
        //    但是实际上最后一个事件是 "click" 而非 "touchend"
        Assertions.assertThat(fetchLastP(clickEvent).getText())
                .isEqualTo("click")
                .isNotEqualTo("touchend");
    }

    private WebElement fetchLastP(WebElement clickEvent) {
        List<WebElement> list = clickEvent.findElements(By.tagName("p"));
        if (!list.isEmpty()) {
            return list.get(list.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * 例程3.7  拖拽操作
     */
    @Test
    public void moveTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/mouse-move.html");
        //    获取目标元素
        WebElement target = driver.findElement(By.id("target-box"));
        WebElement box2 = driver.findElement(By.id("dnd-box-2"));
        //    获取target的位置point1
        Point point1 = target.getLocation();
        //    使用原生方法拖动元素到指定元素
        new Actions(driver)
                .dragAndDrop(target, box2)
                .perform();
        //    获取target当前位置point2
        Point point2 = target.getLocation();
        //    期望移动元素的方法生效
        Assertions.assertThat(point1).isNotEqualTo(point2);
        new Actions(driver).
                clickAndHold(target)
                .moveToElement(box2)
                .release(target)
                .perform();
        //    获取target当前位置point3
        Point point3 = target.getLocation();
        //    期望移动元素的方法生效
        Assertions.assertThat(point1).isNotEqualTo(point3);
        //    采用JavaScript脚本进行移动
        ((JavascriptExecutor) driver)
                .executeScript("simulateDragDrop(arguments[0], arguments[1])", target, box2);
        //    获取target当前位置point3
        Point point4 = target.getLocation();
        //    期望移动元素的方法生效
        Assertions.assertThat(point1)
                .isNotEqualTo(point4);
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/mouse-move-jqueryui.html");
        WebElement draggableElement = driver.findElement(By.id("draggable"));
        WebElement newBox1 = driver.findElement(By.id("dnd-box-1"));
        WebElement newBox2 = driver.findElement(By.id("dnd-box-2"));
        //    获取draggableElement的位置p1
        Point p1 = draggableElement.getLocation();
        //    使用原生方法拖动元素到指定元素
        new Actions(driver)
                .dragAndDrop(draggableElement, newBox2)
                .perform();
        //    获取draggableElement当前位置point2
        Point p2 = draggableElement.getLocation();
        //    期望移动元素的方法生效
        Assertions.assertThat(p1).isNotEqualTo(p2);
        //    使用原生方法按照位置移动元素
        new Actions(driver)
                .clickAndHold(draggableElement)
                .moveByOffset(newBox1.getLocation().getX(), newBox1.getLocation().getY())
                .release(draggableElement)
                .perform();
        //    获取draggableElement当前位置p3
        Point p3 = draggableElement.getLocation();
        //    期望移动元素的方法生效
        Assertions.assertThat(p2).isNotEqualTo(p3);
        new Actions(driver)
                .dragAndDropBy(draggableElement, newBox2.getLocation().getX(), newBox2.getLocation().getY())
                .perform();
        //    获取draggableElement当前位置p4
        Point p4 = draggableElement.getLocation();
        //    期望移动元素的方法生效
        Assertions.assertThat(p3).isNotEqualTo(p4);
    }

    /**
     * 例程3.8  JavaScript操作
     */
    @Test
    public void executeScriptTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/javascript.html");
        //    获取username元素
        WebElement usernameElement = driver.findElement(By.id("username-input"));
        //    获取reset按钮
        WebElement resetElement = driver.findElement(By.id("reset-btn"));
        //    将username元素以及字符串"username"当做参数传入以执行赋值操作
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].value = arguments[1]"
                        , usernameElement
                        , "username");
        //    期望username的值为"username"
        Assertions.assertThat(usernameElement.getDomProperty("value")).isEqualTo("username");
        //    执行reset按钮的点击操作
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click()", resetElement);
        //    期望username的值为空
        Assertions.assertThat(usernameElement.getDomProperty("value")).isEmpty();

        //    定义返回接收对象
        Object returnObject = null;
        //    通过return获取JavaScript函数执行后的返回的值
        returnObject = ((JavascriptExecutor) driver)
                .executeScript("return document.querySelector('#username-input')");
        //    对于HTML元素,返回WebElement
        Assertions.assertThat(returnObject).isInstanceOf(WebElement.class);
        Assertions.assertThat(returnObject).isEqualTo(usernameElement);
        //    对于小数,返回Double
        returnObject = ((JavascriptExecutor) driver)
                .executeScript("return arguments[0]", 3.14F);
        Assertions.assertThat(returnObject).isInstanceOf(Double.class);
        Assertions.assertThat(returnObject).isEqualTo(3.14D);
        //    对于非小数,返回Long
        returnObject = ((JavascriptExecutor) driver)
                .executeScript("return arguments[0]", Integer.MIN_VALUE);
        Assertions.assertThat(returnObject).isInstanceOf(Long.class);
        Assertions.assertThat(returnObject).isEqualTo(Long.valueOf(Integer.MIN_VALUE));
        //    对于null,返回null
        returnObject = ((JavascriptExecutor) driver)
                .executeScript("return null");
        Assertions.assertThat(returnObject).isNull();
        //    对于undefined,返回null
        returnObject = ((JavascriptExecutor) driver)
                .executeScript("return undefined");
        Assertions.assertThat(returnObject).isNull();
        //    对于Infinity,返回null
        returnObject = ((JavascriptExecutor) driver)
                .executeScript("return Infinity");
        Assertions.assertThat(returnObject).isNull();
        //    对于NaN,返回null
        returnObject = ((JavascriptExecutor) driver)
                .executeScript("return NaN");
        Assertions.assertThat(returnObject).isNull();
        //    对于布尔值,返回布尔类型
        returnObject = ((JavascriptExecutor) driver)
                .executeScript("return arguments[0]", Boolean.TRUE);
        Assertions.assertThat(returnObject).isInstanceOf(Boolean.class);
        Assertions.assertThat(returnObject).isEqualTo(true);

        //    对于数组,返回一个List<Object>
        returnObject = ((JavascriptExecutor) driver)
                .executeScript("return [-1,['word']]");
        Assertions.assertThat(returnObject).isInstanceOf(List.class);
        List<Object> convertedList = (List<Object>) returnObject;
        //    期望列表的第一个值为-1,第二个值的第一个元素为"word
        Assertions.assertThat(convertedList.get(0)).isEqualTo(-1L);
        Assertions.assertThat(((List<Object>) convertedList.get(1)).get(0)).isEqualTo("word");
        //    期望JavaScript空对象被返回为空Map
        returnObject = ((JavascriptExecutor) driver)
                .executeScript("return {}");
        Map<String, Object> convertedMap1 = (Map<String, Object>) returnObject;
        Assertions.assertThat(convertedMap1).isEmpty();
        //    定义一个Map对象,并放入初始值
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        //    对于结构化的对象,返回一个Map<String,Object>
        returnObject = ((JavascriptExecutor) driver)
                .executeScript("return arguments[0]", map);
        Assertions.assertThat(returnObject).isInstanceOf(Map.class);
        Map<String, Object> convertedMap = (Map<String, Object>) returnObject;
        Assertions.assertThat(convertedMap).extractingByKey("key2").isEqualTo("value2");
    }

    /**
     * 例程3.9  JavaScript异步操作
     */
    @Test
    public void executeAsyncScriptTest() throws JsonProcessingException {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/javascript.html");
        //    定义返回接收对象
        Object returnObject = null;

        //    executeScript执行潜在的问题
        //    获取username元素
        WebElement usernameElement = driver.findElement(By.id("username-input"));
        //    获取username的值 username1
        String username1 = usernameElement.getDomProperty("value");
        //    执行脚本在五秒钟后为username赋值
        ((JavascriptExecutor) driver).executeScript("window.setTimeout(" +
                "function(){document.querySelector('#username-input').value = new Date()}" +
                ",5000);");
        //    在executeScript执行结束后立即获取最新的username值
        String username2 = usernameElement.getDomProperty("value");
        //    因为executeScript执行不会等待异步结果,所以期望username的值无变化
        Assertions.assertThat(username1).isEqualTo(username2);

        //    executeAsyncScript方法会等待异步执行的脚本运行结束
        //    获取当前时间 time1
        long time1 = System.currentTimeMillis();
        //    执行无等待时间的异步方法
        ((JavascriptExecutor) driver).executeAsyncScript("window.setTimeout(arguments[arguments.length - 1], 0)");
        //    获取当前时间 time2
        long time2 = System.currentTimeMillis();
        //    执行等待时间为5000毫秒的异步方法
        ((JavascriptExecutor) driver).executeAsyncScript("window.setTimeout(arguments[arguments.length - 1], 5000)");
        //    获取当前时间 time3
        long time3 = System.currentTimeMillis();
        //    执行等待时间为5000毫秒的同步方法
        ((JavascriptExecutor) driver).executeScript("window.setTimeout(arguments[arguments.length - 1], 5000)");
        //    获取当前时间 time4
        long time4 = System.currentTimeMillis();
        //    打印每次执行的时间
        System.out.println("Elapsed time [ sleep    0 ms with async]: " + (time2 - time1));
        System.out.println("Elapsed time [ sleep 5000 ms with async]: " + (time3 - time2));
        System.out.println("Elapsed time [ sleep 5000 ms with  sync]: " + (time4 - time3));
        //    期望设置了等待时间的异步方法的执行时长远大于其他两种情况
        Assertions.assertThat(time3 - time2)
                .isGreaterThan(time2 - time1)
                .isGreaterThan(time4 - time3);

        //    从jQuery的AJAX请求中获取结果并解析
        String jqueryString = "let callback = arguments[arguments.length - 1];"
                + "$.get('https://www.ianzhang.cn/s4ia/static/data/ian/async-sample.json'" +
                ",function(data){callback(JSON.stringify(data))})";
        //    将结果 {"author":"ian"} 赋给变量 returnObject
        returnObject = ((JavascriptExecutor) driver).executeAsyncScript(jqueryString);
        //    期望获取的结果中 "author" 字段的值为 "ian"
        Assertions.assertThat(new ObjectMapper()
                        .readTree(String.valueOf(returnObject))
                        .findValue("author")
                        .textValue())
                .isEqualTo("ian");

        //    从axios发起的Promise的请求中获取结果并解析
        String axiosString = "let callback = arguments[arguments.length - 1];"
                + "axios.get('https://www.ianzhang.cn/s4ia/static/data/ian/async-sample.json')"
                + ".then((response)=>{callback(response.data)})";
        //    将结果 {"author":"ian"} 赋给变量 returnObject
        returnObject = ((JavascriptExecutor) driver).executeAsyncScript(axiosString);
        //    如果想获取JSON字符串,则需要将上面注入的callback修改为JSON.stringify(response.data)
        //    此处获取的returnObject被识别为Map类型
        Map<String, Object> convertedMap = (Map<String, Object>) returnObject;
        //    期望获取的结果中键 "author" 的值为 "ian"
        Assertions.assertThat(convertedMap)
                .extractingByKey("author")
                .isEqualTo("ian");

        //    模拟传统的XMLHttpRequest请求并解析结果
        String xhrString = "var callback = arguments[arguments.length - 1];"
                + "var xhr = new XMLHttpRequest();"
                + "xhr.open('GET', 'https://www.ianzhang.cn/s4ia/static/data/ian/async-sample.json', true);"
                + "xhr.onreadystatechange = function() {"
                + "  if (xhr.readyState == 4) {"
                + "    callback(xhr.responseText);"
                + "  }"
                + "};"
                + "xhr.send();";
        //    将结果 {"author":"ian"} 赋给变量 returnObject
        returnObject = ((JavascriptExecutor) driver).executeAsyncScript(xhrString);
        //    期望获取的结果中 "author" 字段的值为 "ian"
        Assertions.assertThat(new ObjectMapper()
                        .readTree(String.valueOf(returnObject))
                        .findValue("author")
                        .textValue())
                .isEqualTo("ian");
    }


    /**
     * 例程3.10  pin操作
     */
    @Test
    public void pinTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/javascript.html");
        //    获取元素username
        WebElement usernameElement = driver.findElement(By.id("username-input"));
        //    获取元素reset按钮
        WebElement resetElement = driver.findElement(By.id("reset-btn"));
        //    定义代码scriptCode1用以给username赋值
        String scriptCode1 = "document.querySelector('#username-input').value = 'test'";
        //    定义代码scriptCode2用以执行按钮点击操作
        String scriptCode2 = "arguments[0].click()";
        //    将两组代码存入当前会话
        ScriptKey scriptKey1 = ((JavascriptExecutor) driver).pin(scriptCode1);
        ScriptKey scriptKey2 = ((JavascriptExecutor) driver).pin(scriptCode2);
        //    获取当前会话中全部pin的代码
        Set<ScriptKey> scriptKeySet = ((JavascriptExecutor) driver).getPinnedScripts();
        //    期望两组代码都在当前会话返回的集合里
        Assertions.assertThat(scriptKeySet)
                .contains(scriptKey1)
                .contains(scriptKey2);
        //    执行代码scriptCode1为username赋值
        ((JavascriptExecutor) driver).executeScript(scriptKey1);
        //    期望username的值为 "test"
        Assertions.assertThat(usernameElement.getDomProperty("value"))
                .isEqualTo("test");
        //    执行代码scriptCode2,并将reset按钮以参数形式传入,实现reset操作
        ((JavascriptExecutor) driver).executeScript(scriptKey2, resetElement);
        //    期望username的值为空
        Assertions.assertThat(usernameElement.getDomProperty("value"))
                .isEmpty();
        //    unpin代码scriptKey1和scriptKey2
        ((JavascriptExecutor) driver).unpin(scriptKey1);
        ((JavascriptExecutor) driver).unpin(scriptKey2);
        //    期望当前会话中pin的集合为空
        Assertions.assertThat(((JavascriptExecutor) driver).getPinnedScripts())
                .isEmpty();
    }

    /**
     * 例程3.11  复杂JavaScript操作
     */
    @Test
    public void caseTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/javascript.html");
        //    获取username元素
        WebElement usernameElement = driver.findElement(By.id("username-input"));
        //    获取reset按钮
        WebElement resetElement = driver.findElement(By.id("reset-btn"));
        //    获取第10个td元素
        WebElement tdElement10 = driver.findElement(By.xpath("//div[@id='container']//table/tbody/tr[10]/td"));
        //    当通过JavascriptExecutor执行 "对象.Property" 时,期望抛出异常
        Assertions.assertThatExceptionOfType(JavascriptException.class)
                .isThrownBy(() -> ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].arguments[1] = arguments[2]"
                                , usernameElement
                                , "value"
                                , "username"));
        //    通过 "对象['Property']" 操作时,期望没有异常
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0][arguments[1]] = arguments[2]"
                        , usernameElement
                        , "value"
                        , "username");
        //    期望username元素的value值为 "username"
        Assertions.assertThat(usernameElement.getDomProperty("value"))
                .isEqualTo("username");
        //    通过JavascriptExecutor设置attribute
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].setAttribute(arguments[1],arguments[2])"
                        , resetElement
                        , "hidden"
                        , "hidden");
        //    因为元素hidden,期望点击时抛出异常
        Assertions.assertThatExceptionOfType(ElementNotInteractableException.class)
                .isThrownBy(() -> resetElement.click());
        //    通过JavascriptExecutor去除Attribute
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].removeAttribute(arguments[1])"
                        , resetElement
                        , "hidden");
        //    期望元素可见
        Assertions.assertThat(resetElement.isDisplayed())
                .isTrue();
        //    获取元素位置
        Point pointStart = tdElement10.getLocation();
        //    滚动到特定元素
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView()"
                        , tdElement10);
        //    获取元素位置
        Point pointEnd = tdElement10.getLocation();
        //    期望位置有变化
        Assertions.assertThat(pointStart).isNotEqualTo(pointEnd);
    }

    /**
     * 例程3.11  弹窗操作
     */
    @Test
    public void alertTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/interaction.html");
        //    触发Alert
        driver.findElement(By.id("alert-btn")).click();
        //    通过driver.switchTo().alert()获取Alert对象alert
        Alert alert = driver.switchTo().alert();
        //    通过getText()方法获取弹窗的文本
        Assertions.assertThat(alert.getText())
                .isEqualTo("I am an alert");
        //    通过accept()关闭弹窗,相当于点击弹窗的 OK 或者 确定 按钮
        alert.accept();

        //    触发confirm
        driver.findElement(By.id("confirm-btn")).click();
        //    获取Alert对象confirm
        Alert confirm = driver.switchTo().alert();
        //    通过getText()方法获取弹窗的文本
        Assertions.assertThat(confirm.getText())
                .isEqualTo("I am a confirm");
        //    通过dismiss()关闭弹窗, 相当于点击弹窗的 Cancel 或者 取消 按钮
        confirm.dismiss();
        //    期望confirm执行dismiss()后获取到的结果是false
        Assertions.assertThat(driver.findElement(By.id("confirm-result")).getText())
                .isEqualTo("false");

        //    触发prompt
        driver.findElement(By.id("prompt-btn")).click();
        //    获取Alert对象prompt
        Alert prompt = driver.switchTo().alert();
        //    通过getText()方法获取弹窗的文本
        Assertions.assertThat(prompt.getText())
                .isEqualTo("What's your name?");
        //    为弹窗的文本框键入文本
        prompt.sendKeys("ian");

        //    通过accept()关闭弹窗,相当于点击弹窗的 OK 或者 确定 按钮
        prompt.accept();
        //    期望键入的文本为 "ian"
        Assertions.assertThat(driver.findElement(By.id("prompt-result")).getText())
                .isEqualTo("ian");

        //    虽然上述代码获取了多个Alert对象,但是Selenium并不区分Alert对象,针对的都是当前弹窗进行操作
        driver.findElement(By.id("confirm-btn")).click();
        alert.accept();
        Assertions.assertThat(driver.findElement(By.id("confirm-result")).getText())
                .isEqualTo("true");

        //    如果当前窗口没有有效弹窗,则会抛出异常
        Assertions.assertThatExceptionOfType(NoAlertPresentException.class)
                .isThrownBy(prompt::accept);
        //    若使用默认的处理弹窗的行为none,则会有以下期望
        //    driver.findElement(By.id("alert-btn")).click();
        //    Assertions.assertThatExceptionOfType(UnhandledAlertException.class)
        //            .isThrownBy(() -> driver.findElement(By.id("confirm-result")).getText());
    }


    /**
     * 例程3.12  文件上传
     */
    @Test
    public void fileUploadTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/interaction.html");
        List<String> tempFileList = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            tempFileList.add(Files.newTemporaryFile().getAbsolutePath());
        }
        WebElement singleUpload = driver.findElement(By.id("single-upload"));
        WebElement multipleUpload = driver.findElement(By.id("multiple-upload"));
        singleUpload.sendKeys(tempFileList.get(0));
        List<Map<String, Object>> singleFileFirst =
                (List<Map<String, Object>>) ((JavascriptExecutor) driver)
                        .executeScript("return arguments[0]['files']", singleUpload);
        singleUpload.sendKeys(tempFileList.get(1));
        //    org.openqa.selenium.InvalidArgumentException: invalid argument:
        //    the element can not hold multiple files
        Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
                .isThrownBy(() -> singleUpload
                        .sendKeys(tempFileList.get(3) + "\n"
                                , tempFileList.get(4)));
        List<Map<String, Object>> singleFileSecond =
                (List<Map<String, Object>>) ((JavascriptExecutor) driver)
                        .executeScript("return arguments[0]['files']", singleUpload);
        Assertions.assertThat(singleFileSecond).hasSize(1).isNotEqualTo(singleFileFirst);
        multipleUpload.sendKeys(tempFileList.get(2));
        List<Map<String, Object>> multipleFilesFirst =
                (List<Map<String, Object>>) ((JavascriptExecutor) driver)
                        .executeScript("return arguments[0]['files']", multipleUpload);
        Assertions.assertThat(multipleFilesFirst).hasSize(1);
        Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
                .isThrownBy(() -> multipleUpload.sendKeys(tempFileList.get(3), tempFileList.get(4)));
        multipleUpload.sendKeys(tempFileList.get(3) + "\n", tempFileList.get(4));
        List<Map<String, Object>> multipleFilesSecond = (List<Map<String, Object>>) ((JavascriptExecutor) driver).executeScript("return arguments[0]['files']", multipleUpload);
        Assertions.assertThat(multipleFilesSecond).hasSize(3);
    }

    /**
     * 例程3.13  文件下载
     */
    @Test
    public void fileTest() throws InterruptedException {
        //    获取临时文件夹
        File downloadFolder = Files.newTemporaryFolder();
        //    定义配置表
        HashMap<String, Object> prefs = new HashMap<>();
        //    设置默认的下载目录
        prefs.put("download.default_directory", downloadFolder.getAbsolutePath());
        //    关闭下载选择提示框
        prefs.put("download.prompt_for_download", false);
        //    新建Chrome配置项
        ChromeOptions chromeOptions = new ChromeOptions();
        //    将配置表放入Chrome配置项
        chromeOptions.setExperimentalOption("prefs", prefs);
        //    新建ChromeDriver实例
        WebDriver driver = null;
        try {
            driver = new ChromeDriver(chromeOptions);
            //    打开目标页面
            driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/interaction.html");
            //    点击页面链接进行下载
            driver.findElement(By.id("download-link-1")).click();
            //    期望文件下载到了预制目录
            String fileName1 = driver.findElement(By.id("download-link-1")).getAttribute("download");
            File targetFile1 = new File(downloadFolder.getAbsolutePath() + File.separator + fileName1);
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(5));
            Assertions.assertThat(targetFile1.exists()).isTrue();
            //    因为浏览器可以直接打开部分类型的文件,所以期望无文件下载
            driver.findElement(By.id("download-link-2")).click();
            String fileName2 = driver.findElement(By.id("download-link-2")).getAttribute("download");
            File targetFile2 = new File(downloadFolder.getAbsolutePath() + File.separator + fileName2);
            Assertions.assertThat(targetFile2.exists()).isFalse();
            //    通过获取属性值的方式得到下载链接,并利用下载类库进行下载
            String filePath2 = driver.findElement(By.id("download-link-2")).getAttribute("href");
//            NativeHttpConnectionUtil.downloadFile(filePath2, targetFile2.getAbsolutePath());
//            Assertions.assertThat(targetFile2.exists()).isTrue();
            boolean downloaded = waitForFileComplete(targetFile2, Duration.ofSeconds(30), Duration.ofMillis(500));
            Assertions.assertThat(downloaded).isTrue();
        } finally {
            if (driver != null)
                driver.quit();
        }
    }

    // 同一测试类中添加下面的辅助方法
    private boolean waitForFileComplete(File file, Duration timeout, Duration pollInterval) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < deadline) {
            if (file.exists()) {
                try {
                    long size1 = file.length();
                    if (size1 > 0) {
                        Thread.sleep(pollInterval.toMillis());
                        long size2 = file.length();
                        if (size1 == size2) {
                            return true; // 文件存在且大小稳定，认为下载完成
                        }
                    }
                } catch (SecurityException ignored) {
                    // 继续重试
                }
            }
            Thread.sleep(Math.max(100, Math.min(pollInterval.toMillis(), 1000)));
        }
        return false; // 超时未完成
    }


    /**
     * 例程3.15  Cookie操作
     */
    @Test
    public void cookieTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/interaction.html");
        //    构造默认的Cookie过期时间
        Date expire = Date.from(
                LocalDateTime.now()
                        .plusSeconds(10L)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
        //    构造测试Cookie
        Cookie cookie1 = new Cookie("cname1", "value1", null, expire);
        Cookie cookie2 = new Cookie("cname2", "value2", null, expire);
        Cookie cookie3 = new Cookie("cname3", "value3", null, expire);
        Cookie cookie4 = new Cookie("cname4", "value4", null, expire);
        //    添加Cookie
        driver.manage().addCookie(cookie1);
        driver.manage().addCookie(cookie2);
        driver.manage().addCookie(cookie3);
        driver.manage().addCookie(cookie4);
        //    获取全部Cookie的集合
        Set<Cookie> set = driver.manage().getCookies();
        //    期望集合中包括上述四个Cookie
        Assertions.assertThat(set)
                .contains(cookie1, cookie2, cookie3, cookie4);
        //    通过Name删除Cookie
        driver.manage().deleteCookieNamed("cname1");
        //    通过对象删除Cookie
        driver.manage().deleteCookie(cookie2);
        //    通过对象删除也是通过Name
        driver.manage().deleteCookie(new Cookie("cname3", "test", "/", new Date()));
        //    期望集合中不包括已删除的Cookie
        Assertions.assertThat(driver.manage().getCookies())
                .doesNotContain(cookie1, cookie2, cookie3);
        //    通过Name获取Cookie
        Cookie tempCookie = driver.manage().getCookieNamed("cname4");
        Assertions.assertThat(tempCookie)
                .isEqualTo(cookie4);
        //    删除所有Cookie
        driver.manage().deleteAllCookies();
        //    期望Cookie被清空
        Assertions.assertThat(driver.manage().getCookies())
                .isEmpty();
    }

    /**
     * 例程3.16  截图操作
     */
    @Test
    public void screenshotTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/interaction.html");
        //    获取表格元素
        WebElement tableElement = driver.findElement(By.id("table-div"));
        //    获取待嵌入图片元素
        WebElement embedElement = driver.findElement(By.id("embed-pic"));
        //    获取截图文件
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        //    期望截图位于系统临时目录,且后缀名为png
        Assertions.assertThat(file.getAbsolutePath())
                .startsWith(System.getProperty("java.io.tmpdir"))
                .endsWith("png");
        //    获取BASE64格式的元素截图
        String picBase64 = tableElement.getScreenshotAs(OutputType.BASE64);
        //    将截图内容追加到待嵌入图片元素
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].src='data:image/png;base64," + picBase64 + "'"
                        , embedElement);
    }

    /**
     * 例程3.17  shutterbug截图操作
     */
    @Test
    public void shutterbugTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/screenshot.html");
        WebElement nav = driver.findElement(By.id("nav"));
        WebElement monthList = driver.findElement(By.id("month-list"));
        WebElement todayBtn = driver.findElement(By.id("today-btn"));
        WebElement searchBtn = driver.findElement(By.id("search-btn"));
        WebElement searchBox = driver.findElement(By.id("text-field"));
        Shutterbug.shootPage(driver, Capture.VIEWPORT)
                .blur(searchBox)
                .highlight(searchBtn)
                .monochrome(nav)
                .highlightWithText(monthList, Color.blue,
                        3, "Monochromed", Color.blue,
                        new Font("SansSerif", Font.BOLD, 20))
                .highlightWithText(todayBtn, "Go back to today")
                .withTitle("Calendar Sample - " + new Date())
                .withName("screenshot")
                .withThumbnail(0.7)
                .save(Files.temporaryFolderPath());
        Shutterbug.shootPage(driver, Capture.FULL_SCROLL)
                .save(Files.temporaryFolderPath());
    }

    /**
     * 例程3.18  页面保存为PDF
     */
    @Test
    public void pdfTest() throws IOException {
        if (driver instanceof
                PrintsPage) {
            //    打开目标页面
            driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/screenshot.html");
            Path printPage = Paths.get(Files.temporaryFolderPath() + "PrintPage.pdf");
            Pdf print = ((PrintsPage) driver).print(new PrintOptions());
            FileUtils.writeByteArrayToFile(printPage.toFile(), OutputType.BYTES.convertFromBase64Png(print.getContent()));
            System.out.println(printPage.toAbsolutePath().toString());
        } else {
            System.out.println("This driver is not instance of printsPage");
        }
    }

    /**
     * 例程3.19  通过截图保存页面为PDF
     */
    @Test
    public void openPdfTest() throws IOException {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/screenshot.html");
        //    获取截图文件
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        //    借助工具类生成截图PDF文件
        PdfUtil.generatePdfFileWithPictures(Paths.get(Files.temporaryFolderPath() + "Screenshot.pdf").toFile()
                , Map.of("Screenshot", file));
    }
}
