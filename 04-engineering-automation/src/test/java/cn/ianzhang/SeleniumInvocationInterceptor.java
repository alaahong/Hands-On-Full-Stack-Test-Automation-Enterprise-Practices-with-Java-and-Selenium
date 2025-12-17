package cn.ianzhang;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.openqa.selenium.TakesScreenshot;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: Ian
 * @Description:
 */
public class SeleniumInvocationInterceptor implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
                                    ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext context) throws Throwable {
        AtomicReference<Throwable> throwable = new AtomicReference<>();
        try {
            invocation.proceed();
        } catch (Throwable t) {
            System.out.println("Failed method " + context.getDisplayName());
            Optional<TestInstances> testInstances = context.getTestInstances();
            testInstances.ifPresent(instances -> instances.getAllInstances().forEach(
                    instance -> {
                        if (instance instanceof WebDriverGetter
                                && (((WebDriverGetter) instance).getWebDriver() instanceof TakesScreenshot)) {
                            ScreenshotUtil.takeScreenshot((TakesScreenshot) ((WebDriverGetter) instance).getWebDriver());
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
}
