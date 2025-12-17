package cn.ianzhang;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @Author: Ian
 * @Description: PageObject
 */
public class PageObjectLoginPage {
    //  默认站点链接
    private static final String baseLink = "https://www.ianzhang.cn/s4ia/04-engineering-automation/login-sample.html";
    //  username定义
    private final By usernameBy = By.id("username");
    //  password定义
    private final By passwordBy = By.id("password");
    //  登录按钮定义
    private final By loginBy = By.id("login-btn");
    //  重置按钮定义
    private final By resetBy = By.id("reset-btn");
    //  结果段落定义
    private final By resultBy = By.id("result-p");
    //  标签定义
    private final By labelBy = By.tagName("label");
    private WebDriver driver;

    /**
     * 通过构造器传入driver
     */
    public PageObjectLoginPage(WebDriver driver) {
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
