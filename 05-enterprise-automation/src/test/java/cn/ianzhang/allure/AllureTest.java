package cn.ianzhang.allure;

import cn.ianzhang.WebDriverBase;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.model.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * @Author: Ian
 */
@Epic("Selenium In Action")
@Feature("Allure Report")
public class AllureTest extends WebDriverBase {

    @Test
    @Story("openThenAssertTitle")
    @DisplayName("Open Then Assert Title")
    public void openThenAssertTitle() {
        driver.get("https://www.ianzhang.cn");
        Allure.step("Open WebSite", Status.PASSED);
        Assertions.assertThat(driver.getTitle()).contains("手中执剑,方能保护所爱之人");
        Allure.step("Assert Title"
                , driver.getTitle().contains("手中执剑,方能保护所爱之人")
                        ? Status.PASSED : Status.FAILED
        );
        takeScreenshot();
        Allure.step("Take Screenshot", Status.PASSED);
    }

    @Test
    @Story("assertTrue")
    @DisplayName("Assert True")
    public void assertTrue() {
        Assertions.assertThat(Boolean.TRUE).isTrue();
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
