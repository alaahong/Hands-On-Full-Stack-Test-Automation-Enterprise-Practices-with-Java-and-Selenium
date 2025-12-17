package cn.ianzhang.calculator;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object 封装工程化示例中的在线计算器页面。
 */
public class CalculatorPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // 显示区域（只读输入框）
    private final By displayMain = By.id("calc-display-main");

    // 数字按键（基于 data-value 属性）
    private final By digitKey    = By.cssSelector("button[data-role='key-digit']");

    // 运算符按键（基于 data-op 属性）
    private final By operatorKey = By.cssSelector("button[data-role='key-operator']");

    // 功能键
    private final By clearAllKey   = By.id("calc-key-clear-all");
    private final By clearEntryKey = By.id("calc-key-clear-entry");
    private final By equalKey      = By.id("calc-key-equal");

    public CalculatorPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /**
     * 清空当前状态，相当于点击页面上的 C 键。
     */
    public CalculatorPage clearAll() {
        wait.until(ExpectedConditions.elementToBeClickable(clearAllKey)).click();
        return this;
    }

    /**
     * 清空当前输入（CE）。
     */
    public CalculatorPage clearEntry() {
        wait.until(ExpectedConditions.elementToBeClickable(clearEntryKey)).click();
        return this;
    }

    /**
     * 依次点击每一位数字，比如 "123" 会连续点击 1、2、3。
     */
    public CalculatorPage inputNumber(String number) {
        if (number == null || number.isBlank()) {
            return this;
        }
        for (char ch : number.toCharArray()) {
            if (ch == '.') {
                clickDecimal();
            } else if (Character.isDigit(ch)) {
                clickDigit(ch);
            } else if (ch == '-') {
                // 如未来页面支持正负号，可在此扩展
                clickOperator("sub");
            } else {
                // 其他字符忽略，交由测试数据设计来避免
            }
        }
        return this;
    }

    /**
     * 点击某个数字键，例如 '7'。
     */
    public CalculatorPage clickDigit(char digit) {
        // 通过 data-value 精确匹配
        By locator = By.cssSelector("button[data-role='key-digit'][data-value='" + digit + "']");
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        return this;
    }

    /**
     * 点击小数点按键。
     */
    public CalculatorPage clickDecimal() {
        By locator = By.id("calc-key-decimal");
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        return this;
    }

    /**
     * 选择运算符：add / sub / mul / div。
     *
     * @param op one of "add", "sub", "mul", "div"
     */
    public CalculatorPage clickOperator(String op) {
        if (op == null || op.isBlank()) {
            return this;
        }
        // 映射一下常见符号，方便数据驱动里用 + - * /
        String normalized = switch (op) {
            case "+" -> "add";
            case "-" -> "sub";
            case "*" -> "mul";
            case "/" -> "div";
            default -> op; // 直接使用传入值
        };
        By locator = By.cssSelector("button[data-role='key-operator'][data-op='" + normalized + "']");
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        return this;
    }

    /**
     * 点击等号键，触发计算。
     */
    public CalculatorPage clickEqual() {
        wait.until(ExpectedConditions.elementToBeClickable(equalKey)).click();
        // 通常结果立即更新，这里做一个轻量等待，确保显示区不为空
        try {
            wait.until(d -> !getDisplayValue().isBlank());
        } catch (TimeoutException ignored) {
        }
        return this;
    }

    /**
     * 一次性完成 left op right = 的输入与计算。
     *
     * @param left  左操作数（字符串形式）
     * @param op    运算符："+", "-", "*", "/" 或 data-op 的值
     * @param right 右操作数
     */
    public CalculatorPage calculate(String left, String op, String right) {
        clearAll();
        inputNumber(left);
        clickOperator(op);
        inputNumber(right);
        clickEqual();
        return this;
    }

    /**
     * 读取显示区域当前的数值文本。
     */
    public String getDisplayValue() {
        WebElement display = wait.until(ExpectedConditions.visibilityOfElementLocated(displayMain));
        String value = display.getAttribute("value");
        return value == null ? "" : value.trim();
    }

    /**
     * 由于该页面没有单独的错误区域，这里保留接口以便将来扩展。
     * 当前实现始终返回空串。
     */
    public String getErrorMessage() {
        try {
            By errorMessage = By.id("error-message");
            WebElement error = driver.findElement(errorMessage);
            return error.getText().trim();
        } catch (NoSuchElementException e) {
            return "";
        }
    }
}
