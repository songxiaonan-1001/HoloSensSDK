package com.huawei.holobase.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: EventMsg
 * @Description: java类作用描述
 * @CreateDate: 2020-01-31 15:57
 * @Version: 1.0
 */
public class EventMsg {

    // 账号提退
    public static final int MSG_EVENT_QUIT = 1000;


    /**
     * 用户添加设备成功刷新设备数量
     */
    public static final int MSG_EVENT_REFERENCEDEVICE = 800000001;

    // 消息标志
    private int msgTag;

    public int getMsgTag() {
        return msgTag;
    }

    public void setMsgTag(int msgTag) {
        this.msgTag = msgTag;
    }
}
