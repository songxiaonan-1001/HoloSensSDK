package com.huawei.holosens.bean;

/**
 * @ProjectName: HoloSensEnterprise
 * @Package: com.huawei.holosens.bean
 * @ClassName: JvmpTimeStamp
 * @Description: java类作用描述  jvmp时间戳
 * @CreateDate: 2020-07-30 14:53
 * @Version: 1.0
 */
public class SDKTimeStamp {
    private String method;
    private TimePos result;
    private Error error;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public TimePos getResult() {
        return result;
    }

    public void setResult(TimePos result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class TimePos{
        private String time_pos;

        public String getTime_pos() {
            return time_pos;
        }

        public void setTime_pos(String time_pos) {
            this.time_pos = time_pos;
        }
    }

    public class Error{
        private int errorcode;

        public int getErrorcode() {
            return errorcode;
        }

        public void setErrorcode(int errorcode) {
            this.errorcode = errorcode;
        }
    }
}
