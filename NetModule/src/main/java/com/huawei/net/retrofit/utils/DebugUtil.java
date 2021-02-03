package com.huawei.net.retrofit.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.utils
 * @ClassName: DebugUtil
 * @Description: java类作用描述
 * @CreateDate: 2020-04-09 15:02
 * @Version: 1.0
 */
public class DebugUtil {

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
