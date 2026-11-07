window.SITE_INDEX = {
  meta: {
    book: {
      zh: "《深入浅出全栈自动化测试——基于Java和Selenium的企业级实践》",
      en: "Hands-On Full-Stack Test Automation: Enterprise Practices with Java and Selenium"
    },
    repo: "https://github.com/alaahong/Hands-On-Full-Stack-Test-Automation-Enterprise-Practices-with-Java-and-Selenium",
    site: "https://www.ianzhang.cn",
    updated: "2026-11-07"
  },
  chapters: [
    {
      id: "ch01",
      code: "01",
      title: { zh: "第 1 章 · 基础知识", en: "Chapter 1 · Getting Started" },
      module: "01-basic-knowledge",
      summary: {
        zh: "环境搭建与第一个 Selenium 脚本。",
        en: "Environment setup and your first Selenium script."
      },
      sites: [
        {
          name: { zh: "站点首页", en: "Site Home" },
          path: "/",
          url: "https://www.ianzhang.cn",
          desc: {
            zh: "验证浏览器可以正常启动并打开项目配套站点。",
            en: "Verifies that the browser starts and opens the companion site."
          },
          refs: ["HelloSeleniumTest#seleniumTest"]
        }
      ]
    },
    {
      id: "ch02",
      code: "02",
      title: { zh: "第 2 章 · WebDriver 基础", en: "Chapter 2 · WebDriver Basics" },
      module: "02-webdriver-basic",
      summary: {
        zh: "浏览器导航、窗口与框架切换、元素定位、元素信息读取。",
        en: "Browser navigation, windows and frames, element location, and reading element information."
      },
      sites: [
        {
          name: { zh: "打开重定向页面", en: "Open Redirect Page" },
          path: "/s4ia/02-webdriver-basic/get.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/get.html",
          desc: {
            zh: "打开目标页面，观察服务端重定向后的实际地址。",
            en: "Opens the target page and observes the actual URL after the server redirect."
          },
          refs: [
            { zh: "例程 2.2", en: "Example 2.2" },
            "Example02Test#getTest"
          ]
        },
        {
          name: { zh: "后退与前进", en: "Back and Forward" },
          path: "/s4ia/02-webdriver-basic/back_forward.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html",
          desc: {
            zh: "读取当前 URL，并使用浏览器历史执行后退、前进与刷新。",
            en: "Reads the current URL and uses browser history to go back, forward, and refresh."
          },
          refs: [
            { zh: "例程 2.4 – 2.5", en: "Examples 2.4 – 2.5" },
            "Example02Test#getCurrentUrlTest",
            "Example02Test#testBackAndForward"
          ],
          variants: [
            { label: { zh: "带查询参数", en: "With query string" }, url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html?wd=selenium" },
            { label: "#first", url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#first" },
            { label: "#second", url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#second" },
            { label: "#third", url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/back_forward.html#third" }
          ]
        },
        {
          name: { zh: "PDF 页面刷新与源码", en: "PDF Refresh and Page Source" },
          path: "/s4ia/02-webdriver-basic/refresh.pdf",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/refresh.pdf",
          desc: {
            zh: "以 PDF 文件为目标页执行刷新，并读取页面源码。",
            en: "Refreshes a PDF target page and reads its page source."
          },
          refs: [
            { zh: "例程 2.6、2.8", en: "Examples 2.6, 2.8" },
            "Example02Test#refreshTest",
            "Example02Test#pageSourceTest"
          ],
          note: { zh: "目标是一个 PDF 文件。", en: "The target is a PDF file." }
        },
        {
          name: { zh: "页面标题", en: "Page Titles" },
          path: "/s4ia/02-webdriver-basic/normal_title.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/normal_title.html",
          desc: {
            zh: "分别读取普通标题、含空格标题与无标题页面的 title。",
            en: "Reads the title of a normal page, a page whose title contains spaces, and a page without a title."
          },
          refs: [
            { zh: "例程 2.7", en: "Example 2.7" },
            "Example02Test#titleTest"
          ],
          variants: [
            { label: { zh: "普通标题", en: "Normal title" }, url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/normal_title.html" },
            { label: { zh: "含空格标题", en: "Title with spaces" }, url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/space_title.html" },
            { label: { zh: "无标题", en: "No title" }, url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/no_title.html" }
          ]
        },
        {
          name: { zh: "多窗口切换", en: "Multi-window Switching" },
          path: "/s4ia/02-webdriver-basic/window-handle-1.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/window-handle-1.html",
          desc: {
            zh: "点击页面内链接打开第二个窗口，并在多个窗口句柄之间切换。",
            en: "Clicks a link that opens a second window and switches between multiple window handles."
          },
          refs: [
            { zh: "例程 2.9", en: "Example 2.9" },
            "Example02Test#windowHandleTest"
          ],
          note: { zh: "页面中的链接会打开新窗口。", en: "A link on the page opens a new window." }
        },
        {
          name: { zh: "iframe 切换", en: "iframe Switching" },
          path: "/s4ia/02-webdriver-basic/frame.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/frame.html",
          desc: {
            zh: "在父页面与 iframe 之间切换，定位框架内的元素。",
            en: "Switches between the parent page and an iframe to locate elements inside the frame."
          },
          refs: [
            { zh: "例程 2.12", en: "Example 2.12" },
            "Example02Test#frameTest"
          ]
        },
        {
          name: { zh: "关闭窗口", en: "Closing Windows" },
          path: "/s4ia/02-webdriver-basic/default.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/default.html",
          desc: {
            zh: "打开附加窗口后关闭当前窗口，并验证剩余窗口句柄。",
            en: "Opens an additional window, closes the current one, and verifies the remaining window handles."
          },
          refs: [
            { zh: "例程 2.13", en: "Example 2.13" },
            "Example02Test#closeTest"
          ]
        },
        {
          name: { zh: "八种基础定位方式", en: "Eight Basic Locator Strategies" },
          path: "/s4ia/02-webdriver-basic/absolute_locators.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/absolute_locators.html",
          desc: {
            zh: "覆盖 id、name、tag name、class name、link text、partial link text、XPath 与 CSS Selector。",
            en: "Covers id, name, tag name, class name, link text, partial link text, XPath, and CSS Selector."
          },
          refs: [
            { zh: "例程 2.14 – 2.21", en: "Examples 2.14 – 2.21" },
            { zh: "Example02Test（8 个定位用例）", en: "Example02Test (8 locator tests)" }
          ]
        },
        {
          name: { zh: "相对定位", en: "Relative Locators" },
          path: "/s4ia/02-webdriver-basic/relative_locators.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/relative_locators.html",
          desc: {
            zh: "演示 above、below、toLeftOf、toRightOf 与 near 五种相对定位。",
            en: "Demonstrates the five relative locators: above, below, toLeftOf, toRightOf, and near."
          },
          refs: [
            { zh: "例程 2.22 – 2.26", en: "Examples 2.22 – 2.26" },
            { zh: "Example02Test（5 个相对定位用例）", en: "Example02Test (5 relative locator tests)" }
          ]
        },
        {
          name: { zh: "重叠元素相对定位", en: "Relative Locators with Overlapping Elements" },
          path: "/s4ia/02-webdriver-basic/relative_locators_position.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/relative_locators_position.html",
          desc: {
            zh: "处理页面元素相互重叠时的相对定位场景。",
            en: "Handles relative location when page elements overlap."
          },
          refs: [
            { zh: "例程 2.27", en: "Example 2.27" },
            "Example02Test#relativeLocatorsYellowTest"
          ]
        },
        {
          name: { zh: "元素集合与活跃元素", en: "Element Collections and the Active Element" },
          path: "/s4ia/02-webdriver-basic/find_elements.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/find_elements.html",
          desc: {
            zh: "findElements、findElement、activeElement 与失效元素（stale element）示例。",
            en: "Examples for findElements, findElement, activeElement, and stale elements."
          },
          refs: [
            { zh: "例程 2.28 – 2.31", en: "Examples 2.28 – 2.31" },
            { zh: "Example02Test（4 个查找用例）", en: "Example02Test (4 find tests)" }
          ]
        },
        {
          name: { zh: "元素信息与属性", en: "Element Information and Attributes" },
          path: "/s4ia/02-webdriver-basic/get_element_information.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html",
          desc: {
            zh: "读取标签名、尺寸、位置、文本、CSS、DOM 属性与 Shadow DOM，并判断元素状态。",
            en: "Reads tag name, size, location, text, CSS, DOM attributes, and Shadow DOM, and checks element state."
          },
          refs: [
            { zh: "例程 2.32 – 2.47", en: "Examples 2.32 – 2.47" },
            { zh: "Example02Test（15 个元素信息用例）", en: "Example02Test (15 element information tests)" },
            "LegacySelenium3Test#shadowElementTest"
          ],
          note: {
            zh: "第 7 章复用此页演示 Selenium 3 的 Shadow DOM 访问方式。",
            en: "Chapter 7 reuses this page to demonstrate Shadow DOM access in Selenium 3."
          }
        },
        {
          name: { zh: "下拉选择", en: "Select Elements" },
          path: "/s4ia/02-webdriver-basic/select.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/select.html",
          desc: {
            zh: "单选、多选、使用 Select 封装操作，以及非 select 元素的异常场景。",
            en: "Single and multiple selection, the Select helper, and the error case for non-select elements."
          },
          refs: [
            { zh: "例程 2.48 – 2.52", en: "Examples 2.48 – 2.52" },
            { zh: "Example02Test（5 个下拉用例）", en: "Example02Test (5 select tests)" }
          ]
        },
        {
          name: { zh: "颜色值", en: "Color Values" },
          path: "/s4ia/02-webdriver-basic/color.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/color.html",
          desc: {
            zh: "读取与校验元素颜色相关的样式值。",
            en: "Reads and verifies color-related style values of elements."
          },
          refs: [
            { zh: "例程 2.53", en: "Example 2.53" },
            "Example02Test#colorTest"
          ]
        }
      ]
    },
    {
      id: "ch03",
      code: "03",
      title: { zh: "第 3 章 · WebDriver 高级", en: "Chapter 3 · Advanced WebDriver" },
      module: "03-webdriver-advanced",
      summary: {
        zh: "键鼠操作、JavaScript、弹窗、截图、异常、BiDi、指针与滚轮。",
        en: "Keyboard and mouse actions, JavaScript, dialogs, screenshots, exceptions, BiDi, pointer input, and wheel events."
      },
      sites: [
        {
          name: { zh: "键盘操作", en: "Keyboard Actions" },
          path: "/s4ia/03-webdriver-advanced/keyboard.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/keyboard.html",
          desc: {
            zh: "发送键位、录入文本、组合按键与清空元素值。",
            en: "Sends keys, types text, uses key combinations, and clears element values."
          },
          refs: [
            { zh: "例程 3.1 – 3.5", en: "Examples 3.1 – 3.5" },
            "Example03Test#keyboard*Test"
          ]
        },
        {
          name: { zh: "鼠标点击", en: "Mouse Clicks" },
          path: "/s4ia/03-webdriver-advanced/mouse.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/mouse.html",
          desc: {
            zh: "使用 Actions 执行鼠标点击交互。",
            en: "Uses Actions to perform mouse click interactions."
          },
          refs: [
            { zh: "例程 3.6", en: "Example 3.6" },
            "Example03Test#clickSampleTest"
          ]
        },
        {
          name: { zh: "拖拽操作", en: "Drag and Drop" },
          path: "/s4ia/03-webdriver-advanced/mouse-move.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/mouse-move.html",
          desc: {
            zh: "拖拽页面元素并验证位置变化。",
            en: "Drags page elements and verifies the resulting position changes."
          },
          refs: [
            { zh: "例程 3.7", en: "Example 3.7" },
            "Example03Test#moveTest"
          ]
        },
        {
          name: { zh: "jQuery UI 拖拽", en: "jQuery UI Drag and Drop" },
          path: "/s4ia/03-webdriver-advanced/mouse-move-jqueryui.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/mouse-move-jqueryui.html",
          desc: {
            zh: "在 jQuery UI 组件上执行拖拽动作。",
            en: "Performs drag actions on jQuery UI components."
          },
          refs: [
            { zh: "例程 3.7", en: "Example 3.7" },
            "Example03Test#moveTest"
          ]
        },
        {
          name: { zh: "JavaScript 操作", en: "JavaScript Execution" },
          path: "/s4ia/03-webdriver-advanced/javascript.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/javascript.html",
          desc: {
            zh: "同步脚本、异步脚本、pin 操作与复杂 JavaScript 场景。",
            en: "Synchronous scripts, asynchronous scripts, pin actions, and complex JavaScript scenarios."
          },
          refs: [
            { zh: "例程 3.8 – 3.11", en: "Examples 3.8 – 3.11" },
            "Example03Test#executeScriptTest",
            "Example03Test#executeAsyncScriptTest",
            "Example03Test#pinTest",
            "Example03Test#caseTest"
          ]
        },
        {
          name: { zh: "弹窗、上传、下载与 Cookie", en: "Dialogs, Uploads, Downloads, and Cookies" },
          path: "/s4ia/03-webdriver-advanced/interaction.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/interaction.html",
          desc: {
            zh: "Alert 处理、文件上传与下载、Cookie 操作以及页面截图。",
            en: "Alert handling, file upload and download, cookie operations, and page screenshots."
          },
          refs: [
            { zh: "例程 3.11 – 3.16", en: "Examples 3.11 – 3.16" },
            "AlertTest",
            "Example03Test#alertTest",
            "Example03Test#fileUploadTest",
            "Example03Test#cookieTest"
          ]
        },
        {
          name: { zh: "截图与 PDF", en: "Screenshots and PDFs" },
          path: "/s4ia/03-webdriver-advanced/screenshot.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/screenshot.html",
          desc: {
            zh: "Shutterbug 截图、将页面保存为 PDF，以及通过截图生成 PDF。",
            en: "Shutterbug screenshots, saving pages as PDF, and generating PDFs from screenshots."
          },
          refs: [
            { zh: "例程 3.17 – 3.19", en: "Examples 3.17 – 3.19" },
            "Example03Test#shutterbugTest",
            "Example03Test#pdfTest",
            "Example03Test#openPdfTest"
          ]
        },
        {
          name: { zh: "WebDriver 异常", en: "WebDriver Exceptions" },
          path: "/s4ia/03-webdriver-advanced/exception.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/exception.html",
          desc: {
            zh: "集中演示点击被拦截、元素不可交互、无此元素、超时等 17 种 WebDriver 异常。",
            en: "Concentrates 17 WebDriver exceptions, including intercepted clicks, non-interactable elements, no such element, and timeouts."
          },
          refs: [
            { zh: "例程 3.21", en: "Example 3.21" },
            { zh: "Example03ExceptionTest（17 个用例）", en: "Example03ExceptionTest (17 tests)" }
          ]
        },
        {
          name: { zh: "BiDi 事件与日志", en: "BiDi Events and Logs" },
          path: "/s4ia/03-webdriver-advanced/bidi.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/bidi.html",
          desc: {
            zh: "通过 HasLogEvents、LogEntries、ConsoleEvent 与 JavaScriptException 监听浏览器事件。",
            en: "Listens to browser events through HasLogEvents, LogEntries, ConsoleEvent, and JavaScriptException."
          },
          refs: [
            { zh: "例程 3.24 – 3.27", en: "Examples 3.24 – 3.27" },
            "Example03AdvancedTest"
          ]
        },
        {
          name: { zh: "BiDi 浏览上下文", en: "BiDi Browsing Contexts" },
          path: "/s4ia/03-webdriver-advanced/browsing-context.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/browsing-context.html",
          desc: {
            zh: "利用 BiDi 协议创建新的浏览上下文（窗口）。",
            en: "Creates a new browsing context (window) with the BiDi protocol."
          },
          refs: ["Example03BidiTest#testCreateAWindowWithAReferenceContext"]
        },
        {
          name: { zh: "触摸指针事件", en: "Touch Pointer Events" },
          path: "/s4ia/03-webdriver-advanced/touch.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/touch.html",
          desc: {
            zh: "使用 PointerInput 模拟触摸并记录指针事件。",
            en: "Simulates touch with PointerInput and records pointer events."
          },
          refs: ["PointerInputTest#shouldLogTouchPointerEvents"]
        },
        {
          name: { zh: "双指缩放", en: "Two-finger Pinch Zoom" },
          path: "/s4ia/03-webdriver-advanced/multi-touch.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/multi-touch.html",
          desc: {
            zh: "模拟双指捏合，验证画布缩放比例。",
            en: "Simulates a two-finger pinch and verifies the canvas zoom level."
          },
          refs: ["PointerInputTest#shouldPerformTwoFingerPinchZoomOut"]
        },
        {
          name: { zh: "虚拟笔输入", en: "Virtual Pen Input" },
          path: "/s4ia/03-webdriver-advanced/pen.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/pen.html",
          desc: {
            zh: "使用虚拟笔在画布上绘制并校验笔迹。",
            en: "Draws on a canvas with a virtual pen and verifies the strokes."
          },
          refs: ["PointerInputTest#shouldDrawWithVirtualPen"]
        },
        {
          name: { zh: "虚拟认证器", en: "Virtual Authenticator" },
          path: "/s4ia/03-webdriver-advanced/virtual-authenticator.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/virtual-authenticator.html",
          desc: {
            zh: "注册虚拟认证器并完成 WebAuthn 认证流程。",
            en: "Registers a virtual authenticator and completes a WebAuthn authentication flow."
          },
          refs: ["VirtualAuthenticatorTest#shouldRegisterAndAuthenticateUsingVirtualAuthenticator"]
        },
        {
          name: { zh: "滚轮事件", en: "Wheel Events" },
          path: "/s4ia/03-webdriver-advanced/wheel.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/wheel.html",
          desc: {
            zh: "使用滚轮动作滚动页面并捕获详细滚轮事件。",
            en: "Scrolls the page with wheel actions and captures detailed wheel events."
          },
          refs: ["WheelActionsTest#shouldCaptureDetailedWheelEvents"]
        },
        {
          name: { zh: "Ctrl + 滚轮缩放", en: "Ctrl + Wheel Zoom" },
          path: "/s4ia/03-webdriver-advanced/ctrl-wheel-zoom.html",
          url: "https://www.ianzhang.cn/s4ia/03-webdriver-advanced/ctrl-wheel-zoom.html",
          desc: {
            zh: "按住 Ctrl 使用滚轮缩放画布并断言缩放比例。",
            en: "Zooms the canvas by holding Ctrl and scrolling, then asserts the zoom level."
          },
          refs: ["WheelActionsTest#shouldZoomCanvasWhenCtrlPlusWheel"]
        },
        {
          name: { zh: "异步请求数据", en: "Asynchronous Request Data" },
          path: "/s4ia/static/data/ian/async-sample.json",
          url: "https://www.ianzhang.cn/s4ia/static/data/ian/async-sample.json",
          desc: {
            zh: "jQuery AJAX、axios Promise 与 XMLHttpRequest 示例共同读取的 JSON 数据。",
            en: "JSON data consumed by the jQuery AJAX, axios Promise, and XMLHttpRequest examples."
          },
          refs: [
            { zh: "例程 3.9", en: "Example 3.9" },
            "Example03Test#executeAsyncScriptTest"
          ],
          note: {
            zh: "该地址返回 JSON，不是 HTML 页面。",
            en: "This URL returns JSON, not an HTML page."
          }
        }
      ]
    },
    {
      id: "ch04",
      code: "04",
      title: { zh: "第 4 章 · 工程化自动化", en: "Chapter 4 · Engineering Test Automation" },
      module: "04-engineering-automation",
      summary: {
        zh: "Page Object、数据驱动、异常处理与测试工程化实践。",
        en: "Page Objects, data-driven tests, exception handling, and test engineering practices."
      },
      sites: [
        {
          name: { zh: "登录页 Page Object", en: "Login Page Objects" },
          path: "/s4ia/04-engineering-automation/login-sample.html",
          url: "https://www.ianzhang.cn/s4ia/04-engineering-automation/login-sample.html",
          desc: {
            zh: "同一个登录页的多种 Page Object 实现：基础封装、PageFactory、LoadableComponent 与 AjaxElementLocatorFactory。",
            en: "Several Page Object implementations for the same login page: basic wrapper, PageFactory, LoadableComponent, and AjaxElementLocatorFactory."
          },
          refs: [
            "LoginTest",
            "PageObjectLoginPage",
            "PageFactoryLoginPage",
            "LoadableComponentLoginPage",
            "AjaxElementLocatorFactoryLoginPage"
          ]
        },
        {
          name: { zh: "在线计算器", en: "Online Calculator" },
          path: "/s4ia/04-engineering-automation/calculator/index.html",
          url: "https://www.ianzhang.cn/s4ia/04-engineering-automation/calculator/index.html",
          desc: {
            zh: "Page Object 结合 JSON 与 YAML 数据驱动执行计算器用例。",
            en: "Page Objects combined with JSON and YAML data-driven calculator tests."
          },
          refs: [
            "CalculatorDataDrivenTest#calculatorJsonCases",
            "CalculatorDataDrivenTest#calculatorYamlCases"
          ]
        },
        {
          name: { zh: "数据驱动页面", en: "Data-Driven Page" },
          path: "/s4ia/04-engineering-automation/data-driven.html",
          url: "https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html",
          desc: {
            zh: "JUnit 5 参数化与 CSV、JSON、YAML、Properties、TOML、XML 多种数据源。",
            en: "JUnit 5 parameterization with CSV, JSON, YAML, Properties, TOML, and XML data sources."
          },
          refs: [
            { zh: "DataDrivenTest（8 个用例）", en: "DataDrivenTest (8 tests)" },
            { zh: "JacksonDataDrivenTest（5 个用例）", en: "JacksonDataDrivenTest (5 tests)" },
            "CustomizedAnnotationDataDriverTest"
          ],
          variants: [
            { label: { zh: "带 name 查询参数", en: "With name query parameter" }, url: "https://www.ianzhang.cn/s4ia/04-engineering-automation/data-driven.html?name=" }
          ]
        },
        {
          name: { zh: "异常处理页面", en: "Exception Handling Page" },
          path: "/s4ia/04-engineering-automation/exception.html",
          url: "https://www.ianzhang.cn/s4ia/04-engineering-automation/exception.html",
          desc: {
            zh: "对比 try/catch 与 InvocationInterceptor 两种异常处理方式。",
            en: "Compares try/catch with InvocationInterceptor as two approaches to exception handling."
          },
          refs: ["TryCatchExceptionTest", "InvocationInterceptorExceptionTest"]
        }
      ]
    },
    {
      id: "ch05",
      code: "05",
      title: { zh: "第 5 章 · 企业级自动化", en: "Chapter 5 · Enterprise Automation" },
      module: "05-enterprise-automation",
      summary: {
        zh: "Allure / ExtentReports 报告与 Selenium Grid 示例。",
        en: "Allure / ExtentReports reporting and Selenium Grid examples."
      },
      sites: [
        {
          name: { zh: "站点首页", en: "Site Home" },
          path: "/",
          url: "https://www.ianzhang.cn",
          desc: {
            zh: "Allure 与 ExtentReports 报告演示使用的入口页面。",
            en: "Entry page used by the Allure and ExtentReports reporting demos."
          },
          refs: [
            "AllureTest",
            "AllureWatcherTest",
            "ExtentReportsTest",
            "ExtentReportsWatcherTest",
            "GridTest"
          ]
        },
        {
          name: { zh: "Selenium Grid", en: "Selenium Grid" },
          path: "localhost:4444",
          url: "http://localhost:4444",
          desc: {
            zh: "通过 RemoteWebDriver 连接本地 Selenium Grid 执行测试。",
            en: "Runs tests against a local Selenium Grid through RemoteWebDriver."
          },
          refs: ["GridTest#gridTest"],
          note: {
            zh: "本地服务，需要先启动 Grid。配套 compose 还提供 Grafana :3000、Loki :3100、Tempo :3200、Prometheus :9090 与 OTLP :4318。",
            en: "Local service; start Grid first. The bundled Compose stack also exposes Grafana :3000, Loki :3100, Tempo :3200, Prometheus :9090, and OTLP :4318."
          },
          local: true
        }
      ]
    },
    {
      id: "ch07",
      code: "07",
      title: { zh: "第 7 章 · Selenium 3 兼容", en: "Chapter 7 · Selenium 3 Compatibility" },
      module: "07-legacy-selenium-3",
      summary: {
        zh: "使用 Selenium 3 访问第 2 章页面，对比旧版 API 行为。",
        en: "Uses Selenium 3 against Chapter 2 pages to compare legacy API behavior."
      },
      sites: [
        {
          name: { zh: "元素信息与 Shadow DOM", en: "Element Information and Shadow DOM" },
          path: "/s4ia/02-webdriver-basic/get_element_information.html",
          url: "https://www.ianzhang.cn/s4ia/02-webdriver-basic/get_element_information.html",
          desc: {
            zh: "Selenium 3 获取 Shadow DOM 的兼容写法，对应第 2 章同一页面。",
            en: "The Selenium 3 compatible way to access Shadow DOM on the same page used in Chapter 2."
          },
          refs: [
            { zh: "例程 2.39", en: "Example 2.39" },
            "LegacySelenium3Test#shadowElementTest"
          ]
        }
      ]
    },
    {
      id: "ch09",
      code: "09",
      title: { zh: "第 9 章 · Selenium 4.39 与 CDP", en: "Chapter 9 · Selenium 4.39 and CDP" },
      module: "09-selenium-4-39-0",
      summary: {
        zh: "固定 Selenium 4.39.0 与 CDP v143，演示设备模拟、地理定位与网络控制。",
        en: "Pinned to Selenium 4.39.0 with CDP v143, covering device emulation, geolocation, and network control."
      },
      sites: [
        {
          name: { zh: "站点首页（CDP 示例）", en: "Site Home (CDP examples)" },
          path: "/",
          url: "https://www.ianzhang.cn",
          desc: {
            zh: "设备模式、其他模拟项、性能指标、网络状态与自定义 CDP 命令的入口页面。",
            en: "Entry page for device mode, other emulation options, performance metrics, network conditions, and custom CDP commands."
          },
          refs: [
            { zh: "例程 3.30 – 3.34", en: "Examples 3.30 – 3.34" },
            "Example03CdpTest"
          ],
          variants: [
            { label: { zh: "不带 www 的地址", en: "Address without www" }, url: "https://ianzhang.cn/" }
          ],
          note: {
            zh: "本机 Chrome 版本高于 CDP v143 时，相关用例会按设计跳过。",
            en: "When the local Chrome version is newer than CDP v143, the related tests are skipped by design."
          }
        }
      ]
    }
  ],
  externalTitle: { zh: "外部与本地站点", en: "External and Local Sites" },
  externalSummary: {
    zh: "测试代码中访问的第三方网站与本机服务。外部站点可能需要可用的网络环境。",
    en: "Third-party websites and local services accessed by the test code. External sites may require network access."
  },
  external: [
    {
      name: { zh: "百度首页", en: "Baidu Home" },
      host: "baidu.com",
      url: "https://www.baidu.com",
      desc: {
        zh: "打开网页、完全限定 URL 与地址自动补全示例。",
        en: "Examples for opening web pages, fully qualified URLs, and URL auto-completion."
      },
      refs: [
        { zh: "例程 2.1、2.3", en: "Examples 2.1, 2.3" },
        "Example02Test#openWebPageTest",
        "Example02Test#fullyQualifiedUrlTest"
      ],
      note: {
        zh: "搜索结果可能跳转验证码页；当前获取当前 URL 的用例已改用站内页面。",
        en: "Search results may redirect to a CAPTCHA page; the current URL test now uses an in-site page."
      }
    },
    {
      name: { zh: "Bing 首页", en: "Bing Home" },
      host: "bing.com",
      url: "https://www.bing.com",
      desc: {
        zh: "移动设备模拟示例的访问目标。",
        en: "Target site for the mobile device emulation example."
      },
      refs: ["MobileChromeTest#openBaiduTest"]
    },
    {
      name: { zh: "Google 首页", en: "Google Home" },
      host: "google.com",
      url: "https://google.com",
      desc: {
        zh: "NetworkInterceptor 网络拦截示例，将响应替换为自定义文本。",
        en: "NetworkInterceptor example that replaces the response with custom text."
      },
      refs: [
        { zh: "例程 3.28", en: "Example 3.28" },
        "Example03AdvancedTest#networkInterceptionTest"
      ],
      note: {
        zh: "需要可访问 google.com 的网络环境。",
        en: "Requires network access to google.com."
      }
    },
    {
      name: { zh: "GitHub REST API", en: "GitHub REST API" },
      host: "api.github.com",
      url: "https://api.github.com/users?since=1",
      desc: {
        zh: "网络拦截示例，将 JSON API 响应替换为自定义内容。",
        en: "Network interception example that replaces a JSON API response with custom content."
      },
      refs: [
        { zh: "例程 3.28", en: "Example 3.28" },
        "Example03AdvancedTest#networkInterceptionTest"
      ],
      note: {
        zh: "需要可访问 GitHub API 的网络环境。",
        en: "Requires network access to the GitHub API."
      }
    },
    {
      name: { zh: "地理位置探测页", en: "Geolocation Probe" },
      host: "my-location.org",
      url: "https://my-location.org",
      desc: {
        zh: "通过 CDP 模拟地理位置后，在页面上读取定位结果。",
        en: "Reads the location from the page after emulating geolocation through CDP."
      },
      refs: [
        { zh: "例程 3.29", en: "Example 3.29" },
        "Example03CdpTest#geoLocationTest"
      ],
      note: {
        zh: "需要可访问该外部站点。",
        en: "Requires access to this external site."
      }
    },
    {
      name: { zh: "本地 Selenium Grid", en: "Local Selenium Grid" },
      host: "localhost:4444",
      url: "http://localhost:4444",
      desc: {
        zh: "Selenium Grid 的 WebDriver 入口。",
        en: "WebDriver endpoint for Selenium Grid."
      },
      refs: ["GridTest#setupTest", "GridTest#gridTest"],
      note: { zh: "本地服务，需要先启动 Grid。", en: "Local service; start Grid first." },
      local: true
    },
    {
      name: { zh: "本地基本认证服务", en: "Local Basic Auth Service" },
      host: "localhost",
      url: "http://admin:123456@localhost",
      desc: {
        zh: "旧版 URL 内嵌用户名与密码的基本认证示例。",
        en: "Legacy basic authentication example with credentials embedded in the URL."
      },
      refs: [
        { zh: "例程 3.22", en: "Example 3.22" },
        "Example03BasicAuthTest#basicAuthWithoutBidiTest"
      ],
      note: {
        zh: "需要本地 HTTP 服务，地址中的账号仅用于示例。",
        en: "Requires a local HTTP server; the credentials in the URL are for demonstration only."
      },
      local: true,
      hideLink: true
    },
    {
      name: { zh: "ianzhang.cn（不带 www）", en: "ianzhang.cn (without www)" },
      host: "ianzhang.cn",
      url: "https://ianzhang.cn/",
      desc: {
        zh: "部分 CDP 示例直接使用不带 www 的域名。",
        en: "Some CDP examples use the domain without www."
      },
      refs: ["Example03CdpTest"],
      note: {
        zh: "该地址的 TLS 证书与域名不匹配，测试以忽略证书错误模式运行。",
        en: "The TLS certificate does not match this domain; tests run with certificate errors ignored."
      }
    }
  ]
};
