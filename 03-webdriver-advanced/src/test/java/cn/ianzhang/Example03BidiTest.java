package cn.ianzhang;


import cn.ianzhang.annotation.Retry;
import cn.ianzhang.extension.RetryExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.bidi.browsingcontext.BrowsingContext;
import org.openqa.selenium.bidi.browsingcontext.CreateContextParameters;
import org.openqa.selenium.bidi.module.Script;
import org.openqa.selenium.bidi.script.EvaluateResult;
import org.openqa.selenium.bidi.script.EvaluateResultSuccess;
import org.openqa.selenium.bidi.script.ResultOwnership;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @Author: Ian
 */
@ExtendWith(RetryExtension.class)
@Retry(3)
public class Example03BidiTest extends ChromeBase {
    @Test
    void createWindowTest() {
        //    利用WebDriver创建新窗口
        driver.switchTo().newWindow(WindowType.WINDOW);
        //    利用Bidi协议创建新窗口
        BrowsingContext browsingContext = new BrowsingContext(driver, WindowType.WINDOW);
        Assertions.assertNotEquals(driver.getWindowHandle(), browsingContext.getId());
    }

    @Test
    void createTabTest() {
        //    利用WebDriver创建新窗口
        driver.switchTo().newWindow(WindowType.TAB);
        //    利用Bidi协议创建新窗口
        BrowsingContext browsingContext = new BrowsingContext(driver, WindowType.TAB);
        Assertions.assertNotEquals(driver.getWindowHandle(), browsingContext.getId());
    }

    @Test
    void reuseWindowIdTest() {
        String id = driver.getWindowHandle();
        BrowsingContext browsingContext = new BrowsingContext(driver, id);
        Assertions.assertEquals(id, browsingContext.getId());
    }

    @Test
    void testCreateAWindowWithAReferenceContext() {
        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/browsing-context.html");
        CreateContextParameters createContextParameters = new CreateContextParameters(WindowType.WINDOW);
        createContextParameters.referenceContext(driver.getWindowHandle());
        BrowsingContext browsingContext = new BrowsingContext(driver, createContextParameters);
        browsingContext.navigate("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/browsing-context.html");
        driver.findElement(By.id("post-message-btn")).click();
        try (Script script = new Script(browsingContext.getId(), driver)) {
            EvaluateResult result =
                    script.callFunctionInBrowsingContext(browsingContext.getId()
                            , "function test(){ return  document.getElementById('post-message-textarea').textContent ;}"
                            , true
                            , Optional.of(new ArrayList<>())
                            , Optional.empty()
                            , Optional.of(ResultOwnership.ROOT));
            Assertions.assertInstanceOf(EvaluateResultSuccess.class, result);
            if (result instanceof EvaluateResultSuccess successResult) {
                Assertions.assertEquals(successResult.getResult().getValue().get(), "Hello Selenium");
            }
        }
    }

}
