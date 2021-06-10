package io.me.agent.bytebuddy.interceptor;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String args, Instrumentation inst) {
        ByteBuddy byteBuddy = new ByteBuddy();
        // byte buddy默认不处理启动类加载器（也就是类的classloader为null）加载的类，不处理byte buddy自己包下面的类
        // 不处理ext类加载加载的类，不处理合成类 不处理sun.reflect包下的类
        AgentBuilder agentBuilder = new AgentBuilder.Default(byteBuddy);
        agentBuilder.type(ElementMatchers.any()).transform(
                (builder, typeDescription, classLoader, module) ->
                        // ElementMatchers是一个工具类，方便提供一些快捷的构造ElementMatcher的方法
                        // 设置匹配的方法
                        builder.method(ElementMatchers.not(ElementMatchers.isStatic()))
                                // 拦截配置
                                .intercept(MethodDelegation.withDefaultConfiguration() // 默认方法委托配置
                                        // 配合@Morph注解使用
                                        .withBinders(Morph.Binder.install(InterceptorCall.class))
                                        // 方法拦截器的实际拦截逻辑类
                                        .to(InterceptorProvider.class)))
//                                .intercept(MethodDelegation.to(InterceptorProvider.class)))
                .installOn(inst);

    }

    public static void agentmain(String args, Instrumentation inst) {

    }

}
