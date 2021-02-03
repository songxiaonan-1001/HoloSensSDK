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
        deviceId = intent.getStringExtra("deviceId");
        deviceType =intent.getStringExtra("deviceType");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            StatusBarUtil.setLightStatusBarColor(this);
            window.setStatusBarColor(getResources().getColor(R.color.bg_settings));
        }
        TopBarLayout topBarLayout = getTopBarView();
        topBarLayout.setTitle("设备通道列表");
        Fragment channelListFragment = new ChannelListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("deviceId",deviceId);
        bundle.putString("deviceType",deviceType);
        channelListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, channelListFragment).commit();

    }


}