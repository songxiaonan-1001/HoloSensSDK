package com.huawei.holosens.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.huawei.holosens.R;

public class ViewsDialog extends Dialog {



    /**
     * 确认和取消按钮
     */
    private TextView negtiveBn ,positiveBn;
    private ClearEditText mName;
    private String content;
    private String title;
    private TextView mLimitTip;
    private Context mContext;
    public ViewsDialog(Context context) {
        super(context, R.style.UpdateDialog);
        mContext = context;
    }

    /**
     * 底部是否只有一个按钮
     */
    private boolean isSingle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_view);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        positiveBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( onClickBottomListener!= null) {
                    onClickBottomListener.onPositiveClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        negtiveBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( onClickBottomListener!= null) {
                    onClickBottomListener.onNegtiveClick();
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        negtiveBn =  findViewById(R.id.btn_negative);
        positiveBn =  findViewById(R.id.btn_positive);
        /*
          显示的标题
         */
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(title);
        mName = findViewById(R.id.view_name);
        mName.setText(content);

        mLimitTip = findViewById(R.id.tip);
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mName.getText().length() > 16){
                    mName.setText(mName.getText().toString().substring(0, 16));
                    mName.setSelection(16);
                    showLimitTip();
                }else{
                    dismissLimitTip();
                }
            }
        });
    }

    /**
     * 设置确定取消按钮的回调
     */
    private OnClickBottomListener onClickBottomListener;
    public ViewsDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }
    public interface OnClickBottomListener{
        /**
         * 点击确定按钮事件
         */
        void onPositiveClick();
        /**
         * 点击取消按钮事件
         */
        void onNegtiveClick();
    }


    public boolean isSingle() {
        return isSingle;
    }

    public ViewsDialog setSingle(boolean single) {
        isSingle = single;
        return this ;
    }
    public ViewsDialog setContent(String content) {
        this.content = content;
        if (null != mName) {
            mName.setText(content);
        }
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ViewsDialog setTitle(String title) {
        this.title = title;
        return this ;
    }

    public String getName(){
        return mName.getText().toString();
    }

    public void showLimitTip(){
        mName.setBackground(mContext.getResources().getDrawable(R.drawable.view_over_limit_border));
        mLimitTip.setVisibility(View.VISIBLE);
    }

    public void dismissLimitTip(){
        mLimitTip.setVisibility(View.INVISIBLE);
        mName.setBackground(mContext.getResources().getDrawable(R.drawable.shape_save_view_edit));
    }
}
