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

import com.huawei.holosens.R;
import com.huawei.holosens.utils.RegularUtil;

import java.lang.reflect.Field;

import androidx.annotation.StringRes;

/**
 * @class name HoloEditTextLayout
 * @date 创建时间：2020-01-09    21:12
 * @description:    默认带有清除按钮；
 *                  若为密码输入框，则显示小眼睛；
 *                  底部横线，默认隐藏；
 *                  顶部显示标题；
 *                  设置输入类型啊输入长度啊，使用getEditText().setXXX;
 *                  注意不可设置EditText的setOnFocusChangeListener，可以使用HoloEditTextLayout.setOnFocusChangeListener
 */
public class HoloEditTextLayout extends RelativeLayout implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {


    private TextView mTvTitle;
    private EditText mEditText;
    private ImageView mBtnClear, mBtnEye;

    private boolean mClearEnable, mPasswordEnable;

    private TextAfterWatcher mTextAfterWatcher;
    private OnFocusChangeListener mOnFocusChangeListener;


    public HoloEditTextLayout(Context context) {
        super(context);
        init(context, null);
    }

    public HoloEditTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HoloEditTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_holo_edittext, this, true);

        mTvTitle = view.findViewById(R.id.tv_title);
        mEditText = view.findViewById(R.id.et_main);
        mBtnClear = view.findViewById(R.id.btn_clear);
        mBtnEye = view.findViewById(R.id.btn_eye);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HoloEditTextLayout);
        String title = typedArray.getString(R.styleable.HoloEditTextLayout_title);
        mTvTitle.setText(title);
        String hint = typedArray.getString(R.styleable.HoloEditTextLayout_hint);
        mEditText.setHint(hint);
        mClearEnable = typedArray.getBoolean(R.styleable.HoloEditTextLayout_enableClear, true);
        mPasswordEnable = typedArray.getBoolean(R.styleable.HoloEditTextLayout_enablePassword, false);
        if (typedArray.getBoolean(R.styleable.HoloEditTextLayout_showBottomLine, false)) {
            findViewById(R.id.line).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.line).setVisibility(GONE);
        }
        boolean isEdit = typedArray.getBoolean(R.styleable.HoloEditTextLayout_isedit, true);
        if (isEdit) {
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mBtnClear.setVisibility(VISIBLE);
            mBtnEye.setVisibility(VISIBLE);
        } else {
            mEditText.setFocusable(false);
            mEditText.setFocusableInTouchMode(false);
            mBtnClear.setVisibility(GONE);
            mBtnEye.setVisibility(GONE);
        }
        //控制输入框的最大输入字数，0代表不控制
        int editMaxLength = typedArray.getInt(R.styleable.HoloEditTextLayout_edit_max_length, 20);
        if (editMaxLength != 0) {
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(editMaxLength) {
            }});
        }
        typedArray.recycle();

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

        if (null != mTextAfterWatcher) {
            mTextAfterWatcher.afterTextChanged(s);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mBtnEye.setVisibility(GONE);
        mBtnClear.setVisibility(GONE);
        if (hasFocus) {
            if (mPasswordEnable) {
                mBtnEye.setVisibility(VISIBLE);
            }
            if (mClearEnable) {
                mBtnClear.setVisibility(VISIBLE);
            }
        } else {
            if (!TextUtils.isEmpty(mEditText.getText())) {
                if (mPasswordEnable) {
                    mBtnEye.setVisibility(VISIBLE);
                }
            }
        }

        if (null != mOnFocusChangeListener) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
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
        mEditText.setSelection(mEditText.getText().length());
    }

    public String getText() {
        String text = mEditText.getText().toString();
        if (!TextUtils.isEmpty(text) && "null".equalsIgnoreCase(text)) {
            text = "";
        }
        return text;
    }

    public void setHint(@StringRes int resid) {
        setHint(getResources().getString(resid));

    }

    public void setHint(String hint) {
        mEditText.setHint(hint);
    }

    public void setTitle(@StringRes int resid) {
        setTitle(getResources().getString(resid));
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void showBottomLine(boolean isVisible) {
        if (isVisible) {
            findViewById(R.id.line).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.line).setVisibility(GONE);
        }
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
