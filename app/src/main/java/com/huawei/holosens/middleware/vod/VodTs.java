package com.huawei.holosens.middleware.vod;

import com.huawei.holobase.bean.SDKRecordBean;
import com.huawei.holobasic.utils.DateUtil;
import com.huawei.holosens.live.playback.bean.RemoteRecord;
import com.huawei.holosens.live.playback.bean.RemoteRecordDate;
import com.huawei.holosens.live.playback.bean.sdk.hw.HwRecordBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: HoloSensEnterprise
 * @Package: com.huawei.holosens.middleware.vod
 * @ClassName: VodTs
 * @Description: java类作用描述
 * VOD 中间件
 * 负责将其他类型的录像文件类型，转化为UI层识别的录像文件类型
 * @CreateDate: 2020-08-11 14:11
 * @Version: 1.0
 */
public class VodTs {

    /**
     * 将好望前端录像转化为UI识别的录像
     *
     * @param hwRecords   好望录像列表
     * @param currentDate 当前选中的日期
     * @return UI层需要的设备列表
     */
    public static List<RemoteRecord> transmitHwLocalRecord(List<HwRecordBean> hwRecords, String currentDate) {
        ArrayList<RemoteRecord> recordList = new ArrayList<>();
        for (HwRecordBean bean : hwRecords) {
            RemoteRecord remoteRecord = new RemoteRecord();
            if (bean.getType().equals("normal"))
                remoteRecord.setRecordType(78);
            if (bean.getType().equals("alarm"))
                remoteRecord.setRecordType(65);
            if (bean.getType().equals("motion"))
                remoteRecord.setRecordType(77);
            remoteRecord.setFileName(bean.getFile_name());
            long startTime = DateUtil.toTimeStamp(bean.getBegin_time(), "yy-MM-dd HH:mm:ss");
            long endTime = DateUtil.toTimeStamp(bean.getEnd_time(), "yy-MM-dd HH:mm:ss");
            remoteRecord.setFileDuration((int) (endTime - startTime));
            remoteRecord.setStartTime(DateUtil.formatCloudRecordStartTime(bean.getBegin_time(), currentDate));
            remoteRecord.setEndTime(DateUtil.formatCloudRecordStartTime(bean.getEnd_time(), currentDate));

            recordList.add(remoteRecord);
        }

        return recordList;
    }

    /**
     * 将云顿录像装华为UI层识别的录像格式
     *
     * @param records
     * @param currentDate
     * @return
     */
    public static List<RemoteRecord> transmitCloudRecord(List<SDKRecordBean> records, String currentDate) {
        ArrayList<RemoteRecord> recordList = new ArrayList<>();

        for (SDKRecordBean b : records) {
            RemoteRecord remoteRecord = new RemoteRecord();
            remoteRecord.setRecordType(78);                     //没有录像类型，统一设置为78 normal
            remoteRecord.setFileName(b.getRecord_name());
            remoteRecord.setFilePath(b.getRecord_name());
            long startTime = DateUtil.toTimeStamp(b.getStart_time(), "yy-MM-dd HH:mm:ss");
            long endTime = DateUtil.toTimeStamp(b.getEnd_time(), "yy-MM-dd HH:mm:ss");
            remoteRecord.setFileDuration((int) (endTime - startTime));
            remoteRecord.setStartTime(DateUtil.formatCloudRecordStartTime(b.getStart_time(), currentDate));
            remoteRecord.setEndTime(DateUtil.formatCloudRecordStartTime(b.getEnd_time(), currentDate));

            recordList.add(remoteRecord);
        }
        return recordList;
    }


    /**
     * 将获取的日历信息转化为UI层可识别的信息
     * @param dates
     * @return
     */
    public static List<RemoteRecordDate> transmitLocalRecordDate(List<String> dates){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<RemoteRecordDate> list = new ArrayList<>();
        for(String date : dates){
            RemoteRecordDate bean = new RemoteRecordDate();
            bean.setDate(date);
            Date d;
            try {
                 d = simpleDateFormat.parse(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                bean.setYear(year);
                bean.setMonth(month+1);
                bean.setDay(day);
                list.add(bean);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
