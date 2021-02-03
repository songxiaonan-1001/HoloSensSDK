package com.huawei.holosens.utils.banner;

import com.huawei.holosens.R;

import java.util.ArrayList;
import java.util.List;

public class DataBean {
    public Integer imageRes;
    public String imageUrl;
    public String title;
    public int viewType;

    public DataBean(Integer imageRes, String title, int viewType) {
        this.imageRes = imageRes;
        this.title = title;
        this.viewType = viewType;
    }

    public DataBean(String imageUrl, String title, int viewType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
    }

    public static List<DataBean> getTestData() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean(R.mipmap.img_banner_bg, "相信自己,你努力的样子真的很美", 1));
        list.add(new DataBean(R.mipmap.img_banner_bg, "相信自己,你努力的样子真的很美", 2));
        return list;
    }

}
