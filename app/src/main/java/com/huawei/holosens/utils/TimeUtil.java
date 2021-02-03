package com.huawei.holosens.utils;

import android.content.Context;
import android.text.format.Time;

import com.huawei.holosens.R;
import com.huawei.holosens.base.BaseApplication;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.utils
 * @ClassName: TimeUtil
 * @Description: java类作用描述
 * @CreateDate: 2020-01-11 09:34
 * @Version: 1.0
 */
public class TimeUtil {

    /**
     * 获取问候语
     * @param context
     * @return
     */
    public static String getTime(Context context) {
        if (isCurrentInTimeScope(3, 0, 6, 0))
            return context.getResources().getString(R.string.good_before_dawn);
        else if (isCurrentInTimeScope(6, 0, 12, 0))
            return context.getResources().getString(R.string.good_morning);
        else if (isCurrentInTimeScope(12, 0, 18, 0))
            return context.getResources().getString(R.string.good_afternoon);
        else
            return context.getResources().getString(R.string.good_night);
    }

    /**
     * 校验是否重复点击
     *
     * @param lastClickTime 上次点击时间，单个页面可以维护一个时间
     * @return 0:时间间隔不足500，其他返回当前的点击时间
     */
    public static long checkClickDisTime(long lastClickTime) {
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime < BaseApplication.CLICK_DIS_TIME) {
            return 0;
        }
        return currentClickTime;
    }

    /**
     * 判断当前系统时间是否在特定时间的段内
     *
     * @param beginHour 开始的小时，例如5
     * @param beginMin  开始小时的分钟数，例如00
     * @param endHour   结束小时，例如 8
     * @param endMin    结束小时的分钟数，例如00
     * @return true表示在范围内，否则false
     */
    private static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result;// 结果
        final long aDayInMillis = 1000 * 60 * 60 * 24;// 一天的全部毫秒数
        final long currentTimeMillis = System.currentTimeMillis();// 当前时间

        Time now = new Time();// 注意这里导入的时候选择android.text.format.Time类,而不是java.sql.Time类
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;

        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }
}
