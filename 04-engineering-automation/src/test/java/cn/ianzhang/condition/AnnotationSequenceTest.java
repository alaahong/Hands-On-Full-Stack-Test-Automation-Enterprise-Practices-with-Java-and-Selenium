package cn.ianzhang.condition;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;


/**
 * @Author: Ian
 * @Description:
 */

public class AnnotationSequenceTest {
    public static boolean returnTrue() {
        return true;
    }

    @Test
    @EnabledIfSystemProperty(named = "sys.prop.enabled", matches = ".*")
    public void enabledIfSystemPropertyTest() {
        System.out.println("enabledIfSystemPropertyTest");
    }

    @Test
    @DisabledIfSystemProperty(named = "sys.prop.disabled", matches = "true")
    public void disabledIfSystemPropertyTest() {
        System.out.println("disabledIfSystemPropertyTest");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".+")
    public void enabledIfEnvironmentVariableTest() {
        System.out.println("enabledIfEnvironmentVariableTest");
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "JAVA_HOME", matches = "^$")
    public void disabledIfEnvironmentVariableTest() {
        System.out.println("disabledIfEnvironmentVariableTest");
    }

    @Test
    @EnabledIf("conditionMethod")
    public void enabledIfTest() {
        System.out.println("enabledIfTest");
    }

    @Test
    @DisabledIf("cn.ianzhang.condition.AnnotationSequenceTest#returnTrue")
    public void disabledIfTest() {
        System.out.println("disabledIfTest");
    }

    private boolean conditionMethod() {
        return true;
    }

}
