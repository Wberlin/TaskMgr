package com.wbl.taskmanager.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.format.Formatter;

import com.wbl.taskmanager.activity.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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


    //获取当前运行进程的名称
    public static String getProcessName(){
        try{
            File file=new File(
                    "/proc/"+android.os.Process.myPid()+"/"+"cmdline");
            BufferedReader mBufferedReader=new BufferedReader(new FileReader(file));
            String processName=mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //判断是否是系统应用
    public static boolean isSystemApp(PackageInfo pInfo){
        return ((pInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)!=0);
    }

    //判断是否是系统更新应用
    public static boolean isSystemUpdateApp(PackageInfo pInfo){
        return ((pInfo.applicationInfo.flags&ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!=0);
    }

    //判断是否是用户应用
    public static boolean isUserApp(PackageInfo pInfo){
        return (!isSystemApp(pInfo)&&(!isSystemUpdateApp(pInfo)));
    }
}
