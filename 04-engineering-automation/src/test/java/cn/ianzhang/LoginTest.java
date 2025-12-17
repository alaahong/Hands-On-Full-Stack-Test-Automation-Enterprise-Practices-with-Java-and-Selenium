package cn.ianzhang;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;

/**
 * @Author: Ian
 * @Description:
 */
public class LoginTest extends WebDriverBase {
    /**
     * Before Page Object
     */
    @Test
    public void loginWithoutPageObjectTest() {
        //    打开目标页面
        driver.get("https://www.ianzhang.cn/s4ia/04-engineering-automation/login-sample.html");
        //    输入无效的用户名
        driver.findElement(By.id("username")).sendKeys("invalid");
        //    输入无效的密码
        driver.findElement(By.id("password")).sendKeys("invalid");
        //    点击登录按钮
        driver.findElement(By.id("login-btn")).click();
        //    期望结果为"invalid username or password"
        Assertions.assertThat(driver.findElement(By.id("result-p")).getText())
                .isEqualTo("invalid username or password");
        //    点击重置按钮
        driver.findElement(By.id("reset-btn")).click();
        //    输入正确的用户名
        driver.findElement(By.id("username")).sendKeys("ian");
        //    输入正确的密码
        driver.findElement(By.id("password")).sendKeys("12345678");
        //    点击登录按钮
        driver.findElement(By.id("login-btn")).click();
        //    期望登录结果为"Hello Ian"
        Assertions.assertThat(driver.findElement(By.id("result-p")).getText())
                .isEqualTo("Hello Ian");
    }

    @Test
    public void loginWithPageObjectTest() {
        //    初始化页面对象
        PageObjectLoginPage loginPage = new PageObjectLoginPage(driver);
        //    加载页面
        loginPage.load();
        //    以无效的用户名密码进行登陆
        String loginMessage1 = loginPage.login("invalid", "invalid");
        //    期望登录结果为"invalid username or password"
        Assertions.assertThat(loginMessage1)
                .isEqualTo("invalid username or password");
        //    以有效的用户名密码进行登陆
        String loginMessage2 = loginPage.login("ian", "12345678");
        //    期望登录结果为"Hello Ian"
        Assertions.assertThat(loginMessage2)
                .isEqualTo("Hello Ian");
        //    打印所有的标签
        loginPage.printLabels();
    }

    @Test
    public void pageFactoryLoginPageTest() {
        //    初始化页面对象
        PageFactoryLoginPage loginPage = new PageFactoryLoginPage(driver);
        //    加载页面
        loginPage.load();
        //    以无效的用户名密码进行登陆
        String loginMessage1 = loginPage.login("invalid", "invalid");
        //    期望登录结果为"invalid username or password"
        Assertions.assertThat(loginMessage1)
                .isEqualTo("invalid username or password");
        //    再次加载页面
        loginPage.load();
        //    以有效的用户名密码进行登陆
        String loginMessage2 = loginPage.login("ian", "12345678");
        //    期望登录结果为"Hello Ian"
        Assertions.assertThat(loginMessage2)
                .isEqualTo("Hello Ian");
        //    打印所有的标签
        loginPage.printLabels();
    }

    @Test
    public void ajaxElementLocatorFactoryLoginPageTest() {
        //    初始化AjaxElementLocatorFactory, 超时时间为30秒
        AjaxElementLocatorFactoryLoginPage ajaxLoginPage = new AjaxElementLocatorFactoryLoginPage(driver);
        //    加载页面
        ajaxLoginPage.load();
        //    因为页面加载5秒后会在页面增加目标元素,所以打印加载日期无异常
        ajaxLoginPage.printLoadDate();
        //    初始化PageFactory, 默认初始化DefaultElementLocatorFactory
        PageFactoryLoginPage loginPage = new PageFactoryLoginPage(driver);
        //    加载页面
        loginPage.load();
        //    因为页面加载5秒后才会在页面增加目标元素,所以打印加载日期抛出异常
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(loginPage::printLoadDate);
    }

    @Test
    public void loadableComponentLoginPageTest() {
        //    初始化LoadableComponent PageFactory
        LoadableComponentLoginPage loginPage = new LoadableComponentLoginPage(driver);
        //    加载页面
        loginPage.load();
        //    再次加载页面, 反映出页面元素初始化是懒加载,既当调用时才会查找元素
        loginPage.load();
        //    以无效的用户名密码进行登陆
        String loginMessage1 = loginPage.login("invalid", "invalid");
        //    期望登录结果为"invalid username or password"
        Assertions.assertThat(loginMessage1)
                .isEqualTo("invalid username or password");
        //    以有效的用户名密码进行登陆
        String loginMessage2 = loginPage.login("ian", "12345678");
        //    期望登录结果为"Hello Ian"
        Assertions.assertThat(loginMessage2)
                .isEqualTo("Hello Ian");
        //    打印所有的标签
        loginPage.printLabels();
        //    再次加载页面
        loginPage.load();
        //    期望抛出异常,因为页面刷新后元素不再存于当前页面
        Assertions.assertThatExceptionOfType(StaleElementReferenceException.class)
                .isThrownBy(loginPage::isLoaded)
                .withMessageContaining("element is not attached to the page document");
    }
}
