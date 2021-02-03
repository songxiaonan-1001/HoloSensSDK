package com.huawei.holosens.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huawei.holosens.R;

public class FingerDialog extends Dialog {



    /**
     * 确认和取消按钮
     */
    private TextView negtiveBn ,positiveBn;
    private Context mContext;
    public FingerDialog(Context context) {
        super(context, R.style.FingerDialog);
        mContext = context;
    }

    /**
     * 底部是否只有一个按钮
     */
    private boolean isSingle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_flinger_print);
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
    }

    /**
     * 设置确定取消按钮的回调
     */
    private OnClickBottomListener onClickBottomListener;
    public FingerDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
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

    public FingerDialog setSingle(boolean single) {
        isSingle = single;
        return this ;
    }
}
