package com.fxq.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension

public class SecondPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        System.out.println("========================");
        System.out.println("这是第二个插件!");
        System.out.println("========================");

        def android = project.extensions.getByType(AppExtension);
        def classTransform = new TestTransform(project);
        android.registerTransform(classTransform);
    }


}
