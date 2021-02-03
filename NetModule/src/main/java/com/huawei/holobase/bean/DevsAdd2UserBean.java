package com.huawei.holobase.bean;

import java.io.Serializable;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: DevsAdd2UserBean
 * @Description: java类作用描述
 * @CreateDate: 2020-01-10 16:59
 * @Version: 1.0
 */
public class DevsAdd2UserBean implements Serializable {

    public DevsAdd2UserBean(String device_id, String device_type, String device_name) {
        this.device_id = device_id;
        this.device_type = device_type;
        this.device_name = device_name;
    }

    private String device_id;
    private String device_type;
    private String device_name;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }
}
