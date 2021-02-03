package com.huawei.holosens.live.playback.bean.sdk.hw;

import java.util.List;

/**
 * @ProjectName: HoloSensEnterprise
 * @Package: com.huawei.holosens.live.playback.bean.sdk.hw
 * @ClassName: HwRecordDateResult
 * @Description: java类作用描述
 * @CreateDate: 2020-08-19 10:18
 * @Version: 1.0
 */
public class HwRecordDateResult {
    private String method;
    private RecordDateBean result;
    private Error error;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public RecordDateBean getResult() {
        return result;
    }

    public void setResult(RecordDateBean result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class RecordDateBean{
        private List<String> dates;

        public List<String> getDates() {
            return dates;
        }

        public void setDates(List<String> dates) {
            this.dates = dates;
        }
    }
}
