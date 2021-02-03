package com.huawei.holobasic.utils;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 超级共通
 * 功能如下:
 * 1.判断数组中是否包含某元素
 * 2.检测手机号是否合法
 * 3.检测邮箱是否合法
 * 4.获取设备的云视通组
 * 5.获取设备的云视通号码(不带组标记)
 * 6.判断Ip是否合法
 * 7.域名检查
 * 8.异常信息打印
 * 9.计算星期几(基姆拉尔森公式)
 * 10.计算字符串的字数
 * 11.过滤特征码字符串
 *
 */

public class SuperCommonUtils {
    /**
     * 判断数组中是否包含某元素
     *
     * @param array
     * @param v
     * @return
     */
    @SuppressLint("NewApi")
    public static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array)
            if (Objects.equals(v, e))
                return true;

        return false;
    }



    /**
     * 检测邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmailFormatLegal(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        boolean result;
        String strPattern = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)" +
                "[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$"; // 输入任意字符，但是必须要在（4～20）位之间

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(email);
        result = m.matches();
        if (result) {
            result = email.length() <= 60;
        }
        return result;
    }

    /**
     * 获取设备的云视通组
     *
     * @param deviceNum
     */
    public static String getGroup(String deviceNum) {

        StringBuffer groupSB = new StringBuffer();
        if (!"".equalsIgnoreCase(deviceNum)) {
            for (int i = 0; i < deviceNum.length(); i++) {
                if (Character.isLetter(deviceNum.charAt(i))) { //
                    // 用char包装类中的判断字母的方法判断每一个字符
                    groupSB = groupSB.append(deviceNum.charAt(i));
                }
            }
        }
        return groupSB.toString();
    }

    /**
     * 获取设备的云视通号码(不带组标记)
     *
     * @param deviceNum
     */
    public static int getYST(String deviceNum) {
        int yst;

        StringBuffer ystSB = new StringBuffer();
        if (!"".equalsIgnoreCase(deviceNum)) {
            for (int i = 0; i < deviceNum.length(); i++) {
                if (Character.isDigit(deviceNum.charAt(i))) {
                    ystSB = ystSB.append(deviceNum.charAt(i));
                }
            }
        }

        if ("".equalsIgnoreCase(ystSB.toString())) {
            yst = 0;
        } else {
            yst = Integer.parseInt(ystSB.toString());
        }
        return yst;
    }

    /**
     * 判断Ip是否合法
     *
     * @param addr ip地址
     * @return
     */
    public static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }

//         * 判断IP格式和范围

        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\." +
                "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        return mat.find();
    }

    /**
     * 域名检查
     */
    public static boolean checkDomain(String addr) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)" +
                        "+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(addr);
        return matcher.find();
    }

    /**
     * 记录异常信息
     *
     * @param tag 日志标识
     * @param e   异常
     */
    public static void printError(String tag, Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            // 将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }

        Log.e(tag, "printError, Throws Exception:" + sw.toString());
    }

    /**
     * 记录异常信息
     *
     * @param tag 日志标识
     * @param e   异常
     */
    public static void printError(String tag, Error e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            // 将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }

        Log.e(tag, "printError, Throws Exception:" + sw.toString());
    }

    /**
     * 计算星期几(基姆拉尔森公式)
     *
     * @param y 年
     * @param m 月
     * @param d 日
     * @return
     */
    public static int calcWeekByDate(int y, int m, int d) {
        if (m == 1) {
            m = 13;
            y--;
        }
        if (m == 2) {
            m = 14;
            y--;
        }
        int week = (d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y /
                400) % 7;

        return week;
    }


    /**
     * 计算字符串的字数<br/>
     * 一个汉字=两个英文字母,一个中文标点=两个英文标点<br/>
     * 注意：该函数的不适用于对单个字符进行计算,因为单个字符四舍五入后都是1
     *
     * @param c
     * @return
     */
    public static long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }


    /**
     * 过滤特征码字符串
     *
     * @param msg
     * @return
     */
    public static HashMap<String, String> filterSpecialMsg(String msg) {
        HashMap<String, String> map = new HashMap<>();
        if (null == msg || "".equalsIgnoreCase(msg)) {
            return null;
        }
        Matcher matcher = Pattern.compile("([^=;]+)=([^=;]+)").matcher(msg);
        while (matcher.find()) {
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }

    /**
     * 判断是否处于锁屏状态
     *
     * @return 返回true为锁屏, 返回false为未锁屏
     */
    public static boolean isScreenLocked(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context
                .KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    /**
     * 动态设置Margin
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
