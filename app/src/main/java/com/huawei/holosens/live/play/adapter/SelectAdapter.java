package com.huawei.holosens.live.play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huawei.holosens.R;


/**
 * 码流选择Adapter
 *
 */
public class SelectAdapter extends BaseAdapter {

    private String[] mTxtArray;
    private Context mContext;
    private LayoutInflater inflater;
    private int selectedIndex;// 当前状态

    public SelectAdapter(Context con) {
        mContext = con;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * @param strArray   字符串数组
     */
    public void setData(String[] strArray) {
        mTxtArray = strArray;
    }

    @Override
    public int getCount() {
        return mTxtArray.length;
    }

    @Override
    public Object getItem(int positon) {
        return mTxtArray[positon];
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
            convertView = inflater.inflate(R.layout.list_item_select, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt = convertView.findViewById(R.id.tv_txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt.setText(mTxtArray[position]);
        viewHolder.txt.setSelected(position == selectedIndex);
        return convertView;
    }

    class ViewHolder {
        TextView txt;
    }
}
