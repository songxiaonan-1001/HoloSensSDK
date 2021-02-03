package com.huawei.holosens.live.playback.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.holobasic.consts.AppConsts;
import com.huawei.holosens.R;
import com.huawei.holosens.base.BaseActivity;
import com.huawei.holosens.consts.JVEncodedConst;
import com.huawei.holosens.consts.SelfConsts;
import com.huawei.holosens.live.playback.bean.RemoteRecord;

import java.util.ArrayList;

/**
 * 远程回放录像adapter
 */
public class RemoteRecordAdapter extends BaseAdapter {

    private ArrayList<RemoteRecord> videoList = new ArrayList<RemoteRecord>();
    private Context mContext;
    private LayoutInflater inflater;
    private boolean supportDownload = false;

    public RemoteRecordAdapter(Context con) {
        mContext = con;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<RemoteRecord> list, boolean support) {
        videoList = list;
        supportDownload = support;
    }

    public ArrayList<RemoteRecord> getData() {
        return videoList;
    }

    @Override
    public int getCount() {
        int size = 0;
        if (null != videoList && 0 != videoList.size()) {
            size = videoList.size();
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        RemoteRecord rv = new RemoteRecord();
        if (null != videoList && 0 != videoList.size()
                && position < videoList.size()) {
            rv = videoList.get(position);
        }
        return rv;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_remote_video, null);
            viewHolder = new ViewHolder();
            viewHolder.videoDate = (TextView) convertView
                    .findViewById(R.id.videodate);
            viewHolder.videoDisk = (TextView) convertView
                    .findViewById(R.id.videodisk);
            viewHolder.videoDownLoad = (ImageView) convertView
                    .findViewById(R.id.videodownload);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (null != videoList && 0 != videoList.size()
                && position < videoList.size()) {

//            public static final String REC_NORMAL = "N";//78,正常录像
//            public static final String REC_TIME = "T";//84，定时录像
//            public static final String REC_MOTION = "M";//77,移动侦测录像
//            public static final String REC_ALARM = "A";//65,报警录像
//            public static final String REC_ONE_MIN = "O";//79,一分钟录像
//            public static final String REC_CHFRAME = "C";//67,抽帧录像

            switch (videoList.get(position).getRecordType()){
                case JVEncodedConst.INT_REC_NORMAL:{
                    viewHolder.videoDisk.setText(mContext.getResources().getString(
                            R.string.video_normal));
                    break;
                }
                case JVEncodedConst.INT_REC_TIME:{
                    viewHolder.videoDisk.setText(mContext.getResources().getString(
                            R.string.video_time));
                    break;
                }
                case JVEncodedConst.INT_REC_MOTION:{
                    viewHolder.videoDisk.setText(mContext.getResources().getString(
                            R.string.video_motion));
                    break;
                }
                case JVEncodedConst.INT_REC_ALARM:{
                    viewHolder.videoDisk.setText(mContext.getResources().getString(
                            R.string.video_alarm));
                    break;
                }
                case JVEncodedConst.INT_REC_ONE_MIN:{
                    viewHolder.videoDisk.setText(mContext.getResources().getString(
                            R.string.video_one_min));
                    break;
                }
                case JVEncodedConst.INT_REC_CHFRAME:{
                    viewHolder.videoDisk.setText(mContext.getResources().getString(
                            R.string.video_ch_frame));
                    break;
                }
                case JVEncodedConst.INT_REC_SMART:{
                    viewHolder.videoDisk.setText(mContext.getResources().getString(
                            R.string.video_smart));
                    break;
                }
                case JVEncodedConst.INT_REC_STOP_ALARM:{
                    viewHolder.videoDisk.setText(mContext.getResources().getString(
                            R.string.video_stop));
                    break;
                }

            }



            if (supportDownload) {
                if (videoList.get(position).isHasDownloaded()) {
                    viewHolder.videoDownLoad.setImageResource(R.mipmap.ic_remote_line_downloaded);
                } else {
                    viewHolder.videoDownLoad.setImageResource(R.mipmap.ic_remote_line_download);
                }
                viewHolder.videoDownLoad.setVisibility(View.VISIBLE);
            } else {
                viewHolder.videoDownLoad.setVisibility(View.INVISIBLE);
            }

            if (videoList.get(position).getEndTime() == null || videoList.get(position).getStartTime() == null){//videoList.get(position).getEndTime().length() == 0) {
                viewHolder.videoDate.setText(
                        videoList.get(position).getFileDate());
            } else {
                viewHolder.videoDate.setText(videoList.get(position).getStartTime()+"-"+videoList.get(position).getEndTime());

                if (AppConsts.DEBUG_STATE) {
                    viewHolder.videoDate.setText(position + "-" +
                            videoList.get(position).getFileDate() + "-" + videoList.get(position).getStartTime()+ "-" + videoList.get(position).getEndTime());

                }
            }


//            if (AppConsts.DEBUG_STATE) {
//                viewHolder.videoDate.setText(
//                        videoList.get(position).remoteDate+"-"+videoList.size()+"-"+videoList.get(position).remoteEndTime);
//            } else {
//                viewHolder.videoDate.setText(
//                        videoList.get(position).remoteDate);
//            }

        }

        viewHolder.videoDownLoad.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ((BaseActivity) mContext).onNotify(SelfConsts.WHAT_REMOTE_DOWNLOAD_FILE,
                        position, 0, null);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView videoDate;
        TextView videoDisk;
        ImageView videoDownLoad;
    }
}
