package com.fxq.gradle.plugin;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author huiguo
 * @date 2019/3/11
 */
public class LifecycleClassVisitor extends ClassVisitor implements Opcodes{

    private String mClassName;

    public LifecycleClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println("LifecycleClassVisitor : visitMethod : " + name + "mClassName = " + mClassName);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (this.mClassName != null && this.mClassName.contains("MainActivity")) {
            if ("onCreate".equals(name) ) {
                //处理onCreate
                System.out.println("LifecycleClassVisitor : change method ----> " + name);
                return new LifecycleOnCreateMethodVisitor(mv);
            } else if ("onDestroy".equals(name)) {
                //处理onDestroy
                System.out.println("LifecycleClassVisitor : change method ----> " + name);
            }
        }
        return mv;
    }
}
