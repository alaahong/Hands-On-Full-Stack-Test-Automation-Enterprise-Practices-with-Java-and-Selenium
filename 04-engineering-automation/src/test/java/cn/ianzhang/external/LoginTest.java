package cn.ianzhang.external;

import cn.ianzhang.WebDriverBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @Author: Ian
 * @Description:
 */
public class LoginTest extends WebDriverBase {
    @Test
    public void loginWithExternalPageObjectTest() {
        //    初始化页面对象
        ExternalPageObjectLoginPage loginPage = new ExternalPageObjectLoginPage(driver);
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

}
