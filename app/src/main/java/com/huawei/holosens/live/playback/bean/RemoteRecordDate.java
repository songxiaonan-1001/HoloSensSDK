package com.huawei.holosens.live.playback.bean;

/**
 * 精准回放，有录像的日期
 */
public class RemoteRecordDate {
    private String date;//2019-07-01
    private int year;//2019
    private int month;//7
    private int day;//6

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
