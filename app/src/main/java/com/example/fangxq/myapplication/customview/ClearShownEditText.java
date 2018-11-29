package com.example.fangxq.myapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.fangxq.myapplication.R;
import com.fxq.lib.utils.Utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Fangxq on 2017/6/28.
 */
public class ClearShownEditText extends RelativeLayout {

    private String mHint;
    private int mTextColorHint;
    private int mTextColor;
    private int mHintTextSize;
    private int mTextSize;
    private int mInputType;
    private boolean isShowPassword;
    private boolean isShowEye;
    private boolean isShowClear;
    private Context mContext;
    private EditText mEditText;
    private ImageView mClearImage;
    private ImageView mShowImage;
    private View mBottomLine;
    private OnFocusChanged mOnFocusChanged;
    private boolean isFocus = false;
    private static final int DEFAULT_TEXT_SIZE = 14;

    public ClearShownEditText(Context context) {
        super(context);
        this.mContext = context;
        initView(null);
    }

    public ClearShownEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        LayoutInflater.from(mContext).inflate(R.layout.view_edit_text_clear_shown, this);
        TypedArray attrsArray = mContext.obtainStyledAttributes(attrs, R.styleable.ClearShownEditText);
        mHint = attrsArray.getString(R.styleable.ClearShownEditText_android_hint);
        mTextColorHint = attrsArray.getInteger(R.styleable.ClearShownEditText_android_textColorHint, R.color.b2);
        mTextColor = attrsArray.getInteger(R.styleable.ClearShownEditText_android_textColor, R.color.b1);
        mTextSize = attrsArray.getDimensionPixelSize(R.styleable.ClearShownEditText_shown_edit_text_size, Utils.dip2px(mContext,DEFAULT_TEXT_SIZE));
        mHintTextSize = attrsArray.getDimensionPixelSize(R.styleable.ClearShownEditText_shown_edit_hint_text_size, Utils.dip2px(mContext,DEFAULT_TEXT_SIZE));
        isShowPassword = attrsArray.getBoolean(R.styleable.ClearShownEditText_show_text, false);
        isShowEye = attrsArray.getBoolean(R.styleable.ClearShownEditText_show_eye, true);
        isShowClear = attrsArray.getBoolean(R.styleable.ClearShownEditText_show_clear, true);
        mInputType = attrsArray.getInteger(R.styleable.ClearShownEditText_android_inputType, -1);
        attrsArray.recycle();
        mEditText = (EditText) findViewById(R.id.shown_edit_text);
        mClearImage = (ImageView) findViewById(R.id.shown_edit_clear_image);
        mShowImage = (ImageView) findViewById(R.id.edit_shown_image);
        mBottomLine = findViewById(R.id.edit_bottom_line);
        mEditText.setHintTextColor(mTextColorHint);
        setHintText(mEditText, mHint, Utils.px2dip(mContext, mHintTextSize));
        mEditText.setTextColor(mTextColor);
        mEditText.setTextSize(Utils.px2dip(mContext, mTextSize));
        mClearImage.setOnClickListener(clearOnClicked);
        if (isShowEye) {
            mShowImage.setVisibility(VISIBLE);
        } else {
            mShowImage.setVisibility(GONE);
            if (mInputType != -1) {
                mEditText.setInputType(mInputType);
            } else {
                mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }
        if (isShowClear) {
            mClearImage.setVisibility(VISIBLE);
        } else {
            mClearImage.setVisibility(GONE);
        }
        mShowImage.setOnClickListener(showOnClicked);
        mEditText.addTextChangedListener(textWatcher);
        mEditText.setOnFocusChangeListener(onFocusChangeListener);
    }

    private OnClickListener clearOnClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mEditText.setText("");
        }
    };

    private OnClickListener showOnClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isShowPassword) {
                mShowImage.setImageResource(R.drawable.ic_biyan);
                hidenPassword();
            } else {
                mShowImage.setImageResource(R.drawable.ic_zhenyan);
                showPassword();
            }
        }
    };

    private int mPreviousInputType;

    private void showPassword() {
        mPreviousInputType = mEditText.getInputType();
        isShowPassword = true;
        Log.e("fxq", "showPassword");
        Log.e("fxq", "mPreviousInputType =" +  mPreviousInputType);
        Log.e("fxq", "inputType = " + (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD));
        setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD, true);
    }

    private void hidenPassword() {
        Log.e("fxq", "hidenPassword");
        isShowPassword = false;
        Log.e("fxq", "inputType = " + mPreviousInputType);
        setInputType(mPreviousInputType, true);
        mPreviousInputType = -1;
    }

    private void setInputType(int inputType, boolean keepState) {
        int selectionStart = -1;
        int selectionEnd = -1;
        if (keepState) {
            selectionStart = mEditText.getSelectionStart();
            selectionEnd = mEditText.getSelectionEnd();
        }
        mEditText.setInputType(inputType);
        if (keepState) {
            mEditText.setSelection(selectionStart, selectionEnd);
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isFocus && s.toString().length() > 0 && isShowClear) {
                mClearImage.setVisibility(View.VISIBLE);
            } else {
                mClearImage.setVisibility(View.GONE);
            }
        }
    };

    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (mOnFocusChanged != null) {
                    mOnFocusChanged.onFocusChange(v, hasFocus);
                }
                isFocus = true;
                if (StringUtils.isNotEmpty(mEditText.getText().toString()) && isShowClear) {
                    mClearImage.setVisibility(View.VISIBLE);
                }
                mBottomLine.setBackgroundResource(R.color.a1);
            } else {
                isFocus = false;
                mClearImage.setVisibility(View.GONE);
                mBottomLine.setBackgroundResource(R.color.b2);
            }
        }
    };

    private void setHintText(EditText editText, String hintText, int textSize) {
        if (StringUtils.isNotEmpty(hintText)) {
            SpannableString ss = new SpannableString(hintText);
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editText.setHint(new SpannedString(ss));
        }
    }

    public void setOnFocusChanged(OnFocusChanged onFocusChanged) {
        this.mOnFocusChanged = onFocusChanged;
    }

    public float getEditTextSize() {
        return mEditText.getTextSize();
    }

    public interface OnFocusChanged {
        void onFocusChange(View view, boolean hasFocus);
    }

}
