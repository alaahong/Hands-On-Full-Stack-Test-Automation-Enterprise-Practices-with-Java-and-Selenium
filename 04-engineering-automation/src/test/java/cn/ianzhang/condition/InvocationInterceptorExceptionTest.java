package cn.ianzhang.condition;

import cn.ianzhang.SeleniumInvocationInterceptor;
import cn.ianzhang.WebDriverBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

/**
 * @Author: Ian
 * @Description:
 */
@ExtendWith(SeleniumInvocationInterceptor.class)
public class InvocationInterceptorExceptionTest extends WebDriverBase {

    @Test
    public void tryCatchExceptionTest() {
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/exception.html");
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> driver.findElement(By.id("nobody")));
    }
}
