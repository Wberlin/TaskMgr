package com.wbl.taskmanager.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;

import com.wbl.taskmanager.models.ProcessInfo;

import java.util.List;

/**
 * Created by djtao on 2016/8/22.
 */

public class CalculateProcessMemorySize extends AsyncTask<List<ProcessInfo>,Integer,Boolean> {
        private Context mContext;
        private ActivityManager am;

        public interface AsynTaskFinishedListener{
            //刷新UI
            void refreshUI();
        }

    public AsynTaskFinishedListener getListener() {
        return listener;
    }

    public void setAsynTaskFinishedListener(AsynTaskFinishedListener listener) {
        this.listener = listener;
    }

    private AsynTaskFinishedListener listener;

        public CalculateProcessMemorySize(Context mContext) {
            super();
            this.mContext=mContext;
            am=(ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        }

        //执行耗时的操作
        @Override
        protected Boolean doInBackground(List<ProcessInfo>... processInfos) {
            for(int i=0;i<processInfos[0].size();i++){

                Debug.MemoryInfo[] memoryInfo=am.getProcessMemoryInfo(new int[]{processInfos[0].get(i).getPid()});
                int totalPrivateDirty=(memoryInfo[0].getTotalPss())*1024;
                processInfos[0].get(i).setMemSize(Formatter.formatFileSize(mContext,totalPrivateDirty));

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
                if(listener!=null){
                    listener.refreshUI();
                }
            }
        }

}
