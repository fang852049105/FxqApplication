package com.fxq.findactivity;

import java.util.Stack;

/**
 * @author Fanxq
 * @date 2019-09-30
 */
public class TaskStack {
    private int id;
    Stack<ActivityInstanceInfo> mActivityInstanceInfoStack;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Stack<ActivityInstanceInfo> getActivityInstanceInfoStack() {
        return mActivityInstanceInfoStack;
    }

    public void setActivityInstanceInfoStack(Stack<ActivityInstanceInfo> activityInstanceInfoStack) {
        mActivityInstanceInfoStack = activityInstanceInfoStack;
    }
}
