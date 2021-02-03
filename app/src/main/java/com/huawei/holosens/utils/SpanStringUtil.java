package com.huawei.holosens.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.ColorInt;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.utils
 * @ClassName: SpanStringUtil
 * @Description: java类作用描述
 * @CreateDate: 2020-01-09 15:58
 * @Version: 1.0
 */
public class SpanStringUtil {

    /**
     *
     * @param str 全部的字符串
     * @param needHighStr   需要高亮的字符串
     * @return  这里默认高亮的是红色，需要自定义时，再添加重载的方法吧
     * 两个方法亲测都可以的，只是这个方法看着高大上一点
     */
    public static Spanned highStr(Context mContext, String str, String needHighStr, int color){
        SpannableString s = new SpannableString(str);
        
        Pattern pSplit = Pattern.compile("/");
        Matcher mSplit = pSplit.matcher(s);
        int spliteStart = 0;
        while (mSplit.find()){
            spliteStart = mSplit.start();
        }
        
        Pattern p = Pattern.compile(needHighStr);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            if(start < spliteStart)
                s.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    /**
     * @method keywordColor
     * @description 改变字符串中出现的关键字的颜色
     * @params keyword
     * @params content
     * @params textColor
     * @return SpannableString
     */
    public static SpannableString highLightKeyword(String keyword, String content, @ColorInt int textColor) {

        SpannableString spannableString = new SpannableString(content);
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(content)) {
            return spannableString;
        }

        String temp_content = content;
        int startNew;
        int startOld = 0;
        while (temp_content.contains(keyword)) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(textColor);
            spannableString.setSpan(colorSpan, startOld + temp_content.indexOf(keyword), startOld + temp_content.indexOf(keyword) + keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            startNew = temp_content.indexOf(keyword) + keyword.length();
            startOld += startNew;
            temp_content = temp_content.substring(startNew);
        }

        return spannableString;

    }
}
