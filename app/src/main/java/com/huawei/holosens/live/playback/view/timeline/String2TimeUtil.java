
package com.huawei.holosens.live.playback.view.timeline;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 用于将String转化为时间
 */
public class String2TimeUtil {

    /**
     * 转换日期格式到用户体验好的时间格式
     *
     * @param time 2015-04-11 12:45:06
     * @return
     */
    public static String dateString2GoodExperienceFormat(String time) {
        if (isNullString(time)) {
            return "";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat
                    ("yyyy-MM-dd HH:mm:ss",
                            Locale.CHINA);
            try {
                String timeString;
                Date parse = simpleDateFormat.parse(time);
                long distanceTime = new Date().getTime() - parse.getTime();
                if (distanceTime < 0L) {
                    // 正常不会走到这里
                    timeString = "刚刚";
                } else {
                    // 计算过去的分钟数
                    long minute = distanceTime / 60000L;
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat
                            ("MM-dd", Locale.CHINA);
                    if (minute < 1L) {
                        // 1分钟之内
                        timeString = "刚刚";
                    } else if (minute < 60L) {
                        // 1小时之内
                        timeString = String.valueOf(minute) + "分钟前";
                    } else if (minute < 720L) {
                        // 12小时之内
                        timeString = String.valueOf(minute / 60L) + "小时前";
                    } else {
                        // 显示某月某日
                        timeString = simpleDateFormat2.format(parse);
                    }
                }
                return timeString;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
        }
    }

    /**
     * 转换日期格式到用户体验好的时间格式
     *
     * @param time 20160607142641
     * @return 2016-06-07 14:26:41
     */
    public static String dateString2Time(String time) {
        if (isNullString(time)) {
            return "";
        } else {
            // 转换前格式
            SimpleDateFormat before = new SimpleDateFormat
                    ("yyyyMMddHHmmss",
                            Locale.CHINA);
            // 转换后格式
            SimpleDateFormat after = new SimpleDateFormat
                    ("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            try {
                String timeString;
                Date beforeDate = before.parse(time);
                timeString = after.format(beforeDate);
                return timeString;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
        }
    }

    /**
     * 转换日期格式到用户体验好的时间格式
     *
     * @param time        20160607142641
     * @param format      time的日期格式 例：yyyyMMddHHmmss
     * @param afterFormat 要转换成的日期格式 例：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String dateString2Time(String time, String format, String
            afterFormat) {
        if (isNullString(time)) {
            return "";
        } else {
            // 转换前格式
            SimpleDateFormat before = new SimpleDateFormat
                    (format, Locale.CHINA);
            // 转换后格式
            SimpleDateFormat after = new SimpleDateFormat
                    (afterFormat, Locale.CHINA);
            try {
                String timeString;
                Date beforeDate = before.parse(time);
                timeString = after.format(beforeDate);
                return timeString;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
        }
    }



    /**
     * 获取当前时间(精确到毫秒)
     *
     * @return
     */
    public static String getCurrentTime() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat
                ("yyyy-MM-dd HH:mm:ss:sss", Locale.CHINA);
        return sdf.format(date);
    }

    public static boolean isNullString(String s) {
        return s == null || s.equals("") || s.equals("null");
    }


    /**
     * 获取当前时间的秒数
     *
     * @param timeStr
     * @return
     */
    public static int getSecondsByTimeStr(String timeStr) {
        Log.e("5678909877", "getSecondsByTimeStr=" + timeStr);
        int allSeconds = 0;
        if ("".equalsIgnoreCase(timeStr)) {
            return allSeconds;
        }

        if (timeStr.length() > 8) {
            timeStr = timeStr.substring(0, 8);
        }

        try {
            String[] my = timeStr.split(":");
            int hour = Integer.parseInt(my[0]);
            int min = Integer.parseInt(my[1]);
            int sec = 0;
            if (3 == my.length) {
                sec = Integer.parseInt(my[2]);
            }
            allSeconds = hour * 3600 + min * 60 + sec;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return allSeconds;
    }


    /**
     * 秒转时间 a integer to xx:xx:xx
     * @param time
     * @return
     */
    public static String secToTime(int time) {

        String timeStr;
        int hour = 0;
        int minute;
        int second;
        if (time <= 0 || time == 86400)//24：00显示00：00防止播放24点的视频有问题
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
