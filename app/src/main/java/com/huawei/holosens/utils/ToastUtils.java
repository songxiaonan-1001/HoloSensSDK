
package com.huawei.holosens.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.holobasic.utils.SuperCommonUtils;
import com.huawei.holosens.R;

/**
 * ToastUtils 工具类
 */
public class ToastUtils {

    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static final Object synObj = new Object();

    private ToastUtils() {
        throw new AssertionError();
    }

    public static void show(Context context, int resId) {
        showMessage(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        showMessage(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        showMessage(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, Object... args) {
        showMessage(context, String.format(context.getResources().getString(resId), args),
                Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String format, Object... args) {
        showMessage(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        showMessage(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        showMessage(context, String.format(format, args), duration);
    }

    private static void showMessage(final Context context, final CharSequence text,
                                    final int duration) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (synObj) {
//                    if (toast != null) {
//                        toast.setText(text);
//                        toast.setDuration(duration);
//                    } else {
//                        toast = Toast.makeText(context, text, duration);
//                    }
//                    toast.show();
//                }
//            }
//        });

        /*
         * 检查context
         */
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        // 判断线程, 进行消息处理
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showTxt(context, text, duration);
        } else {
            // 切到主线程进行Toast提示
            handler.post(new Runnable() {
                @Override
                public void run() {
                    showTxt(context, text, duration);
                }
            });
        }
    }
    
    public static void cancel(){
    	handler.removeCallbacksAndMessages(null);
    	if(toast != null){
    		toast.cancel();
    	}
    }

    private static void showTxt(Context context, CharSequence
            text, int duration) {
        /*
         * 创建并展示Toast
         */
        Context mContext = context.getApplicationContext();
        if (toast == null) {
            // 根据效果图自定义Toast样式
            View layout = LayoutInflater.from(mContext)
                    .inflate(R.layout.dialog_toast, null);
            TextView tip = (TextView) layout.findViewById(R.id
                    .tv_tip);
            // 调整提示内容的对齐方式
            if (text == null)
                return;
            doTipStyle(tip, text.toString());
            // 创建Toast
            toast = new Toast(mContext);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(duration);
            toast.setView(layout);
            toast.show();
            Log.e("ttttoast", "showTxt:1 " );
        } else {
            TextView tip = (TextView) toast.getView()
                    .findViewById(R.id
                            .tv_tip);
            // 调整提示内容的对齐方式
            if (text == null)
                return;
            doTipStyle(tip, text.toString());
            toast.setDuration(duration);
            toast.show();
            Log.e("ttttoast", "showTxt:2 " );
        }
    }

    /**
     * 设置提示的对齐样式
     */
    private static void doTipStyle(TextView content, String message) {
        // 计算字数
        long wordLen = SuperCommonUtils.calculateLength(message);
        // 超过30个字,首行缩进
        if (wordLen > 30) {
            content.setText("\u3000\u3000" + message);
        } else {
            content.setText(message);
        }
        // 超过10个字,切换成左对齐
        if (wordLen > 10) {
            content.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        } else {
            content.setGravity(Gravity.CENTER);
        }
    }

}
