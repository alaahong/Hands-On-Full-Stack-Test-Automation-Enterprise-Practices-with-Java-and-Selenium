package cn.ianzhang;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

/**
 * @Author: Ian
 * @Description: AjaxElementLocatorFactory
 */
public class AjaxElementLocatorFactoryLoginPage {
    private static final String baseLink = "https://www.ianzhang.cn/s4ia/04-engineering-automation/login-sample.html";
    private WebDriver driver;
    @FindBy(id = "load-date")
    private WebElement loadDate;

    /**
     * 通过构造器传入driver
     */
    public AjaxElementLocatorFactoryLoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 60), this);
    }

    /**
     * 加载目标页面
     */
    public void load() {
        driver.get(baseLink);
    }

    /**
     * 打印延迟加载的日期
     */
    public void printLoadDate() {
        System.out.println(loadDate.getText());
    }
}
