package com.huawei.holosens.utils;

import android.view.View;
import android.view.animation.TranslateAnimation;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.utils
 * @ClassName: AnimUtil
 * @Description: java类作用描述
 * @CreateDate: 2020-01-08 15:40
 * @Version: 1.0
 */
public class AnimUtil {

    public static void showFromBotton(final View view){
        final TranslateAnimation ctrlAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 1, TranslateAnimation.RELATIVE_TO_SELF, 0);
        ctrlAnimation.setDuration(200L);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                view.startAnimation(ctrlAnimation);
            }
        }, 0);
    }

    public static void hiddenToBottom(final View view){
        final TranslateAnimation ctrlAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 1);
        ctrlAnimation.setDuration(200L);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
                view.startAnimation(ctrlAnimation);
            }
        }, 0);
    }

}
