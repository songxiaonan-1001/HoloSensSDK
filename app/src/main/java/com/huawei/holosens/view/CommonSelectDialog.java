package com.huawei.holosens.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.huawei.holosens.R;

public class CommonSelectDialog extends Dialog implements View.OnClickListener {


    private TextView mBtnNvr, mBtnIpc;

    private String oper1Str, oper2Str;


    public CommonSelectDialog(Context context, String oper1Str, String oper2Str) {
        super(context, R.style.UpdateDialog);
        this.oper1Str = oper1Str;
        this.oper2Str = oper2Str;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_device_type);

        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
    }

    @Override
    public void show() {
        super.show();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mBtnNvr = findViewById(R.id.btn_nvr);
        if (!TextUtils.isEmpty(oper1Str)) {
            mBtnNvr.setText(oper1Str);
        }
        mBtnIpc = findViewById(R.id.btn_ipc);
        if (!TextUtils.isEmpty(oper2Str)) {
            mBtnIpc.setText(oper2Str);
        }
        mBtnNvr.setOnClickListener(this);
        mBtnIpc.setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_nvr:
                if (null != onClickBottomListener) {
                    onClickBottomListener.onOper1Click(mBtnNvr.getText().toString().trim());
                }
                dismiss();
                break;
            case R.id.btn_ipc:
                if (null != onClickBottomListener) {
                    onClickBottomListener.onOper2Click(mBtnIpc.getText().toString().trim());
                }
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    /**
     * 设置确定取消按钮的回调
     */
    private OnClickBottomListener onClickBottomListener;

    public CommonSelectDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public interface OnClickBottomListener {
        /**
         * 点击确定按钮事件
         */
        void onOper1Click(String text);

        /**
         * 点击取消按钮事件
         */
        void onOper2Click(String text);
    }

}
