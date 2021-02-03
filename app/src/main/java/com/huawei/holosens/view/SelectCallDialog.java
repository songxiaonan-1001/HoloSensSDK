package com.huawei.holosens.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.huawei.holosens.R;

public class SelectCallDialog extends Dialog implements View.OnClickListener {


    private TextView mTvSupportNvr, mTvSupportChannel;
    private boolean mSupportNvr, mSupportChannel;


    public SelectCallDialog(Context context) {
        super(context, R.style.UpdateDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_call);

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
        mTvSupportNvr = findViewById(R.id.btn_call_nvr);
        mTvSupportChannel = findViewById(R.id.btn_call_channel);
        mTvSupportNvr.setEnabled(mSupportNvr);
        mTvSupportChannel.setEnabled(mSupportChannel);
        mTvSupportNvr.setOnClickListener(this);
        mTvSupportChannel.setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call_nvr:
                if (null != onClickBottomListener) {
                    onClickBottomListener.onCallNvrClick();
                }
                break;
            case R.id.btn_call_channel:
                if (null != onClickBottomListener) {
                    onClickBottomListener.onCallChannelClick();
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    public void setSupport(boolean supportNvr, boolean supportChannel) {
        mSupportNvr = supportNvr;
        mSupportChannel = supportChannel;
        if (null != mTvSupportNvr) {
            mTvSupportNvr.setEnabled(supportNvr);
        }
        if (null != mTvSupportChannel) {
            mTvSupportChannel.setEnabled(supportChannel);
        }
    }

    /**
     * 设置确定取消按钮的回调
     */
    private OnClickBottomListener onClickBottomListener;
    public SelectCallDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }
    public interface OnClickBottomListener{
        /**
         * 点击确定按钮事件
         */
        void onCallNvrClick();
        /**
         * 点击取消按钮事件
         */
        void onCallChannelClick();
    }

}
