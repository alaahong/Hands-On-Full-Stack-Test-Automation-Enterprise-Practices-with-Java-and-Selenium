package cn.ianzhang;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
}
