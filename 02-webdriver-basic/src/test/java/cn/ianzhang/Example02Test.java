package cn.ianzhang;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;

/**
 * @Author: Ian
 * @Date: 2024/7/22 22:49
 * @Description:
 */
public class Example02Test extends ChromeBase {
  /**
   * 例程2.1  打开百度网站首页
   */
  @Test
  public void openWebPageTest() {
    //    两种方式分别打开https://www.baidu.com
    driver.get("https://www.baidu.com");
    driver.navigate().to("https://www.baidu.com");
  }

  /**
   * 例程2.2  打开重定向页面
   */
  @Test
  public void getTest() {
    //    打开第一个页面get.html
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/get.html");
    //    当打开页面操作结束时，期望当前页面为最终重定向的get3.html
    Assertions.assertThat(driver.getCurrentUrl())
            .endsWith("get3.html");
    //    同时判断当前页面不为get.html或get2.html
    Assertions.assertThat(driver.getCurrentUrl())
            .doesNotEndWith("get.html")
            .doesNotEndWith("get2.html");
  }

  /**
   * 例程2.3  完全限定URL
   */
  @Test
  public void fullyQualifiedUrlTest() {
    //    传入正常的网址，但是不符合完全限定URL的要求，期望抛出异常InvalidArgumentException
    Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
            .isThrownBy(() -> driver.get("www.baidu.com"));
    //    传入完全限定URL后，可以正常访问
    driver.get("https://www.baidu.com/");
    //    满足格式要求的参数都可以正常传入
    //    例如Chrome下可以访问chrome://version/
    //    // EDGE浏览器可以访问edge://version/
    driver.get("chrome://version/");
    //    浏览器有自动补全机制，例如以下传入参数被自动追加了斜杠
    driver.get("https://www.baidu.com");
    Assertions.assertThat(driver.getCurrentUrl())
            .isEqualTo("https://www.baidu.com/");
    //    localhost:port这样的参数需特别注意，即使资源不存在，也不会抛出异常
    driver.get("localhost:1234");
    //    但是其他相近情况依然会抛出异常
    Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
            .isThrownBy(() -> driver.get("127.0.0.1:1234"));
    Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
            .isThrownBy(() -> driver.get("localhost"));
  }

  /**
   * 例程2.4  获取当前URL
   */
  @Test
  public void getCurrentUrlTest() {
    //    期望返回值包括浏览器地址栏可见的Get参数
    driver.get("https://www.baidu.com/s?wd=selenium");
    Assertions.assertThat(driver.getCurrentUrl())
            .isEqualTo("https://www.baidu.com/s?wd=selenium");
    //    期望返回值包括浏览器自动补全的斜杠
    driver.get("chrome://settings");
    Assertions.assertThat(driver.getCurrentUrl())
            .isEqualTo("chrome://settings/");
  }

  /**
   * 例程2.5  后退与前进
   */
  @Test
  public void testBackAndForward() {
    //    依次打开三个地址不同的页面
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#first");
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#second");
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#third");
    //    执行后退操作
    driver.navigate().back();
    //    期望页面后退到第二个地址
    Assertions.assertThat(driver.getCurrentUrl())
            .isEqualTo("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#second");
    //    执行前进操作
    driver.navigate().forward();
    //    期望页面前进到第三个地址
    Assertions.assertThat(driver.getCurrentUrl())
            .isEqualTo("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#third");
    //    点击链接再次跳转到第一个地址
    driver.findElement(By.xpath("//*[@id='firstLink']")).click();
    //    点击链接再次跳转到第二个地址
    driver.findElement(By.xpath("//*[@id='secondLink']")).click();
    //    期望页面为第二个地址
    Assertions.assertThat(driver.getCurrentUrl())
            .isEqualTo("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#second");
    //    执行前进操作
    driver.navigate().forward();
    //    因为当前页面没有执行过后退操作
    //    所以前进操作没有实际行为
    //    期望页面无变化
    Assertions.assertThat(driver.getCurrentUrl())
            .isEqualTo("https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#second");
  }

  /**
   * 例程2.6  刷新当前页面
   */
  @Test
  public void refreshTest() {
    //    打开目标页面
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/refresh.pdf");
    //    刷新当前页面
    driver.navigate().refresh();
  }

  /**
   * 例程2.7  获取页面标题
   */
  @Test
  public void titleTest() {
    //    获取页面的title属性，期望等于“标题”
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/normal_title.html");
    Assertions.assertThat(driver.getTitle()).isEqualTo("标题");
    //    获取页面的title属性，期望为空
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/no_title.html");
    Assertions.assertThat(driver.getTitle()).isEqualTo("");
    Assertions.assertThat(driver.getTitle()).isEmpty();
    //    获取页面的title属性，期望不包含前后的空格，等于“标 题”
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/space_title.html");
    Assertions.assertThat(driver.getTitle()).isEqualTo("标 题");
  }

  /**
   * 例程2.8  获取页面源码
   */
  @Test
  public void pageSourceTest() {
    //    打开目标页面后，执行获取页面源码方法
    driver.get("https://www.ianzhang.cn/s4ia/02-webdriver-basic/refresh.pdf");
    driver.getPageSource();
  }

}
