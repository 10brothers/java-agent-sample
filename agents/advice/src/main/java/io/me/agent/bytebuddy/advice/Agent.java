package io.me.agent.bytebuddy.advice;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
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
                        builder.visit(
                                Advice.to(AdviceProvider.class).on(ElementMatchers.any())))
                .installOn(inst);

    }

    public static void agentmain(String args, Instrumentation inst) {
        
    }

}
