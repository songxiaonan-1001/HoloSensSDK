package com.huawei.holosens.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.bean
 * @ClassName: FileBean
 * @Description: java类作用描述
 * @CreateDate: 2020-03-31 19:32
 * @Version: 1.0
 */
public class FileBean {

    public FileBean(String time, String name) {
        this.time = time;
        this.name = name;
    }

    private String time;
    private String name;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
