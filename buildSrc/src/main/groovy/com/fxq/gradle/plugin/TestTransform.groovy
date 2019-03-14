package com.fxq.gradle.plugin

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project
import com.android.utils.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import static org.objectweb.asm.ClassReader.EXPAND_FRAMES


public class TestTransform extends Transform {
    Project mProject;

    public TestTransform(Project project) {
        this.mProject = project;
    }


    @Override
    public String getName() {
        return "TestTransform";
    }

    //需要处理的数据类型，有两种枚举类型:CLASSES和RESOURCES，CLASSES代表处理的java的class文件，RESOURCES代表要处理java的资源
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    //    指Transform要操作内容的范围，官方文档Scope有7种类型：
    //
    //    EXTERNAL_LIBRARIES        只有外部库
    //    PROJECT                       只有项目内容
    //    PROJECT_LOCAL_DEPS            只有项目的本地依赖(本地jar)
    //    PROVIDED_ONLY                 只提供本地或远程依赖项
    //    SUB_PROJECTS              只有子项目。
    //    SUB_PROJECTS_LOCAL_DEPS   只有子项目的本地依赖项(本地jar)。
    //    TESTED_CODE                   由当前变量(包括依赖项)测试的代码
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }


    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        System.out.println("----------------进入transform了--------------")
        inputs.each { TransformInput transformInput ->
            // Transform的inputs有两种类型，一种是目录，一种是jar包，要分开遍历
            //对类型为“文件夹”的input进行遍历
            transformInput.directoryInputs.each { DirectoryInput directoryInput ->
                //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
                //使用javassist
                //TestInject.injectDir(directoryInput.file.absolutePath, mProject)
                //使用ASM
                if (directoryInput.file.isDirectory()) {
                    //列出目录所有文件（包含子文件夹，子文件夹内文件）
                    directoryInput.file.eachFileRecurse { File file ->
                        def name = file.name
                        if (checkClassFile(name)) {
                            System.out.println('----------- deal with "class" file <' + name + '> -----------')
                            ClassReader classReader = new ClassReader(file.bytes)
                            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                            //ClassVisitor cv = new LifecycleClassVisitor(classWriter)
                            ClassVisitor cv = new MethodCostClassVisitor(classWriter)
                            classReader.accept(cv, EXPAND_FRAMES)
                            byte[] code = classWriter.toByteArray()
                            FileOutputStream fos = new FileOutputStream(
                                    file.parentFile.absolutePath + File.separator + name)
                            fos.write(code)
                            fos.close()
                        }
                    }
                }
                // 获取output目录
                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)
                // 将input的目录复制到output指定目录
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
            //对类型为jar文件的input进行遍历
            transformInput.jarInputs.each { JarInput jarInput ->
                //jar文件一般是第三方依赖库jar文件

                // 重命名输出文件（同目录copyFile会冲突）
                def jarName = jarInput.name
                def md5Name = org.apache.commons.codec.digest.DigestUtils.md5Hex(jarInput.file.getAbsolutePath())

                if(jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0,jarName.length()-4)
                }

                //生成输出路径
                def dest = outputProvider.getContentLocation(jarName+md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                //将输入内容复制到输出

                FileUtils.copyFile(jarInput.file, dest)

            }
        }
        System.out.println("--------------结束transform了----------------");
    }

    static boolean checkClassFile(String name) {
        //只处理需要的class文件
        return (name.endsWith(".class") && !name.startsWith("R\$")
                && !"R.class".equals(name) && !"BuildConfig.class".equals(name))
    }
}
