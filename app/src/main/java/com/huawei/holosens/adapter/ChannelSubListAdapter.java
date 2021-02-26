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

/**
 * 通道子列表适配器
 *
 * @author CSV
 */
public class ChannelSubListAdapter extends BaseQuickAdapter<ChannelResponseBean.ChannelsBean, BaseViewHolder> {


    public ChannelSubListAdapter(int layoutResId, @Nullable List<ChannelResponseBean.ChannelsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChannelResponseBean.ChannelsBean item) {
        TextView tv_device_name = helper.getView(R.id.tv_device_name);
        helper.getView(R.id.iv_checked).setVisibility(View.VISIBLE);

        String deviceName = item.getChannel_name();
        //获取通道名称(如果名称为空则获取通道id)
        if (!TextUtils.isEmpty(deviceName)) {
            tv_device_name.setText(item.getChannel_name());
        } else {
            tv_device_name.setText(item.getChannel_id());
        }

        //判断条目通道状态(枚举值:ONLINE/OFFLINE/UNALLOCATED)
        if (item.getChannel_state().equals("ONLINE")) {
            helper.getView(R.id.device_state).setBackgroundResource(R.mipmap.ic_device_online);
        } else {
            helper.getView(R.id.device_state).setBackgroundResource(R.mipmap.ic_device_offline);
        }

        //获取条目选中状态
        ImageView iv_checked = helper.getView(R.id.iv_checked);
        if (item.isSelected()) {
            iv_checked.setImageResource(R.mipmap.ic_checkbox_blue_pressed);
        } else {
            iv_checked.setImageResource(R.mipmap.ic_checkbox_grey_normal);
        }

        if (item.isEnable()) {
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setSelected(!item.isSelected());
                    notifyDataSetChanged();
                    //通道被选中
                    if (listener != null) {
                        listener.onNVRChannelSelected(item, item.isSelected());
                    }
                }
            });
            iv_checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setSelected(!item.isSelected());
                    notifyDataSetChanged();
                    //通道被选中
                    if (listener != null) {
                        listener.onNVRChannelSelected(item, item.isSelected());
                    }
                }
            });
        } else {
            helper.itemView.setEnabled(false);
            iv_checked.setEnabled(false);
        }


    }


    public interface NvrDeviceChannelSelectedListener {
        void onNVRChannelSelected(ChannelResponseBean.ChannelsBean bean, boolean checked);
    }

    private NvrDeviceChannelSelectedListener listener;

    public void setNvrDeviceChannelSelectedListener(NvrDeviceChannelSelectedListener listener) {
        this.listener = listener;
    }

}
