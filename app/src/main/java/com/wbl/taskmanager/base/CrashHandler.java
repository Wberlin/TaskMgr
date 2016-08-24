package com.wbl.taskmanager.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;


import com.wbl.taskmanager.services.CrashService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by djtao on 2016/8/16.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG="CrashHandler";
    private static final boolean DEBUG=true;
    private static final String PATH= Environment.getExternalStorageDirectory().getPath()
            +"/wbl/log/";
    private static  final String FILE_NAME="crash";
    private static final String FILE_NAME_SUFFIX=".trace";
    private static CrashHandler sInstance=new CrashHandler();

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private CrashHandler(){

    }

    public static CrashHandler getInstance(){
        return sInstance;
    }
    public void init(Context context){
        Intent intent=new Intent(context, CrashService.class);
        context.startService(intent);
        mDefaultCrashHandler=Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext=context.getApplicationContext();
    }



    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
            try{
                //导出异常信息到SD卡中
                dumpExceptionToSDCard(throwable);
                //这里可以上传一次信息到服务器，便于开发人员分析日志从而解决bug
                uploadExceptionToServer();
            }catch (IOException e){
                e.printStackTrace();
            }

            throwable.printStackTrace();
            if(mDefaultCrashHandler!=null){
                mDefaultCrashHandler.uncaughtException(thread,throwable);
            }else{
                Process.killProcess(Process.myPid());
            }
    }


    private void dumpExceptionToSDCard(Throwable ex) throws IOException{
        //如果SD卡布存在或者无法使用，则无法把异常信息写入SD卡
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            if(DEBUG){
                Log.w(TAG,"sdcard unmounted,skip dump exception");
                return;
            }
        }

        File dir=new File(PATH);
        if(!dir.exists()){
            dir.mkdirs();
        }

        long current=System.currentTimeMillis();
        String time=new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(current));
        File file=new File(PATH+FILE_NAME+time+FILE_NAME_SUFFIX);
        try{
            PrintWriter pw=null;
            if(file.exists())
                pw=new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
            else
                pw=new PrintWriter(new BufferedWriter(new FileWriter(file)));


            pw.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.CHINA).format(new Date(current)));
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.println();
            pw.close();
        }catch (Exception e){
            Log.e(TAG,"dump crash info failed");
            Process.killProcess(Process.myPid());
        }
    }


    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException{
        PackageManager pm=mContext.getPackageManager();
        PackageInfo pi=pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);


        //Android版本号
        pw.print("OS Version：");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);


        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);


        //CPU架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);


    }


    private void uploadExceptionToServer(){

    }

}
