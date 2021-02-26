package com.huawei.holosens.adapter;

import java.io.Serializable;
import java.util.List;

/**
 * 通道响应结果实体类
 *
 * @author CSV
 */
public class ChannelResponseBean extends BaseBean {


    /**
     * total : 2
     * channels : [{"device_id":"DHFDSAFADSF432","channel_id":"0","channel_name":"龙岗通道1","channel_state":"ONLINE","create_time":"2020-06-26 20:42:16","update_time":"2020-06-26 20:42:16","model":"L","access_protocol":"HOLO","channel_system_state":"NORMAL","channel_resource_state":["MANAGEMENT_GRANT","EVENT_RECORD_CLOSED","FULL_RECORD_CLOSED"]},{"device_id":"DHFDSAFADSF432","channel_id":"0","channel_name":"龙岗通道2","channel_state":"ONLINE","create_time":"2020-06-26 20:42:16","update_time":"2020-06-26 20:42:16","model":"L","access_protocol":"HOLO","channel_system_state":"NORMAL","channel_resource_state":["MANAGEMENT_GRANT","EVENT_RECORD_CLOSED","FULL_RECORD_CLOSED"]}]
     */

    private String total;
    private List<ChannelsBean> channels;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ChannelsBean> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelsBean> channels) {
        this.channels = channels;
    }

    public static class ChannelsBean implements Serializable {
        /**
         * device_id : DHFDSAFADSF432
         * channel_id : 0
         * channel_name : 龙岗通道1
         * channel_state : ONLINE
         * create_time : 2020-06-26 20:42:16
         * update_time : 2020-06-26 20:42:16
         * model : L
         * access_protocol : HOLO
         * channel_system_state : NORMAL
         * channel_resource_state : ["MANAGEMENT_GRANT","EVENT_RECORD_CLOSED","FULL_RECORD_CLOSED"]
         */

        private String device_id;
        private String channel_id;
        private String channel_name;
        private String channel_state;
        private String create_time;
        private String update_time;
        private String model;
        private String access_protocol;
        private String channel_system_state;
        private List<String> channel_resource_state;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
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

        public String getChannel_state() {
            return channel_state;
        }

        public void setChannel_state(String channel_state) {
            this.channel_state = channel_state;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getAccess_protocol() {
            return access_protocol;
        }

        public void setAccess_protocol(String access_protocol) {
            this.access_protocol = access_protocol;
        }

        public String getChannel_system_state() {
            return channel_system_state;
        }

        public void setChannel_system_state(String channel_system_state) {
            this.channel_system_state = channel_system_state;
        }

        public List<String> getChannel_resource_state() {
            return channel_resource_state;
        }

        public void setChannel_resource_state(List<String> channel_resource_state) {
            this.channel_resource_state = channel_resource_state;
        }

        /**
         * 设备是否已经添加到播放界面
         * 如果添加，则不可移动
         */
        private boolean enable = true;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }
}
