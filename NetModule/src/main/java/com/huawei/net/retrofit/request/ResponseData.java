package com.huawei.net.retrofit.request;


public class ResponseData<T> {
    /**
     * {
     *     "code":1000,
     *     "msg":"OK",
     *     "data":{
     *         "token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX3JvbGUiOjEsInN1YiI6IjE5NTQyNzg2MjAyMDIwMDEwNjE3MDQyNSIsInVzZXJfaWQiOiIxOTU0Mjc4NjIwMjAyMDAxMDYxNzA0MjUiLCJpYXQiOjE1NzgzNjYyMzN9.p-PoRZLjTmGQMMpTrL3D0JBsEx6b01QX8s_u3I78ufY",
     *         "tiken":"f4678eb6ac366656062b47d912ca5b344bd9ad83ece064e32f2f2b189506c429"
     *     }
     * }
     */
    private int code;
    private String msg;
    private T data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
