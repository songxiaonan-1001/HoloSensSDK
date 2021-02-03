package com.huawei.holosens;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.huawei.holosens.base.BaseActivity;

public class CommonActivity extends BaseActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTopBarView().setTopBar(R.drawable.selector_back_icon, -1, R.string.app_name_holo, this);
        initView();
    }

    private void initView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
        }
    }
}
