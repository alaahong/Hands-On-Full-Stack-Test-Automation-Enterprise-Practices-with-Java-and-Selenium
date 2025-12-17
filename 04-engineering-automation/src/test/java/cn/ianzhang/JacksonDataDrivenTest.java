package cn.ianzhang;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Ian
 * @Description:
 */
public class JacksonDataDrivenTest extends WebDriverBase {
    @BeforeEach
    void setUp() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html");
    }

    @Test
    public void jsonDataTest() throws IOException {
        //    定义Json工厂，这也是Jackson的默认实现
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        loadDataThenCompareWithWebPage(objectMapper, "src/test/resources/book-order.json");
    }

    @Test
    public void yamlDataTest() throws IOException {
        //    定义YAML工厂
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        loadDataThenCompareWithWebPage(objectMapper, "src/test/resources/book-order.yaml");
    }


    @Test
    public void propertiesDataTest() throws IOException {
        //    定义JavaProperties工厂
        ObjectMapper objectMapper = new ObjectMapper(new JavaPropsFactory());
        loadDataThenCompareWithWebPage(objectMapper, "src/test/resources/book-order.properties");
    }

    @Test
    public void tomlDataTest() throws IOException {
        //    定义TOML工厂
        ObjectMapper objectMapper = new ObjectMapper(new TomlFactory());
        loadDataThenCompareWithWebPage(objectMapper, "src/test/resources/book-order.toml");
    }

    @Test
    public void xmlDataTest() throws IOException {
        //    创建XML映射实例
        ObjectMapper objectMapper = new XmlMapper();
        loadDataThenCompareWithWebPage(objectMapper, "src/test/resources/book-order.xml");
    }


    private void loadDataThenCompareWithWebPage(ObjectMapper objectMapper, String path) throws IOException {
        //    注册jsr310模块
        objectMapper.findAndRegisterModules();
        //    读取数据文件
        Order order = objectMapper.readValue(new File(path), Order.class);
        //    基于页面元素生成订单对象
        Order expectOrder = fetchOrder();
        //    期望页面元素符合预期
        Assertions.assertThat(order).isEqualTo(expectOrder);
    }

    private Order fetchOrder() {
        //    抓取订单号
        WebElement orderNumberElement = driver.findElement(By.id("order-number"));
        String orderNumber = orderNumberElement.getText();
        //    抓取订单日期
        WebElement orderDateElement = driver.findElement(By.id("order-date"));
        //    将基于ISO日期的文本转换为LocalDate对象
        LocalDate orderDate = LocalDate.parse(orderDateElement.getText()
                , DateTimeFormatter.ISO_DATE);
        //    逐行抓取书籍信息
        List<WebElement> bookDetailRows = driver
                .findElements(By.xpath("//table[@id='order-table']//tr[not(th)]"));
        //    构造订单对象
        return buildOrder(orderNumber, orderDate, buildBookList(bookDetailRows));
    }

    private List<Book> buildBookList(List<WebElement> bookDetailRows) {
        //    遍历转换书籍对象
        return bookDetailRows
                .stream()
                .map(row -> {
                    //              基于行抓取对应子元素
                    List<WebElement> fields = row.findElements(By.tagName("td"));
                    return new Book(
                            fields.get(0).getText()
                            , fields.get(1).getText()
                            , fields.get(2).getText());
                })
                .collect(Collectors.toList());
    }

    private Order buildOrder(String orderNumber, LocalDate orderDate, List<Book> bookDetailRows) {
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setOrderDate(orderDate);
        order.setBookDetails(bookDetailRows);
        return order;
    }

    private void printXml(Order order) throws JsonProcessingException {
        ObjectMapper xmlMapper = new XmlMapper();
        xmlMapper.findAndRegisterModules();
        xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        System.out.println(xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(order));
    }
}
