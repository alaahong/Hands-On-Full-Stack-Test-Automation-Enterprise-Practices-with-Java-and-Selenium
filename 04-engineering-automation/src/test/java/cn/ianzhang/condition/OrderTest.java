package cn.ianzhang.condition;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


/**
 * @Author: Ian
 * @Description: cn.ianzhang.Order as below
 * preTest
 * midTest
 * noAnnotationTest
 * defaultOrderTest2
 * defaultOrderTest1
 * postTest
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderTest {

    @Test
    @Order(Integer.MAX_VALUE)
    public void postTest() {
        System.out.println("postTest");
    }

    @Test
    public void noAnnotationTest() {
        System.out.println("noAnnotationTest");
    }

    @Test
    @Order(Order.DEFAULT)
    public void defaultOrderTest2() {
        System.out.println("defaultOrderTest2");
    }

    @Test
    @Order(Integer.MAX_VALUE / 2)
    public void defaultOrderTest1() {
        System.out.println("defaultOrderTest1");
    }

    @Test
    @Order(50)
    public void midTest() {
        System.out.println("midTest");
    }

    @Test
    @Order(Integer.MIN_VALUE)
    public void preTest() {
        System.out.println("preTest");
    }
}
