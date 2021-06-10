package io.me.agent.agent.bytebuddy.interceptor;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;

public class InterceptorProvider {


    /**
     * 使用RuntimeType注解表明这是一个拦截器方法
     */
    @RuntimeType
    public Object intercept(@Origin Class<?> targetClass,
                                   @This(optional = true) Object target,
                                   @Origin Method method,
                                   @AllArguments Object[] arguments) throws Exception {

        System.out.println("before");
        // 反射调用被委托的方法，也就是目标方法
        Object invoke = method.invoke(target, arguments);
        System.out.println("after");
        return invoke;


    }

}
