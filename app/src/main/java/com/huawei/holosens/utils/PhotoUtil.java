package com.huawei.holosens.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.utils
 * @ClassName: PhotoUtil
 * @Description: java类作用描述
 * @CreateDate: 2020-01-08 22:51
 * @Version: 1.0
 */
public class PhotoUtil {

    /**
     * 压缩并且保存图片
     * @param filePath
     * @return
     */
    public static String compressBitmapToFile(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //读取图片到ByteArrayOutputStream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos); //参数如果为100那么就不压缩
        byte[] bytes = baos.toByteArray();

        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

}
