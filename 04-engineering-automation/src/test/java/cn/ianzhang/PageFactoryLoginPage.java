package cn.ianzhang;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * @Author: Ian
 * @Description: PageFactory
 */
public class PageFactoryLoginPage {
    //  默认站点链接
    private static final String baseLink = "https://www.ianzhang.cn/s4ia/04-engineering-automation/login-sample.html";

    //  username定义
    @FindBy(how = How.ID, using = "username")
    private WebElement usernameInput;
    //  password定义
    @FindBy(how = How.ID, using = "password")
    private WebElement passwordInput;
    //  登录按钮定义
    @FindBy(id = "login-btn")
    private WebElement loginBtn;
    //  重置按钮定义
    @FindBy(id = "reset-btn")
    private WebElement resetBtn;
    //  结果段落定义
    @FindBys({
            @FindBy(id = "parent-div"),
            @FindBy(id = "result-p")
    })
    private WebElement result;
    //  标签定义
    @FindAll({
            @FindBy(tagName = "label")
    })
    private List<WebElement> labelList;
    //  加载时间定义
    @FindBy(id = "load-date")
    private WebElement loadDate;
    private WebDriver driver;

    /**
     * 通过构造器传入driver
     */
    public PageFactoryLoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * 加载目标页面
     */
    public void load() {
        driver.get(baseLink);
    }

    /**
     * 执行登录操作
     *
     * @param username
     * @param password
     * @return 登录结果
     */
    public String login(String username, String password) {
        resetBtn.click();
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginBtn.click();
        return result.getText();
    }

    /**
     * 打印所有的标签
     */
    public void printLabels() {
        labelList.stream()
                .map(WebElement::getText)
                .forEach(System.out::println);
    }

    /**
     * 打印延迟加载的日期
     */
    public void printLoadDate() {
        System.out.println(loadDate.getText());
    }
}
