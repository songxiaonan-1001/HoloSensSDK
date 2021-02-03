//package com.huawei.holosens.view;
//
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//
//import com.huawei.holosens.R;
//
//public class DownloadDialog extends Dialog {
//
//
//    private ProgressBar mbar;
//
//    public DownloadDialog(Context context) {
//        super(context, R.style.UpdateDialog);
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_update);
//        //按空白处不能取消动画
//        setCanceledOnTouchOutside(false);
//        //初始化界面控件
//        initView();
//        //初始化界面数据
//        refreshView();
//        //初始化界面控件的事件
//        initEvent();
//    }
//
//    /**
//     * 初始化界面的确定和取消监听器
//     */
//    private void initEvent() {
////        //设置确定按钮被点击后，向外界提供监听
////        positiveBn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if ( onClickBottomListener!= null) {
////                    onClickBottomListener.onPositiveClick();
////                }
////            }
////        });
////        //设置取消按钮被点击后，向外界提供监听
////        negtiveBn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if ( onClickBottomListener!= null) {
////                    onClickBottomListener.onNegtiveClick();
////                }
////            }
////        });
//    }
//
//    /**
//     * 更新进度条
//     * @param rotation
//     */
//    private void refreshView(float rotation) {
//        //如果用户自定了title和message
//
//        mbar.setProgressRotation(rotation);
//
//    }
//
//    @Override
//    public void show() {
//        super.show();
//        refreshView(0);
//    }
//
//    /**
//     * 初始化界面控件
//     */
//    private void initView() {
//
//        messageTv =  findViewById(R.id.message);
//    }
//
//}
