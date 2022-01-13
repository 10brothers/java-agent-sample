# 使用ByteBuddy来编写JavaAgent

JVM提供了一种方式，可以在类还未加载到虚拟机时修改类的内容，或者对已经加载的类进行重新定义。
也就是transform和redefine。
由于直接操作字节码比较困难，因此通过字节码操作工具类库来实现。在字节码类库中，
Byte-Buddy提供了专门针对编写JavaAgent的API，使得编写一个JavaAgent更加的方便快捷高效。

## JavaAgent的一些知识点

JavaAgent是JVMTI（JVM Tool Interface）的一种实现，由JVM提供的用于操作JVM的一个工具编程接口。
使用Agent，可以实现在类加载时对字节码进行修改，也可以对已经加载的进行再定义。前者是通过静态agent实现，也即-javaagent参数。
后者是通过Attach API来实现的。

无论是哪种方式，都不能修该方法的签名，仅能对方法体内容做一些改动，否则有可能会导致虚拟机崩溃。

agent的入口类需要指定，在agent jar中有一个META-INF目录下的MANIFEST.MF文件中，用Premain-Class和Agent-Class分别指明两个Agent入口类。
agent可以有多个，按照顺序执行其中的premain方法。在第一个premain方法被执行前，是不无法执行任何transform操作的，此时连transformer都么有。
无论是哪个类加载器加载的类，都可以被transform，除了在premain执行前已经加载的类。

### Instrumentation API
在Agent Jar的premain或者agentmain方法中可以拿到，拿到后可以添加ClassFileTransformer，也可以对已加载的类进行重定义，也就是redefine
。
通过Instrumentation的redefine 方法可以在运行时动态的修改方法体，然后生效。和重新加载不一样，这里不用使用新创建的ClassLoader实例加载。
redefine一个类时，会把已定义的Transformer都执行一遍。

可以将Instrumentation的实例保存到某个类变量上，然后应用程序代码可以拿到这个实例对象，从而实现运行时动态修改类。

### Attach API
JVM在启动时，会启动一个信号分发线程，用于监听事件，比如kill的系统调用。Attach正是利用了这个，会触发JVM启动一个ServerSocket，
然后Attach进程通过Socket目标进程通讯，可以发送一些命令，比如输出线程栈，dump堆等等。其中比较重要的一个操作是可以加载Agent Jar，
这个时候加载的AgentJar，会执行agentmain方法。
com.sun.tools.attach.VirtualMachine就是用来操作attach的类


## Byte Buddy提供的快速编码Agent的方式

byte buddy提供了便捷编写agent的api，可以针对不同的目标方法有不同的transformer，有点类似于spring mvc仅提供一个servlet类，实际上可以有那么多对的控制层方法。
byte buddy提供了大量元素匹配器，用于方便的匹配目标类和方法。在对目标类做修改时，支持两种方式：interceptor、advice。


### interceptor 
会将目标匹配到的方法体，直接替换成拦截器的相应方法，通过调用拦截器方法。
这样实际的逻辑全部由拦截器方法来承担
在拦截器方法中，如果获取被拦截的方法Method和this对象，企图通过反射调用被拦截的方法是行不通的，只会陷入无终止的递归之中。
是一个死循环调用。

因此，如果还想继续执行目标被拦截的方法逻辑，需要使用@Morph注解，也就是生成一个类来承担原先的方法逻辑


### advice
advice是织入的方式，也就是直接将逻辑写入到原目标方法中，通过修改目标方法体的方式，来去修改加载的类。


## Arthas 的原理
先通过Attach API的方式，来加载JavaAgent，在Agent Jar中获取Instrumentation的实例，并在目标进程中启动一个NettyServer专门用于接受请求处理类的修改等操作。
