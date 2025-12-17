package cn.ianzhang.condition;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * @Author: Ian
 * @Description:
 */
public class ParallelTest {

    @Execution(ExecutionMode.SAME_THREAD)
    @RepeatedTest(value = 5)
    void sameThreadTest(TestInfo testInfo, RepetitionInfo repetitionInfo) {
        System.out.println(testInfo.getTestMethod().get().getName()
                + " - " + repetitionInfo.getCurrentRepetition()
                + "/" + repetitionInfo.getTotalRepetitions());
    }

    @Execution(ExecutionMode.CONCURRENT)
    @RepeatedTest(value = 5)
    void concurrentTest(TestInfo testInfo, RepetitionInfo repetitionInfo) {
        System.out.println(testInfo.getTestMethod().get().getName()
                + " - " + repetitionInfo.getCurrentRepetition()
                + "/" + repetitionInfo.getTotalRepetitions());
    }
}
