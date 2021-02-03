package com.huawei.holosens.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.holobasic.consts.MySharedPreferenceKey;
import com.huawei.holobasic.utils.MySharedPreference;
import com.huawei.holosens.bean.User;

import java.util.List;

class UserUtil {

    private UserUtil() {
    }

    public static User getLatestUser() {
        String str = MySharedPreference.getString(MySharedPreferenceKey.LoginKey.LATEST_USER);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return JSON.parseObject(str, User.class);
    }

    public static void deleteLastUser(){
        MySharedPreference.remove(MySharedPreferenceKey.LoginKey.LATEST_USER);
        MySharedPreference.putString(MySharedPreferenceKey.LoginKey.LATEST_USER, "");
    }

    public static void setLatestUser(User user) {
        if (user == null) {
            MySharedPreference.remove(MySharedPreferenceKey.LoginKey.LATEST_USER);
        } else {
            String str = JSON.toJSONString(user);
            MySharedPreference.putString(MySharedPreferenceKey.LoginKey.LATEST_USER, str);
        }
    }

    private static List<User> getUserList() {
        String str = MySharedPreference.getString(MySharedPreferenceKey.LoginKey.USERLIST);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return JSON.parseArray(str, User.class);
    }

    private static void addUser(User user) {
        String str = MySharedPreference.getString(MySharedPreferenceKey.LoginKey.USERLIST);
        JSONArray ja;
        if (!TextUtils.isEmpty(str)) {
            ja = JSON.parseArray(str);
        } else {
            ja = new JSONArray();
        }
        JSONObject jo = (JSONObject) JSON.toJSON(user);
        if (!ja.contains(jo)) {
            ja.add(jo);
            MySharedPreference.putString(MySharedPreferenceKey.LoginKey.USERLIST, JSONArray.toJSONString(ja));
        }
    }

    public static void addNewUser(User user) {
        List<User> users = getUserList();
        if (users != null) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUsername().equals(user.getUsername())) {
                    deleteUser(users.get(i));
                }
            }
        }
        addUser(user);
    }


    private static void deleteUser(User user) {
        String str = MySharedPreference.getString(MySharedPreferenceKey.LoginKey.USERLIST);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        JSONArray ja = JSON.parseArray(str);
        JSONObject jo = (JSONObject) JSON.toJSON(user);
        ja.remove(jo);
        MySharedPreference.putString(MySharedPreferenceKey.LoginKey.USERLIST, JSONArray.toJSONString(ja));
    }
}
