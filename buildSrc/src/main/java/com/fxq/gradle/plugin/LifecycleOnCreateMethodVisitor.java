package com.fxq.gradle.plugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author huiguo
 * @date 2019/3/11
 */
public class LifecycleOnCreateMethodVisitor extends MethodVisitor{


    public LifecycleOnCreateMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM5, mv);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitLdcInsn("--->");
        mv.visitLdcInsn("Hello ASM");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
        //方法执行前插入
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
        //方法执行后插入
    }
}
