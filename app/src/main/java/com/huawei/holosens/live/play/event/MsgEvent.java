package com.huawei.holosens.live.play.event;

import org.json.JSONObject;

/**
 * 消息事件
 */

public class MsgEvent {
    // 改变窗户上的玻璃数量(双击)
    public static final int MSG_EVENT_CHANGE_WINDOW_DOUBLECLICK = 0;
    // 改变玻璃的选择(单击)
    public static final int MSG_EVENT_GLASS_SELECT = 1;
    // 滑动切换玻璃
    public static final int MSG_EVENT_GLASS_CHANGE = 2;
    // 功能区和播放区的交互
    public static final int MSG_EVENT_INTERACTION = 3;
    // 显示/隐藏横屏时的顶部和底部工具栏
    public static final int MSG_EVENT_TOP_BOTTOM_NAV = 4;
    // 退出播放
    public static final int MSG_EVENT_EXIT_PLAY = 5;
    // 实况页面跳转到回放页面之后异常断开
    public static final int MSG_EVENT_ABNORMAL_DISCONNECT = 6;
    // 隐藏MainActivity底部tab
    public static final int MSG_EVENT_HIDDEN_TAB = 10;
    // 显示MainActivity底部tab
    public static final int MSG_EVENT_SHOW_TAB = 11;
    // 更新消息未读数量
    public static final int MSG_EVENT_UPDATE_UNREAD_COUNT = 12;
    //更新沉浸式状态栏 白字
    public static final int MSG_EVENT_STATUS_BAR = 13;
    //更新沉浸式状态栏 黑字
    public static final int MSG_EVENT_STATUS_LIGHT_BAR = 14;
    //白底黑字状态栏
    public static final int MSG_EVENT_WHITE_STATUS_BAR = 15;
    //分享成功
    public static final int MSG_EVENT_SHARE_SUCCESS = 16;
    //选中玻璃
    public static final int MSG_EVENT_GLASS_CHECK = 17;
    //切换企业
    public static final int MSG_EVENT_CHANGE_ENTERPRISE = 18;




    // 消息标志
    private int msgTag;
    // 消息内容
    private Object msgContent;
    // 附加内容
    private String attachment;
    // 简单的Json对象
    private JSONObject mJSONObject;


    public int getMsgTag() {
        return msgTag;
    }

    public void setMsgTag(int msgTag) {
        this.msgTag = msgTag;
    }

    public Object getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(Object msgContent) {
        this.msgContent = msgContent;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public JSONObject getJSONObject() {
        return mJSONObject;
    }

    public void setJSONObject(JSONObject JSONObject) {
        mJSONObject = JSONObject;
    }
}
