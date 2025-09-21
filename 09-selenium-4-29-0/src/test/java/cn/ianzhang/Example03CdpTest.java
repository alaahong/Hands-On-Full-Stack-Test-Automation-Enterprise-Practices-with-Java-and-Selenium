package cn.ianzhang;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chromium.ChromiumNetworkConditions;
import org.openqa.selenium.chromium.HasCdp;
import org.openqa.selenium.chromium.HasNetworkConditions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v85.dom.model.RGBA;
import org.openqa.selenium.devtools.v85.emulation.Emulation;
import org.openqa.selenium.devtools.v85.performance.Performance;
import org.openqa.selenium.devtools.v85.performance.model.Metric;
import org.openqa.selenium.remote.Augmenter;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: Ian
 */
public class Example03CdpTest extends ChromeRemoteBase {

    /**
     * 例程3.29  模拟地理位置
     */
    @Test
    public void geoLocationTest() {
        //    通过HasDevTools接口获取DevTools实例
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        //    创建会话
        devTools.createSession();
        //    模拟发送地理坐标
        devTools.send(Emulation.setGeolocationOverride(Optional.of(40.4121), Optional.of(74.0241), Optional.of(1)));
        //    打开探测地理信息的网站
        driver.get("https://my-location.org");
        //    获取纬度元素
        WebElement latitude = driver.findElement(By.id("latitude"));
        //    期望地理位置纬度符合预期
        Assertions.assertThat(latitude.getText()).isEqualTo("40.4121");
        //    获取经度元素
        WebElement longitude = driver.findElement(By.id("longitude"));
        //    期望地理位置经度符合预期
        Assertions.assertThat(longitude.getText()).isEqualTo("74.0241");
    }

    /**
     * 例程3.30  覆盖设备模式
     */
    @Test
    public void deviceModeTest() {
        //    定义菜单按钮定位
        By menuButtonBy = By.xpath("//label[contains(@class,'menu-button')]");
        //    打开目标网站
        driver.get("https://ianzhang.cn/");
        //    全屏打开窗口的情况下,画面尺寸足够,菜单按钮不可见
        Assertions.assertThat(driver.findElement(menuButtonBy).isDisplayed()).isFalse();
        //    通过HasDevTools接口获取DevTools实例
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        //    创建会话
        devTools.createSession();
        //    设置设备信息
        devTools.send(Emulation.setDeviceMetricsOverride(480, 640, 50, true, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty()));
        //    再次打开目标网站
        driver.get("https://ianzhang.cn/");
        //    模拟手机打开窗口的情况下,画面尺寸较小,菜单按钮可见
        Assertions.assertThat(driver.findElement(menuButtonBy).isDisplayed()).isTrue();
    }

    /**
     * 例程3.31  其他模拟测试
     */
    @Test
    public void otherEmulationTest() {
        //    通过HasDevTools接口获取DevTools实例
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        //    创建会话
        devTools.createSession();
        //    设置CPU减缓倍数
        devTools.send(Emulation.setCPUThrottlingRate(2));
        //    设置背景色
        devTools.send(Emulation.setDefaultBackgroundColorOverride(Optional.of(new RGBA(1, 1, 1, Optional.empty()))));
        //    设置文档Cookie禁用
        devTools.send(Emulation.setDocumentCookieDisabled(false));
        //    设置滚动栏隐藏
        devTools.send(Emulation.setScrollbarsHidden(true));
        //    设置视觉障碍级别
        devTools.send(Emulation.setEmulatedVisionDeficiency(Emulation.SetEmulatedVisionDeficiencyType.ACHROMATOPSIA));
        //    设置时区
        devTools.send(Emulation.setTimezoneOverride("Asia/Shanghai"));
        //    打开目标网站
        driver.get("https://ianzhang.cn/");
        //    获取截图文件
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        //    获取截图文件位置,可以后续校验效果
        Assertions.assertThat(file.getAbsolutePath()).startsWith(System.getProperty("java.io.tmpdir")).endsWith("png");
    }

    /**
     * 例程3.32  性能指标
     */
    @Test
    public void performanceMetricsTest() {
        //    通过HasDevTools接口获取DevTools实例
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        //    创建会话
        devTools.createSession();
        //    开启性能监控
        devTools.send(Performance.enable(Optional.empty()));
        //    获取性能指标
        List<Metric> metricList = devTools.send(Performance.getMetrics());
        //    打开目标网站
        driver.get("https://ianzhang.cn/");
        //    打印全部性能指标
        metricList.forEach(m -> System.out.println(m.getName() + " = " + m.getValue()));
        //    以下为样例输出
        String sampleResult = """
                Timestamp = 4909.802051
                AudioHandlers = 0
                Documents = 2
                Frames = 1
                JSEventListeners = 0
                LayoutObjects = 6
                MediaKeySessions = 0
                MediaKeys = 0
                Nodes = 8
                Resources = 0
                ContextLifecycleStateObservers = 2
                V8PerContextDatas = 1
                WorkerGlobalScopes = 0
                UACSSResources = 0
                RTCPeerConnections = 0
                ResourceFetchers = 2
                AdSubframes = 0
                DetachedScriptStates = 0
                ArrayBufferContents = 0
                LayoutCount = 0
                RecalcStyleCount = 0
                LayoutDuration = 0
                RecalcStyleDuration = 0
                DevToolsCommandDuration = 2.1E-5
                ScriptDuration = 0
                V8CompileDuration = 0
                TaskDuration = 2.8E-5
                TaskOtherDuration = 7.0E-6
                ThreadTime = 1.87E-4
                ProcessTime = 0.203125
                JSHeapUsedSize = 1248524
                JSHeapTotalSize = 2306048
                FirstMeaningfulPaint = 0
                DomContentLoaded = 4909.290091
                NavigationStart = 4908.885616
                """;
    }

    /**
     * 例程3.33  网络状态
     */
    @Test
    public void networkConditionsTest() {
        //    以增强模式添加动态代理
        WebDriver augmentedDriver = new Augmenter().augment(driver);
        //    期望抛出WebDriverException
        Assertions.assertThatExceptionOfType(WebDriverException.class).isThrownBy(() ->
                        //  尝试获取网络状态
                        ((HasNetworkConditions) augmentedDriver).getNetworkConditions())
                //   异常提示网络状态必须提前设置
                .withMessageContaining("network conditions must be set before it can be retrieved");
        //    创建网络状态实例
        ChromiumNetworkConditions networkConditions = new ChromiumNetworkConditions();
        //    设置离线模式
        networkConditions.setOffline(true);
        //    发送网络状态,使得配置生效
        ((HasNetworkConditions) augmentedDriver).setNetworkConditions(networkConditions);
        Assertions.assertThatExceptionOfType(WebDriverException.class).isThrownBy(() ->
                        //  打开目标网站
                        driver.get("https://www.ianzhang.cn"))
                //    期望抛出网络无法连接的异常
                .withMessageContaining("ERR_INTERNET_DISCONNECTED");
        //      关闭离线模式,默认是false
        networkConditions.setOffline(false);
        //      设置下载吞吐量,默认是-1,既不做限制
        networkConditions.setDownloadThroughput(4096);
        //      设置上传吞吐量,默认是-1,既不做限制
        networkConditions.setUploadThroughput(2048);
        //      设置延迟,默认是Duration.ZERO,既不做限制
        networkConditions.setLatency(Duration.ofSeconds(1L));
        //      重新发送网络状态,使得新的配置生效
        ((HasNetworkConditions) augmentedDriver).setNetworkConditions(networkConditions);
        //      再次打开目标网站
        driver.get("https://www.ianzhang.cn");
    }

    /**
     * 例程3.34  执行CDP命令
     */
    @Test
    public void customizedCommandTest() {
        //    打开目标网站
        driver.get("https://www.ianzhang.cn");
        //    利用实现了HasCdp接口的Driver发送CDP命令,
        //    执行截图操作
        //    注意参数不能为null,如果采用默认参数可传入空Map
        Map<String, Object> result = ((HasCdp) driver).executeCdpCommand("Page.captureScreenshot", new HashMap<>());
        //    期望返回的结果集包含data字段,
        //    此方法结果将返回Base64格式的页面截图
        Assertions.assertThat(result.containsKey("data")).isTrue();
    }

}
