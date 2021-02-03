package com.huawei.holosens.view;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huawei.holosens.R;

public class ProgressDialog extends Dialog {

    private ProgressBar mBar;
    private TextView mProgressValue;

    public ProgressDialog(Context context) {
        super(context, R.style.UpdateDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progressbar);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
    }


    @Override
    public void show() {
        super.show();
    }

    @SuppressLint("SetTextI18n")
    public void updateProgress(int progress){
        mProgressValue.setText(progress+"%");
        mBar.setProgress(progress);
    }
    /**
     * 初始化界面控件
     */
    private void initView() {

        mProgressValue  = findViewById(R.id.progress_num);
        mBar  = findViewById(R.id.progressbar);
    }
}
