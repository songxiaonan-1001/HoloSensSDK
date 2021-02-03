package com.huawei.holosens.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

public class BaseSingleItemAdapter extends BaseQuickAdapter<BaseBean, BaseViewHolder> {

    private  int layoutResId;


    public BaseSingleItemAdapter(int layoutResId, @Nullable List<BaseBean> data) {
        super(layoutResId, data);
        this.layoutResId = layoutResId;
    }

    public BaseSingleItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseBean item) {


    }
}
