package com.wbl.taskmanager.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.wbl.taskmanager.R;
import com.wbl.taskmanager.adapter.ProcessAdapter;
import com.wbl.taskmanager.base.BaseActivity;
import com.wbl.taskmanager.models.ProcessInfo;
import com.wbl.taskmanager.models.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 显示进程数量和进程详情
 * Created by djtao on 2016/8/19.
 */

public class ProcessActivity extends BaseActivity {

    private ActivityManager mActivityManager=null;
    private PackageManager mPackageManager=null;
    private List<ProcessInfo> processInfos=new ArrayList<>();
    private List<AppInfo> appInfos=new ArrayList<>();
    private ListView lv;
    //所有进程数
    private TextView tvTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_process);
        lv=(ListView)findViewById(R.id.process_lv);
        tvTotal=(TextView)findViewById(R.id.process_total);
        // 获得ActivityManager服务的对象
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //获取包管理器对象
        mPackageManager=getPackageManager();


        //获取系统进程信息
        getRunningAppProcessInfo();
        ProcessAdapter mProcessAdapter=new ProcessAdapter(this);
        mProcessAdapter.setList(processInfos);
        lv.setAdapter(mProcessAdapter);
        tvTotal.setText("当前系统进程共有"+processInfos.size());
    }
    //获得系统进程信息
    private void getRunningAppProcessInfo() {
        processInfos=new ArrayList<>();

        //通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo>
                appProcessList=mActivityManager.getRunningAppProcesses();

        // 保存所有正在运行的包名 以及它所在的进程信息
        Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap =
                new HashMap<>();

        for(ActivityManager.RunningAppProcessInfo appProcessInfo:appProcessList){
            ProcessInfo processInfo=new ProcessInfo();
            processInfo.setPid(appProcessInfo.pid);
            processInfo.setUid(appProcessInfo.uid);
            processInfo.setProcessName(appProcessInfo.processName);

            Debug.MemoryInfo[] memoryInfo=
                    mActivityManager.getProcessMemoryInfo(new int[]{appProcessInfo.pid});
            // 获取进程占内存用信息 kb单位
            processInfo.setMemSize(memoryInfo[0].dalvikPrivateDirty);

            processInfos.add(processInfo);
            // 获得每个进程里运行的应用程序(包),即每个应用程序的包名
            String[] packageList = appProcessInfo.pkgList;

            for (String pkg : packageList) {
                pgkProcessAppMap.put(pkg,appProcessInfo);
            }
        }
        // 保存所有正在运行的应用程序信息
        List<AppInfo> runningAppInfos=new ArrayList<>();

        Intent mainIntent=new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //通过查询，获取所有ResolveInfos对象
        List<ResolveInfo> resolveInfos=mPackageManager
                .queryIntentActivities(mainIntent,PackageManager.MATCH_DEFAULT_ONLY);
        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(mPackageManager));
        for(ResolveInfo reInfo:resolveInfos){
            //获取该packageName的pid和processName
            if(pgkProcessAppMap.containsKey(reInfo.activityInfo.packageName)){

            }
        }

    }




}
