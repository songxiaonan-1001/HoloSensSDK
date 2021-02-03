package com.huawei.holobasic.consts;

public class MySharedPreferenceKey {

    public interface GuideKey {
        String GUIDE_PAGE = "guide_page";

        String GUIDE_PLAY = "guide_play";

        String GUIDE_PLAYBACK = "guide_playback";

        String GUIDE_DEVICE = "guide_device";
    }

    /**
     * 推送消息设置
     */
    public interface PushKey {
        String PUSH_TOKEN = "push_token";
        String PUSH_SOUND_SWITCH = "push_sound_switch";
        String PUSH_SHOCK_SWITCH = "push_shock_switch";
        String PUSH_SHOCK_LAST_TIME = "push_shock_last_time";
        String PUSH_NOIFICATION_SWITCH="push_notifycation_switch"; //首页打开通知开关
    }


    /**
     * 登录模块相关
     */
    public static class LoginKey {
        public static final String SERVER_ADDR_SETTING = "SERVER_ADDR_SETTING";

        public static final String USERLIST = "userlist";
        public static final String LATEST_USER = "latest_user";

        public static final String USER_NAME = "user_name";
        public static final String PASSWORD = "password";

        public static final String TOKEN = "token";
        public static final String TOKEN_TIME = "token_time"; //获取token的时间点
        public static final String TOKEN_EXPIRES_TIME = "token_expire_time"; //token有效期
        public static final String USER_ROLE = "user_role";     //0=普通用户 ; 1=管理员用户
        public static final String USER_TYPE = "user_type";     //0=个人用户 ; 1=企业用户
        public static final String TIKEN = "tiken";
        public static final String TIKEN_EXPIRES_TIME = "tiken_expire_time"; //tiken
        public static final String PUSH_URL = "push_url";
        public static final String REMEMBER_PWD = "remember_password";
        public static final String AUTO_LOGIN = "auto_login";
        public static final String LOGOUT = "logout";

        public static final String ENTERPRISE = "login_enterprirse";
        public static final String CURRENT_ENTERPRISE = "current_enterprirse";
        public static final String ENTERPRISE_NAME = "enterprise_name";
        public static final String ENTERPRISE_ROLE_MANAGER = "enterprise_role_manager";
    }

    public static final String ENTERPRISE_INFO = "enterprise_info";

    public static final String PTZ_CONTROL_TOKEN = "ptz_control_token";

    public static class Account{
        public static final String AccountInfo = "account_info";
    }

    public interface PlayKey {
        //实况页面上一次播放记录
        String PLAY_LIST = "play_list_";
        //实况页面上一次播放记录分屏数
        String PLAY_SPAN_COUNT = "play_span_count_";
        //实况页面上一次播放记录选中窗口
        String PLAY_SELECT_NO = "play_select_no_";

        //实况页面上一次连接时间
        String PLAY_LAST_CONNECT_TIME = "play_last_connect_time";

        String PLAY_LAST_DIS_TIME = "play_last_dis_time";

        //非wifi网络观看视频提示
        String PLAY_WITHOUT_WIFI = "play_without_wifi";

        //回放页面窗口下标
        String PLAYBACK_INDEX = "playback_index";
    }

    /**
     * 收藏通道
     */
    public static final String FAVOR_LIST = "FAVOR_LIST";
    /***
     * 通道相关
     */
    public static class LocalDevKey {
        public static final String DEV_LIST = "DEV_LIST";
    }

    /***
     * 通道相关
     */
    public static class LocalViewKey {
        public static final String LocalView = "LOCALVIEW_LIST";
    }

    public static class GROUP{
        public static final String GROUP_STATUS = "GROUP_STATUS";
        public static final String GROUP_USER_ROLE = "group_user_role";
    }

    //第一次显示权限dialog
    public static final String FIRST_SHOW_PERMISSION_DIALOG = "first_show_permission_dialog";

    //    指纹开关
    public static final String FINGER_ON = "FINGER_ON";

    //第一次打开指纹
    public static final String FINGER_FIRST = "FINGER_FIRST";

    //指纹状态
    public static final String FINGER_STATUS = "FINGER_STATUS";

    //安全信息，日志收集上传
    public static final String SAFE_SETTING = "SAFE_SETTING";

    //提示弹窗不再提示
    public static final String NOT_REMIND = "NOT_REMIND";
}
