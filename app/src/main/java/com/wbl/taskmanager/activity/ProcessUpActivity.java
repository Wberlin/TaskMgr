package com.wbl.taskmanager.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.AndroidProcess;
import com.jaredrummler.android.processes.models.Stat;
import com.jaredrummler.android.processes.models.Statm;
import com.wbl.taskmanager.R;
import com.wbl.taskmanager.adapter.GridView2Adapter;
import com.wbl.taskmanager.adapter.GridViewAdapter;
import com.wbl.taskmanager.base.BaseActivity;
import com.wbl.taskmanager.models.AppInfo;
import com.wbl.taskmanager.models.ProcessInfo;
import com.wbl.taskmanager.models.ServiceInfo;
import com.wbl.taskmanager.utils.AssociateProcessToService;
import com.wbl.taskmanager.utils.CalculateProcessMemorySize;
import com.wbl.taskmanager.utils.SystemUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * android5.1版本以上获取正在运行的程序
 * Created by djtao on 2016/8/22.
 */

public class ProcessUpActivity extends BaseActivity{
    //所有进程数
    private TextView tvTotal;//系统进程总个数
    private TextView tvTotalMem;//系统总内存
    private TextView tvAvaibleMem;//系统可用内存

    private GridView gv; //进程列表
    private GridView gv2;//服务列表
    private List<ProcessInfo> processInfos=new ArrayList<>();
    private List<ServiceInfo> serviceInfos=new ArrayList<>();
    private GridViewAdapter adapter;//进程适配器
    private GridView2Adapter adapter2;//服务适配器
    private List<AndroidAppProcess> processes=null;

    private PackageManager pm;
    private ActivityManager am;
    private CalculateProcessMemorySize myAysTask;//计算进程所占内存大小的异步类
    private AssociateProcessToService aeAysTask;//关联进程与服务器
    //实时显示可用内存信息
    private Timer mTimer;

    //可侧滑布局
    private SwipeLayout mSwipeLayout;

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
                case 1://读取进程所占内存大小信息完成
                    adapter.notifyDataSetChanged();
                    break;
                case 0://监测系统内存变化
                    YoYo.with(Techniques.Tada).duration(500).playOn(tvAvaibleMem);
                    YoYo.with(Techniques.Tada).duration(500).playOn(tvTotalMem);
                    tvAvaibleMem.setText(SystemUtil.getSystemAvaiableMemorySize(ProcessUpActivity.this));
                    tvTotalMem.setText(SystemUtil.getSystemAllMemorySize(ProcessUpActivity.this));
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        tvTotal=(TextView)findViewById(R.id.process_total);
        tvTotalMem=(TextView)findViewById(R.id.process_tv_total_size);
        tvAvaibleMem=(TextView)findViewById(R.id.process_tv_avaible_size);
        gv=(GridView)findViewById(R.id.process_gv);
        gv2=(GridView)findViewById(R.id.process_gv_service);
        mSwipeLayout=(SwipeLayout)findViewById(R.id.process_swipe);



        pm=getPackageManager();
        am=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        try {
            getRunningAppInfo();
        } catch (IOException e){
            e.printStackTrace();
        }

        adapter=new GridViewAdapter(this,processInfos);
        adapter.setMode(Attributes.Mode.Multiple);
        gv.setAdapter(adapter);

        adapter2=new GridView2Adapter(this,serviceInfos);
        adapter2.setMode(Attributes.Mode.Multiple);
        gv2.setAdapter(adapter2);



        myAysTask=new CalculateProcessMemorySize(this);
        myAysTask.execute(processInfos);
        myAysTask.setAsynTaskFinishedListener(new CalculateProcessMemorySize.AsynTaskFinishedListener() {
            @Override
            public void refreshUI() {
                mHandler.sendEmptyMessage(1);
            }
        });

        aeAysTask=new AssociateProcessToService(processInfos,serviceInfos);
        aeAysTask.execute();
        aeAysTask.setAssociateFinishedListener(new AssociateProcessToService.AssociateFinishedListener() {
            @Override
            public void refreshUI() {
                mHandler.sendEmptyMessage(2);
            }
        });

        tvTotal.setText("当前系统进程共有："+processInfos.size());
        tvAvaibleMem.setText(SystemUtil.getSystemAvaiableMemorySize(this));
        tvTotalMem.setText(SystemUtil.getSystemAllMemorySize(this));


        initListener();



    }
    //初始化监听器
    private void initListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG","GridView->ItemClick");
            }
        });
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG","GridView->ItemLongClick");

                return false;
            }
        });


        mSwipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                //Log.e("TAG","swipe->onStartOpen");
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //Log.e("TAG","swipe->onOpen");
                tvTotal.setText("当前运行中的服务共有："+serviceInfos.size());
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                //Log.e("TAG","swipe->onStartClose");
            }

            @Override
            public void onClose(SwipeLayout layout) {
                // Log.e("TAG","swipe->onClose");
                tvTotal.setText("当前运行中的系统进程共有："+processInfos.size());
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //Log.e("TAG","swipe->onUpdate");
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //Log.e("TAG","swipe->onHandRelease");
            }
        });

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

    private void getRunningAppInfo() throws IOException{
        List<ActivityManager.RunningServiceInfo> serviceInfoList=am.getRunningServices(200);
        for(ActivityManager.RunningServiceInfo sInfo:serviceInfoList){
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
        processes= AndroidProcesses.getRunningAppProcesses();
        if(processes!=null&&processes.size()!=0){
            for(AndroidAppProcess process:processes){
                ProcessInfo proInfo=new ProcessInfo();
                Stat stat=process.stat();
                Statm statm=process.statm();
                PackageInfo packageInfo=null;

                try {
                    packageInfo=process.getPackageInfo(this,0);
                }catch (PackageManager.NameNotFoundException e){
                    e.printStackTrace();
                }



                proInfo.setProcessName(process.name);
                proInfo.setPid(stat.getPid());
                SimpleDateFormat sf=new SimpleDateFormat("###:00", Locale.CHINA);
                proInfo.setTime(sf.format(stat.stime()));
                if(packageInfo!=null){
                    List<AppInfo> appInfoList=new ArrayList<>();
                    AppInfo appInfo=new AppInfo();
                    //获取到app的名字
                    String appName=packageInfo.applicationInfo.loadLabel(pm).toString();
                    appInfo.setAppLabel(appName);

                    //获取应用图标
                    Drawable icon=packageInfo.applicationInfo.loadIcon(pm);
                    appInfo.setAppIcon(((BitmapDrawable)icon).getBitmap());
                    appInfo.setPkgName(packageInfo.packageName);
                    appInfo.setProcessInfo(proInfo);
                    appInfo.setTime(sf.format(stat.stime()));
                    appInfoList.add(appInfo);
                    proInfo.setAppInfoList(appInfoList);
                }
                processInfos.add(proInfo);
            }
        }

    }
}
