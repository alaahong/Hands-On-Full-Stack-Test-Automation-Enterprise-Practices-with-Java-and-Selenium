package cn.ianzhang;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class PointerInputTest extends WebDriverBase {

    @Test
    void shouldLogTouchPointerEvents() {
        // 按实际访问方式修改 URL，比如本地静态资源服务器
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/touch.html");

        WebElement board = driver.findElement(By.id("board"));
        WebElement log = driver.findElement(By.id("log"));

        // 用 Touch 类型创建一根“手指”
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");

        // 计算画板中心坐标（这里简单用 getRect）
        var rect = board.getRect();
        int centerX = rect.getX() + rect.getWidth() / 2;
        int centerY = rect.getY() + rect.getHeight() / 2;

        // 构造：移动到画板中心 -> 按下 -> 短暂停留 -> 抬起
        Sequence tap = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(
                        Duration.ZERO,
                        PointerInput.Origin.viewport(),
                        centerX,
                        centerY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(
                        Duration.ofMillis(100),
                        PointerInput.Origin.viewport(),
                        centerX,
                        centerY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        ((RemoteWebDriver) driver).perform(List.of(tap));

        // 断言：日志中包含 touch 类型的 down/up 事件
        String logText = log.getText();
        assertThat(logText)
                .contains("down pointerType=touch")
                .contains("up pointerType=touch");
    }

    @Test
    void shouldPerformTwoFingerPinchZoomOut() {
        // 按你的实际部署地址修改 URL
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/multi-touch.html");

        WebElement board = driver.findElement(By.id("board"));
        WebElement log = driver.findElement(By.id("log"));

        var rect = board.getRect();
        int centerX = rect.getX() + rect.getWidth() / 2;
        int centerY = rect.getY() + rect.getHeight() / 2;

        // 初始两指位置: 在中心附近，彼此间隔较小
        int finger1StartX = centerX - 20;
        int finger1StartY = centerY;
        int finger2StartX = centerX + 20;
        int finger2StartY = centerY;

        // 结束位置: 分别向两侧移动，拉大距离(模拟放大)
        int finger1EndX = centerX - 80;
        int finger1EndY = centerY;
        int finger2EndX = centerX + 80;
        int finger2EndY = centerY;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        // finger1 时间线: 移动到起点 -> 按下 -> 一段时间内移动到终点 -> 抬起
        Sequence finger1Seq = new Sequence(finger1, 0)
                .addAction(finger1.createPointerMove(
                        Duration.ZERO,
                        PointerInput.Origin.viewport(),
                        finger1StartX,
                        finger1StartY))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger1.createPointerMove(
                        Duration.ofMillis(400),
                        PointerInput.Origin.viewport(),
                        finger1EndX,
                        finger1EndY))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // finger2 时间线: 与 finger1 同步
        Sequence finger2Seq = new Sequence(finger2, 0)
                .addAction(finger2.createPointerMove(
                        Duration.ZERO,
                        PointerInput.Origin.viewport(),
                        finger2StartX,
                        finger2StartY))
                .addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger2.createPointerMove(
                        Duration.ofMillis(400),
                        PointerInput.Origin.viewport(),
                        finger2EndX,
                        finger2EndY))
                .addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // 并发执行两根手指的轨迹
        ((RemoteWebDriver) driver).perform(List.of(finger1Seq, finger2Seq));

        String logText = log.getText();

        // 基本事件链
        assertThat(logText).contains("pinch-start");
        assertThat(logText).contains("pinch-move scale=");
        assertThat(logText).contains("pinch-end final-scale=");

        // 使用正则从日志中提取最终缩放比例
        Pattern pattern = Pattern.compile("final-scale=([0-9]+\\.[0-9]+)");
        Matcher matcher = pattern.matcher(logText);
        assertThat(matcher.find())
                .as("日志中应该包含 final-scale 数值")
                .isTrue();

        double finalScale = Double.parseDouble(matcher.group(1));
        // 断言缩放比例大于 1.0（放大）
        assertThat(finalScale).isGreaterThan(1.0);
    }

    @Test
    void shouldDrawWithVirtualPen() {
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/pen.html");

        WebElement canvas = driver.findElement(By.id("board"));
        WebElement log = driver.findElement(By.id("log"));

        PointerInput pen = new PointerInput(PointerInput.Kind.PEN, "stylus");
        Sequence draw = new Sequence(pen, 0)
                .addAction(pen.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 150, 150))
                .addAction(pen.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(pen.createPointerMove(Duration.ofMillis(200), PointerInput.Origin.viewport(), 250, 120))
                .addAction(pen.createPointerMove(Duration.ofMillis(200), PointerInput.Origin.viewport(), 320, 200))
                .addAction(pen.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        ((RemoteWebDriver) driver).perform(List.of(draw));

        assertThat(log.getText()).contains("down", "move", "up");
    }
}
