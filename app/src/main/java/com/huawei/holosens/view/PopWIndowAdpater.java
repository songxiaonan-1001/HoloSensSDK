package com.huawei.holosens.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.holosens.R;


class PopWIndowAdpater extends BaseAdapter {

    private final int[] resIds;
    private final String[] titles;
    private final Context context;

    public PopWIndowAdpater(Context context, int[] resIds, String[] titles ) {
        this.resIds = resIds;
        this.titles = titles;
        this.context = context;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.popup_window_list, null);
        ImageView img = convertView.findViewById(R.id.popwindow_img);
        TextView tv = convertView.findViewById(R.id.popwindow_text);
        if(resIds!=null)
            img.setImageResource(resIds[position]);
        else
            img.setVisibility(View.GONE);
        tv.setText(titles[position]);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mListener != null){
//                    mListener.onClick(position);
//                }
//            }
//        });

        return convertView;
    }
}
