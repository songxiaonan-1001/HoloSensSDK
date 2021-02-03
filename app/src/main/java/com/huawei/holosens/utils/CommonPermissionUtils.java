package com.huawei.holosens.utils;

import android.Manifest;
import android.content.Context;

import com.huawei.holosens.R;


/**
 * @ProjectName: FanKong
 * @Package: com.jovision.fankong
 * @ClassName: CommonActivity
 * @Description: 基础类，复制使用
 * @CreateDate: 2019/9/23 10:20 AM
 * @Version: 1.0
 */
public class CommonPermissionUtils {

    private CommonPermissionUtils() {
    }

    /**
     * 根据权限标识获取权限名称
     *
     * @param context
     * @param permission
     * @return
     */
    public String getNameByPermissionTag(Context context, String permission) {
        String name = "";
        if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {// 存储
            name = context.getResources().getString(R.string.permission_storage);
        } else if (permission.equals(Manifest.permission.READ_PHONE_STATE) ) {// 获取手机信息
            name = context.getResources().getString(R.string.permission_contacts);
        } else if (permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {// 定位
            name = context.getResources().getString(R.string.permission_location);
        } else if (permission.equals(Manifest.permission.RECORD_AUDIO)) {// 录音
            name = context.getResources().getString(R.string.permission_record_audio);
        } else if (permission.equals(Manifest.permission.CAMERA)) {// 相机
            name = context.getResources().getString(R.string.permission_camera);
        }else if (permission.equals(Manifest.permission.CAMERA)) {// 相机
            name = context.getResources().getString(R.string.permission_camera);
        }else if (permission.equals(Manifest.permission.READ_CONTACTS)) {// 通讯录
            name = context.getResources().getString(R.string.permission_conn_list);
        }

        return name;
    }

    /**
     * 解析权限数组获取权限名称
     * @param context
     * @param permissions
     * @return
     */
    public String getNameByPermissionArray(Context context, String[] permissions) {
        StringBuffer permissionNames = new StringBuffer();
        int count = permissions.length;
        for (int i = 0; i < count; i++) {
            if (i > 0 && i!=(count -1)) {
                permissionNames.append("、");
            }
            permissionNames.append(getNameByPermissionTag(context, permissions[i]));
        }

        return permissionNames.toString();
    }


    public static CommonPermissionUtils getInstance() {
        return SingletonLoader.INSTANCE;
    }

    private static class SingletonLoader {
        private static final CommonPermissionUtils INSTANCE = new CommonPermissionUtils();
    }
}
