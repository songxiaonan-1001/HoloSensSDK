package com.huawei.holosens.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.huawei.holosens.R;

public class DeviceTypeDialog extends Dialog implements View.OnClickListener {


    private TextView mBtnNvr, mBtnIpc;


    public DeviceTypeDialog(Context context) {
        super(context, R.style.UpdateDialog);
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
        mBtnIpc = findViewById(R.id.btn_ipc);
        mBtnNvr.setOnClickListener(this);
        mBtnIpc.setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_nvr:
                if (null != onClickBottomListener) {
                    onClickBottomListener.onNvrClick();
                }
                break;
            case R.id.btn_ipc:
                if (null != onClickBottomListener) {
                    onClickBottomListener.onIpcClick();
                }
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
    public DeviceTypeDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }
    public interface OnClickBottomListener {
        /**
         * 点击确定按钮事件
         */
        void onNvrClick();
        /**
         * 点击取消按钮事件
         */
        void onIpcClick();
    }

}
