package com.example.fangxq.myapplication.manager;

/**
 * @author huiguo
 * @date 2018/11/12
 */
public class DeveloperOptionsManager {

    private static DeveloperOptionsManager mDeveloperOptionsManager;
    private boolean isFromTestHelper;
    private String mTargetTitle;

    public static DeveloperOptionsManager getInstance() {
        if (mDeveloperOptionsManager == null) {
            mDeveloperOptionsManager = new DeveloperOptionsManager();
        }
        return mDeveloperOptionsManager;
    }

    public void setFromTestHelper(boolean fromTestHelper) {
        isFromTestHelper = fromTestHelper;
    }

    public void setTargetTitle(String targetTitle) {
        mTargetTitle = targetTitle;
    }

    public boolean isFromTestHelper() {
        return isFromTestHelper;
    }

    public String getTargetTitle() {
        return mTargetTitle;
    }
}
