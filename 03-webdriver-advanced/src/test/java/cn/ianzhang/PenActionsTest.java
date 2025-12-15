package cn.ianzhang;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebDriver;

class PenActionsTest extends WebDriverBase {

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
