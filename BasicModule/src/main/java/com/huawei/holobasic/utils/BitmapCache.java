package com.huawei.holobasic.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Video;
import android.util.Log;


import com.huawei.holobasic.consts.AppConsts;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

//http://www.360doc.com/content/14/1112/14/20361811_424545444.shtml

/**
 * @author 该类用于图片缓存，防止内存溢出
 */
@SuppressWarnings("unchecked")
public class BitmapCache {
    static BitmapCache bitmapCache;
    /**
     * 用于Chche内容的存储
     */
    Hashtable bitmapRefs;
    /**
     * 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中）
     */
    ReferenceQueue q;

    public BitmapCache() {
        bitmapRefs = new Hashtable();
        q = new ReferenceQueue();
    }

    /**
     * 取得缓存器实例
     */
    public static BitmapCache getInstance() {
        if (bitmapCache == null) {
            bitmapCache = new BitmapCache();
        }
        return bitmapCache;

    }

    /**
     * 按照路径加载图片
     *
     * @param path     图片资源的存放路径
     * @param scalSize 缩小的倍数
     * @return
     */
    public static Bitmap loadImageBitmap(String path, int scalSize) {
        Log.e("loadBitmap-from-local", path);
        if (-1 == scalSize) {
            return BitmapFactory.decodeFile(path);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = scalSize;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 获取视频缩略图
     *
     * @param filePath
     * @return
     */
    public static Bitmap loadVideoBitmap(String filePath) {
        return ThumbnailUtils.createVideoThumbnail(filePath,
                Video.Thumbnails.MINI_KIND);
    }

    /**
     * 获取视频缩略图
     *
     * @return
     */
    public static Bitmap decodeVideoThumbnail(String path, int width,
                                              int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            bitmap = retriever.getFrameAtTime();
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
        if (bitmap != null) {
            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int id,
                                                         int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(resources, id, options);

    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    // "http://imgsrc.baidu.com/forum/pic/item/b2738bd49f8fd32da18bb7a4.jpg"
    // 声明称为静态变量有助于调用
    public static int saveToLocal(String path, String imgUrl, String fileName) {
        int responseCode;
        try {
            FileUtil.createDirectory(path);
            String fullPath;
            if (fileName.contains(AppConsts.IMAGE_JPG_KIND)) {
                fullPath = path + fileName;
            } else {
                fullPath = path + fileName + AppConsts.IMAGE_JPG_KIND;
            }
            File adFile = new File(fullPath);
            adFile.createNewFile();
            FileOutputStream outStream = new FileOutputStream(adFile);
            URL url = new URL(imgUrl);

            // 记住使用的是HttpURLConnection类
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            responseCode = conn.getResponseCode();
            // 如果运行超过5秒会自动失效这是android规定
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            byte[] data = readStream(inStream);// 调用readStream方法
            outStream.write(data);

            // 关闭流的这个地方需要完善一下
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            responseCode = -1;
        }
        if (200 == responseCode) {
            responseCode = 0;
        }
        return responseCode;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {

        // 把数据读取存放到内存中去
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 以软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
     */
    public void addCacheBitmap(Bitmap bmp, String key) {
        cleanCache();// 清除垃圾引用
        BtimapRef ref = new BtimapRef(bmp, q, key);
        bitmapRefs.put(key, ref);
    }

    /**
     * 移除一个软引用
     */
    public void removeCacheBitmap(String key) {
        cleanCache();// 清除垃圾引用
        bitmapRefs.remove(key);
    }

    /**
     * 依据所指定的drawable下的图片资源ID号（可以根据自己的需要从网络或本地path下获取），重新获取相应Bitmap对象的实例
     */
    public Bitmap getBitmap(String path, String kind, String fileName) {
        Bitmap bmp = null;
        // 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
        if (bitmapRefs.containsKey(path)) {
            BtimapRef ref = (BtimapRef) bitmapRefs.get(path);
            bmp = (Bitmap) ref.get();
        }
        // 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
        // 并保存对这个新建实例的软引用
        if (bmp == null) {
            if ("image".equalsIgnoreCase(kind)) {
                bmp = loadImageBitmap(path, -1);// BitmapFactory.decodeResource(context.getResources(),
                this.addCacheBitmap(bmp, path);
            } else if (("video".equalsIgnoreCase(kind))
                    || ("downVideo".equalsIgnoreCase(kind))) {
                bmp = loadVideoBitmap(path);
                this.addCacheBitmap(bmp, path);
            } else if ("net".equalsIgnoreCase(kind)) {
                File file = new File(AppConsts.AD_PATH + fileName
                        + AppConsts.IMAGE_JPG_KIND);

                if (file.length() <= 1) {
                    file.delete();
                }

                if (file.isFile() && file.exists()) {
                    bmp = loadImageBitmap(file.getAbsolutePath(), -1);
                } else {
                    bmp = loadNetBitmap(path);
                    saveToLocal(AppConsts.AD_PATH, path, fileName);
                }
                this.addCacheBitmap(bmp, path);
            } else if ("welcome".equalsIgnoreCase(kind)) {
                File file = new File(AppConsts.WELCOME_IMG_PATH + fileName
                        + AppConsts.IMAGE_JPG_KIND);

                if (file.length() <= 1) {
                    file.delete();
                }

                if (file.isFile() && file.exists()) {
                    bmp = loadImageBitmap(file.getAbsolutePath(), -1);
                    this.addCacheBitmap(bmp, path);
                } else {
                    bmp = loadNetBitmap(path);
                    saveToLocal(AppConsts.WELCOME_IMG_PATH, path, fileName);
                }
            }

        }
        return bmp;
    }

    /**
     * 依据所指定的drawable下的图片资源ID号（可以根据自己的需要从网络或本地path下获取），重新获取相应Bitmap对象的实例
     */
    public Bitmap getCacheBitmap(String path) {
        Bitmap bmp = null;
        // 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
        if (bitmapRefs.containsKey(path)) {
            BtimapRef ref = (BtimapRef) bitmapRefs.get(path);
            bmp = (Bitmap) ref.get();
        }
        return bmp;
    }

    public Bitmap loadNetBitmap(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            }
            Log.e("loadBitmap-from-net", path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void cleanCache() {
        BtimapRef ref;
        while ((ref = (BtimapRef) q.poll()) != null) {
            if (!ref._key.startsWith(AppConsts.AD_PATH)) {
                bitmapRefs.remove(ref._key);
            }
        }
    }

    // 清除Cache内的全部内容
    public void clearCache() {
        cleanCache();
        System.gc();
        System.runFinalization();
    }

    // 清除Cache内的全部内容
    public void clearAllCache() {
        cleanCache();
        bitmapRefs.clear();
        System.gc();
        System.runFinalization();
    }

    /**
     * 继承SoftReference，使得每一个实例都具有可识别的标识。
     */
    class BtimapRef extends SoftReference {
        String _key;

        public BtimapRef(Bitmap bmp, ReferenceQueue q, String key) {
            super(bmp, q);
            _key = key;
        }
    }

}
