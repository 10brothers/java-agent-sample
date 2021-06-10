# 使用ByteBuddy来编写JavaAgent

JVM提供了一种方式，可以在类还未加载到虚拟机时修改类的内容，或者对已经加载的类进行重新定义。
也就是transform和redefine。
由于直接操作字节码比较困难，因此通过字节码操作工具类库来实现。在字节码类库中，
Byte-Buddy提供了专门针对编写JavaAgent的API，使得编写一个JavaAgent更加的方便快捷高效。

## JavaAgent的一些知识点

JavaAgent是JVMTI（JVM Tool Interface）的一种实现，由JVM提供的用于操作JVM的一个工具编程接口。
使用Agent，可以实现对类加载字节码的修改，可以动态的去修改一个已经加载的类

## Byte Buddy提供的快速编码Agent的方式