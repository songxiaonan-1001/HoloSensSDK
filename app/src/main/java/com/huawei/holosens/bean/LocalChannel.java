package com.huawei.holosens.bean;



import java.io.Serializable;

public class LocalChannel implements Serializable{
    public LocalChannel() {
    }

    public LocalChannel(String id) {
        this.id = id;
    }

    public LocalChannel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public LocalChannel(String id, String name, long permission) {
        this.id = id;
        this.name = name;
        this.permission = permission;
    }

    private String id;
    private String date;
    private String name;
    private boolean top;        //是否置顶
    private long topTime;     //置顶时间      排序用到
    private long permission;    //权限

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public long getTopTime() {
        return topTime;
    }

    public void setTopTime(long topTime) {
        this.topTime = topTime;
    }

    public long getPermission() {
        return permission;
    }

    public void setPermission(long permission) {
        this.permission = permission;
    }

    public LocalChannel(String alise, boolean isTitle) {
        this.alise = alise;
        this.isTitle = isTitle;
    }

    private String alise;
    private boolean isTitle;

    public String getAlise() {
        return alise;
    }

    public void setAlise(String alise) {
        this.alise = alise;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


    /**是否在线**/
    private int isOnline;

    public int isOnline() {
        return isOnline;
    }

    public void setOnline(int online) {
        isOnline = online;
    }
}
