package cn.ianzhang.extents;

import cn.ianzhang.WebDriverGetter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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
public class ExtentReportsInvocationInterceptor implements InvocationInterceptor {

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
                                        instance instanceof ExtentReportsGetter &&
                                        (((WebDriverGetter) instance).getWebDriver() instanceof TakesScreenshot)) {
                            testNegative(context
                                    , ((ExtentReportsGetter) instance).getExtentReports()
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

    private void testNegative(ExtensionContext context, ExtentReports extent, WebDriver driver, Throwable throwable) {
        ExtentTest test = extent.createTest(context.getDisplayName(), "Test Failed");
        test.info(context.getDisplayName());
        test.log(Status.FAIL, throwable);
        test.addScreenCaptureFromBase64String(((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BASE64), "Get screenshot");
        extent.flush();
    }
}
