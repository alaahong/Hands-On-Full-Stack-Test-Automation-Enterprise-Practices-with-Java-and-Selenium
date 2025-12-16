package cn.ianzhang;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.KeyInput;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.interactions.WheelInput;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WheelActionsTest extends WebDriverBase {

    @Test
    void shouldCaptureDetailedWheelEvents() {
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/wheel.html");

        WebElement box = driver.findElement(By.id("scroll-box"));
        WebElement log = driver.findElement(By.id("log"));
        WebElement summary = driver.findElement(By.id("summary"));

        WheelInput wheel = new WheelInput("virtual-wheel");
        WheelInput.ScrollOrigin origin = WheelInput.ScrollOrigin.fromElement(box, 20, 20);

        Sequence scrolls = new Sequence(wheel, 0)
                .addAction(wheel.createScroll(0, 0, 0, 240, Duration.ofMillis(200), origin))
                .addAction(wheel.createScroll(0, 0, 0, -120, Duration.ofMillis(150), origin))
                .addAction(wheel.createScroll(0, 0, 90, 0, Duration.ofMillis(150), origin));

        ((RemoteWebDriver) driver).perform(List.of(scrolls));

        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.attributeToBe(log, "data-count", "3"));

        List<String> entries = Arrays.stream(log.getText().trim().split("\\R"))
                .filter(line -> !line.isBlank())
                .toList();

        assertThat(entries).hasSizeGreaterThanOrEqualTo(3);
        assertThat(entries.get(0)).contains("dy=240.0");
        assertThat(entries.get(entries.size() - 1)).contains("dx=90.0");
        assertThat(entries.stream().anyMatch(line -> line.contains("dy=-120.0"))).isTrue();
        assertThat(summary.getText()).contains("Events: 3");
        assertThat(summary.getText()).contains("Total dx: 90.0");
        assertThat(summary.getText()).contains("Total dy: 120.0");
    }


    @Test
    void shouldZoomCanvasWhenCtrlPlusWheel() {
        // 按实际映射修改 URL
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/ctrl-wheel-zoom.html");

        WebElement canvas = driver.findElement(By.id("canvas"));
        WebElement scaleSpan = driver.findElement(By.id("scale"));
        WebElement log = driver.findElement(By.id("log"));

        // 初始缩放应为 1.00
        assertThat(scaleSpan.getText()).isEqualTo("1.00");

        // 计算 canvas 中心点
        var rect = canvas.getRect();
        int centerX = rect.getX() + rect.getWidth() / 2;
        int centerY = rect.getY() + rect.getHeight() / 2;

        // 1) 键盘输入源：Ctrl
        KeyInput keyboard = new KeyInput("keyboard");


        Sequence keySeq = new Sequence(keyboard, 0)
                // t0：按下 Ctrl
                .addAction(keyboard.createKeyDown(Keys.CONTROL.getCodePoint()))
                // t1：等待一小段时间，再抬起 Ctrl，便于与滚轮对齐
                .addAction(new Pause(keyboard, Duration.ofMillis(300)))
                .addAction(keyboard.createKeyUp(Keys.CONTROL.getCodePoint()));

        // 2) 滚轮输入源：在 Ctrl 按下期间向上滚一次
        WheelInput wheel = new WheelInput("wheel");

        // 这里 origin 使用视口，滚动中心点设为 canvas 中心
        WheelInput.ScrollOrigin origin =
                WheelInput.ScrollOrigin.fromViewport(centerX, centerY);

        Sequence wheelSeq = new Sequence(wheel, 0)
                // t0：在同一时刻开始滚动，duration 表示滚动持续时间
                .addAction(wheel.createScroll(
                        centerX,
                        centerY,
                        0,          // 水平不滚动
                        -120,       // deltaY<0 -> 向上滚，页面逻辑为放大
                        Duration.ofMillis(300),
                        origin));

        // 同时执行键盘和滚轮动作，它们位于同一时间轴上
        ((RemoteWebDriver) driver).perform(List.of(keySeq, wheelSeq));

        // 验证：日志中出现 ctrlKey=true 的 wheel 事件与 zoom 记录
        String logText = log.getText();
        assertThat(logText).contains("wheel").contains("ctrlKey=true");
        assertThat(logText).contains("zoom scale=");

        // 验证：缩放值大于 1.0
        double scale = Double.parseDouble(scaleSpan.getText());
        assertThat(scale).isGreaterThan(1.0);
    }
}
