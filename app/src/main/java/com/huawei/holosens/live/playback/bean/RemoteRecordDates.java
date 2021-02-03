package com.huawei.holosens.live.playback.bean;

import java.util.List;

/**
 * 创建时间：2020-07-15
 * 创建人：dongdz
 * 功能描述：本地存在录像日期列表对象
 */
public class RemoteRecordDates {

    private List<RemoteRecordDate> date_list;

    public List<RemoteRecordDate> getDate_list() {
        return date_list;
    }

    public void setDate_list(List<RemoteRecordDate> date_list) {
        this.date_list = date_list;
    }
}
