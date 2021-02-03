package com.huawei.holosens.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.huawei.holosens.R;

public class ViewMenuDialog extends Dialog {

    private View.OnClickListener mOnClickListener;

    public ViewMenuDialog(Context context, View.OnClickListener onClickListener) {
        super(context, R.style.UpdateDialog);
        mOnClickListener = onClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view_menu);

        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        //按空白处取消
        setCanceledOnTouchOutside(true);
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
        if (null != mOnClickListener) {
            findViewById(R.id.btn_rename).setOnClickListener(mOnClickListener);
            findViewById(R.id.btn_edit).setOnClickListener(mOnClickListener);
            findViewById(R.id.btn_delete).setOnClickListener(mOnClickListener);
            findViewById(R.id.btn_cancel).setOnClickListener(mOnClickListener);
        }
    }

}
