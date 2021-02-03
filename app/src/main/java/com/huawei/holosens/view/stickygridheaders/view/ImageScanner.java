package com.huawei.holosens.view.stickygridheaders.view;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.huawei.holobasic.consts.AppConsts;


//图片扫描类
class ImageScanner {
    private Context mContext;

    public ImageScanner(Context context) {
        this.mContext = context;
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    public void scanImages(final ScanCompleteCallBack callback) {
        @SuppressLint("HandlerLeak") final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                callback.scanComplete((Cursor) msg.obj);
            }
        };

        new Thread(new Runnable() {

            @Override
            public void run() {
                String path = AppConsts.CAPTURE_PATH;
//                String selection = MediaStore.Images.Media.DATA + " like ?";
//                String[] selectionArgs = {path + "%"};
//                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                //System.out.println("mImageUri======="+mImageUri.toString());
//                ContentResolver mContentResolver =
//                        mContext.getContentResolver();
//                Cursor mCursor = mContentResolver.query(mImageUri, null, selection, selectionArgs, null);

                String DCIMPath = AppConsts.CAPTURE_PATH;

                ContentResolver mContentResolver = mContext.getApplicationContext().getContentResolver();
                Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA},
                        MediaStore.Images.Media.MIME_TYPE + "=? AND " + MediaStore.Images.Media.DATA + " like ? ",
                        new String[]{"image/jpeg", "%/MIX/capture/"  + "%"},
                        MediaStore.Images.Media.DATE_MODIFIED + " DESC"); // 降序排列


                // 利用Handler通知调用线程
                Message msg = mHandler.obtainMessage();
                msg.obj = mCursor;
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    //	 扫描完成后的回调
    public interface ScanCompleteCallBack {
        void scanComplete(Cursor cursor);
    }

}
