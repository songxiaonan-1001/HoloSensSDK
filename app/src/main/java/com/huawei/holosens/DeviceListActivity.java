package com.huawei.holosens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.huawei.holobasic.utils.StatusBarUtil;
import com.huawei.holobasic.utils.TUtils;
import com.huawei.holosens.base.BaseActivity;
import com.huawei.holosens.base.BaseApplication;
import com.huawei.holosens.utils.JniUtil;
import com.huawei.holosens.view.TopBarLayout;

import androidx.fragment.app.Fragment;

public class DeviceListActivity extends BaseActivity {

    static {
        System.loadLibrary("jvplay");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        initHoloPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            StatusBarUtil.setLightStatusBarColor(this);
            window.setStatusBarColor(getResources().getColor(R.color.bg_settings));
        }

        TopBarLayout topBarLayout = getTopBarView();
        topBarLayout.setTitle("设备列表");

        Fragment dev_fragment = new DeviceListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dev_fragment).commit();
    }

    /**
     * 初始化播放库
     */
    private void initHoloPlayer(){
        JniUtil.holosensPlayerInit(BaseApplication.getInstance());
        BaseApplication.getInstance().playerIdWindowMap.clear();
        JniUtil.windowPlayerIdMap.clear();
    }

    // -----------------------------------
    // 退出
    // -----------------------------------
    private long exitTime = 0;
    private boolean isKillProcess;

    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            isKillProcess = false;
            TUtils.show(getApplicationContext(), R.string.exit);
            exitTime = System.currentTimeMillis();
        } else {
            if (isKillProcess) {
                return;
            }
            isKillProcess = true;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            DeviceListActivity.this.finish();
            System.exit(0);
        }
    }

}