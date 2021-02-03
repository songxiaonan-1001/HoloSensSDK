package com.huawei.holobase.bean;

import java.util.List;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: ViewChannelBean
 * @Description: java类作用描述
 * @CreateDate: 2020-01-15 22:16
 * @Version: 1.0
 */
public class ViewChannelBean {

    public ViewChannelBean(String device_id, String channel_id, String channel_name, int channel_state, String device_type, int position) {
        this.device_id = device_id;
        this.channel_id = channel_id;
        this.channel_name = channel_name;
        this.channel_state = channel_state;
        this.device_type = device_type;
        this.position = position;
    }

    private String device_id;
    private String channel_id;
    private String channel_name;
    private int channel_state;
    private String device_type;
    private int position;
    private List<String> channel_ability;
    private int own_type;

    public boolean isGB28181Device() {
        return connect_type == 2;
    }

    /**
     * 1：好望 2：GB28181
     */
    private int connect_type;

    public int getConnect_type() {
        return connect_type;
    }

    public void setConnect_type(int connect_type) {
        this.connect_type = connect_type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public int getChannel_state() {
        return channel_state;
    }

    public void setChannel_state(int channel_state) {
        this.channel_state = channel_state;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<String> getChannel_ability() {
        return channel_ability;
    }

    public void setChannel_ability(List<String> channel_ability) {
        this.channel_ability = channel_ability;
    }

    public int getOwn_type() {
        return own_type;
    }

    public void setOwn_type(int own_type) {
        this.own_type = own_type;
    }
}
