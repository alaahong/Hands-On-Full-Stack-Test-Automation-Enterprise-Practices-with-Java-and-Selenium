package cn.ianzhang;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @Author: Ian
 * @Description:
 */
public class CustomizedAnnotationDataDriverTest extends WebDriverBase {

    @ParameterizedTest
    @JsonFileSource(file = {"src/test/resources/book-order-1.json", "src/test/resources/book-order-2.json"})
    public void annotationTest(List<Order> orders) {
        orders.forEach(this::process);
    }

    private void process(Order order) {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html");
        //    抓取订单号
        WebElement orderNumberElement = driver.findElement(By.id("order-number"));
        String orderNumber = orderNumberElement.getText();
        //    期望目标元素的值等于获取的参数值
        Assertions.assertThat(order.getOrderNumber()).isEqualTo(orderNumber);
    }
}
