package com.huawei.net.retrofit.request;

/**
 * 返回码
 */
public class ResponseCode {

    /**
     * 服务器请求，操作成功。
     */
    public static final int SUCCESS = 1000;

    public static final int REQUEST_ERROR = 4000;

    public static final int REQUEST_ERROR_OVERSION = 2000;

    /**
     * 服务器请求，token失效。
     */
    public static final int INVALID_TOKEN = 21016;

    public static final int INVALID_TIKEN = 21031;

    /**
     * 账号被踢退
     */
    public static final int ACCOUNT_QUIT = 21032;

    /**
     * 账号锁定,需验证码登录。
     */
    public static final int ACCOUNT_LOCKED = 21014;
    /**
     * 账号锁定5分钟
     */
    public static final int ACCOUNT_LOCKED_5m = 21059;

    /**
     * 服务内部发生错误
     */
    public static final int SERVER_ERROR = 500;


}
