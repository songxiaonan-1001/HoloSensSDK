package com.huawei.holosens.utils;


import java.util.HashMap;

public class HeaderUtil {
    public static HashMap<String, Object> createHeader(String token) {
        HashMap<String, Object> header = new HashMap<>();
        header.put("Authorization", "Bearer " + token);
        return header;
    }
}
