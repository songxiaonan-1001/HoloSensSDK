package com.huawei.holosens.utils;

import android.text.TextUtils;

import com.huawei.holobasic.consts.MySharedPreferenceKey;
import com.huawei.holobasic.utils.MySharedPreference;

/**
 * @ProjectName: HoloSensEnterprise
 * @Package: com.huawei.holosens.utils
 * @ClassName: EnterpriseUtil
 * @Description: java类作用描述
 * @CreateDate: 2020-04-27 16:59
 * @Version: 1.0
 */
public class EnterpriseUtil {

    /**
     * 检查是否选择了企业
     * @return
     */
    public static boolean checkCurrentEnterprise(){
        if(TextUtils.isEmpty(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.CURRENT_ENTERPRISE))){
            return false;
        }
        return true;
    }
}
