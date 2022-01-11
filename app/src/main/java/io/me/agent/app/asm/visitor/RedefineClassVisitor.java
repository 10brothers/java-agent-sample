package io.me.agent.app.asm.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

public class RedefineClassVisitor extends ClassVisitor {

    public RedefineClassVisitor(ClassVisitor cv) {
        super(ASM9, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.equals("redefine")) {
            return new ThisMethodVisitor(methodVisitor);
        } else {
            return methodVisitor;
        }
    }



    static class ThisMethodVisitor extends MethodVisitor{

        public ThisMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM9, methodVisitor);
        }

        @Override
        public void visitCode() {
            mv.visitCode();
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == RETURN) {
                mv.visitFieldInsn(GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;");
                mv.visitLdcInsn("i am asm");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
            super.visitInsn(opcode);
        }
    }
}
