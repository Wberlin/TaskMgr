package com.wbl.taskmanager.activity;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wbl.taskmanager.R;
import com.wbl.taskmanager.base.BaseActivity;
import com.wbl.taskmanager.utils.SystemUtil;
import com.wbl.taskmanager.utils.TextColorUtil;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {

    private ActivityManager mActivityManager=null;
    //当前可用内存
    private TextView tvAvaible;
    //总内存
    private TextView tvAll;
    //显示进程信息
    private Button btn;

    //实时显示可用内存信息
    private Timer mTimer;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    YoYo.with(Techniques.Tada).duration(500).playOn(tvAvaible);
                    YoYo.with(Techniques.Tada).duration(500).playOn(tvAll);
                    tvAvaible.setText(SystemUtil.getSystemAvaiableMemorySize(MainActivity.this));
                    tvAll.setText(SystemUtil.getSystemAllMemorySize(MainActivity.this));
                    break;

            }
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onStart() {
        mTimer=new Timer();
        mTimer.schedule(new FixedRefresh(),0,5000);
        super.onStart();
    }
    @Override
    protected void onStop() {
        if(mTimer!=null)
        mTimer.cancel();
        super.onStop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findView();

        mActivityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        tvAvaible.setText("系统可用内存为：");

        //拿到系统可用内容信息
        String availMemStr= SystemUtil.getSystemAvaiableMemorySize(this);
        tvAvaible.setText(availMemStr);

        tvAll.setText("系统总内容为：");
        //拿到系统总内存信息
        String allMemStr=SystemUtil.getSystemAllMemorySize(this);
        tvAll.setText(allMemStr);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.RubberBand).duration(500).playOn(view);
                //android系统小于5.0
                if(Build.VERSION.SDK_INT<=19)
                    openActivity(ProcessActivity.class);
                else
                    openActivity(ProcessUpActivity.class);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(mTimer!=null){
            mTimer=null;
        }
        super.onDestroy();
    }




    private void findView() {

        tvAvaible=(TextView) findViewById(R.id.main_tv);

        tvAll=(TextView)findViewById(R.id.main_tv1);

        btn=(Button)findViewById(R.id.main_btn);
    }



    private class FixedRefresh extends TimerTask{

        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    }


}
