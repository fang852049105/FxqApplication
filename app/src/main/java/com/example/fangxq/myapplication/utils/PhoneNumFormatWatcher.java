package com.example.fangxq.myapplication.utils;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.commons.lang3.StringUtils;

public class PhoneNumFormatWatcher implements TextWatcher {
    private EditText mEditText;
    private int beforeTextLength = 0;
    private int onTextLength = 0;
    private boolean isChanged = false;
    int location = 0;
    private char[] tempChar;
    private StringBuffer buffer = new StringBuffer();
    private int konggeNumberB = 0;

    public PhoneNumFormatWatcher(EditText numberEditText, ImageView confirmButton) {
        this.mEditText = numberEditText;

        if (confirmButton != null) {
            confirmButton.setOnClickListener(click);
        }
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mEditText.setText("");
        }
    };

    @Override
    public void beforeTextChanged(CharSequence text, int start, int count, int after) {
        beforeTextLength = text.length();
        if (buffer.length() > 0) {
            buffer.delete(0, buffer.length());
        }
        konggeNumberB = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                konggeNumberB++;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        onTextLength = text.length();
        buffer.append(text.toString());
        if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
            isChanged = false;
            return;
        }
        isChanged = true;
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (isChanged) {
            location = mEditText.getSelectionEnd();
            int index = 0;
            while (index < buffer.length()) {
                if (buffer.charAt(index) == ' ') {
                    buffer.deleteCharAt(index);
                } else {
                    index++;
                }
            }
            index = 0;
            int konggeNumberC = 0;
            while (index < buffer.length()) {
                if ((index == 3 || index == 8 || index == 13 || index
                        == 18)) {
                    buffer.insert(index, ' ');
                    konggeNumberC++;
                }
                index++;
            }
            if (konggeNumberC > konggeNumberB) {
                location += (konggeNumberC - konggeNumberB);
            }
            tempChar = new char[buffer.length()];
            buffer.getChars(0, buffer.length(), tempChar, 0);
            String str = buffer.toString();
            if (location > str.length()) {
                location = str.length();
            } else if (location < 0) {
                location = 0;
            }
            if (s != null && !StringUtils.equalsIgnoreCase(str, s.toString())) {
                mEditText.setText(str);
                Editable editable = mEditText.getText();
                Selection.setSelection(editable, location);
            }
            isChanged = false;
        }

    }
}
