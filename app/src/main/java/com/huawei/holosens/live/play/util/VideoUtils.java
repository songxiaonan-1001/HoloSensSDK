package com.huawei.holosens.live.play.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import java.io.File;

import androidx.core.content.FileProvider;

public class VideoUtils {

    /**
     * 获取视频截图
     *
     * @param filePath
     * @return
     */
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
            bitmap = Bitmap.createScaledBitmap(bitmap, 200,
                    200, true);
            return bitmap;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    /**
     * 获取视频/音频的时长
     *
     * @param filePath 文件路径
     * @return 时长 单位毫秒
     */
    public static int getVideoLongTime(String filePath) {
        int duration = 0;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(filePath); //在获取前，设置文件路径（应该只能是本地路径）
            String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            retriever.release(); //释放
            if (!TextUtils.isEmpty(durationStr)) {
                duration = Integer.valueOf(durationStr);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return duration;
    }

    /**
     * 尝试调起播放器播放视频
     *
     * @param context
     * @param path      视频地址
     */
    public static void tryPlayVideo(Context context, String path) {
        if (TextUtils.isEmpty(path) || !new File(path).exists()) return;

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        // 7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(path));
            intent.setDataAndType(uri, "video/mp4");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.parse(path);
            if (uri.getScheme() == null || uri.getScheme().isEmpty() || uri.getPath() == null || uri.getPath().isEmpty()) {
                uri = Uri.parse("file://" + path);
            }
            intent.setDataAndType(uri, "video/mp4");
        }

        ComponentName componentName = intent.resolveActivity
                (context.getPackageManager());
        if (componentName != null) {
            context.startActivity(intent);
        }
    }

}
