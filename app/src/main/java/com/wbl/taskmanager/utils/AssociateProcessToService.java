package com.wbl.taskmanager.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.wbl.taskmanager.models.ProcessInfo;
import com.wbl.taskmanager.models.ServiceInfo;
import com.wbl.taskmanager.utils.aysntask.onFinishListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by djtao on 2016/8/23.
 */

public class AssociateProcessToService extends AsyncTask<Void,Integer,Boolean> {
    private List<ProcessInfo> processes;
    private List<ServiceInfo> services;
    private onFinishListener listener;

    public void setOnFinishedListener(onFinishListener listener) {
        this.listener = listener;
    }

    public AssociateProcessToService(List<ProcessInfo> processInfos,List<ServiceInfo> serviceInfos) {
        this.processes=processInfos;
        this.services=serviceInfos;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        for(int i=0;i<processes.size();i++){
            ProcessInfo processInfo=processes.get(i);
            List<ServiceInfo> serviceInfos=new ArrayList<>();
            for(int j=0;j<services.size();j++){
                ServiceInfo serviceInfo=services.get(j);
                if(serviceInfo.getPid()==processInfo.getPid()){
                    serviceInfos.add(serviceInfo);
                }
            }
            processInfo.setServiceInfoList(serviceInfos);
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean){
            if(listener!=null){
                listener.onFinish(aBoolean);
            }
            Log.e("TAG","关联完成");
        }
    }
}
