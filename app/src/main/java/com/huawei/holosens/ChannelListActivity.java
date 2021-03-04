package com.huawei.holosens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.huawei.holobasic.utils.StatusBarUtil;
import com.huawei.holosens.base.BaseActivity;
import com.huawei.holosens.view.TopBarLayout;

import androidx.fragment.app.Fragment;

public class ChannelListActivity extends BaseActivity {

    private String deviceId;
    private String deviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);

        Intent intent = getIntent();
        //获取设备id
        deviceId = intent.getStringExtra("deviceId");
        //获取设备类型(IPC/NVR)
        deviceType = intent.getStringExtra("deviceType");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置状态栏字体颜色
            StatusBarUtil.setLightStatusBarColor(this);
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.bg_settings));
        }
        //获取TopBar,设置标题
        TopBarLayout topBarLayout = getTopBarView();
        topBarLayout.setTitle("设备通道列表");

        //创建fragment,并传递信息(设备id,设备类型)到fragment中
        Fragment channelListFragment = new ChannelListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("deviceId", deviceId);
        bundle.putString("deviceType", deviceType);
        channelListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, channelListFragment).commit();

    }


}