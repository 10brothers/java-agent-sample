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
