package com.huawei.holosens.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huawei.holosens.R;

import java.util.List;

import androidx.annotation.Nullable;

public class ChannelListAdapter extends BaseQuickAdapter<ChannelResponseBean.ChannelsBean, BaseViewHolder> {


    public ChannelListAdapter(int layoutResId, @Nullable List<ChannelResponseBean.ChannelsBean> data) {
        super(layoutResId, data);
       /* item_device_list*/
    }

    @Override
    protected void convert(BaseViewHolder helper, ChannelResponseBean.ChannelsBean item) {

       TextView tv_device_name =  helper.getView(R.id.tv_device_name);
       String deviceName = item.getChannel_name();
       if(!TextUtils.isEmpty(deviceName)){
           tv_device_name.setText(item.getChannel_name());
       }else {
           tv_device_name.setText(item.getChannel_id());
       }
       if(item.getChannel_state().equals("ONLINE")){
           helper.getView(R.id.device_state).setBackgroundResource(R.mipmap.ic_device_online);
       }else{
           helper.getView(R.id.device_state).setBackgroundResource(R.mipmap.ic_device_offline);
       }
    }
}
