package com.example.fangxq.myapplication.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

/**
 * @author huiguo
 * @date 2018/9/26
 */
public class TestUtils {

    /**
     * 打开开发者模式界面
     */
    public static void startDevelopmentActivity(Context context) {

        boolean enableAdb = (Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ADB_ENABLED, 0) > 0);//判断adb调试模式是否打开
        if (!enableAdb) {
            Toast.makeText(context, "开发者选项未打开", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.DevelopmentSettings");
                Intent intent = new Intent();
                intent.setComponent(componentName);
                intent.setAction("android.intent.action.View");
                context.startActivity(intent);
            } catch (Exception e1) {
                try {
                    Intent intent = new Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS");//部分小米手机采用这种方式跳转
                    context.startActivity(intent);
                } catch (Exception e2) {

                }

            }
        }
    }
}
