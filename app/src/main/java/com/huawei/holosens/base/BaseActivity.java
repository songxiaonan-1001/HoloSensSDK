package com.huawei.holosens.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.huawei.holobase.bean.EventMsg;
import com.huawei.holobase.view.LoadingDialog;
import com.huawei.holobase.view.TipDialog;
import com.huawei.holobasic.play.IHandlerLikeNotify;
import com.huawei.holobasic.play.IHandlerNotify;
import com.huawei.holobasic.utils.ActivityManager;
import com.huawei.holobasic.utils.FileUtil;
import com.huawei.holobasic.utils.ScreenUtils;
import com.huawei.holobasic.utils.StatusBarUtil;
import com.huawei.holosens.R;
import com.huawei.holosens.commons.BundleKey;
import com.huawei.holosens.consts.SelfConsts;
import com.huawei.holosens.live.play.util.NavigationBarTools;
import com.huawei.holosens.utils.CommonPermissionUtils;
import com.huawei.holosens.utils.ScreenObserver;
import com.huawei.holosens.utils.ToastUtils;
import com.huawei.holosens.utils.aop.SingleClick;
import com.huawei.holosens.view.TopBarLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import pub.devrel.easypermissions.EasyPermissions;


public class BaseActivity extends FragmentActivity implements IHandlerLikeNotify,
        IHandlerNotify, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    protected CommonHandler handler = new CommonHandler(this);
    private WeakReference<Activity> mWeakReference;
    private IHandlerNotify handlerNotify = this;

    public Activity mActivity;
    /**
     * Title bar
     * 1.定义一个根布局,
     * 2.如果界面有标题栏,把标题栏添加到根布局中,
     * 3.把界面的布局添加到根布局中.
     */
    private View mBaseLayoutView;
    /**
     * root view
     */
    private LinearLayout mContentLayoutView;
    /**
     * 标题
     */
    private View mTopBarView;

    private LayoutInflater mLayoutInflater;

    protected int mScreenWidth, mScreenHeight, mNavBarHeight;

    public ScreenObserver mScreenObserver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeakReference = new WeakReference<>((Activity) this);
        mActivity = this;
        ((BaseApplication) getApplication()).setCurrentNotifier(this);

        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Log.e("play_dis_resume_pause", "lockScreen-解锁");
                BaseActivity.this.handlerNotify.onHandler(SelfConsts.WHAT_ON_SCREEN_ON, 0, 0, null);
            }

            @Override
            public void onScreenOff() {
                Log.e("play_dis_resume_pause", "lockScreen-锁屏");
                BaseActivity.this.handlerNotify.onHandler(SelfConsts.WHAT_ON_SCREEN_OFF, 0, 0, null);

            }
        });

        mLayoutInflater = LayoutInflater.from(this);
        ActivityManager.getInstance().push(mWeakReference);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            StatusBarUtil.setLightStatusBarColor(this);
            window.setStatusBarColor(getResources().getColor(R.color.white));
        }

//        if (this instanceof MainActivity) {
//            /**首页的topbar，在布局中添加*/
//        } else {
            initSuperView();
//        }

        mScreenWidth = ScreenUtils.getScreenWidth();
        mScreenHeight = ScreenUtils.getScreenHeight();
        mNavBarHeight = NavigationBarTools.getNavigationBarHeight(this);

        EventBus.getDefault().register(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((BaseApplication) getApplication()).setCurrentNotifier(this);
    }

    @Override
    protected void onDestroy() {
        mScreenObserver.stopScreenStateUpdate();

        handler.removeCallbacksAndMessages(null);
        /* 将当前Activity从Activity管理栈中移除*/
        if(mWeakReference!=null)
            ActivityManager.getInstance().pop(mWeakReference);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventMsg event) {
        switch (event.getMsgTag()) {
            case EventMsg.MSG_EVENT_QUIT:
                ActivityManager.getInstance().popAllActivityExceptThis();
                Intent intent = new Intent();
                intent.putExtra(BundleKey.LOGOUT, true);
//                intent.setClass(mActivity, LoginActivity.class);
                startActivity(intent);
                Activity currentActivity = ActivityManager.getInstance().currentActivity();
                currentActivity.finish();
                break;
        }
    }


    /**
     * 初始化操作
     */
    @SuppressLint("InflateParams")
    private void initSuperView() {
        mBaseLayoutView = mLayoutInflater.inflate(R.layout.activity_fragment_base_view, null);
        mContentLayoutView = (LinearLayout) mBaseLayoutView.findViewById(R.id.root_view);

        // 往根View中添加标题栏
        if (getTitleLayout() != -1 && null != mContentLayoutView) {
            mTopBarView = mLayoutInflater.inflate(getTitleLayout(), null);
            mContentLayoutView.addView(mTopBarView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * 子类重载该方法自定义标题布局文件<br />
     * 子类重载该方法返回-1,不显示标题
     *
     * @return
     */
    protected int getTitleLayout() {
        return R.layout.common_titlebar;
    }

    /**
     * 获取TopBar
     *
     * @return
     */
    public TopBarLayout getTopBarView() {
        if (mTopBarView instanceof TopBarLayout) {
            return (TopBarLayout) mTopBarView;
        }
        return null;
    }

    /**
     * 重写setContentView
     * 将布局添加到根View中
     */
    @Override
    public void setContentView(int layoutResID) {
//        if (this instanceof MainActivity) {
//            /**首页的topbar，在布局中添加*/
//            super.setContentView(layoutResID);
//        } else {
            View view = mLayoutInflater.inflate(layoutResID, null);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mContentLayoutView.addView(view);
            super.setContentView(mBaseLayoutView);
//        }

    }

    /**
     * 重写setContentView
     * 将布局添加到根View中
     */
    @Override
    public void setContentView(View view) {
//        if (this instanceof MainActivity) {
//            /**首页的topbar，在布局中添加*/
//            super.setContentView(view);
//        } else {
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mContentLayoutView.addView(view);
            super.setContentView(mBaseLayoutView);
//        }
    }

    /**
     * # 权限(使用google提供的sample)
     * # https://github.com/googlesamples/easypermissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions,
                grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            StringBuffer permissionNames = new StringBuffer();
            for (int i = 0; i < perms.size(); i++) {
                if (i > 0) {
                    permissionNames.append("、");
                }
                permissionNames.append(CommonPermissionUtils.getInstance().getNameByPermissionTag(this, perms.get(i)));
            }
            onWorksWhenPermissionsDenied();
            final TipDialog dialog = new TipDialog(mActivity);
            dialog.setTitle(getResources().getString(R.string.tips))
                    .setMessage(getString(R.string.permission_request, permissionNames))
                    .setSingle(false).setOnClickBottomListener(new TipDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    dialog.dismiss();
                    // 跳转到APP设置页面
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onNegtiveClick() {
                    dialog.dismiss();
                    finish();
                }
            }).show();
        }
    }

    /**
     * 当权限被拒绝以后所要做的事情
     *
     */
    public void onWorksWhenPermissionsDenied() {

    }

    @SingleClick
    @Override
    public void onClick(View v) {

    }

    /**
     * 跳转activity
     *
     * @param clazz
     * @param finishMyself
     * @param bundle
     */
    public void jump(Class<? extends BaseActivity> clazz, boolean finishMyself, Bundle bundle) {
        Intent i = new Intent(this, clazz);
        if (bundle != null) {
            i.putExtras(bundle);
        }
        //隐藏掉软键盘
        hideKeyboard();
        this.startActivity(i);
        if (finishMyself) {
            this.finish();
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        hideKeyboard(view);
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 共通的Handler
     */
    protected static class CommonHandler extends android.os.Handler {

        private final WeakReference<BaseActivity> mWeakReference;

        public CommonHandler(BaseActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseActivity mActivity;
            if ((mActivity = mWeakReference.get()) != null) {
                mActivity.handlerNotify.onHandler(msg.what, msg.arg1, msg.arg2, msg.obj);
            }
        }
    }

    @Override
    public void onNotify(int what, int arg1, int arg2, Object obj) {

    }

    @Override
    public void onHandler(int what, int arg1, int arg2, Object obj) {

    }

    @SuppressWarnings("unchecked")
    public <T> T $(@IdRes int resid) {
        return (T) findViewById(resid);
    }


    /**
     * 判断是否有sd卡
     *
     * @param minSize 最小容量
     * @param alert   是否弹提示
     * @return
     */
    public boolean hasSDCard(int minSize, boolean alert) {
        boolean canSave = true;
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            if (alert) {
                ToastUtils.show(this, R.string.sdcard_out_memery);
            }
            canSave = false;
        } else {
            if (FileUtil.getSDFreeSize() < minSize) {
                if (alert) {
                    ToastUtils.show(this, R.string.sdcard_notenough);
                }
                canSave = false;
            }
        }
        return canSave;
    }

    /**
     * 加载动画
     */
    private LoadingDialog mLoading;

    /**
     * 显示
     */
    protected void loading(boolean cancelable){
        if(null != mLoading && mLoading.isShowing())
            return;
        if(!isFinishing()){
            if(null == mLoading) {
                mLoading = new LoadingDialog(this);
                mLoading.setCancelable(cancelable);
            }

            mLoading.show();
        }
    }

    /**
     * 显示
     */
    protected void loading(boolean cancelable, DialogInterface.OnShowListener showListener) {
        if (null != mLoading && mLoading.isShowing())
            return;
        if (!isFinishing()) {
            if (null == mLoading) {
                mLoading = new LoadingDialog(this);
                mLoading.setCancelable(cancelable);
                mLoading.setOnShowListener(showListener);
            }

            mLoading.show();
        }
    }

    /**
     * 隐藏
     */
    protected void dismissLoading(){
        if(null != mLoading){
            if(mLoading.isShowing())
                mLoading.dismiss();
            mLoading = null;
        }
    }

}
