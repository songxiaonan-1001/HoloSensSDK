package com.huawei.holobase.bean;

import java.io.Serializable;

/**
 * @class name ShareSendBean
 * @date 创建时间：2020-03-24    10:35
 * @description: 检查是否可以分享
 */
public class CheckShareEnableBean implements Serializable {

    private int is_can; //分享是否可用 0-不可用 1-可用

    public int getIs_can() {
        return is_can;
    }

    public void setIs_can(int is_can) {
        this.is_can = is_can;
    }
}
