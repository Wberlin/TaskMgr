package com.wbl.taskmanager.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.util.Attributes;
import com.wbl.taskmanager.R;
import com.wbl.taskmanager.adapter.GridView2Adapter;
import com.wbl.taskmanager.adapter.GridViewAdapter;
import com.wbl.taskmanager.base.BaseActivity;
import com.wbl.taskmanager.models.ProcessInfo;
import com.wbl.taskmanager.models.AppInfo;
import com.wbl.taskmanager.models.ServiceInfo;
import com.wbl.taskmanager.utils.AssociateProcessToService;
import com.wbl.taskmanager.utils.BitmapUtil;
import com.wbl.taskmanager.utils.CalculateProcessMemorySize;
import com.wbl.taskmanager.utils.SystemUtil;
import com.wbl.taskmanager.utils.aysntask.onFinishListener;

import java.util.ArrayList;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


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
    private GridView gv; //进程列表
    private GridView gv2;//服务列表
    private GridViewAdapter adapter;//进程适配器
    private GridView2Adapter adapter2;//服务适配器
    //所有进程数
    private TextView tvTotal;
    private TextView tvTotalMem;//系统总内存
    private TextView tvAvaibleMem;//系统可用内存


    //实时显示可用内存信息
    private Timer mTimer;
    private CalculateProcessMemorySize myAysTask;
    private AssociateProcessToService aeAysTask;//关联进程与服务器
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 3://每秒刷新一次
                    adapter.notifyDataSetChanged();
                    break;
                case 2://关联服务与程序信息完成
                    adapter.notifyDataSetChanged();
                    break;
                case 1://读取内存大小信息完成
                    adapter.notifyDataSetChanged();
                    break;
                case 0://监测系统内存变化
                    YoYo.with(Techniques.Tada).duration(500).playOn(tvAvaibleMem);
                    YoYo.with(Techniques.Tada).duration(500).playOn(tvTotalMem);
                    tvAvaibleMem.setText(SystemUtil.getSystemAvaiableMemorySize(ProcessActivity.this));
                    tvTotalMem.setText(SystemUtil.getSystemAllMemorySize(ProcessActivity.this));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_process);
        gv=(GridView)findViewById(R.id.process_gv);
        //gv2=(GridView)findViewById(R.id.process_gv_service);
        tvTotal=(TextView)findViewById(R.id.process_total);
        tvAvaibleMem=(TextView)findViewById(R.id.process_tv_avaible_size);
        tvTotalMem=(TextView)findViewById(R.id.process_tv_total_size);

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

        myAysTask=new CalculateProcessMemorySize(this);
        myAysTask.execute(processInfos);
        myAysTask.setOnFinishedListener(new onFinishListener() {
            @Override
            public void onFinish(boolean isfinish) {
                mHandler.sendEmptyMessage(1);
            }
        });
        aeAysTask=new AssociateProcessToService(processInfos,serviceInfos);
        aeAysTask.execute();
        aeAysTask.setOnFinishedListener(new onFinishListener() {
            @Override
            public void onFinish(boolean isfinish) {
                mHandler.sendEmptyMessage(2);
            }
        });

        adapter=new GridViewAdapter(this,processInfos);
        adapter.setMode(Attributes.Mode.Multiple);
        gv.setAdapter(adapter);
        adapter2=new GridView2Adapter(this,serviceInfos);
        adapter2.setMode(Attributes.Mode.Multiple);
        gv2.setAdapter(adapter2);
        tvTotal.setText("当前系统进程共有"+processInfos.size());
    }

    @Override
    protected void onStart() {
        mTimer=new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        },0,5000);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(3);
            }
        },0,1000);
        super.onStart();

    }

    @Override
    protected void onStop() {
        if(mTimer!=null)
            mTimer.cancel();
        super.onStop();

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
            serviceInfo.setServicename(sInfo.service.getClassName());
            serviceInfos.add(serviceInfo);
        }

        for(ActivityManager.RunningAppProcessInfo appProcessInfo:appProcessList){
            ProcessInfo processInfo=new ProcessInfo();
            processInfo.setPid(appProcessInfo.pid);
            processInfo.setUid(appProcessInfo.uid);
            processInfo.setProcessName(appProcessInfo.processName);

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
                appInfo.setAppIcon(BitmapUtil.drawableToBitmap(icon));


                //appInfo.setTime(df.format(SystemClock.elapsedRealtime()));
                processInfo.getAppInfoList().add(appInfo);

                appInfos.add(appInfo);
            }
        }

    }




}
