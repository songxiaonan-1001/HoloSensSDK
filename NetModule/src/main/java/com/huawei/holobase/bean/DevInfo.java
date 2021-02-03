package com.huawei.holobase.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: DevInfo
 * @Description: java类作用描述
 * @CreateDate: 2020-01-10 09:33
 * @Version: 1.0
 */
public class DevInfo implements Serializable {

    private String device_id;
    private String device_name;
    private String device_type;
    private String firmware;
    private String mac;
    private int device_update;      //是否存在固件新版本（1是0否）
    private List<String> ability;
    private String model;
    private String manufacture;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getDevice_update() {
        return device_update;
    }

    public void setDevice_update(int device_update) {
        this.device_update = device_update;
    }

    public List<String> getAbility() {
        return ability;
    }

    public void setAbility(List<String> ability) {
        this.ability = ability;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }
}
