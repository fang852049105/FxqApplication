package com.fxq.gradle.plugin

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

public class TestInject {
    private static ClassPool pool = ClassPool.getDefault();

    public static void injectDir(String path, Project project) {
        System.out.println("injectDir start ");
        pool.appendClassPath(path)
        File dir = new File(path)
        pool.appendClassPath(project.android.bootClasspath[0].toString())
        pool.importPackage("android.os.Bundle")
        if (dir.isDirectory()) {
            //遍历文件夹
            dir.eachFileRecurse { File file ->

                String filePath = file.absolutePath
                //确保当前文件是class文件，并且不是系统自动生成的class文件
                if (filePath.endsWith(".class")
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")) {
                    if (file.getName().equals("MainActivity.class")) {
                        CtClass ctClass = pool.getCtClass("com.example.fangxq.myapplication.ui.MainActivity")
                        println("ctClass = " + ctClass)
                        // 解冻
                        if (ctClass.isFrozen()) {
                            ctClass.defrost()
                        }

                        // 获取到 onCreate() 方法
                        CtMethod ctMethod = ctClass.getDeclaredMethod("onCreate")
                        println("ctMethod = " + ctMethod)
                        // 插入日志打印代码
                        String insertBeforeStr = """android.util.Log.e("--->", "Hello transform");"""

                        ctMethod.insertBefore(insertBeforeStr)
                        ctClass.writeFile(path)
                        ctClass.detach()
                    }
                }
            }
        }
        System.out.println("injectDir end");

    }

}
