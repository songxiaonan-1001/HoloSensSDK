package com.huawei.holosens.view.stickygridheaders.view;

import java.io.Serializable;

public class GridItem implements Serializable{
	//路径
	private String path;
	//时间
	private String time;
	private int section;
	private int subSection;
	//设备
	private String devName;

	private boolean hideDate;

	public GridItem(String path, String time) {
		super();
		this.path = path;
		this.time = time;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}


	public int getSubSection() {
		return subSection;
	}

	public void setSubSection(int subSection) {
		this.subSection = subSection;
	}

	private long modifyTime;

	public long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public GridItem(String path, String time, long modifyTime) {
		super();
		this.path = path;
		this.time = time;
		this.modifyTime = modifyTime;
	}

	public GridItem(String path, String time, String devName, long modifyTime) {
		this.path = path;
		this.time = time;
		this.devName = devName;
		this.modifyTime = modifyTime;
	}

	public boolean isHideDate() {
		return hideDate;
	}

	public void setHideDate(boolean hideDate) {
		this.hideDate = hideDate;
	}
}
