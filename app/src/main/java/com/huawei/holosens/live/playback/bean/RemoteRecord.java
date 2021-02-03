package com.huawei.holosens.live.playback.bean;

/**
 * 精准回放，远程录像
 *
 * //            public static final String REC_NORMAL = "N";//78,正常录像
 * //            public static final String REC_TIME = "T";//84，定时录像
 * //            public static final String REC_MOTION = "M";//77,移动侦测录像
 * //            public static final String REC_ALARM = "A";//65,报警录像
 * //            public static final String REC_ONE_MIN = "O";//79,一分钟录像
 * //            public static final String REC_CHFRAME = "C";//67,抽帧录像
 */
public class RemoteRecord {

    private String channel;//通道号从1开始
    private String startTime;//开始时间
    private String endTime;//结束时间
    private int recordType;//录像类型
    private String fileName;//文件名
    private String filePath;//文件路径
    private int fileSize;//文件大小
    private String fileDate;//文件日期  老的云视通设备(非精准)，流媒体设备(非精准)，此字段是starttime开始时间
    private int fileDisk;//文件存放位置  硬盘
    private int fileDuration;//持续时长

    //以下两个参数值为原始字节数据(兼容云视通1.0协议需使用以下两个字段)
    public byte disk;// 录像所在盘
    public byte kind;// 录像类型


    private boolean hasRead;//是否已读
    private boolean hasDownloaded;//是否已下载



    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public int getFileDisk() {
        return fileDisk;
    }

    public void setFileDisk(int fileDisk) {
        this.fileDisk = fileDisk;
    }

    public int getFileDuration() {
        return fileDuration;
    }

    public void setFileDuration(int fileDuration) {
        this.fileDuration = fileDuration;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public boolean isHasDownloaded() {
        return hasDownloaded;
    }

    public void setHasDownloaded(boolean hasDownloaded) {
        this.hasDownloaded = hasDownloaded;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}
