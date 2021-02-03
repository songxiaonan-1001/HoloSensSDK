package com.huawei.holosens.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.holosens.base.BaseApplication;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;

/**
 * 网络相关工具类
 */
@SuppressWarnings("ALL")
public class NetWorkUtil {

    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    @SuppressWarnings("WeakerAccess")
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;
    private static final String TAG = "NetWorkUtil";
    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    // private static final int NETWORK_TYPE_MOBILE = -100;
    private static final int NETWORK_TYPE_WIFI = -101;

    // 适配低版本手机
    public static final int NETWORK_CLASS_WIFI = -101;
    private static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /**
     * Unknown network class.
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks.
     */
    public static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks.
     */
    public static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks.
     */
    public static final int NETWORK_CLASS_4_G = 3;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static DecimalFormat df = new DecimalFormat("#.##");

    static {
        mContext = BaseApplication.getContext();
    }

    /**
     * Wifi是否连接
     *
     * @return
     */
    public static boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo
                .getType() == ConnectivityManager.TYPE_WIFI);
    }
    /**
     * 网络是否可用
     *
     * @return
     */
    public static boolean IsNetWorkEnable() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                 Log.e(TAG, "无法连接网络");
                return false;
            }

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
         Log.e(TAG, "无法连接网络");
        return false;
    }



    /**
     * 2G/3G/4G网络判断<br/>
     * return 2G/3G/4G|当前无网络连接
     */
    public static String getSpecialNetWorkInfo() {
        String strNetTips = "";
        String type = getCurrentNetworkType();
        if (TextUtils.equals(type, "2G") || TextUtils.equals(type, "3G")
                || TextUtils.equals(type, "4G")) {
            strNetTips = ResourcesUnusualUtil.getString("net_tip_234g");
        } else if (TextUtils.equals(type, "无")) {
            strNetTips = ResourcesUnusualUtil.getString("net_tip_unavailable");
        }

        return strNetTips;
    }

    /**
     * 检查是否能连接网络
     */
    public static boolean checkNetConnected(Context con) {
        ConnectivityManager cwjManager = (ConnectivityManager) con
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            if (cwjManager.getActiveNetworkInfo() != null
                    && cwjManager.getActiveNetworkInfo().isAvailable()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取网络类型
     *
     * @return 各种网络类型
     */
    public static String getCurrentNetworkType() {
        int networkClass = getNetworkClass();
        String type = "未知";
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
                type = "无";
                break;
            case NETWORK_CLASS_WIFI:
                type = "Wi-Fi";
                break;
            case NETWORK_CLASS_2_G:
                type = "2G";
                break;
            case NETWORK_CLASS_3_G:
                type = "3G";
                break;
            case NETWORK_CLASS_4_G:
                type = "4G";
                break;
            case NETWORK_CLASS_UNKNOWN:
                type = "未知";
                break;
        }
        return type;
    }


    /**
     * 获取网络类型
     * 网络类型:0-未知，1-移动数据网络，2-2G，3-3G，4-4G，5-5G，6-wifi
     *
     * @return 各种网络类型
     */
    public static int getCurrentNetworkIntType() {
        int networkClass = getNetworkClass();
        int type = 0;
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
            case NETWORK_CLASS_UNKNOWN:
                type = 0;
                break;
            case NETWORK_CLASS_WIFI:
                type = 6;
                break;
            case NETWORK_CLASS_2_G:
                type = 2;
                break;
            case NETWORK_CLASS_3_G:
                type = 3;
                break;
            case NETWORK_CLASS_4_G:
                type = 4;
                break;
        }
        return type;
    }

    private static int getNetworkClass() {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            final NetworkInfo network = ((ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) mContext
                            .getSystemService(
                                    Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNetworkClassByType(networkType);

    }

    private static int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE;
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    public static String getWifiRssi() {
        int asu = 85;
        try {
            final NetworkInfo network = ((ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    @SuppressLint("WifiManagerPotentialLeak") WifiManager wifiManager = (WifiManager) mContext.getApplicationContext()
                            .getSystemService(Context.WIFI_SERVICE);

                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        asu = wifiInfo.getRssi();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asu + "dBm";
    }

    public static String getWifiSsid() {
        String ssid = "";
        try {
            final NetworkInfo network = ((ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    WifiManager wifiManager = (WifiManager) mContext.getApplicationContext()
                            .getSystemService(Context.WIFI_SERVICE);

                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        ssid = wifiInfo.getSSID();
                        if (ssid == null) {
                            ssid = "";
                        }
                        ssid = ssid.replaceAll("\"", "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssid;
    }

    /**
     * 检查sim卡状态
     *
     * @param
     * @return
     */
    public static boolean checkSimState() {
        TelephonyManager tm = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT
                || tm.getSimState() == TelephonyManager.SIM_STATE_UNKNOWN);

    }


    /**
     * 获取6.0以上系统手机的MAC地址
     * @return
     */
    public static String getMacAddress6(){
        String str="";
        String macSerial="";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        return macSerial;
    }
    public static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }
    public static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }


    /**
     * 是否是移动网络环境
     *
     * @return 是否是移动网络
     * @see ://stackoverflow.com/questions/12806709/how-to-tell-if-mobile-network
     * -data-is-enabled-or-disabled-even-when-connected
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isMobileNetOpen() {
        boolean isMobileNet = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isMobileNet = Settings.Global.getInt(mContext.getContentResolver(), "mobile_data", 1) == 1;
        } else {
            isMobileNet = Settings.Secure.getInt(mContext.getContentResolver(), "mobile_data", 1) == 1;
        }
        return isMobileNet;
    }

    /**
     * 获取本机ip地址
     *
     * @return
     */
    public static String getLocalHostIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                if ("wlan0".equalsIgnoreCase(nif.getName())) {
                    // 遍历每一个接口绑定的所有ip
                    while (inet.hasMoreElements()) {
                        InetAddress ip = inet.nextElement();
                        if (!ip.isLoopbackAddress()
                                && ip instanceof Inet4Address) {
                            return ipaddress = ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipaddress;

    }

    /**
     * 格式化大小
     *
     * @param size
     * @return
     */
    public static String formatSize(long size) {
        String unit = "B";
        float len = size;
        if (len > 900) {
            len /= 1024f;
            unit = "KB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "MB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "GB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "TB";
        }
        return df.format(len) + unit;
    }

    public static String formatSizeBySecond(long size) {
        String unit = "B";
        float len = size;
        if (len > 900) {
            len /= 1024f;
            unit = "KB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "MB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "GB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "TB";
        }
        return df.format(len) + unit + "/s";
    }

    public static String format(long size) {
        String unit = "B";
        float len = size;
        if (len > 1000) {
            len /= 1024f;
            unit = "KB";
            if (len > 1000) {
                len /= 1024f;
                unit = "MB";
                if (len > 1000) {
                    len /= 1024f;
                    unit = "GB";
                }
            }
        }
        return df.format(len) + "\n" + unit + "/s";
    }

    /**
     * 网络连接判断方式_ping web地址<br/>
     * 此中方式设置超时也不管用
     */
    private static boolean pingWebUrl() {
        // 接口开始执行
        String ip = "www.baidu.com";
        Runtime run = Runtime.getRuntime();
        Process proc = null;
        try {
            String str = "ping -c 3 -i 0.2 -W 1 " + ip;
            long starttime = System.currentTimeMillis();
            proc = run.exec(str);
            int retCode = proc.waitFor();
            long difftime = System.currentTimeMillis() - starttime;
             Log.v(TAG, "ping耗时:" + difftime);
            if (retCode == 0) {
                 Log.v(TAG, "ping连接成功");
                return true;
            } else {
                 Log.e(TAG, "ping测试失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (proc != null) {
                proc.destroy();
            }
        }
        return false;
    }


    /**
     * 获取运营商名字
     */
    public static String getOperatorName(Context mContext) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telephonyManager.getSimOperator();
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002")) {
                // operatorName="中国移动";
//                 Toast.makeText(mContext, "此卡属于(中国移动)",
//                 Toast.LENGTH_SHORT).show();
                return "中国移动";
            } else if (operator.equals("46001")) {
                // operatorName="中国联通";
//                 Toast.makeText(mContext, "此卡属于(中国联通)",
//                 Toast.LENGTH_SHORT).show();
                return "中国联通";
            } else if (operator.equals("46003")) {
                // operatorName="中国电信";
//                 Toast.makeText(mContext, "此卡属于(中国电信)",
//                 Toast.LENGTH_SHORT).show();
                return "中国电信";
            } else {
//                Toast.makeText(mContext, "未知",
//                        Toast.LENGTH_SHORT).show();
                return operator;
            }
        }
        return "Unknown";
    }

}
