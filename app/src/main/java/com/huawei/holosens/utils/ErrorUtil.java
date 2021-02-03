package com.huawei.holosens.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.huawei.holosens.R;
import com.huawei.net.retrofit.request.ResponseCode;

import java.util.HashMap;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.utils
 * @ClassName: ErrorUtil
 * @Description: java类作用描述
 * @CreateDate: 2020-01-19 10:14
 * @Version: 1.0
 */
public class ErrorUtil {

    @SuppressLint("StaticFieldLeak")
    private static ErrorUtil mSingleton = null;
    @SuppressLint("UseSparseArrays")
    private ErrorUtil () {
        errors = new HashMap<>();
    }

    public static ErrorUtil getInstance() {
        if (mSingleton == null) {
            synchronized (ErrorUtil.class) {
                if (mSingleton == null) {
                    mSingleton = new ErrorUtil();
                }
            }
        }
        return mSingleton;

    }

    private Context mContext;
    HashMap<Integer, String> errors ;

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
        initErrors();
    }



    private void initErrors(){
        errors.put(500, mContext.getResources().getString(R.string.erro_500));
        errors.put(999, mContext.getResources().getString(R.string.erro_999));
        errors.put(1002, mContext.getResources().getString(R.string.erro_1002));
        errors.put(1003, mContext.getResources().getString(R.string.erro_1003));
        errors.put(1005, mContext.getResources().getString(R.string.erro_1005));
        errors.put(1006, mContext.getResources().getString(R.string.erro_1006));
        errors.put(2000, mContext.getResources().getString(R.string.erro_2000));
        errors.put(4000, mContext.getResources().getString(R.string.erro_4000));
        errors.put(20001, mContext.getResources().getString(R.string.erro_20001));
        errors.put(20002, mContext.getResources().getString(R.string.erro_20002));
        errors.put(20003, mContext.getResources().getString(R.string.erro_20003));
        errors.put(21001, mContext.getResources().getString(R.string.erro_21001));
        errors.put(21002, mContext.getResources().getString(R.string.erro_21002));
        errors.put(21004, mContext.getResources().getString(R.string.erro_21004));
        errors.put(21005, mContext.getResources().getString(R.string.erro_21005));
        errors.put(21006, mContext.getResources().getString(R.string.erro_21006));
        errors.put(21007, mContext.getResources().getString(R.string.erro_21007));
        errors.put(21008, mContext.getResources().getString(R.string.erro_21008));
        errors.put(21009, mContext.getResources().getString(R.string.erro_21009));
        errors.put(21010, mContext.getResources().getString(R.string.erro_21010));
        errors.put(21011, mContext.getResources().getString(R.string.erro_21011));
        errors.put(21012, mContext.getResources().getString(R.string.erro_21012));
        errors.put(21013, mContext.getResources().getString(R.string.erro_21013));
        errors.put(21014, mContext.getResources().getString(R.string.erro_21014));
        errors.put(21015, mContext.getResources().getString(R.string.erro_21015));
        errors.put(21016, mContext.getResources().getString(R.string.erro_21016));
        errors.put(21017, mContext.getResources().getString(R.string.erro_21017));
        errors.put(21018, mContext.getResources().getString(R.string.erro_21018));
        errors.put(21019, mContext.getResources().getString(R.string.erro_21019));
        errors.put(21020, mContext.getResources().getString(R.string.erro_21020));
        errors.put(21021, mContext.getResources().getString(R.string.erro_21021));
        errors.put(21022, mContext.getResources().getString(R.string.erro_21022));
        errors.put(21023, mContext.getResources().getString(R.string.erro_21023));
        errors.put(21024, mContext.getResources().getString(R.string.erro_21024));
        errors.put(21025, mContext.getResources().getString(R.string.erro_21025));
        errors.put(21028, mContext.getResources().getString(R.string.erro_21028));
        errors.put(21029, mContext.getResources().getString(R.string.erro_21029));
        errors.put(21030, mContext.getResources().getString(R.string.erro_21030));
        errors.put(21031, mContext.getResources().getString(R.string.erro_21031));
        errors.put(21032, mContext.getResources().getString(R.string.erro_21032));
        errors.put(21033, mContext.getResources().getString(R.string.erro_21033));
        errors.put(21036, mContext.getResources().getString(R.string.erro_21036));
        errors.put(21037, mContext.getResources().getString(R.string.erro_21037));
        errors.put(21038, mContext.getResources().getString(R.string.erro_21038));
        errors.put(21039, mContext.getResources().getString(R.string.erro_21039));
        errors.put(21040, mContext.getResources().getString(R.string.erro_21040));
        errors.put(21041, mContext.getResources().getString(R.string.erro_21041));
        errors.put(21042, mContext.getResources().getString(R.string.erro_21042));
        errors.put(21043, mContext.getResources().getString(R.string.erro_21043));
        errors.put(21044, mContext.getResources().getString(R.string.erro_21044));
        errors.put(21045, mContext.getResources().getString(R.string.erro_21045));
        errors.put(21046, mContext.getResources().getString(R.string.erro_21046));
        errors.put(21047, mContext.getResources().getString(R.string.erro_21047));
        errors.put(21048, mContext.getResources().getString(R.string.erro_21048));
        errors.put(21049, mContext.getResources().getString(R.string.erro_21049));
        errors.put(21050, mContext.getResources().getString(R.string.erro_21050));
        errors.put(21051, mContext.getResources().getString(R.string.erro_21051));
        errors.put(21052, mContext.getResources().getString(R.string.erro_21052));
        errors.put(21053, mContext.getResources().getString(R.string.erro_21053));
        errors.put(21054, mContext.getResources().getString(R.string.erro_21054));
        errors.put(21055, mContext.getResources().getString(R.string.erro_21055));
        errors.put(21056, mContext.getResources().getString(R.string.erro_21056));
        errors.put(21057, mContext.getResources().getString(R.string.erro_21057));
        errors.put(21058, mContext.getResources().getString(R.string.erro_21058));
        errors.put(21059, mContext.getResources().getString(R.string.erro_21059));
        errors.put(21060, mContext.getResources().getString(R.string.erro_21060));
        errors.put(21061, mContext.getResources().getString(R.string.erro_21061));
        errors.put(21062, mContext.getResources().getString(R.string.erro_21062));
        errors.put(21063, mContext.getResources().getString(R.string.erro_21062));
        errors.put(21064, mContext.getResources().getString(R.string.erro_21062));
        errors.put(21065, mContext.getResources().getString(R.string.erro_21062));
        errors.put(21066, mContext.getResources().getString(R.string.erro_21062));

        errors.put(22001, mContext.getResources().getString(R.string.erro_22001));
        errors.put(22002, mContext.getResources().getString(R.string.erro_22002));
        errors.put(22003, mContext.getResources().getString(R.string.erro_22003));
        errors.put(22004, mContext.getResources().getString(R.string.erro_22004));
        errors.put(22005, mContext.getResources().getString(R.string.erro_22005));
        errors.put(22006, mContext.getResources().getString(R.string.erro_22006));
        errors.put(22007, mContext.getResources().getString(R.string.erro_22007));
        errors.put(22008, mContext.getResources().getString(R.string.erro_22008));
        errors.put(22009, mContext.getResources().getString(R.string.erro_22009));
        errors.put(22010, mContext.getResources().getString(R.string.erro_22010));
        errors.put(22011, mContext.getResources().getString(R.string.erro_22011));
        errors.put(22012, mContext.getResources().getString(R.string.erro_22012));
        errors.put(22013, mContext.getResources().getString(R.string.erro_22013));
        errors.put(22014, mContext.getResources().getString(R.string.erro_22014));
        errors.put(22015, mContext.getResources().getString(R.string.erro_22015));
        errors.put(22016, mContext.getResources().getString(R.string.erro_22016));
        errors.put(22017, mContext.getResources().getString(R.string.erro_22017));
        errors.put(22018, mContext.getResources().getString(R.string.erro_22018));
        errors.put(22019, mContext.getResources().getString(R.string.erro_22019));
        errors.put(22020, mContext.getResources().getString(R.string.erro_22020));
        errors.put(22022, mContext.getResources().getString(R.string.erro_22022));
        errors.put(22023, mContext.getResources().getString(R.string.erro_22023));
        errors.put(22024, mContext.getResources().getString(R.string.erro_22024));
        errors.put(22025, mContext.getResources().getString(R.string.erro_22025));
        errors.put(22026, mContext.getResources().getString(R.string.erro_22026));
        errors.put(22027, mContext.getResources().getString(R.string.erro_22027));
        errors.put(22028, mContext.getResources().getString(R.string.erro_22028));
        errors.put(22029, mContext.getResources().getString(R.string.erro_22029));
        errors.put(22030, mContext.getResources().getString(R.string.erro_22030));
        errors.put(22031, mContext.getResources().getString(R.string.erro_22031));
        errors.put(22032, mContext.getResources().getString(R.string.erro_22032));
        errors.put(22033, mContext.getResources().getString(R.string.erro_22033));
        errors.put(22034, mContext.getResources().getString(R.string.erro_22034));
        errors.put(22035, mContext.getResources().getString(R.string.erro_22035));
        errors.put(22036, mContext.getResources().getString(R.string.erro_22036));
        errors.put(22037, mContext.getResources().getString(R.string.erro_22037));
        errors.put(22038, mContext.getResources().getString(R.string.erro_22038));
        errors.put(22039, mContext.getResources().getString(R.string.erro_22039));
        errors.put(22040, mContext.getResources().getString(R.string.erro_22040));
        errors.put(22041, mContext.getResources().getString(R.string.erro_22041));
        errors.put(22042, mContext.getResources().getString(R.string.erro_22042));
        errors.put(22043, mContext.getResources().getString(R.string.erro_22043));
        errors.put(22044, mContext.getResources().getString(R.string.erro_22044));
        errors.put(22045, mContext.getResources().getString(R.string.erro_22045));
        errors.put(22046, mContext.getResources().getString(R.string.erro_22046));
        errors.put(22047, mContext.getResources().getString(R.string.erro_22047));
        errors.put(22048, mContext.getResources().getString(R.string.erro_22048));
        errors.put(22049, mContext.getResources().getString(R.string.erro_22049));
        errors.put(22050, mContext.getResources().getString(R.string.erro_22050));
        errors.put(22051, mContext.getResources().getString(R.string.erro_22051));
        errors.put(22052, mContext.getResources().getString(R.string.erro_22052));
        errors.put(22053, mContext.getResources().getString(R.string.erro_22053));
        errors.put(22054, mContext.getResources().getString(R.string.erro_22054));
        errors.put(22055, mContext.getResources().getString(R.string.erro_22055));
        errors.put(22056, mContext.getResources().getString(R.string.erro_22056));
        errors.put(22057, mContext.getResources().getString(R.string.erro_22057));
        errors.put(22058, mContext.getResources().getString(R.string.erro_22058));
        errors.put(22059, mContext.getResources().getString(R.string.erro_22059));
        errors.put(22060, mContext.getResources().getString(R.string.erro_22060));
        errors.put(22061, mContext.getResources().getString(R.string.erro_22061));
        errors.put(22062, mContext.getResources().getString(R.string.erro_22062));
        errors.put(22063, mContext.getResources().getString(R.string.erro_22063));
        errors.put(22064, mContext.getResources().getString(R.string.erro_22064));
        errors.put(22065, mContext.getResources().getString(R.string.erro_22065));
        errors.put(22066, mContext.getResources().getString(R.string.erro_22066));
        errors.put(22067, mContext.getResources().getString(R.string.erro_22067));
        errors.put(22068, mContext.getResources().getString(R.string.erro_22068));
        errors.put(22069, mContext.getResources().getString(R.string.erro_22069));
        errors.put(22070, mContext.getResources().getString(R.string.erro_22070));
        errors.put(22071, mContext.getResources().getString(R.string.erro_22071));
        errors.put(22072, mContext.getResources().getString(R.string.erro_22072));
        errors.put(22073, mContext.getResources().getString(R.string.erro_22073));
        errors.put(22074, mContext.getResources().getString(R.string.erro_22074));
        errors.put(22075, mContext.getResources().getString(R.string.erro_22075));
        errors.put(22076, mContext.getResources().getString(R.string.erro_22076));
        errors.put(22077, mContext.getResources().getString(R.string.erro_22077));

        errors.put(17001, mContext.getResources().getString(R.string.erro_17001));
        errors.put(17002, mContext.getResources().getString(R.string.erro_17002));
    }

    /**
     * 校验云台controltoken是否有效
     * @param erroCode
     * @returno
     */
    public boolean isControlTokenValid(int erroCode){
        return erroCode == 22011;
    }

    public String getErrorValue(int key){
        return errors.get(key);
    }


    public static boolean CheckError(int erroCode){
        if(erroCode!= ResponseCode.ACCOUNT_QUIT && erroCode != ResponseCode.INVALID_TOKEN)
            return true;
        return false;
    }
}
