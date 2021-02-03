package com.huawei.holosens.live.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huawei.holosens.R;

/**
 * 多分屏选择Adapter
 */
public class MultiScreenAdapter extends BaseAdapter {

    private int[] mIconResIdArray;
    private Context mContext;
    private LayoutInflater inflater;
    private int selectedIndex;// 当前状态

    public MultiScreenAdapter(Context con) {
        mContext = con;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * @param imgIdArray 图标数组
     */
    public void setData(int[] imgIdArray) {
        mIconResIdArray = imgIdArray;
    }

    @Override
    public int getCount() {
        return mIconResIdArray.length;
    }

    @Override
    public Object getItem(int positon) {
        return mIconResIdArray[positon];
    }

    @Override
    public long getItemId(int positon) {
        return positon;
    }

    public void setSelected(int position) {
        selectedIndex = position;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_multi_screen, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = convertView.findViewById(R.id.iv_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.icon.setImageResource(mIconResIdArray[position]);
        viewHolder.icon.setSelected(selectedIndex == position);

        return convertView;
    }

    class ViewHolder {
        ImageView icon;
    }

}
