package com.example.fangxq.myapplication.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author huiguo
 * @date 2018/10/23
 */
public class SystemSettingUtil {

    /**
     * 打开开发者模式界面
     */
    public static void startDevelopmentActivity(Context context) {

        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.DevelopmentSettings");
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(componentName);
                intent.setAction("android.intent.action.View");
                context.startActivity(intent);
            } catch (Exception e1) {
                try {
                    Intent intent = new Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS");//部分小米手机采用这种方式跳转
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e2) {
                }

            }
        }
    }

    public static boolean checkAccessibilityEnabled(String serviceName, Context context) {
        AccessibilityManager mAccessibilityManager  = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        if (accessibilityServices == null || accessibilityServices.isEmpty()) {
            return false;
        }
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (StringUtils.isNotEmpty(info.getId()) && info.getId().contains(serviceName)) {
                return true;
            }
        }
        return false;
    }


}
