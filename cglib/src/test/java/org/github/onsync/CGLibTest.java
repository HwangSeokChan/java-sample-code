package org.github.onsync;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.FixedValue;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.Mixin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CGLibTest {

    @Test
    void Enhancer_FixedValue() {
        // given
        Greeting proxied = (Greeting) Enhancer.create(Greeting.class, (FixedValue) () -> "Bye Jack");
        // when
        String greeting = proxied.greeting("Jack");
        // then
        Assertions.assertEquals("Bye Jack", greeting);
    }

    @Test
    void Enhancer_MethodInterceptor() {
        // given
        Greeting proxied = (Greeting) Enhancer.create(Greeting.class, (MethodInterceptor) (obj, method, args, proxy) -> {
            if (method.getDeclaringClass() != Object.class && method.getReturnType() == int.class) {
                return 7;
            } else if (method.getName().equals("greeting")) {
                return "Bye Jack";
            } else {
                return proxy.invokeSuper(obj, args);
            }
        });
        // when
        String greeting = proxied.greeting("Jack");
        int lengthOfName = proxied.lengthOfName("Jack");
        // then
        Assertions.assertEquals("Bye Jack", greeting);
        Assertions.assertEquals(7, lengthOfName);
    }

    @Test
    void BeanGenerator() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.addProperty("name", String.class);
        Object myBean = beanGenerator.create(); // Create POJO
        Method setter = myBean.getClass().getMethod("setName", String.class);
        Method getter = myBean.getClass().getMethod("getName");
        // when
        setter.invoke(myBean, "some string value set by a cglib");
        // then
        Assertions.assertEquals("some string value set by a cglib", getter.invoke(myBean));
    }

    @Test
    void Mixin() {
        // given
        Calculator mixined = (Calculator) Mixin.create(
                new Class[]{ Plus.class, Minus.class, Calculator.class },
                new Object[]{ new PlusImpl(), new MinusImpl() }
        );
        // when
        int plus = mixined.plus(2, 1);
        int minus = mixined.minus(2, 1);
        // then
        Assertions.assertEquals(3, plus);
        Assertions.assertEquals(1, minus);
    }
}
