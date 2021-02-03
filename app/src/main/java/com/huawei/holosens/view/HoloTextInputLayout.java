package com.huawei.holosens.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.huawei.holosens.R;
import com.huawei.holosens.utils.RegularUtil;

import java.lang.reflect.Field;

import androidx.annotation.StringRes;

/**
 * @class name HoloTextInputLayout
 * @date 创建时间：2020-01-09    21:12
 * @description:    默认带有清除按钮；
 *                  若为密码输入框，则显示小眼睛；
 *                  hint显示长hint，hint收缩到顶部时显示短hint;
 *                  输入错误警告，错误警告使用setHelperText而不用setError是因为setError时顶部hint会变成error颜色;
 *                  设置输入类型啊输入长度啊，使用getEditText().setXXX;
 *                  注意不可设置EditText的setOnFocusChangeListener，可以使用HoloTextInputLayout.setOnFocusChangeListener
 */
public class HoloTextInputLayout extends RelativeLayout implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {


    private TextInputLayout mTextInputLayout;
    private EditText mEditText;
    private ImageView mBtnClear, mBtnEye;
    private TextView mBtnForgetPwd;

    private String mLongHint, mShortHint;
    private boolean mClearEnable, mPasswordEnable;

    private String mHelperText = "";
    private int mHelperTextColor = -1;

    private TextAfterWatcher mTextAfterWatcher;
    private OnFocusChangeListener mOnFocusChangeListener;
    private View.OnClickListener mForgetPasswordClickListener;


    public HoloTextInputLayout(Context context) {
        super(context);
        init(context, null);
    }

    public HoloTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HoloTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_text_input, this, true);

        mTextInputLayout = view.findViewById(R.id.til_main);
        mEditText = view.findViewById(R.id.et_main);
        mBtnClear = view.findViewById(R.id.btn_clear);
        mBtnEye = view.findViewById(R.id.btn_eye);
        mBtnForgetPwd = view.findViewById(R.id.forget_password);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HoloTextInputLayout);
        mLongHint = typedArray.getString(R.styleable.HoloTextInputLayout_longHint);
        mShortHint = typedArray.getString(R.styleable.HoloTextInputLayout_shortHint);
        mClearEnable = typedArray.getBoolean(R.styleable.HoloTextInputLayout_clearEnable, true);
        mPasswordEnable = typedArray.getBoolean(R.styleable.HoloTextInputLayout_passwordEnable, false);
        boolean forgetPasswordEnable = typedArray.getBoolean(R.styleable.HoloTextInputLayout_forgetPasswordEnable, false);
        if (forgetPasswordEnable) {
            mBtnForgetPwd.setVisibility(VISIBLE);
        } else {
            mBtnForgetPwd.setVisibility(GONE);
        }
        typedArray.recycle();

        mTextInputLayout.setHint(mLongHint);
        mBtnClear.setVisibility(GONE);
        if (mPasswordEnable) {
            mEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20), new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    //当前输入的字符串若包含中文，则整个字符串都被过滤掉，返回""
                    if (RegularUtil.isContainChinese(source)) {
                        return "";
                    }
                    return null;
                }
            }});

            mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mEditText.setLongClickable(false);
            mEditText.setTextIsSelectable(false);
            mEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
            mEditText.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // setInsertionDisabled when user touches the view
                        setInsertionDisabled();
                    }
                    return false;
                }
            });
        }
        mBtnEye.setVisibility(GONE);

        mEditText.addTextChangedListener(this);
        mEditText.setOnFocusChangeListener(this);
        mBtnClear.setOnClickListener(this);
        mBtnEye.setOnClickListener(this);
        mBtnForgetPwd.setOnClickListener(this);
    }

    /**
     * 小米/OPPO手机上禁止复制粘贴功能
     * 反射 android.widget.Editor 修改弹框菜单不显示
     */
    private void setInsertionDisabled() {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editorObject = editorField.get(mEditText);
            Class editorClass = Class.forName("android.widget.Editor");
            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
            mInsertionControllerEnabledField.setAccessible(true);
            mInsertionControllerEnabledField.set(editorObject, false);
            Field mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled");
            mSelectionControllerEnabledField.setAccessible(true);
            mSelectionControllerEnabledField.set(editorObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_clear) {
            if (mClearEnable) {
                mEditText.setText("");
            }
        } else if (v.getId() == R.id.btn_eye) {
            if (mPasswordEnable) {
                mBtnEye.setSelected(!mBtnEye.isSelected());
                int selection = mEditText.getSelectionEnd();
                if (mBtnEye.isSelected()) {
//                    mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEditText.setTransformationMethod(null);
                } else {
//                    mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                mEditText.setSelection(selection);
            }
        } else if (v.getId() == R.id.forget_password) {
            if (null != mForgetPasswordClickListener) {
                mForgetPasswordClickListener.onClick(v);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        if (TextUtils.isEmpty(s)) {
//            mBtnClear.setVisibility(GONE);
//        } else {
        if (mClearEnable && hasFocus()) {
            mBtnClear.setVisibility(VISIBLE);
        } else {
            mBtnClear.setVisibility(GONE);
        }
//        }
        setHelperText(mHelperText);
        mEditText.setSelected(false);

        if (null != mTextAfterWatcher) {
            mTextAfterWatcher.afterTextChanged(s);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mBtnEye.setVisibility(GONE);
        mBtnClear.setVisibility(GONE);
        if (hasFocus) {
            mTextInputLayout.setHint(mShortHint);
            if (mPasswordEnable) {
                mBtnEye.setVisibility(VISIBLE);
            }
            if (mClearEnable) {
                mBtnClear.setVisibility(VISIBLE);
            }
        } else {
            if (TextUtils.isEmpty(mEditText.getText())) {
                mTextInputLayout.setHint(mLongHint);
            } else {
                mTextInputLayout.setHint(mShortHint);
                if (mPasswordEnable) {
                    mBtnEye.setVisibility(VISIBLE);
                }
            }
        }

        if (null != mOnFocusChangeListener) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    public TextInputLayout getTextInputLayout() {
        return mTextInputLayout;
    }

    public EditText getEditText() {
        return mEditText;
    }

    public void setMaxLength(int maxLength) {
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void setText(@StringRes int resid) {
        mEditText.setText(resid);
    }

    public void setText(String text) {
        mEditText.setText(text);
        mEditText.setSelection(text.length());
    }

    public String getText() {
        return mEditText.getText().toString();
    }

    public void setLongHint(@StringRes int resid) {
        mLongHint = getResources().getString(resid);
        onFocusChange(mEditText, mEditText.hasFocus());
    }

    public void setLongHint(String longHint) {
        mLongHint = longHint;
        onFocusChange(mEditText, mEditText.hasFocus());
    }

    public void setShortHint(@StringRes int resid) {
        mShortHint = getResources().getString(resid);
        onFocusChange(mEditText, mEditText.hasFocus());
    }

    public void setShortHint(String shortHint) {
        mShortHint = shortHint;
        onFocusChange(mEditText, mEditText.hasFocus());
    }

    public void setErrorText(@StringRes int resid) {
        setErrorText(getResources().getString(resid));
    }

    public void setErrorText(String errorText) {
        mTextInputLayout.setHelperTextTextAppearance(R.style.ErrorTextAppearance);
        mTextInputLayout.setHelperText(errorText);
        mEditText.setSelected(true);
    }

    public void setHelperText(@StringRes int resid) {
        setHelperText(getResources().getString(resid));
    }

    public void setHelperText(String helperText) {
        mHelperText = helperText;
//        mEditText.setPadding(0, 0, 0, 0);
        if(mHelperTextColor == -1)
            mTextInputLayout.setHelperTextTextAppearance(R.style.HintTextAppearance);
        else
            mTextInputLayout.setHelperTextTextAppearance(mHelperTextColor);
        mTextInputLayout.setHelperText(helperText);
    }

    public void setHelperText(String helperText, int res) {
        mHelperText = helperText;
        mHelperTextColor = res;
        mTextInputLayout.setHelperTextTextAppearance(res);
        mTextInputLayout.setHelperText(helperText);
    }

    public void setForgetPasswordClickListener(View.OnClickListener onClickListener) {
        mForgetPasswordClickListener = onClickListener;
    }

    public void setTextAfterWatcher(TextAfterWatcher textAfterWatcher) {
        mTextAfterWatcher = textAfterWatcher;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }


    public interface TextAfterWatcher {

        void afterTextChanged(Editable s);

    }

}
