package com.example.fangxq.myapplication.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.fangxq.myapplication.manager.DeveloperOptionsManager;
import com.example.fangxq.myapplication.utils.SystemSettingUtil;

import java.util.List;

/**
 * @author huiguo
 * @date 2018/11/12
 */
public class DeveloperOptionsService extends AccessibilityService {


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e("fxq", "onServiceConnected");
        SystemSettingUtil.startDevelopmentActivity(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e("fxq", "onAccessibilityEvent");
        AccessibilityNodeInfo nodeInfo = findViewByText(DeveloperOptionsManager.getInstance().getTargetTitle());
        if (nodeInfo != null && DeveloperOptionsManager.getInstance().isFromTestHelper()) {
            performViewClick(nodeInfo);
            DeveloperOptionsManager.getInstance().setFromTestHelper(false);
        }
    }


    @Override
    public void onInterrupt() {
        Log.e("fxq", "onInterrupt");
    }

    public static void performViewClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        while (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            nodeInfo = nodeInfo.getParent();
        }
    }

    public AccessibilityNodeInfo findViewByText(String text) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null ) {
                    return nodeInfo;
                }
            }
        } else {
            scrollView(accessibilityNodeInfo);
        }
        return null;
    }

    public boolean scrollView(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }
        if (nodeInfo.isScrollable()) {
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            if (scrollView(nodeInfo.getChild(i))) {
                return true;
            }
        }
        return false;
    }

}
