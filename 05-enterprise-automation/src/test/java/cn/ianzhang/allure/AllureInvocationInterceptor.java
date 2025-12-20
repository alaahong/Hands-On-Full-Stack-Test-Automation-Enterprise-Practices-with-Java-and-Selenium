package cn.ianzhang.allure;

import cn.ianzhang.WebDriverGetter;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: Ian
 */
public class AllureInvocationInterceptor implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
                                    ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext context) throws Throwable {
        AtomicReference<Throwable> throwable = new AtomicReference<>();
        try {
            invocation.proceed();
        } catch (Throwable t) {
            System.out.printf("Failed method %s", context.getDisplayName());
            Optional<TestInstances> testInstances = context.getTestInstances();
            testInstances.ifPresent(instances -> instances.getAllInstances().forEach(
                    instance -> {
                        if (
                                instance instanceof WebDriverGetter &&
                                        (((WebDriverGetter) instance).getWebDriver() instanceof TakesScreenshot)) {
                            testNegative(context
                                    , ((WebDriverGetter) instance).getWebDriver()
                                    , t);
                        }
                    }
            ));
            throwable.set(t);
        }
        Throwable t = throwable.get();
        if (t != null) {
            throw t;
        }
    }

    private void testNegative(ExtensionContext context, WebDriver driver, Throwable throwable) {
        Allure.description(context.getDisplayName() + " failed");
        takeScreenshot(driver);
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] takeScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
