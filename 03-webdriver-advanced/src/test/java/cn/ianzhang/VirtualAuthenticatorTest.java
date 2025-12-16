package cn.ianzhang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.virtualauthenticator.HasVirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticatorOptions;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class VirtualAuthenticatorTest extends WebDriverBase {
    private VirtualAuthenticator authenticator;

    @AfterEach
    void tearDown() {
        if (authenticator != null) {
            ((HasVirtualAuthenticator) driver).removeVirtualAuthenticator(authenticator);
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldRegisterAndAuthenticateUsingVirtualAuthenticator() {
        VirtualAuthenticatorOptions authOptions = new VirtualAuthenticatorOptions();
        authOptions.setProtocol(VirtualAuthenticatorOptions.Protocol.CTAP2)
                .setTransport(VirtualAuthenticatorOptions.Transport.USB)
                .setHasResidentKey(true)
                .setHasUserVerification(true)
                .setIsUserVerified(true);

        authenticator = ((HasVirtualAuthenticator) driver).addVirtualAuthenticator(authOptions);

        driver.get("https://www.ianzhang.cn/s4ia/03-webdriver-advanced/virtual-authenticator.html");

        WebElement register = driver.findElement(By.id("register"));
        WebElement authenticate = driver.findElement(By.id("authenticate"));
        WebElement log = driver.findElement(By.id("log"));

        register.click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.textToBePresentInElement(log, "Registration succeeded"));
        assertThat(authenticate.isEnabled()).isTrue();

        authenticate.click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.textToBePresentInElement(log, "Authentication succeeded"));
        assertThat(log.getText()).contains("credId=");
    }
}
