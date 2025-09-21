// java
package cn.ianzhang.extension;

import cn.ianzhang.annotation.Retry;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.openqa.selenium.WebDriverException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * JUnit 5 扩展：遇到 WebDriverException 且包含 ERR_CONNECTION_RESET 时进行重试
 * <p>
 * 实现要点：
 * - 仅对拦截器链调用一次 invocation.proceed()（第一次尝试）。
 * - 后续重试通过反射直接调用测试方法，避免再次触发其他 InvocationInterceptor（如 Timeout）。
 */
public class RetryExtension implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
                                    ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {

        // 读取注解（方法优先，类次之）
        Retry retryAnno = invocationContext.getExecutable().getAnnotation(Retry.class);
        if (retryAnno == null) {
            retryAnno = extensionContext.getTestClass()
                    .map(cls -> cls.getAnnotation(Retry.class))
                    .orElse(null);
        }
        int maxAttempts = (retryAnno == null) ? 1 : Math.max(1, retryAnno.value());

        int attempt = 0;
        long baseDelayMs = 1000L;
        Throwable lastThrowable = null;

        // 第一次尝试通过 invocation.proceed() 调用（保持拦截器链正常执行一次）
        attempt++;
        try {
            invocation.proceed();
            return; // 成功直接返回
        } catch (Throwable t) {
            lastThrowable = t;
            if (attempt >= maxAttempts || !shouldRetry(t)) {
                throw t; // 不重试或次数耗尽，直接抛出
            }
            // 否则继续进行反射重试循环
        }

        // 反射重试（绕过 InvocationInterceptor 链）
        Method testMethod = invocationContext.getExecutable();
        List<Object> args = invocationContext.getArguments();
        Object[] argArray = args.toArray();
        Object testInstance = extensionContext.getRequiredTestInstance();

        while (attempt < maxAttempts) {
            attempt++;
            long delay = baseDelayMs * (1L << (attempt - 2)); // 指数退避：1s,2s,4s,...
            try {
                Thread.sleep(Math.min(delay, 30_000L));
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }

            try {
                testMethod.setAccessible(true);
                // 直接反射调用测试方法，捕获 InvocationTargetException 并取得 cause
                testMethod.invoke(testInstance, argArray);
                return; // 成功
            } catch (InvocationTargetException ite) {
                Throwable cause = ite.getCause() != null ? ite.getCause() : ite;
                lastThrowable = cause;
                if (!shouldRetry(cause) || attempt >= maxAttempts) {
                    throw cause;
                }
                // 否则继续下一次重试
            } catch (Throwable reflectEx) {
                lastThrowable = reflectEx;
                if (!shouldRetry(reflectEx) || attempt >= maxAttempts) {
                    throw reflectEx;
                }
            }
        }

        // 所有尝试后仍失败，抛出最后一次捕获的异常
        if (lastThrowable != null) {
            throw lastThrowable;
        }
    }

    private boolean shouldRetry(Throwable t) {
        Throwable cause = t;
        while (cause != null) {
            if (cause instanceof WebDriverException) {
                String msg = cause.getMessage();
                if (msg != null) {
                    String lower = msg.toLowerCase();
                    if (lower.contains("err_connection_reset") || lower.contains("net::err_connection_reset") || lower.contains("connection reset")) {
                        return true;
                    }
                }
            }
            cause = cause.getCause();
        }
        return false;
    }
}
