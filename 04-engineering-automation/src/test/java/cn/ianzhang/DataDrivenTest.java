package cn.ianzhang;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.Stream;

/**
 * @Author: Ian
 * @Description:
 */
public class DataDrivenTest extends WebDriverBase {

    private static String[] methodSourceProvider() {
        Faker faker = new Faker();
        return new String[]{faker.name().fullName(), faker.name().fullName(), faker.name().fullName()};
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"ian", "zhe", "zhang"})
    public void valueSourceTest(String name) {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html?name=" + name);
        //    点击按钮获取参数值
        driver.findElement(By.id("query-name-btn")).click();
        //    期望目标元素的值等于获取的参数值
        Assertions.assertThat(driver.findElement(By.id("query-name"))
                        .getAttribute("value"))
                .isEqualTo(name);
    }

    @ParameterizedTest
    @EnumSource(names = {"IAN", "ZHANG"})
    public void valueSourceEnumTest(NameEnum name) {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html?name=" + name);
        //    点击按钮获取参数值
        driver.findElement(By.id("query-name-btn")).click();
        //    期望目标元素的值等于获取的参数值
        Assertions.assertThat(driver.findElement(By.id("query-name"))
                        .getAttribute("value"))
                .isEqualTo(name.name());
    }

    @ParameterizedTest
    @MethodSource("methodSourceProvider")
    public void methodSourceTest(String name) {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html?name=" + name);
        //    点击按钮获取参数值
        driver.findElement(By.id("query-name-btn")).click();
        //    期望目标元素的值等于获取的参数值
        Assertions.assertThat(driver.findElement(By.id("query-name"))
                        .getAttribute("value"))
                .isEqualTo(name);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "ian, ian",
            "zhe, zhe",
            "zhang, zhang",
    }
    )
    public void csvSourceTest(String name) {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html?name=" + name);
        //    点击按钮获取参数值
        driver.findElement(By.id("query-name-btn")).click();
        //    期望目标元素的值等于获取的参数值
        Assertions.assertThat(driver.findElement(By.id("query-name"))
                        .getAttribute("value"))
                .isEqualTo(name);
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/data-driven-sample.csv", numLinesToSkip = 1)
    public void csvFileSourceTest(String name, String target) {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html?name=" + name);
        //    点击按钮获取参数值
        driver.findElement(By.id("query-name-btn")).click();
        //    期望目标元素的值等于获取的参数值
        Assertions.assertThat(driver.findElement(By.id("query-name"))
                        .getAttribute("value"))
                .isEqualTo(target);
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/data-driven-sample.csv", numLinesToSkip = 1)
    public void csvFileSourceTest(ArgumentsAccessor arguments) {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html?name=" + arguments.getString(0));
        //    点击按钮获取参数值
        driver.findElement(By.id("query-name-btn")).click();
        //    期望目标元素的值等于获取的参数值
        Assertions.assertThat(driver.findElement(By.id("query-name"))
                        .getAttribute("value"))
                .isEqualTo(arguments.getString(1));
    }

    @ParameterizedTest
    @ArgumentsSource(CustomizedArgumentsProvider.class)
    public void argumentsSourceTest(String name) {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html?name=" + name);
        //    点击按钮获取参数值
        driver.findElement(By.id("query-name-btn")).click();
        //    期望目标元素的值等于获取的参数值
        Assertions.assertThat(driver.findElement(By.id("query-name"))
                        .getAttribute("value"))
                .isEqualTo(name);
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/data-driven-sample.csv", numLinesToSkip = 1)
    public void argumentsAggregatorTest(@AggregateWith(NameAggregator.class) NameObject nameObject) {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html?name=" + nameObject.getName());
        //    点击按钮获取参数值
        driver.findElement(By.id("query-name-btn")).click();
        //    期望目标元素的值等于获取的参数值
        Assertions.assertThat(driver.findElement(By.id("query-name"))
                        .getAttribute("value"))
                .isEqualTo(nameObject.getTarget());
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/data-driven-sample.csv", numLinesToSkip = 1)
    public void argumentsAggregatorAnnotationTest(@CsvToName NameObject nameObject) {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html?name=" + nameObject.getName());
        //    点击按钮获取参数值
        driver.findElement(By.id("query-name-btn")).click();
        //    期望目标元素的值等于获取的参数值
        Assertions.assertThat(driver.findElement(By.id("query-name"))
                        .getAttribute("value"))
                .isEqualTo(nameObject.getTarget());
    }

    enum NameEnum {
        IAN("IAN"), ZHE("ZHE"), ZHANG("ZHANG");
        private final String name;

        NameEnum(String name) {
            this.name = name;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AggregateWith(NameAggregator.class)
    public @interface CsvToName {
    }

    static class CustomizedArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            Faker faker = new Faker();
            return Stream
                    .of(faker.name().fullName(), faker.name().fullName(), faker.name().fullName())
                    .map(Arguments::of);
        }
    }

    static class NameAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor arguments, ParameterContext context) {
            NameObject name = new NameObject();
            name.setName(arguments.getString(0));
            name.setTarget(arguments.getString(1));
            return name;
        }
    }

    static class NameObject {
        String name;
        String target;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }
    }
}
