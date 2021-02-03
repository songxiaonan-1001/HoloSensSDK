package com.huawei.holosens.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: HoloSensEnterprise
 * @Package: com.huawei.holosens.bean
 * @ClassName: LiveUrlBean
 * @Description: java类作用描述
 * @CreateDate: 2020-08-18 10:16
 * @Version: 1.0
 */
public class LiveUrlBean implements Serializable {
    private int failNum;
    private List<LiveUrlChannel>  channels;

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }

    public List<LiveUrlChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<LiveUrlChannel> channels) {
        this.channels = channels;
    }

    public class LiveUrlChannel{
        private String device_id;
        private String channel_id;
        private String live_url;
        private Error result;

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

        public String getLive_url() {
            return live_url;
        }

        public void setLive_url(String live_url) {
            this.live_url = live_url;
        }

        public Error getResult() {
            return result;
        }

        public void setResult(Error result) {
            this.result = result;
        }

        public class Error{
            private String code;
            private String msg;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }
        }
    }
}
