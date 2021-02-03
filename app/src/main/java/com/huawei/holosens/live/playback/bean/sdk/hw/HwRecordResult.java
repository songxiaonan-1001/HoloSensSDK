package com.huawei.holosens.live.playback.bean.sdk.hw;

import java.util.List;

/**
 * @ProjectName: HoloSensEnterprise
 * @Package: com.huawei.holosens.live.playback.bean.sdk.hw
 * @ClassName: HwRecordResult
 * @Description: java类作用描述 通过P2P返回的所有录像json
 * @CreateDate: 2020-08-11 14:06
 * @Version: 1.0
 */
public class HwRecordResult {
    private String method;
    private Result result;
    private Error error;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }


    /**
     * 内容
     */
    public class Result{
        private List<HwRecordBean> records;

        public List<HwRecordBean> getRecords() {
            return records;
        }

        public void setRecords(List<HwRecordBean> records) {
            this.records = records;
        }
    }

}
