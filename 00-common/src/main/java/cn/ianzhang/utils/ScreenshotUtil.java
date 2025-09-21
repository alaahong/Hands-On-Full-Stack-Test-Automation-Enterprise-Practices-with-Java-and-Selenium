package cn.ianzhang.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS");

    private ScreenshotUtil() {}

    public static Path take(TakesScreenshot driver, String testClass, String testMethod) {
        if (driver == null) return null;
        if (!(driver instanceof TakesScreenshot)) return null;

        try {
            String baseDirProp = System.getProperty("screenshots.dir", "artifacts/screenshots");
            Path dir = Paths.get(baseDirProp, sanitize(testClass));
            Files.createDirectories(dir);

            String filename = sanitize(testMethod) + "-" + TS.format(LocalDateTime.now()) + ".png";
            Path target = dir.resolve(filename);

            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(target, png);

            System.out.println("[screenshot] " + target.toAbsolutePath());
            return target;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String sanitize(String s) {
        return s == null ? "unknown" : s.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}