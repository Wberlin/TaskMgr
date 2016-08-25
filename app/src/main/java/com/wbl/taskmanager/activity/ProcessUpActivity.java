package com.wbl.taskmanager.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import com.wbl.taskmanager.utils.BitmapUtil;
import com.wbl.taskmanager.utils.CalculateProcessMemorySize;
import com.wbl.taskmanager.utils.SystemUtil;
import com.wbl.taskmanager.utils.aysntask.onFinishListener;
import com.wbl.taskmanager.view.RoundProgressBarWidthNumber;

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
    private List<ProcessInfo> processInfos=new ArrayList<>();
    private List<ServiceInfo> serviceInfos=new ArrayList<>();
    private GridViewAdapter adapter;//进程适配器
    private List<AndroidAppProcess> processes=null;

    private PackageManager pm;
    private ActivityManager am;
    private CalculateProcessMemorySize myAysTask;//计算进程所占内存大小的异步类
    private AssociateProcessToService aeAysTask;//关联进程与服务器
    private ProcessAysnTask proAysTask;//获取所有进程信息
    //实时显示可用内存信息
    private Timer mTimer;
    private RoundProgressBarWidthNumber mProgressBar;

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
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }

    }


    private void initView() {
        tvTotal=(TextView)findViewById(R.id.process_total);
        tvTotalMem=(TextView)findViewById(R.id.process_tv_total_size);
        tvAvaibleMem=(TextView)findViewById(R.id.process_tv_avaible_size);
        gv=(GridView)findViewById(R.id.process_gv);
        mProgressBar=(RoundProgressBarWidthNumber)findViewById(R.id.process_progessbar);

    }
    private void initData() {
        pm=getPackageManager();
        am=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        mTimer=new Timer();
        proAysTask=new ProcessAysnTask();
        proAysTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        proAysTask.setOnFinishLisener(new onFinishListener() {
            @Override
            public void onFinish(boolean isfinish) {

                if(!isfinish){
                    mProgressBar.setVisibility(View.GONE);
                    return;
                }
                if(processInfos!=null){
                    tvTotal.setText("当前系统进程共有："+processInfos.size());
                    gv.setVisibility(View.VISIBLE);
                    adapter=new GridViewAdapter(ProcessUpActivity.this,processInfos);
                    adapter.setMode(Attributes.Mode.Multiple);
                    gv.setAdapter(adapter);
                    myAysTask=new CalculateProcessMemorySize(ProcessUpActivity.this);
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
                    if(mTimer!=null){
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                mHandler.sendEmptyMessage(3);
                            }
                        },0,1000);
                    }

                }
            }
        });




        tvAvaibleMem.setText(SystemUtil.getSystemAvaiableMemorySize(this));
        tvTotalMem.setText(SystemUtil.getSystemAllMemorySize(this));
    }
    //初始化监听器
    private void initListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG","GridView->ItemClick");
                Bundle bundle=new Bundle();
                bundle.putInt("Pid",processInfos.get(i).getPid());
                openActivity(ProcessDetailActivity.class,bundle);
            }
        });
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG","GridView->ItemLongClick");

                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        try{
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            },0,5000);
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    private void getRunningAppInfo() throws IOException{
        List<ActivityManager.RunningServiceInfo> serviceInfoList=am.getRunningServices(200);
        for(int i=0;i<serviceInfoList.size();i++){
            ActivityManager.RunningServiceInfo sInfo=serviceInfoList.get(i);
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
            for(int i=0;i<processes.size();i++){
                AndroidAppProcess process=processes.get(i);
                ProcessInfo proInfo=new ProcessInfo();
                Stat stat=process.stat();
                PackageInfo packageInfo=null;
                try {
                    packageInfo=process.getPackageInfo(this,0);
                }catch (PackageManager.NameNotFoundException e){
                    e.printStackTrace();
                }
                proInfo.setProcessName(process.name);
                proInfo.setPid(stat.getPid());
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
                    appInfoList.add(appInfo);
                    proInfo.setAppInfoList(appInfoList);
                }
                processInfos.add(proInfo);
               // proAysTask.setProgress((int)((i*1.0f/processes.size()*48)+55));
            }

        }

    }


    public class ProcessAysnTask extends AsyncTask<Void,Integer,Boolean>{
        private int progress=-1;
        private long starttime;
        private boolean isStart;
        onFinishListener listener;

        private void setOnFinishLisener(onFinishListener lisener){
            this.listener=lisener;
        }
        public ProcessAysnTask() {
            super();
        }
        @Override
        protected void onPreExecute() {
           progress=0;
            isStart=true;
           starttime=SystemClock.currentThreadTimeMillis();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            setProgress(0);
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(isStart){
                            if(proAysTask.getProgress()<=100){
                                try {
                                    Thread.sleep(11);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                proAysTask.setProgress(proAysTask.getProgress()+1);

                                //Log.e("TAG","process:"+proAysTask.getProgress());
                            }
                        }
                    }
                }).start();
                getRunningAppInfo();

            } catch (IOException e){
                e.printStackTrace();
                setProgress((int)((SystemClock.currentThreadTimeMillis()-starttime)/1200*100));
                isStart=false;
                return false;
            }

            isStart=false;
            return true;
        }



        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
            if(values[0]>=100){
                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            /////////////////////
            if(listener!=null)
            listener.onFinish(aBoolean);

        }


        private void setProgress(int progress){
            this.progress=progress;
            publishProgress(this.progress);
        }
        private int getProgress(){
            return progress;
        }
    }


}
