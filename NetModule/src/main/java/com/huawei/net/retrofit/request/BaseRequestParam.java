package com.huawei.net.retrofit.request;

import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * 基础公共参数
 */
public class BaseRequestParam {
    private final LinkedHashMap<String, Object> baseParam;
    private final HashMap<String, Object> baseHeaderParam;

    public BaseRequestParam() {
        baseParam = new LinkedHashMap<>();
        baseHeaderParam = new HashMap<>();

    }


    public BaseRequestParam putAll(HashMap<String, Object> paramsMap) {

        baseParam.putAll(paramsMap);

        return this;
    }

    public BaseRequestParam put(String key, Object value) {
        baseParam.put(key, value);
        return this;
    }


    public HashMap<String, Object> build() {
        return baseParam;
    }


    /**
     * header
     * @param paramsMap http报文头
     * @return BaseRequestParam
     */
    public BaseRequestParam putAllHeader(HashMap<String, Object> paramsMap) {

        baseHeaderParam.putAll(paramsMap);

        return this;
    }

    public BaseRequestParam putHeader(String key, Object value) {
        baseHeaderParam.put(key, value);
        return this;
    }


    public HashMap<String, Object> buildHeader() {
        return baseHeaderParam;
    }

}
