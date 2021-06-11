package io.me.agent.bytebuddy.advice;

import io.me.agent.common.SaveTransformedClassFileListener;
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
