package com.wbl.taskmanager.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;

import com.wbl.taskmanager.models.ProcessInfo;
import com.wbl.taskmanager.utils.aysntask.onFinishListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by djtao on 2016/8/22.
 */

public class CalculateProcessMemorySize extends AsyncTask<List<ProcessInfo>,Integer,Boolean> {
    private Context mContext;
    private ActivityManager am;
    private onFinishListener listener;
    public void setOnFinishedListener(onFinishListener listener) {
        this.listener = listener;
    }
        public CalculateProcessMemorySize(Context mContext) {
            super();
            this.mContext=mContext;
            am=(ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        }

        //执行耗时的操作
        @Override
        protected Boolean doInBackground(List<ProcessInfo>... processInfos) {

            if(processInfos!=null){
                for(int i=0;i<processInfos[0].size();i++){
                    if(processInfos[0].get(i)!=null){
                        Debug.MemoryInfo[] memoryInfo=am.getProcessMemoryInfo(new int[]{processInfos[0].get(i).getPid()});

                        int totalPrivateDirty=(memoryInfo[0].getTotalPss())*1024;

                        processInfos[0].get(i).setMemSize(Formatter.formatFileSize(mContext,totalPrivateDirty));
                    }
                }
                return true;
            }else{
                return false;
            }

        }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm;ss", Locale.CHINA);
        Log.e("TAG","Calculateprocess->execute finish at:"+df.format(new Date()));
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
                if(listener!=null){
                    listener.onFinish(aBoolean);
                }
            }

        }

}
