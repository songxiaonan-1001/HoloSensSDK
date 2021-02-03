package com.huawei.holosens.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.holosens.R;

public class FingerCheckDialog extends Dialog {



    /**
     * 确认和取消按钮
     */
    private LinearLayout layout;
    private TextView negtiveBn ,positiveBn;
    private ImageView imageView;
    private TextView mTitle, mContent;
    private View midLine;
    private Context mContext;
    public FingerCheckDialog(Context context) {
        super(context, R.style.FingerDialog);
        mContext = context;
    }

    /**
     * 底部是否只有一个按钮
     */
    private boolean isSingle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_flinger_check);
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
        layout =  findViewById(R.id.layout);
        negtiveBn =  findViewById(R.id.btn_negative);
        positiveBn =  findViewById(R.id.btn_positive);
        imageView =  findViewById(R.id.image);
        mTitle =  findViewById(R.id.title);
        mContent =  findViewById(R.id.content);
        midLine =  findViewById(R.id.dialog_custom_midline);
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

    }

    /**
     * 设置确定取消按钮的回调
     */
    private OnClickBottomListener onClickBottomListener;
    public FingerCheckDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
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

    public FingerCheckDialog setSingle(boolean single) {
        isSingle = single;
        return this ;
    }

    public LinearLayout getLayout(){
        return layout;
    }

    /**
     * 显示标题
     * 用于指纹验证错误的时候
     */
    public void retry(){
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(mContext.getResources().getString(R.string.finger_try_again));
        mContent.setTextColor(mContent.getResources().getColor(R.color.finger_check_content_color_retry));
    }

    /**
     * 验证失败
     */
    public void handlerFail(){
        imageView.setVisibility(View.GONE);
        mTitle.setText(mContext.getResources().getString(R.string.tips));
        mContent.setText(mContext.getResources().getString(R.string.finger_fail));
        mContent.setTextColor(mContent.getResources().getColor(R.color.finger_check_content_color_fail));
        positiveBn.setText(mContext.getResources().getString(R.string.sure));
    }
}
