package com.wbl.taskmanager.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;


/**
 * 添加有颜色的字体
 * Created by djtao on 2016/8/19.
 */

public class TextColorUtil {
    /**
     *
     * @param view
     * @param content
     * @param color
     */
    public static void setTextHasColor(TextView view,String content,int color){
        SpannableString span=new SpannableString(content);
        span.setSpan(new ForegroundColorSpan(
                GetResourcesUtil.getColor(view,color)),
                0,span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.append(span);
    }
}
