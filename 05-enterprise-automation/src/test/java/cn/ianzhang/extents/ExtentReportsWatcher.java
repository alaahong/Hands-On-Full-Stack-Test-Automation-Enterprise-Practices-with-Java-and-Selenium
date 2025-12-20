package cn.ianzhang.extents;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

/**
 * @Author: Ian
 * @Description:
 */
public class ExtentReportsWatcher implements TestWatcher {
    private final ExtentReports extent;

    public ExtentReportsWatcher(ExtentReports extent) {
        this.extent = extent;
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        ExtentTest test = extent.createTest(context.getDisplayName(), "Test Successfully");
        test.log(Status.PASS, context.getDisplayName());
        extent.flush();
    }
}
