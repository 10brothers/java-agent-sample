package io.me.agent.bytebuddy.interceptor;

import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;

public class InterceptorProvider {


    /**
     * 使用RuntimeType注解表明这是一个拦截器方法。
     * 如果直接使用的话，此方法要声明为静态方法才可以，否则不会起到拦截的租用，参见{@link MethodDelegation.WithCustomProperties#to(net.bytebuddy.description.type.TypeDescription)}
     * 如果没有声明为静态方法的话，需要使用这个 {@link MethodDelegation.WithCustomProperties#to(java.lang.Object)}
     */
    @RuntimeType
    public static Object intercept(@Origin Class<?> targetClass,
                                   @This(optional = true) Object target,
                                   @Origin Method method,
                                   @Morph InterceptorCall call,
                                   @AllArguments Object[] arguments) throws Exception {

        System.out.println("before");
        // 反射调用被委托的方法，也就是目标方法
        System.out.println(targetClass.getName()+"  "+method.getName());
//        Object invoke = call.invoke(arguments);
//        Object invoke = method.invoke(target, arguments);
        System.out.println("after");
        return null;


    }

}
