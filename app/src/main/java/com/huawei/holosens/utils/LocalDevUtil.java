package com.huawei.holosens.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.huawei.holobasic.consts.MySharedPreferenceKey;
import com.huawei.holobasic.utils.MySharedPreference;
import com.huawei.holosens.bean.LocalDev;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.utils
 * @ClassName: LocalDevUtil
 * @Description: java类作用描述
 * @CreateDate: 2020-01-14 15:41
 * @Version: 1.0
 */
public class LocalDevUtil {

    public static List<LocalDev> getList(){
        String str = MySharedPreference.getString(MySharedPreferenceKey.LocalDevKey.DEV_LIST+MySharedPreference.getString(MySharedPreferenceKey.LoginKey.USER_NAME));
        if(TextUtils.isEmpty(str)){
            return null;
        }
        return JSON.parseArray(str, LocalDev.class);
    }

    /**
     * 	检查通道是否被收藏
     * @return
     */
    public static boolean checkDevStatus(String devid, int channelid){
        String str = MySharedPreference.getString(MySharedPreferenceKey.LocalDevKey.DEV_LIST+MySharedPreference.getString(MySharedPreferenceKey.LoginKey.USER_NAME));
        if(TextUtils.isEmpty(str)){
            return false;
        }
        List<LocalDev> list =  JSON.parseArray(str, LocalDev.class);
        for(LocalDev d : list){
            if(d.getDeviceId().equals(devid) && d.getChannelId()==channelid) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加设备
     * @param dev
     */
    public static void addLocalDev(LocalDev dev){
        String str = MySharedPreference.getString(MySharedPreferenceKey.LocalDevKey.DEV_LIST+MySharedPreference.getString(MySharedPreferenceKey.LoginKey.USER_NAME));
        List<LocalDev> list;
        if(str.equals("")){
            list = new ArrayList<>();
        }else{
            list = JSON.parseArray(str, LocalDev.class);
            for(LocalDev d : list){
                if(d.getDeviceId().equals(dev.getDeviceId()) && d.getChannelId() == dev.getChannelId()){
                    list.remove(d);
                }
            }
        }

        dev.setDate(System.currentTimeMillis());
        list.add(dev);
        MySharedPreference.putString(MySharedPreferenceKey.LocalDevKey.DEV_LIST+MySharedPreference.getString(MySharedPreferenceKey.LoginKey.USER_NAME), JSON.toJSONString(list));
    }


    /**
     * 删除设备
     * @param devid
     * @param channelid
     */
    public static void deleteDev(String devid, int channelid){
        String str = MySharedPreference.getString(MySharedPreferenceKey.LocalDevKey.DEV_LIST+MySharedPreference.getString(MySharedPreferenceKey.LoginKey.USER_NAME));
        if(TextUtils.isEmpty(str)){
            return;
        }
        List<LocalDev> list = JSON.parseArray(str, LocalDev.class);
        int delIndex = -1;
        for(int i=0; i<list.size();i++){
            LocalDev d = list.get(i);
            if(d.getDeviceId().equals(devid) && d.getChannelId()==channelid){
                delIndex = i;
            }
        }
        if(delIndex >= 0)
            list.remove(delIndex);
        MySharedPreference.putString(MySharedPreferenceKey.LocalDevKey.DEV_LIST+MySharedPreference.getString(MySharedPreferenceKey.LoginKey.USER_NAME), JSON.toJSONString(list));
    }
}
