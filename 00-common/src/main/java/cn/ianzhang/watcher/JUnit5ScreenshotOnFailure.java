package cn.ianzhang.watcher;

import cn.ianzhang.utils.ScreenshotUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.TakesScreenshot;

import java.util.Optional;

public class JUnit5ScreenshotOnFailure implements TestWatcher {
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Optional<Object> instance = context.getTestInstance();
        String cls = context.getTestClass().map(Class::getSimpleName).orElse("UnknownClass");
        String method = context.getTestMethod().map(m -> m.getName()).orElse("unknownTest");

        instance.ifPresent(obj -> {
            if (obj instanceof TakesScreenshot) {
                ScreenshotUtil.take(((TakesScreenshot) obj), cls, method);
            }
        });
    }
}