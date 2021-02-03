package com.huawei.net.retrofit.impl;

public interface ResponseListener {
    void onSuccess(String result);
    void onFailed(Throwable throwable);
}
