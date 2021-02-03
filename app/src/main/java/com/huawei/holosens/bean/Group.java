package com.huawei.holosens.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 设备分组
 */

public class Group implements Serializable, Cloneable {

    public static final String TYPE_FREE = "a";//分组类型--未分组
    public static final String TYPE_AP = "b";//分组类型--ap设备
    public static final String TYPE_GROUP = "c";//分组类型--已分组

    public static final String ID_FREE = "free";//未分组设备自定义id
    public static final String ID_AP = "ap";//ap组设备自定义id

    private String name;
    private ArrayList<Device> group;
    private int state = 1;//0 在家，1 外出
    private String id;
    private String tag = ""; //现在用来排序，未分组a,ap设备b,分组设备c
    private boolean isAPMode = false;//分组是否是AP设备组

    public Group() {

    }

    public Group(String name, int switchState, String id, ArrayList<Device> group) {
        this.setName(name);
        this.setState(switchState);
        this.setId(id);
        this.setGroup(group);
    }

    public Group(String name, int switchState, String id, ArrayList<Device> group, String tag) {
        this.setName(name);
        this.setState(switchState);
        this.setId(id);
        this.setGroup(group);
        this.setTag(tag);
    }

    public Group(String name, int switchState, String id, ArrayList<Device> group, String tag, boolean isAPMode) {
        this.setName(name);
        this.setState(switchState);
        this.setId(id);
        this.setGroup(group);
        this.setTag(tag);
        this.setAPMode(isAPMode);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Device> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<Device> group) {
        if (this.group == null) {
            this.group = new ArrayList<>();
        }else {
            this.group.clear();
        }
        if (group == null) {
            this.group = new ArrayList<>();
        }else {
            this.group.addAll(group);
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isAPMode() {
        return isAPMode;
    }

    public void setAPMode(boolean APMode) {
        isAPMode = APMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "name:"+name+",state:"+state+",isApMode:"+isAPMode()+",id:"+id+",tag:"+tag+",group:"+group;
    }
}
