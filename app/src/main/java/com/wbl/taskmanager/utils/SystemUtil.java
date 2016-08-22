package com.wbl.taskmanager.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.text.format.Formatter;

import com.wbl.taskmanager.activity.MainActivity;

/**
 * Created by djtao on 2016/8/22.
 */

public class SystemUtil {

    //获取系统总内存信息
    public static String getSystemAllMemorySize(Context context){
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        ActivityManager am=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        am.getMemoryInfo(memoryInfo);
        long memSize=0;
        if(Build.VERSION.SDK_INT>16)
            memSize=memoryInfo.totalMem;
        else{
            //默认为2GB
            memSize=1024000000*2;
        }
        return formateFileSize(context,memSize);
    }
    //调用系统函数，字符串转换 long -String KB/MB
    public static String formateFileSize(Context context,long size){
        return Formatter.formatFileSize(context, size);
    }

    //获取系统可用内存信息
    public static String getSystemAvaiableMemorySize(Context context){
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        ActivityManager am=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        am.getMemoryInfo(memoryInfo);
        //系统可用内存
        long memSize=memoryInfo.availMem;

        return formateFileSize(context,memSize);

    }
}
