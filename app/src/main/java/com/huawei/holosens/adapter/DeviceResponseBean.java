package com.huawei.holosens.adapter;

import java.io.Serializable;
import java.util.List;

/**
 * 设备响应结果实体类
 *
 * @author CSV
 */
public class DeviceResponseBean extends BaseBean implements Serializable {


    /**
     * total : 48
     * devices : [{"device_id":"2198061248WLL3000028","device_name":"展厅NVR800","device_state":"OFFLINE","device_type":"NVR","access_protocol":"HOLO","manufacture":"HUAWEI","device_ability":"","channel_total":"32","create_time":"2020-07-23 14:58:56","update_time":"2020-07-30 23:59:11","device_system_state":null},{"device_id":"T0001115","device_name":"ss","device_state":"OFFLINE","device_type":"IPC","access_protocol":"HOLO","manufacture":"JOVISION","device_ability":"ptz,talk","channel_total":"1","create_time":"2020-07-23 18:43:40","update_time":"2020-08-05 13:59:13","device_system_state":"NORMAL"},{"device_id":"09054910001320000003","device_name":"09054910001320000003","device_state":"ONLINE","device_type":"IPC","access_protocol":"GB28181","manufacture":"huawei","device_ability":"","channel_total":"1","create_time":"2020-07-24 18:25:12","update_time":"2020-07-30 17:12:40","device_system_state":"NORMAL"},{"device_id":"101590000006","device_name":"0001222","device_state":"ONLINE","device_type":"IPC-BOX","access_protocol":"HOLO","manufacture":"huawei","device_ability":"","channel_total":"1","create_time":"2020-07-26 15:12:53","update_time":"2020-07-28 14:44:46","device_system_state":"NORMAL"},{"device_id":"21024125349SL5000036","device_name":"热不热","device_state":"OFFLINE","device_type":"IPC-CONCH","access_protocol":"HOLO","manufacture":"huawei","device_ability":"","channel_total":"1","create_time":"2020-07-27 11:56:15","update_time":"2020-07-27 19:23:30","device_system_state":null},{"device_id":"34020000001320000008","device_name":"34020000001320000008","device_state":"ONLINE","device_type":"IPC","access_protocol":"GB28181","manufacture":"huawei","device_ability":"","channel_total":"1","create_time":"2020-07-27 13:37:17","update_time":"2020-07-30 17:09:31","device_system_state":null},{"device_id":"101590000020","device_name":"H265-有录像-G711U-有对讲","device_state":"OFFLINE","device_type":"IPC","access_protocol":"HOLO","manufacture":"huawei","device_ability":"talk","channel_total":"1","create_time":"2020-07-27 15:00:55","update_time":"2020-07-30 21:42:51","device_system_state":null},{"device_id":"09055270001320000000","device_name":"IP DOME","device_state":"ONLINE","device_type":"IPC","access_protocol":"GB28181","manufacture":"Hikvision","device_ability":"","channel_total":"1","create_time":"2020-07-28 15:10:27","update_time":"2020-07-28 21:38:00","device_system_state":null},{"device_id":"09055160001110000000","device_name":"","device_state":"OFFLINE","device_type":"DVR","access_protocol":"GB28181","manufacture":"HIKVISION","device_ability":"talk","channel_total":"4","create_time":"2020-07-28 16:34:26","update_time":"2020-07-31 00:20:26","device_system_state":null},{"device_id":"09052000001320000044","device_name":"09052000001320000044","device_state":"OFFLINE","device_type":"IPC","access_protocol":"GB28181","manufacture":"huawei","device_ability":"","channel_total":"1","create_time":"2020-07-28 23:42:35","update_time":"2020-07-29 11:45:50","device_system_state":"NORMAL"},{"device_id":"09055270001320000456","device_name":"IP DOME","device_state":"OFFLINE","device_type":"IPC","access_protocol":"GB28181","manufacture":"Hikvision","device_ability":"talk","channel_total":"1","create_time":"2020-07-28 23:53:54","update_time":"2020-07-31 02:34:42","device_system_state":"NORMAL"},{"device_id":"T0000113","device_name":null,"device_state":"UNREGISTERED","device_type":"IPC","access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-07-29 09:49:08","update_time":"2020-07-29 09:49:08","device_system_state":null},{"device_id":"34020000001320000222","device_name":null,"device_state":"UNREGISTERED","device_type":"IPC","access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-07-29 13:42:50","update_time":"2020-07-29 13:42:50","device_system_state":null},{"device_id":"T12345678","device_name":"JV IPC","device_state":"OFFLINE","device_type":"IPC","access_protocol":"HOLO","manufacture":"JOVISION","device_ability":"ptz,talk","channel_total":"1","create_time":"2020-07-29 21:23:51","update_time":"2020-08-05 13:59:10","device_system_state":null},{"device_id":"666sixSIX","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-07-30 09:48:41","update_time":"2020-08-03 20:11:49","device_system_state":null},{"device_id":"25classSuper","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-07-30 17:15:09","update_time":"2020-07-30 17:15:09","device_system_state":null},{"device_id":"pleaseStop111","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-07-30 17:15:09","update_time":"2020-07-30 17:15:09","device_system_state":null},{"device_id":"34020000001110000042","device_name":"Network Video Recorder","device_state":"OFFLINE","device_type":"DVR","access_protocol":"GB28181","manufacture":"HIKVISION","device_ability":"talk","channel_total":"4","create_time":"2020-07-30 21:49:44","update_time":"2020-07-30 22:50:10","device_system_state":"NORMAL"},{"device_id":"34020000001110000200","device_name":"Network Video Recorder","device_state":"OFFLINE","device_type":"DVR","access_protocol":"GB28181","manufacture":"HIKVISION","device_ability":"talk","channel_total":"4","create_time":"2020-07-31 17:44:20","update_time":"2020-08-01 10:09:39","device_system_state":"NORMAL"},{"device_id":"34020000001320000058","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":null,"create_time":"2020-07-31 18:58:03","update_time":"2020-07-31 18:58:03","device_system_state":"NORMAL"},{"device_id":"00000000001111111113","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-07-31 19:10:14","update_time":"2020-07-31 19:10:14","device_system_state":null},{"device_id":"00001111111115","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-07-31 19:36:09","update_time":"2020-07-31 19:36:09","device_system_state":null},{"device_id":"09052000001180000034","device_name":"","device_state":"OFFLINE","device_type":"NVR","access_protocol":"GB28181","manufacture":"HIKVISION","device_ability":"talk","channel_total":"4","create_time":"2020-07-31 19:46:10","update_time":"2020-08-01 01:21:01","device_system_state":null},{"device_id":"34020000001320000201","device_name":"34020000001320000201","device_state":"OFFLINE","device_type":"IPC","access_protocol":"GB28181","manufacture":"huawei","device_ability":"talk","channel_total":"1","create_time":"2020-07-31 22:01:16","update_time":"2020-08-01 14:20:58","device_system_state":"NORMAL"},{"device_id":"34020000001320000059","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":null,"create_time":"2020-07-31 22:44:30","update_time":"2020-07-31 22:44:30","device_system_state":"NORMAL"},{"device_id":"34020000001320000531","device_name":"admin","device_state":"OFFLINE","device_type":"IPC","access_protocol":"GB28181","manufacture":"huawei","device_ability":"talk","channel_total":"1","create_time":"2020-07-31 23:39:40","update_time":"2020-08-01 00:27:46","device_system_state":null},{"device_id":"09052000001320731259","device_name":null,"device_state":"UNREGISTERED","device_type":"IPC","access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-08-01 00:10:31","update_time":"2020-08-01 00:10:31","device_system_state":null},{"device_id":"21024124692503000011","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":null,"create_time":"2020-08-01 11:52:43","update_time":"2020-08-01 11:52:43","device_system_state":"NORMAL"},{"device_id":"34020000001110000001","device_name":null,"device_state":"OFFLINE","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":null,"create_time":"2020-08-01 17:59:11","update_time":"2020-08-01 17:59:11","device_system_state":"NORMAL"},{"device_id":"34020000001110000009","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":null,"create_time":"2020-08-03 11:11:09","update_time":"2020-08-03 11:11:09","device_system_state":"NORMAL"},{"device_id":"34020000001320000205","device_name":"34020000001320000205","device_state":"OFFLINE","device_type":"IPC","access_protocol":"GB28181","manufacture":"huawei","device_ability":"talk","channel_total":"1","create_time":"2020-08-03 11:12:06","update_time":"2020-08-03 20:33:19","device_system_state":"NORMAL"},{"device_id":"34020000001320000202","device_name":"Huawei34020000001320000202","device_state":"ONLINE","device_type":"IPC","access_protocol":"GB28181","manufacture":"huawei","device_ability":"","channel_total":"1","create_time":"2020-08-03 19:41:57","update_time":"2020-08-07 19:57:07","device_system_state":"NORMAL"},{"device_id":"00000000001111111111","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-08-03 20:02:26","update_time":"2020-08-03 20:02:26","device_system_state":null},{"device_id":"00000000001111111121","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-08-03 20:02:26","update_time":"2020-08-03 20:02:26","device_system_state":null},{"device_id":"34020000001320000060","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":null,"create_time":"2020-08-05 14:30:12","update_time":"2020-08-05 14:30:12","device_system_state":"NORMAL"},{"device_id":"34020000001110000002","device_name":null,"device_state":"OFFLINE","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":null,"create_time":"2020-08-05 14:35:30","update_time":"2020-08-05 14:35:30","device_system_state":"NORMAL"},{"device_id":"2102412467WLL5000240","device_name":"HoloSens SDC","device_state":"ONLINE","device_type":"IPC","access_protocol":"HOLO","manufacture":"huawei","device_ability":"","channel_total":"1","create_time":"2020-08-05 14:55:23","update_time":"2020-08-07 19:57:15","device_system_state":"NORMAL"},{"device_id":"2102412467WLL5000241","device_name":"ababababa","device_state":"ONLINE","device_type":"IPC","access_protocol":"HOLO","manufacture":"huawei","device_ability":"","channel_total":"1","create_time":"2020-08-05 16:30:26","update_time":"2020-08-07 09:57:34","device_system_state":"NORMAL"},{"device_id":"534534534553453","device_name":null,"device_state":"UNREGISTERED","device_type":"NVR","access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-08-05 17:18:48","update_time":"2020-08-05 17:18:48","device_system_state":null},{"device_id":"34020000001320000188","device_name":"IP CAMERA","device_state":"OFFLINE","device_type":"IPC","access_protocol":"GB28181","manufacture":"Hikvision","device_ability":"","channel_total":"1","create_time":"2020-08-06 14:20:29","update_time":"2020-08-06 17:45:24","device_system_state":"NORMAL"},{"device_id":"34020000001320000001","device_name":null,"device_state":"UNREGISTERED","device_type":null,"access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":null,"create_time":"2020-08-06 16:22:15","update_time":"2020-08-06 16:22:15","device_system_state":"NORMAL"},{"device_id":"2198061243WLL3000206","device_name":"NVR800","device_state":"ONLINE","device_type":"NVR","access_protocol":"HOLO","manufacture":"HUAWEI","device_ability":"intelligent,localstorage,ptz","channel_total":"8","create_time":"2020-08-06 17:33:20","update_time":"2020-08-07 19:57:27","device_system_state":"NORMAL"},{"device_id":"456453645645645645","device_name":null,"device_state":"UNREGISTERED","device_type":"NVR","access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-08-06 21:33:44","update_time":"2020-08-06 21:33:44","device_system_state":null},{"device_id":"4564564564564645645","device_name":null,"device_state":"UNREGISTERED","device_type":"IPC","access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-08-06 21:35:06","update_time":"2020-08-06 21:35:06","device_system_state":null},{"device_id":"12345678900987654321","device_name":null,"device_state":"UNREGISTERED","device_type":"NVR","access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-08-07 10:25:33","update_time":"2020-08-07 10:25:33","device_system_state":null},{"device_id":"34020000001110000010","device_name":null,"device_state":"UNREGISTERED","device_type":"IPC","access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-08-07 11:12:23","update_time":"2020-08-07 11:12:23","device_system_state":null},{"device_id":"12345678901234567896","device_name":null,"device_state":"UNREGISTERED","device_type":"IPC","access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-08-07 17:11:46","update_time":"2020-08-07 17:11:46","device_system_state":null},{"device_id":"12355678912345678944","device_name":null,"device_state":"UNREGISTERED","device_type":"IPC","access_protocol":"GB28181","manufacture":null,"device_ability":"","channel_total":"0","create_time":"2020-08-07 20:54:29","update_time":"2020-08-07 20:54:29","device_system_state":null}]
     */

    private int total;
    private List<DevicesBean> devices;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DevicesBean> getDevices() {
        return devices;
    }

    public void setDevices(List<DevicesBean> devices) {
        this.devices = devices;
    }

    public static class DevicesBean implements Serializable {
        /**
         * device_id : 2198061248WLL3000028
         * device_name : 展厅NVR800
         * device_state : OFFLINE
         * device_type : NVR
         * access_protocol : HOLO
         * manufacture : HUAWEI
         * device_ability :
         * channel_total : 32
         * create_time : 2020-07-23 14:58:56
         * update_time : 2020-07-30 23:59:11
         * device_system_state : null
         */

        private String device_id;
        private String device_name;
        private String device_state;
        private String device_type;
        private String access_protocol;
        private String manufacture;
        private String device_ability;
        private String channel_total;
        private String create_time;
        private String update_time;
        private String device_system_state;
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

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }

        public String getDevice_state() {
            return device_state;
        }

        public void setDevice_state(String device_state) {
            this.device_state = device_state;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public String getAccess_protocol() {
            return access_protocol;
        }

        public void setAccess_protocol(String access_protocol) {
            this.access_protocol = access_protocol;
        }

        public String getManufacture() {
            return manufacture;
        }

        public void setManufacture(String manufacture) {
            this.manufacture = manufacture;
        }

        public String getDevice_ability() {
            return device_ability;
        }

        public void setDevice_ability(String device_ability) {
            this.device_ability = device_ability;
        }

        public String getChannel_total() {
            return channel_total;
        }

        public void setChannel_total(String channel_total) {
            this.channel_total = channel_total;
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

        public Object getDevice_system_state() {
            return device_system_state;
        }

        public void setDevice_system_state(String device_system_state) {
            this.device_system_state = device_system_state;
        }
    }
}
