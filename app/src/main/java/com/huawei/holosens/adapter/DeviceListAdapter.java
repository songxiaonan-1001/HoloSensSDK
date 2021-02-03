package com.huawei.holosens.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huawei.holosens.R;

import java.util.List;

import androidx.annotation.Nullable;

public class DeviceListAdapter extends BaseQuickAdapter<DeviceResponseBean.DevicesBean, BaseViewHolder> {


    public DeviceListAdapter(int layoutResId, @Nullable List<DeviceResponseBean.DevicesBean> data) {
        super(layoutResId, data);
       /* item_device_list*/
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceResponseBean.DevicesBean item) {

       TextView tv_device_name =  helper.getView(R.id.tv_device_name);
       String deviceName = item.getDevice_name();

        ImageView device_state = helper.getView(R.id.device_state);
        device_state.setVisibility(View.VISIBLE);
        if(TextUtils.equals(item.getDevice_state(),"ONLINE")){
            if(item.getDevice_type().equals("NVR"))
                device_state.setImageResource(R.mipmap.ic_nvr_online);
            else
                device_state.setImageResource(R.mipmap.ic_device_online);
        }else {
            if(item.getDevice_type().equals("NVR"))
            device_state.setImageResource(R.mipmap.icon_nvr_offline);
        else
            device_state.setImageResource(R.mipmap.ic_device_offline);
        }

       if(!TextUtils.isEmpty(deviceName)){
           tv_device_name.setText(item.getDevice_name());
       }else {
           tv_device_name.setText(item.getDevice_id());
       }
    }
}
