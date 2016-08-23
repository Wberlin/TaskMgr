package com.wbl.taskmanager.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by djtao on 2016/8/23.
 */

public class TimerView extends TextView {
    private Timer timer;
    private long activeSince;
    private String time;


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    setText(time);
                    break;
                case 0:
                    setText("重新计算中...");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public TimerView(Context context) {
        super(context);
        init();
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化定时器
    public void init(){
        timer=new Timer();
    }
    public void setTimeData(long activeSince){
        this.activeSince=activeSince;
    }

    public void start(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                StringBuilder mBuilder=new StringBuilder(128);
                if(activeSince>0){
                    time=DateUtils.formatElapsedTime(mBuilder,(SystemClock.elapsedRealtime()-activeSince)/1000);
                    mHandler.sendEmptyMessage(1);
                }else{
                    mHandler.sendEmptyMessage(0);
                }

            }
        },0,1000);
    }
}
