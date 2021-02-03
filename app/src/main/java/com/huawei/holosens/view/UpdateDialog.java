package com.huawei.holosens.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.holosens.R;

public class UpdateDialog  extends Dialog {

    /**
     * 显示的版本号
     */
    private TextView versionTv ;

    /**
     * 显示的版本大小
     */
    private TextView sizeTv ;


    /**
     * 显示的消息
     */
    private TextView messageTv ;

    /**
     * 确认和取消按钮
     */
    private TextView negtiveBn ,positiveBn;

    private View midLine;

    private LinearLayout mContentLayout;

    public UpdateDialog(Context context) {
        super(context, R.style.UpdateDialog);
    }

    /**
     * 都是内容数据
     */
    private String message;
    private String title;
    private String version;
    private String size;

    /**
     * 底部是否只有一个按钮
     */
    private boolean isSingle = false;
    /**
     * 是否显示内容
     */
    private boolean showContent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        refreshView();
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

    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {
        //如果用户自定了title和message

        if (!TextUtils.isEmpty(version)) {
            versionTv.setText(version);
        }
        if (!TextUtils.isEmpty(size)) {
            sizeTv.setText(size);
        }
        if (!TextUtils.isEmpty(message)) {
            messageTv.setText(message);
        }

        if (!TextUtils.isEmpty(message)) {
            messageTv.setText(message);
        }

        /**
         * 只显示一个按钮的时候隐藏取消按钮，回掉只执行确定的事件
         */
        if (isSingle){
            midLine.setVisibility(View.GONE);
            negtiveBn.setVisibility(View.GONE);
        }else {
            negtiveBn.setVisibility(View.VISIBLE);
            midLine.setVisibility(View.VISIBLE);
        }
        if(showContent){
            mContentLayout.setVisibility(View.VISIBLE);
        }else{
            mContentLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void show() {
        super.show();
        refreshView();
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
        versionTv = findViewById(R.id.version);
        sizeTv =  findViewById(R.id.size);
        messageTv =  findViewById(R.id.message);
        midLine =  findViewById(R.id.dialog_custom_midline);
        mContentLayout =  findViewById(R.id.content_layout);
    }

    /**
     * 设置确定取消按钮的回调
     */
    private OnClickBottomListener onClickBottomListener;
    public UpdateDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
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

    public String getMessage() {
        return message;
    }

    public UpdateDialog setMessage(String message) {
        this.message = message;
        return this ;
    }

    public String getVersion() {
        return version;
    }

    public UpdateDialog setVersion(String version) {
        this.version = version;
        return this ;
    }

    public String getSize() {
        return size;
    }

    public UpdateDialog setSize(String size) {
        this.size = size;
        return this ;
    }

    public String getTitle() {
        return title;
    }

    public UpdateDialog setTitle(String title) {
        this.title = title;
        return this ;
    }


    public boolean isSingle() {
        return isSingle;
    }

    public UpdateDialog setSingle(boolean single) {
        isSingle = single;
        return this ;
    }

    public UpdateDialog setShowContent(boolean showContent) {
        this.showContent = showContent;
        return this ;
    }
}
