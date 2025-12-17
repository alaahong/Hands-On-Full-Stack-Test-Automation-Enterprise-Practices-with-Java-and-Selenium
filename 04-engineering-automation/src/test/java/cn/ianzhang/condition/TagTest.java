package cn.ianzhang.condition;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * @Author: Ian
 * @Description:
 */
@Tag("tagging")
public class TagTest {

    @Test
    @Tag("tagging-01")
    @Tag("TC-01")
    public void firstTest(TestInfo testInfo) {
        System.out.println("firstTest");
    }

    @Test
    @Tag("tagging-01")
    @Tag("TC-02")
    public void secondTest() {
        System.out.println("secondTest");
    }

    @Test
    @Tag("tagging-02")
    @Tag("TC-03")
    public void thirdTest() {
        System.out.println("thirdTest");
    }
}
