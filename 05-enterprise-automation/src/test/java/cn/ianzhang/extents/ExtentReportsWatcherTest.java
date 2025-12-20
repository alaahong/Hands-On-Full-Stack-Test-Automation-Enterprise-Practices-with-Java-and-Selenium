package cn.ianzhang.extents;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * @Author: Ian
 * @Description:
 */
@Disabled
@ExtendWith(ExtentReportsInvocationInterceptor.class)
public class ExtentReportsWatcherTest extends ExtentReportsChromeBase {

    @RegisterExtension
    public ExtentReportsWatcher testWatcher
            = new ExtentReportsWatcher(extent);

    @Disabled
    @Test
    public void openThenAssertTitle() {
        driver.get("https://www.ianzhang.cn");
        Assertions.assertThat(driver.getTitle()).contains("Ian");
    }

    @Disabled
    @Test
    public void throwFailure() {
        driver.get("https://www.ianzhang.cn");
        throw new RuntimeException("A runtime exception");
    }
}