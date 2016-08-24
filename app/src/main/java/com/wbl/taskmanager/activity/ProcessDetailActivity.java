package com.wbl.taskmanager.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.Stat;
import com.jaredrummler.android.processes.models.Statm;
import com.wbl.taskmanager.R;
import com.wbl.taskmanager.base.BaseActivity;
import com.wbl.taskmanager.models.AppInfo;
import com.wbl.taskmanager.models.ProcessInfo;
import com.wbl.taskmanager.models.ServiceInfo;
import com.wbl.taskmanager.utils.BitmapUtil;
import com.wbl.taskmanager.utils.CalculateProcessMemorySize;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class ProcessDetailActivity extends BaseActivity {
    private ImageView tvAppIcon;//图标
    private TextView tvAppName;//
    private TextView tvServiceCount;
    private TextView tvMemSize;
    private TextView tvTimer;
    private ProcessInfo proInfo;
    private Intent intent;
    private int pid;
    private List<ServiceInfo> serviceInfos=new ArrayList<>();
    private Timer timer;

    private CalculateProcessMemorySize myAsynTask;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1://更新进程运行时间
                    if(serviceInfos.size()!=0){
                        tvTimer.setText(DateUtils.formatElapsedTime(new StringBuilder(128),(SystemClock.elapsedRealtime()-
                                serviceInfos.get(0).getActivesince())/1000));
                    }
                    break;
                case 0://计算进程占用内存大小
                    tvMemSize.setText(proInfo.getMemSize());
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_detail);
        try{
            intent=getIntent();
            Bundle bundle=intent.getExtras();
            pid= bundle.getInt("Pid");
        }catch (Exception e){
            e.printStackTrace();
        }
        initView();
        try {
            initDate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        timer=new Timer();
        if(serviceInfos.size()!=0){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(1);
                }
            },0,1000);

        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(timer!=null){
            timer.cancel();
        }
        super.onStop();
    }

    private void initDate() throws IOException {
        getRunningProcessInfo();
        myAsynTask=new CalculateProcessMemorySize(this);
        List<ProcessInfo> processInfos=new ArrayList<>();
        processInfos.add(proInfo);
        myAsynTask.execute(processInfos);
        myAsynTask.setAsynTaskFinishedListener(new CalculateProcessMemorySize.AsynTaskFinishedListener() {
            @Override
            public void refreshUI() {
                mHandler.sendEmptyMessage(0);
            }
        });
        if(proInfo.getAppInfoList().size()!=0){
            tvAppIcon.setImageBitmap(proInfo.getAppInfoList().get(0).getAppIcon());
            tvAppName.setText(proInfo.getAppInfoList().get(0).getAppLabel());
        }
        tvServiceCount.setText("共有"+serviceInfos.size()+"个服务");
        tvMemSize.setText(proInfo.getMemSize());

    }

    private void getRunningProcessInfo() throws IOException{
        ActivityManager am=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList=am.getRunningServices(200);
        for(ActivityManager.RunningServiceInfo sInfo:serviceInfoList){
            if(sInfo.pid==pid){
                ServiceInfo serviceInfo=new ServiceInfo();
                serviceInfo.setActivesince(sInfo.activeSince);
                serviceInfo.setStart(sInfo.started);
                serviceInfo.setPid(sInfo.pid);
                serviceInfo.setProcessname(sInfo.process);
                serviceInfo.setServicemessage(sInfo.service);
                serviceInfo.setServicename(sInfo.service.getClassName());
                serviceInfo.setLastactivitytime(sInfo.lastActivityTime);
                serviceInfos.add(serviceInfo);
            }

        }
        PackageManager pm=getPackageManager();
        List<AndroidAppProcess> processes= AndroidProcesses.getRunningAppProcesses();
        if(processes!=null&&processes.size()!=0){
        for(AndroidAppProcess process:processes){
            if(process.pid==pid){
                proInfo=new ProcessInfo();
                Stat stat=process.stat();

                PackageInfo packageInfo=null;
                try {
                    packageInfo=process.getPackageInfo(this,0);

                }catch (PackageManager.NameNotFoundException e){
                    e.printStackTrace();
                }
                proInfo.setProcessName(process.name);
                proInfo.setPid(stat.getPid());
                proInfo.setTime(DateUtils.formatElapsedTime(new StringBuilder(128),stat.stime()));
                if(packageInfo!=null){
                    List<AppInfo> appInfoList=new ArrayList<>();
                    AppInfo appInfo=new AppInfo();
                    //获取到app的名字
                    String appName=packageInfo.applicationInfo.loadLabel(pm).toString();
                    appInfo.setAppLabel(appName);

                    //获取应用图标
                    Drawable icon=packageInfo.applicationInfo.loadIcon(pm);
                    appInfo.setAppIcon(BitmapUtil.drawableToBitmap(icon));
                    appInfo.setPkgName(packageInfo.packageName);
                   // appInfo.setTime(sf.format(stat.stime()));
                    appInfoList.add(appInfo);
                    proInfo.setAppInfoList(appInfoList);
                }
            }


        }

        }

    }

    private void initView() {
        tvAppIcon=(ImageView)findViewById(R.id.detail_icon);
        tvAppName=(TextView)findViewById(R.id.detail_title);
        tvServiceCount=(TextView)findViewById(R.id.detail_service_count);
        tvMemSize=(TextView)findViewById(R.id.detail_process_size);
        tvTimer=(TextView)findViewById(R.id.detail_process_timer);
    }
}
