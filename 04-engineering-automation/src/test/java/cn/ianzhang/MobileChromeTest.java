package cn.ianzhang;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @Author: Ian
 * @Description:
 */
@Disabled
public class MobileChromeTest extends WebDriverBase {
    @Test
    public void openBaiduTest() {
        driver.get("https://www.bing.com");
        System.out.println();
    }
}
