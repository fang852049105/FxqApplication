package com.example.fangxq.myapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.fangxq.myapplication.R;
import com.example.fangxq.myapplication.utils.PhoneNumFormatWatcher;
import com.fxq.lib.utils.Utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Fangxq on 2017/6/27.
 */

public class NumberClearEditText extends RelativeLayout {
    private String mHint;
    private int mTextColorHint;
    private int mTextColor;
    private int mHintTextSize;
    private int mTextSize;
    private int mInputType;
    private Context mContext;
    private EditText mEditText;
    private ImageView mClearImage;
    private View mBottomLine;
    private OnFocusChanged mOnFocusChanged;
    private static final int DEFAULT_TEXT_SIZE = 14;

    public NumberClearEditText(Context context) {
        super(context);
        this.mContext = context;
        initView(null);
    }

    public NumberClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(attrs);
    }

    public NumberClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        LayoutInflater.from(mContext).inflate(R.layout.view_edit_text_number, this);
        TypedArray attrsArray = mContext.obtainStyledAttributes(attrs, R.styleable.NumberClearEditText);
        mHint = attrsArray.getString(R.styleable.NumberClearEditText_android_hint);
        mTextColorHint = attrsArray.getInteger(R.styleable.NumberClearEditText_android_textColorHint, R.color.b2);
        mTextColor = attrsArray.getInteger(R.styleable.NumberClearEditText_android_textColor, R.color.b1);
        mTextSize = attrsArray.getDimensionPixelSize(R.styleable.NumberClearEditText_edit_text_size, Utils.dip2px(mContext,DEFAULT_TEXT_SIZE));
        mHintTextSize = attrsArray.getDimensionPixelSize(R.styleable.NumberClearEditText_edit_hint_text_size, Utils.dip2px(mContext,DEFAULT_TEXT_SIZE));
        mInputType = attrsArray.getInteger(R.styleable.NumberClearEditText_android_inputType, -1);
        attrsArray.recycle();
        mEditText = (EditText) findViewById(R.id.number_edit_text);
        mClearImage = (ImageView) findViewById(R.id.number_edit_clear_image);
        mBottomLine = findViewById(R.id.clear_bottom_line);
        mEditText.setHintTextColor(mTextColorHint);
        setHintText(mEditText, mHint, Utils.px2dip(mContext, mHintTextSize));
        mEditText.setTextColor(mTextColor);
        mEditText.setTextSize(Utils.px2dip(mContext, mTextSize));
        mEditText.addTextChangedListener(new PhoneNumFormatWatcher(mEditText, mClearImage));
        if (mInputType != -1) {
            mEditText.setInputType(mInputType);
        } else {
            mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        if (mInputType == InputType.TYPE_CLASS_PHONE) {
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
            mEditText.setKeyListener(numberKeyListener);
        }
        mEditText.addTextChangedListener(textWatcher);
        mEditText.setOnFocusChangeListener(onFocusChangeListener);
    }

    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (mOnFocusChanged != null) {
                mOnFocusChanged.onFocusChange(v, hasFocus);
            }
            if (hasFocus) {
                if (StringUtils.isNotEmpty(mEditText.getText().toString())) {
                    mClearImage.setVisibility(View.VISIBLE);
                }
                mBottomLine.setBackgroundResource(R.color.a1);
            } else {
                mClearImage.setVisibility(View.GONE);
                mBottomLine.setBackgroundResource(R.color.b2);
            }
        }
    };

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
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
            if (s.toString().length() > 0) {
                mClearImage.setVisibility(View.VISIBLE);
            } else {
                mClearImage.setVisibility(View.GONE);
            }
        }
    };

    private NumberKeyListener numberKeyListener = new NumberKeyListener() {
        @Override
        protected char[] getAcceptedChars() {
            return new char[] { '1', '2', '3', '4', '5', '6', '7', '8','9', '0'};
        }

        @Override
        public int getInputType() {
            return InputType.TYPE_CLASS_PHONE;
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