package cn.ianzhang.external;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @Author: Ian
 * @Description: PageObject
 */
public class ExternalPageObjectLoginPage {
    //  默认站点链接
    private static final String baseLink = "https://www.ianzhang.cn/s4ia/04-engineering-automation/login-sample.html";
    //  username定义
    private final By usernameBy = ByUtil.getInstance().lookup("LoginSample_Input_Username");
    //  password定义
    private final By passwordBy = ByUtil.getInstance().lookup("LoginSample_Input_Password");
    //  登录按钮定义
    private final By loginBy = ByUtil.getInstance().lookup("LoginSample_Button_LoginBtn");
    //  重置按钮定义
    private final By resetBy = ByUtil.getInstance().lookup("LoginSample_Button_ResetBtn");
    //  结果段落定义
    private final By resultBy = ByUtil.getInstance().lookup("LoginSample_P_ResultP");
    //  标签定义
    private final By labelBy = ByUtil.getInstance().lookup("LoginSample_Label_All");
    private final WebDriver driver;

    /**
     * 通过构造器传入driver
     */
    public ExternalPageObjectLoginPage(WebDriver driver) {
        this.driver = driver;
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
        driver.findElement(resetBy).click();
        driver.findElement(usernameBy).sendKeys(username);
        driver.findElement(passwordBy).sendKeys(password);
        driver.findElement(loginBy).click();
        return driver.findElement(resultBy).getText();
    }

    /**
     * 打印所有的标签
     */
    public void printLabels() {
        driver.findElements(labelBy)
                .stream()
                .map(WebElement::getText)
                .forEach(System.out::println);
    }
}
