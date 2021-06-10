package io.me.agent.bytebuddy.interceptor;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;

public class InterceptorProvider {


    /**
     * 使用RuntimeType注解表明这是一个拦截器方法
     */
    @RuntimeType
    public static Object intercept(@Origin Class<?> targetClass,
                                   @This(optional = true) Object target,
                                   @Origin Method method,
                                   @Morph InterceptorCall call,
                                   @AllArguments Object[] arguments) throws Exception {

        System.out.println("before");
        // 反射调用被委托的方法，也就是目标方法
//        Object invoke = method.invoke(target, arguments);
//        method.setAccessible(true);
//        Object ret = method.invoke(target,arguments);
        System.out.println(targetClass.getName()+"  "+method.getName());
        System.out.println("after");
        return null;


    }

}
