package io.me.agent.bytebuddy.advice;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

public class AdviceProvider {


    /**
     * 注解@Advice.OnMethodEnter表明这是一个在执行到目标方法前执行的逻辑
     */
    @Advice.OnMethodEnter
    public static void enter(@Advice.Origin Class<?> cls, @Advice.Origin Method method) {
        System.out.println("enter: " + cls.getName() + "#" + method.getName());
    }

    /**
     * 注解@Advice.OnMethodEnter表明这是一个在执行过目标方法逻辑后执行的内容
     */
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Origin Class<?> cls, @Advice.Origin Method method, @Advice.Thrown(readOnly = false) Throwable throwable) {

        System.out.println("exit: " + cls.getName() + "#" + method.getName());

        if (throwable != null) {
            System.out.println("h");
        }
    }
}
