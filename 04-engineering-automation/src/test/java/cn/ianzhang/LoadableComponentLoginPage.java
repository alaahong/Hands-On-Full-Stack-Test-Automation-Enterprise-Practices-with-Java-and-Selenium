package cn.ianzhang;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;

/**
 * @Author: Ian
 * @Description: LoadableComponent
 */
public class LoadableComponentLoginPage extends LoadableComponent<LoadableComponentLoginPage> {
    //  默认站点链接
    private static final String baseLink = "https://www.ianzhang.cn/s4ia/04-engineering-automation/login-sample.html";
    //  username定义
    @FindBy(how = How.ID, using = "username")
    @CacheLookup
    private WebElement usernameInput;
    //  password定义
    @FindBy(how = How.ID, using = "password")
    @CacheLookup
    private WebElement passwordInput;
    //  登录按钮定义
    @FindBy(id = "login-btn")
    @CacheLookup
    private WebElement loginBtn;
    //  重置按钮定义
    @FindBy(id = "reset-btn")
    @CacheLookup
    private WebElement resetBtn;
    //  结果段落定义
    @FindBys({
            @FindBy(id = "parent-div"),
            @FindBy(id = "result-p")
    })
    @CacheLookup
    private WebElement result;
    //  标签定义
    @FindAll({
            @FindBy(tagName = "label")
    })
    private List<WebElement> labelList;
    private WebDriver driver;

    /**
     * 通过构造器传入driver
     */
    public LoadableComponentLoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
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
     * 加载目标页面
     */
    @Override
    public void load() {
        driver.get(baseLink);
    }

    /**
     * 判断是否加载成功
     *
     * @throws Error
     */
    @Override
    protected void isLoaded() throws Error {
        if (!resetBtn.isEnabled())
            throw new Error("No Loaded");
    }
}
