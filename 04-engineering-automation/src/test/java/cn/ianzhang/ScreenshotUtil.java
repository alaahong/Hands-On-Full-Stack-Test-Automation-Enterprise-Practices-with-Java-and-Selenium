package cn.ianzhang;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @Author: Ian
 * @Description:
 */
public class ScreenshotUtil {

    public static void takeScreenshot(TakesScreenshot takesScreenshot, Path path) {
        try {
            File file = takesScreenshot.getScreenshotAs(OutputType.FILE);
            System.out.println("Screenshot generated at " + file.getAbsolutePath());
            if (Objects.nonNull(path)) {
                if (Files.isDirectory(path)) {
                    Files.copy(file.toPath(), path);
                } else {
                    Files.copy(file.toPath(), path);
                }
                System.out.println("Screenshot copied to " + path.toFile().getAbsolutePath());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void takeScreenshot(TakesScreenshot takesScreenshot) {
        takeScreenshot(takesScreenshot, null);
    }
}
