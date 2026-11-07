package cn.ianzhang.allure;

import cn.ianzhang.WebDriverBase;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @Author: Ian
 */
@Epic("Selenium In Action")
@Feature("Allure Auto Report")
@ExtendWith(AllureInvocationInterceptor.class)
@ExtendWith(AllureWatcher.class)
public class AllureWatcherTest extends WebDriverBase {

    @Test
    @DisplayName("Open Then Assert Title")
    public void openThenAssertTitle() {
        driver.get("https://www.ianzhang.cn");
        Assertions.assertThat(driver.getTitle()).contains("手中执剑,方能保护所爱之人");
    }

    @Disabled("演示 Allure 对失败用例的截图与报告，默认禁用")
    @Test
    @DisplayName("Throw Failure")
    public void throwFailure() {
        driver.get("https://www.ianzhang.cn");
        throw new RuntimeException("A runtime exception");
    }
}