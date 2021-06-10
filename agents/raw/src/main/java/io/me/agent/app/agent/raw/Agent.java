package io.me.agent.app.agent.raw;

import sun.instrument.TransformerManager;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.Collections;

/**
 * premain方法执行过后，会添加transformer到{@link TransformerManager}的mTransformerList字段。
 * 在premain之前类的加载同样需要经过<code>sun.instrument.InstrumentationImpl#transform</code>方法，
 * 只是此时TransformerManager还没有Transformer添加进来，因此自然也就无法被修改了。这是一个顺序性问题。
 * premain的调用是通过反射调用的，具体是由虚拟机调用{@link Instrumentation}的 loadClassAndCallPremain方法
 *
 * instrumentation#transform方法早于instrumentation#loadClassAndPremain方法的执行。第一个premain方法执行后就会有Transformer，
 * 之后加载的类会继续调用transform，此时已存在Transformer，可以干预加载类的内容。
 *
 * 启动时加载Agent，目的是为了调用premain和agentmain方法然后设置Transformer，之后设置一个类加载时的回调函数，回调函数中调用InstrumentationImpl的transform方法。
 * setLivePhaseEventHandlers  -->
 * 添加一个类加载时的hook src/java.instrument/share/native/libinstrument/InvocationAdapter.c:628   eventHandlerClassFileLoadHook    然后里面调用 JPLISAgent  transformClassFile  最终调用到Instrumentation的transform方法
 *
 * 在KlassFactory.cpp 的 create_from_stream（虚拟机创建class对象的方法）方法中会调用 check_class_file_load_hook，最终调用到设置的回调方法，执行到transform方法。
 *
 *
 */
public class Agent {

    public static void premain(String args, Instrumentation inst) {

        System.out.println("start execute premain");
        // 输出虚拟机初始化过程中执行到当前Agent的premain方法已经加载的类
        Class[] loadedClasses = inst.getAllLoadedClasses();
        for (int i = loadedClasses.length - 1; i >= 0; i--) {
            Class<?> loadedClass = loadedClasses[i];
            System.out.println("ClassLoader:" + loadedClass.getClassLoader() + " --- LoadClass:" + loadedClass);
        }
        System.out.println("premain executed");

    }

    public static void agentmain(String args, Instrumentation inst) {
        
    }

}
