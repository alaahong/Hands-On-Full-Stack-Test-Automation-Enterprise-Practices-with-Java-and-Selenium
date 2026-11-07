# 本项目为《深入浅出全栈自动化测试——基于Java和Selenium的企业级实践》的配套代码

[English](README.en.md)

本仓库包含一组循序渐进的 Java 自动化测试示例，涵盖 Selenium WebDriver
基础、高级交互、测试工程化与企业级实践，也包含 Selenium 3、
Appium、Cucumber 和 Playwright 的扩展示例。

## 环境要求

- JDK 25 或更高版本
- Maven
- 运行浏览器测试时，需要安装对应的浏览器（Chrome、Firefox 或 Edge）
- Android 示例需要 Android 模拟器或真机，以及已配置 UiAutomator2
  驱动的 Appium Server
- Grid 示例需要可访问的 Selenium Grid；相关测试可能需要根据本机
  环境调整地址和浏览器版本

确认 Java 和 Maven 已安装：

```shell
java -version
mvn -version
```

## 快速开始

在仓库根目录运行全部 Maven 模块的测试：

```shell
mvn clean test
```

运行单个模块及其所需的上游模块：

```shell
mvn -pl 02-webdriver-basic -am test
```

默认使用本地 Chrome。也可以通过 `BROWSER` 环境变量或同名 Java
系统属性选择浏览器：

```shell
# Windows PowerShell
$env:BROWSER = "chrome-headless"
mvn -pl 02-webdriver-basic -am test

# macOS / Linux
BROWSER=firefox mvn -pl 02-webdriver-basic -am test
```

支持的浏览器选项包括 `chrome`、`chrome-headless`、`firefox`、`edge`
和 `chrome-remote`。远程 Chrome 示例默认连接
`http://localhost:4444/wd/hub`，可通过 `selenium.remote.url` 系统属性
指定 Grid 地址。

`09-selenium-4-39-0` 模块默认跳过测试。如需运行该模块：

```shell
mvn -pl 09-selenium-4-39-0 -am test -DskipTests=false
```

## 测试站点索引

配套测试实际访问的页面与站点已按章节整理成索引，包含页面地址、
对应的测试类与例程编号，并区分站内页面、外部站点和本地服务。
启用 GitHub Pages 后，可通过以下地址访问：

- 中文版：<https://alaahong.github.io/Hands-On-Full-Stack-Test-Automation-Enterprise-Practices-with-Java-and-Selenium/>
- English: <https://alaahong.github.io/Hands-On-Full-Stack-Test-Automation-Enterprise-Practices-with-Java-and-Selenium/en/>

索引源码位于 `docs/`，可直接在浏览器中打开 `docs/index.html`
（英文版为 `docs/en/index.html`）。如需通过 GitHub Pages 发布，请在仓库
**Settings → Pages** 中选择 `master` 分支的 `/docs` 目录。

## 模块导览

| 模块 | 内容 |
| --- | --- |
| `00-common` | 共享 WebDriver 基类、浏览器配置、重试扩展和截图工具 |
| `first-selenium-project` | 使用 JUnit 4 的初始 Maven 项目骨架 |
| `01-basic-knowledge` | Selenium 入门与第一个浏览器测试 |
| `02-webdriver-basic` | WebDriver 基础操作示例 |
| `03-webdriver-advanced` | 高级交互、等待、异常、BiDi、虚拟认证器等 |
| `04-engineering-automation` | Page Object、数据驱动、测试条件及多格式测试数据 |
| `05-enterprise-automation` | Selenium Grid、Allure / Extent 报告及可观测性示例 |
| `06-expansion` | Appium Android、Cucumber / Gherkin 和 Playwright 示例 |
| `07-legacy-selenium-3` | 基于 Selenium 3 的兼容性示例 |
| `09-selenium-4-39-0` | Selenium 4.39.0 与 Chrome DevTools Protocol 示例 |

除 `00-common` 外，各模块通常在自己的 `src/test/java` 目录下存放测试，
测试数据和配置则位于 `src/test/resources`。

## 测试与报告

测试使用 JUnit 5（入门项目使用 JUnit 4），由 Maven Surefire 执行。
Surefire 测试报告位于各模块的 `target/surefire-reports`；配置了
Allure 的模块还会生成 `target/allure-report`。部分示例需要访问外部网站、
浏览器或本地服务，运行前请检查对应测试中的地址和服务要求。

GitHub Actions 工作流位于 `.github/workflows/maven.yml`，在 Windows
Runner 上使用 Java 25、Chrome 和 Firefox 执行 Maven 测试。

## 许可

仓库未在根目录声明统一许可。使用或分发代码前，请先确认仓库许可及
各依赖的许可要求。

## 更多讨论

可以在本项目的issue里提出您的问题，或者发送电子邮件与我联系 hi@ianzhang.cn
