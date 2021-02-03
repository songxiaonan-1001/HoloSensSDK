package com.huawei.holosens.utils.finger;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.utils.finger
 * @ClassName: FingerUtil
 * @Description: java类作用描述
 * @CreateDate: 2020-03-26 15:28
 * @Version: 1.0
 */

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;

import androidx.annotation.RequiresApi;

/**
 * 指纹识别工具类
 */
public class FingerUtil {

    private final FingerprintManager fingerprintManager;
    private final KeyguardManager keyguardManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private FingerUtil(Context context) {
        fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    private static FingerUtil singleton = null;

//    @RequiresApi(api = Build.VERSION_CODES.M)
    public static FingerUtil getInstance(Context context) {
        if (singleton == null) {
            synchronized (FingerUtil.class) {
                if (singleton == null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        singleton = new FingerUtil(context);
                    }
                }
            }
        }
        return singleton;
    }


    /**
     * ②检查手机硬件（有没有指纹感应区）
     */


    public boolean isHardFinger() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fingerprintManager != null && fingerprintManager.isHardwareDetected()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * ③检查手机是否开启锁屏密码
     */

    public boolean isWindowSafe() {
        if (keyguardManager != null && keyguardManager.isKeyguardSecure()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ④检查手机是否已录入指纹
     */

    public boolean isHaveHandler() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fingerprintManager != null && fingerprintManager.hasEnrolledFingerprints()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 创建指纹验证
     */

    public void authenticate(FingerprintManager.CryptoObject cryptoObject, CancellationSignal cancellationSignal,
                             int flag,
                             FingerprintManager.AuthenticationCallback authenticationCallback, Handler handler) {
        if (fingerprintManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fingerprintManager.authenticate(cryptoObject, cancellationSignal, flag, authenticationCallback, handler);
            }
        }
    }

    /**
     * 取消指纹验证  . 应该不会用上
     */
    public void cannelFinger(CancellationSignal cancellationSignal) {
        cancellationSignal.cancel();

    }

    @TargetApi(23)
    public boolean judgeFingerprintIsCorrect() {
        //判断硬件是否支持指纹识别
        if (!isHardFinger()) {
            return false;
        }
        //判断是否开启锁屏密码
        if (!isWindowSafe()) {
            return false;
        }
        //判断是否有指纹录入
        if (!isHaveHandler()) {
            return false;
        }
        return true;
    }

//
//
//
//    /**
//     * 指纹校验回调
//
//     */
//    FingerprintManager.AuthenticationCallback mSelfCancelled = new FingerprintManager.AuthenticationCallback() {
//        @Override
//        public void onAuthenticationError(int errorCode, CharSequence errString) {
//            //多次指纹密码验证错误后，进入此方法；并且，不可再验（短时间）
//            //errorCode是失败的次数
//        }
//
//        @Override
//        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//            //指纹验证失败，可再验，可能手指过脏，或者移动过快等原因。
//        }
//
//        @Override
//        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
//            //指纹密码验证成功
//        }
//
//        @Override
//        public void onAuthenticationFailed() {
//            //指纹验证失败，指纹识别失败，可再验，错误原因为：该指纹不是系统录入的指纹。
//        }
//    };
}