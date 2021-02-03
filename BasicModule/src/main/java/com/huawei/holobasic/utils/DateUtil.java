package com.huawei.holobasic.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * 日期date在本类中主要指年月日，标准yyyy-MM-dd
 * 时间time在本类中主要指时分秒，标准HH:mm:ss
 * date and time，标准yyyy-MM-dd HH:mm:ss
 * 获取的时间戳一般都以毫秒为单位
 */
public class DateUtil {


    /**
     * 将时间戳转换为对应格式的时间字符串
     *
     * @param millis     时间戳
     * @param dateFormat 时间格式
     * @return 格式化之后的时间字符串
     * */
    public static String millis2String(long millis, String dateFormat) {
        if (TextUtils.isEmpty(dateFormat)) return "";
        return new SimpleDateFormat(dateFormat).format(new Date(millis));
    }

    /**
     * 将时间字符串转换为时间戳
     *
     * @param date       时间字符串
     * @param dateFormat 时间格式
     * @return 时间戳
     * */
    public static long string2Millis(String date, String dateFormat) {
        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(dateFormat)) return 0;
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将时间字符串转换为其他时间字符串
     *
     * @param date          时间
     * @param currentFormat 当前时间格式
     * @param targetFormat  目标时间格式
     * @return 时间字符串
     * */
    public static String string2String(String date, String currentFormat, String targetFormat) {
        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(currentFormat) || TextUtils.isEmpty(targetFormat)) return "";
        if (TextUtils.equals(currentFormat, targetFormat)) return date;
        SimpleDateFormat format = new SimpleDateFormat(currentFormat);
        try {
            return new SimpleDateFormat(targetFormat).format(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 将时间戳转换为年月日时分秒格式的时间字符串
     *
     * @param millis     时间戳
     * @return 格式化之后的时间字符串
     * */
    public static String millis2DateTime(long millis) {
        return millis2String(millis, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将时间戳转换为年月日格式的时间字符串
     *
     * @param millis     时间戳
     * @return 格式化之后的时间字符串
     * */
    public static String millis2Date(long millis) {
        return millis2String(millis, "yyyy-MM-dd");
    }

    /**
     * 将时间戳转换为时分秒格式的时间字符串
     *
     * @param millis     时间戳
     * @return 格式化之后的时间字符串
     * */
    public static String millis2Time(long millis) {
        return millis2String(millis, "HH:mm:ss");
    }


    /**
     * 获取指定格式的当前日期
     * */
    public static String getCurrentDate(String format) {
        return millis2String(System.currentTimeMillis(), format);
    }

    /**
     * 获取当前年月日时分秒
     * */
    public static String getCurrentDateAndTime() {
        return getCurrentDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前年月日
     * */
    public static String getCurrentDateSimple() {
        return getCurrentDate("yyyy-MM-dd");
    }

    /**
     * 获取当前时分秒
     * */
    public static String getCurrentTime() {
        return getCurrentDate("HH:mm:ss");
    }


    /**
     * 获取时间差
     *
     * @param startMillis 开始时间戳
     * @param endMillis   结束时间戳
     * @return 时间差
     * */
    public static long getTimeSpan(long startMillis, long endMillis) {
        return endMillis - startMillis;
    }

    /**
     * 获取时间差
     *
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param dateFormat 时间格式
     * @return 时间差
     * */
    public static long getTimeSpan(String startTime, String endTime, String dateFormat) {
        return getTimeSpan(string2Millis(startTime, dateFormat), string2Millis(endTime, dateFormat));
    }

    /**
     * 获取距离当前时间的时间差
     *
     * @param millis 开始时间戳
     * @return 时间差
     * */
    public static long getTimeSpanByNow(long millis) {
        return millis - System.currentTimeMillis();
    }

    /**
     * 获取距离当前时间的时间差
     *
     * @param time       时间
     * @param dateFormat 时间格式
     * @return 时间差
     * */
    public static long getTimeSpanByNow(String time, String dateFormat) {
        return getTimeSpanByNow(string2Millis(time, dateFormat));
    }


    /**
     * 把秒格式化成时分秒格式的字符串，例如，125:35:45
     * */
    public static String getHoursAndSeconds(int seconds) {
        if (seconds <= 0) return "00:00";
        int hour = 0, min = 0, second;
        if (seconds < 60) {
            second = seconds;
        } else if (seconds < 60 * 60) {
            min = seconds / 60;
            second = seconds % 60;
        } else {
            hour = seconds / 60 / 60;
            min = seconds / 60 % 60;
            second = seconds % 60;
        }
        if (hour <= 0) {
            return String.format("%02d:%02d", min, second);
        }
        return String.format("%02d:%02d:%02d", hour, min, second);
    }


    /**
     * 判断两个日期是否是同一天
     * */
    public static boolean isTheSameDay(long millis1, long millis2) {
        return TextUtils.equals(millis2Date(millis1), millis2Date(millis2));
    }

    /**
     * 判断是否是今天
     * */
    public static boolean isToday(long millis) {
        return isTheSameDay(millis, System.currentTimeMillis());
    }

    /**
     * 判断是否是昨天
     * */
    public static boolean isYesterday(long millis) {
        return isTheSameDay(millis, System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    }

    /**
     * 日期转为时间戳
     * @param time
     * @param format
     * @return
     */
    public static Long toTimeStamp(String time,String format){
        //yyyy-MM-dd'T'HH:mm:ss.sss
        //2019-10-24T22:12:00.000+08:00
        Long timeStamp;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date date = dateFormat.parse(time);
            timeStamp =date.getTime();
        }catch(Exception e){
            timeStamp = null;
        }
        return timeStamp;
    }


    /**
     * 开始时间和结束时间的处理
     * 2020-07-29 09:24:44 原始格式
     * 09:24:44最终格式
     */

    public static String formatCloudRecordStartTime(String timeStr, String selectDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");

        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date selectD = simpleDateFormat3.parse(selectDate);
            Date time = simpleDateFormat.parse(timeStr);
            if(time.getTime()>=selectD.getTime())
                return simpleDateFormat2.format(time);
            else
                return "00:00:00";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 返回指定日期的月的第一天
     *
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的月的最后一天
     *
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }


    /**
     * 返回指定年月的月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        return calendar.getTime();
    }

    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }


}
