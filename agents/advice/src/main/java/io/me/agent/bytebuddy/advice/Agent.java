package io.me.agent.bytebuddy.advice;

import io.me.agent.common.SaveTransformedClassFileListener;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;

/**
 * 启动流程
 * 启动虚拟机，加载bootstrap类路径中的类
 * jvm调用Java代码的 sun.misc.Launcher.ExtClassLoader#getExtClassLoader()，
 * 然后再调用 sun.misc.Launcher.AppClassLoader#getAppClassLoader(java.lang.ClassLoader)初始化两个类加载器
 * 加载JavaAgent类及其使用到的类，这个加载是由AppClassLoader加载
 * 实例化InstrumentationImpl
 * 调用Java代码 sun.instrument.InstrumentationImpl#loadClassAndCallPremain(java.lang.String, java.lang.String)
 * 之后执行premain方法
 * premain方法中可以通过Instrumentation实例对bootstrap类加载器加载的类进行修改之后再redefine。同时可以将Instrumentation实例
 * 保存下来，在运行时对某些类进行redefine，需要再经过一系列的ClassFileTransformer的话，再调用restransformClasses方法即可
 */
public class Agent {

    public static void premain(String args, Instrumentation inst) {
        System.out.println(Thread.currentThread().getContextClassLoader());
        Arrays.stream(inst.getAllLoadedClasses()).forEach(System.out::println);
        ByteBuddy byteBuddy = new ByteBuddy();
        // byte buddy默认不处理启动类加载器（也就是类的classloader为null）加载的类，不处理byte buddy自己包下面的类
        // 不处理ext类加载加载的类，不处理合成类 不处理sun.reflect包下的类
        AgentBuilder agentBuilder = new AgentBuilder.Default(byteBuddy).with(new SaveTransformedClassFileListener());
        agentBuilder.type(ElementMatchers.named("io.me.agent.app.pkg.Service")).transform(
                (builder, typeDescription, classLoader, module) ->
                        builder.visit(Advice.to(AdviceProvider.class)
                                // 这里不能对Constructor方法进行advice植入，会报错
                                .on(ElementMatchers.isMethod())))
                .installOn(inst);

    }

    public static void agentmain(String args, Instrumentation inst) {
        
    }

}
