package com.example.fangxq.myapplication.customview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fangxq on 2017/7/27.
 */
public class CustomEditText extends EditText {

    private List<OnFocusChangeListener> onFocusChangeListeners = new ArrayList<>();

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        onFocusChangeListeners.add(onFocusChangeListener);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        for (OnFocusChangeListener onFocusChangeListener : onFocusChangeListeners) {
            onFocusChangeListener.onFocusChange(CustomEditText.this, focused);
        }
    }
}
