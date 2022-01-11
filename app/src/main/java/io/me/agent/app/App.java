package io.me.agent.app;

import io.me.agent.app.asm.visitor.RedefineClassVisitor;
import io.me.agent.app.pkg.RedefineClass;
import io.me.agent.app.pkg.Service;
import io.me.agent.utility.TransferInstrumentation;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.UnmodifiableClassException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws UnmodifiableClassException, ClassNotFoundException, IOException {
        System.out.println("INSTRUMENTATION ---> "+TransferInstrumentation.instrumentation);
        Service service = new Service();
        service.cook("a");
        System.out.println( Service.staticMethod());

        System.out.println("retransform ");

        RedefineClass redefineClass = new RedefineClass();
        redefineClass.redefine();
        ClassReader classReader = new ClassReader(RedefineClass.class.getName());
        ClassWriter classWriter = new ClassWriter(classReader, 0);
        ClassVisitor classVisitor = new RedefineClassVisitor(classWriter);
        classReader.accept(classVisitor, 0);

        ClassDefinition definition = new ClassDefinition(RedefineClass.class, classWriter.toByteArray());

        File file = new File("C:\\Users\\ke.wang8\\Desktop\\RedefineClass.class");
        FileOutputStream fileInputStream = new FileOutputStream(file);
        fileInputStream.write(classWriter.toByteArray());
        // 重新加载修改后的类
        TransferInstrumentation.instrumentation.redefineClasses(definition);

        // redefineClasses时，会自动再触发transform
//        TransferInstrumentation.instrumentation.retransformClasses(RedefineClass.class);

        redefineClass.redefine();

    }
}
