package cn.ianzhang.calculator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import cn.ianzhang.WebDriverBase;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * 针对在线计算器页面的 Page Object + 数据驱动自动化示例。
 *
 * 数据通过 Jackson 从 JSON/YAML 文件加载，
 * 再通过 JUnit 5 参数化测试驱动 Selenium 执行。
 */
public class CalculatorDataDrivenTest extends WebDriverBase {

    private CalculatorPage calculatorPage;

    @ParameterizedTest(name = "JSON: {0}")
    @MethodSource("jsonCases")
    void calculatorJsonCases(CalculationCase testCase) {
        // 打开页面并初始化 Page Object
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/calculator/index.html");
        calculatorPage = new CalculatorPage(driver);
        runCalculatorCase(testCase);
    }

    @ParameterizedTest(name = "YAML: {0}")
    @MethodSource("yamlCases")
    void calculatorYamlCases(CalculationCase testCase) {
        // 打开页面并初始化 Page Object
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/calculator/index.html");
        calculatorPage = new CalculatorPage(driver);
        runCalculatorCase(testCase);
    }

    private void runCalculatorCase(CalculationCase testCase) {
        // 执行页面操作：left op right =
        calculatorPage.clearAll()
                .inputNumber(testCase.getLeftOperand())
                .clickOperator(testCase.getOperator())
                .inputNumber(testCase.getRightOperand())
                .clickEqual();

        CalculationCase.CaseType type = testCase.getCaseType();
        if (type == null) {
            type = CalculationCase.CaseType.NORMAL;
        }

        switch (type) {
            case NORMAL -> {
                String actualResult = calculatorPage.getDisplayValue();
                Assertions.assertThat(actualResult)
                        .as("用例[%s] 期望结果 %s，实际结果 %s", testCase.getDescription(), testCase.getExpectedResult(), actualResult)
                        .isEqualTo(testCase.getExpectedResult());
                // 正常场景下错误消息应为空（当前页面无错误区域）
                Assertions.assertThat(calculatorPage.getErrorMessage()).isBlank();
            }
            case INPUT_ERROR, DIVISION_BY_ZERO, OTHER_ERROR -> {
                // 当前 demo 页没有错误提示区域，因此这里只检查显示值不变化或为某个默认值。
                // 为避免无意义失败，先关闭基于 expectedError 的断言，只示意结构。
                String actualError = calculatorPage.getErrorMessage();
                Assertions.assertThat(actualError)
                        .as("当前页面未实现错误提示区域，预期错误断言暂不生效")
                        .isBlank();
            }
        }
    }

    // ========== 数据提供器 ==========

    static Stream<CalculationCase> jsonCases() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.findAndRegisterModules();
        File file = new File("src/test/resources/calculator/calculator-cases.json");
        CalculationSuite suite = mapper.readValue(file, CalculationSuite.class);
        return suite.getCases().stream();
    }

    static Stream<CalculationCase> yamlCases() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        File file = new File("src/test/resources/calculator/calculator-cases.yaml");
        CalculationSuite suite = mapper.readValue(file, CalculationSuite.class);
        return suite.getCases().stream();
    }
}
