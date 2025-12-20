package cn.ianzhang.allure;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

/**
 * @Author: Ian
 */
public class AllureWatcher implements TestWatcher {

    @Override
    public void testSuccessful(ExtensionContext context) {
        Allure.story(context.getDisplayName());
        Allure.description(context.getDisplayName());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        Allure.story(context.getDisplayName());
        Allure.description(context.getDisplayName());
    }

}
