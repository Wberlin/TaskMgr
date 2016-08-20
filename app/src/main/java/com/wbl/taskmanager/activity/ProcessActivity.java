package com.wbl.taskmanager.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wbl.taskmanager.R;
import com.wbl.taskmanager.adapter.ProcessAdapter;
import com.wbl.taskmanager.base.BaseActivity;
import com.wbl.taskmanager.models.ProcessInfo;
import com.wbl.taskmanager.models.AppInfo;
import com.wbl.taskmanager.models.ServiceInfo;

import java.util.ArrayList;

import java.util.List;

/**
 * 显示进程数量和进程详情
 * Created by djtao on 2016/8/19.
 */

public class ProcessActivity extends BaseActivity {

    private ActivityManager mActivityManager=null;
    private PackageManager mPackageManager=null;
    private List<ProcessInfo> processInfos=new ArrayList<>();
    private List<AppInfo> appInfos=new ArrayList<>();
    private List<ServiceInfo> serviceInfos=new ArrayList<>();
    private ListView lv;

    //所有进程数
    private TextView tvTotal;
    private ProcessAdapter mProcessAdapter;
    private MyAysTask myAysTask;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1://读取内存大小信息完成
                    mProcessAdapter.notifyDataSetChanged();
                    break;
                case 0:
                    break;
            }
        }
    };

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
        try {
            getRunningAppProcessInfo();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        myAysTask=new MyAysTask();
        myAysTask.execute();



        mProcessAdapter =new ProcessAdapter(this);
        mProcessAdapter.setList(processInfos);
        lv.setAdapter(mProcessAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        tvTotal.setText("当前系统进程共有"+processInfos.size());
    }
    //获得系统进程信息
    private void getRunningAppProcessInfo() throws PackageManager.NameNotFoundException{
        //通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo>
                appProcessList=mActivityManager.getRunningAppProcesses();
        List<ActivityManager.RunningServiceInfo> serviceInfoList=mActivityManager.getRunningServices(200);
        for(ActivityManager.RunningServiceInfo sInfo:serviceInfoList){
            ServiceInfo serviceInfo=new ServiceInfo();
            serviceInfo.setActivesince(sInfo.activeSince);
            serviceInfo.setStart(sInfo.started);
            serviceInfo.setPid(sInfo.pid);
            serviceInfo.setProcessname(sInfo.process);
            serviceInfo.setServicemessage(sInfo.service);
            serviceInfo.setLastactivitytime(sInfo.lastActivityTime);
            serviceInfos.add(serviceInfo);
        }

        for(ActivityManager.RunningAppProcessInfo appProcessInfo:appProcessList){
            ProcessInfo processInfo=new ProcessInfo();
            processInfo.setPid(appProcessInfo.pid);
            processInfo.setUid(appProcessInfo.uid);
            processInfo.setProcessName(appProcessInfo.processName);

//            Debug.MemoryInfo[] memoryInfo=
//                    mActivityManager.getProcessMemoryInfo(new int[]{appProcessInfo.pid});
//            // 获取进程占内存用信息 kb单位
//            //获取所在进程占用内存大小
//            int totalPrivateDirty=(memoryInfo[0].getTotalPss())*1024;
//            processInfo.setMemSize(Formatter.formatFileSize(this,totalPrivateDirty));

            processInfos.add(processInfo);
            // 获得每个进程里运行的应用程序(包),即每个应用程序的包名
            String[] packageList = appProcessInfo.pkgList;

            for (String pkg : packageList) {
                AppInfo appInfo=new AppInfo();
                PackageInfo packageInfo=mPackageManager.getPackageInfo(pkg,0);
                //获取应用图标
                Drawable icon=packageInfo.applicationInfo.loadIcon(mPackageManager);
                //获取到app的名字
                String appName=packageInfo.applicationInfo.loadLabel(mPackageManager).toString();
                appInfo.setAppLabel(appName);
                appInfo.setPkgName(appProcessInfo.processName);
                appInfo.setAppIcon(icon);


                //appInfo.setTime(df.format(SystemClock.elapsedRealtime()));
                processInfo.getAppInfoList().add(appInfo);

                appInfos.add(appInfo);
            }
        }

    }


    class MyAysTask extends AsyncTask<Void,Integer,Boolean>{

        public MyAysTask() {
            super();
        }

        //执行耗时的操作
        @Override
        protected Boolean doInBackground(Void... voids) {
            for(int i=0;i<processInfos.size();i++){

                Debug.MemoryInfo[] memoryInfo=mActivityManager.getProcessMemoryInfo(new int[]{processInfos.get(i).getPid()});
                int totalPrivateDirty=(memoryInfo[0].getTotalPss())*1024;
                processInfos.get(i).setMemSize(Formatter.formatFileSize(ProcessActivity.this,totalPrivateDirty));

            }
            return true;
        }

        //进度条更新
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        //执行完毕
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                mHandler.sendEmptyMessage(1);
            }
        }
    }

}
