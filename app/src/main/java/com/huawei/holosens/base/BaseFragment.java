package com.huawei.holosens.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;

import com.huawei.holobasic.play.IHandlerLikeNotify;
import com.huawei.holobasic.play.IHandlerNotify;
import com.huawei.holobasic.utils.ActivityManager;

import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;


public abstract class BaseFragment extends Fragment implements
        IHandlerNotify, IHandlerLikeNotify {

    protected BaseActivity mActivity;
    private WeakReference<Activity> mWeakReference;
    protected MyHandler mHandler = new MyHandler(this);
    private IHandlerNotify handlerNotify = this;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        mWeakReference = new WeakReference<Activity>(mActivity);
        ActivityManager.getInstance().push(mWeakReference);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseApplication) mActivity.getApplication()).setCurrentNotifier(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 将当前Fragment作为接收服务器回调(onNotify/onHandler)的对象
        if (mWeakReference != null) {
            ActivityManager.getInstance().pop(mWeakReference);
        }
    }

    /**
     * 共通的Handler
     */
    protected static class MyHandler extends android.os.Handler {

        private final WeakReference<BaseFragment> mWeakReference;

        public MyHandler(BaseFragment fragment) {
            mWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseFragment mFragment;
            if ((mFragment = mWeakReference.get()) != null) {
                mFragment.handlerNotify.onHandler(msg.what, msg.arg1, msg.arg2, msg.obj);
            }
        }
    }

//    /**
//     * 账号失效，被踢
//     */
//    private UpdateDialog mLogoutDialog;
//
//    public void showAccountInvalid(){
//        if(mLogoutDialog == null){
//            UpdateDialog.Builder builder = new UpdateDialog.Builder(mActivity);
//            mLogoutDialog = builder.setMessage(getString(R.string.account_invalid))
//                    .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            // 除了当前Activity以外,其它全部移除
//                            ActivityManager.getInstance().popAllActivityExceptThis();
//                            // 跳转到登录界面
//                            Intent intent = new Intent();
//                            intent.setClass(mActivity, LoginActivity.class);
//                            startActivity(intent);
//                            // 结束当前Activity
//                            Activity currentActivity = ActivityManager.getInstance().currentActivity();
//                            currentActivity.finish();
//                        }
//                    }).create();
//        }
//        mLogoutDialog.show();
//    }

    /**
     * 服务器回调
     */
    @Override
    public void onNotify(int what, int arg1, int arg2, Object obj) {
    }

    @Override
    public void onHandler(int what, int arg1, int arg2, Object obj) {
    }
}
