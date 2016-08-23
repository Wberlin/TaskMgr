package com.wbl.taskmanager.models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djtao on 2016/8/19.
 */

public class ProcessInfo {


    //进程id
    private int pid;

    //用户id
    private int uid;
    //进程名
    private String processName;
    //所占内存大小
    private String memSize;

    //应用图标信息
    private List<AppInfo> appInfoList=new ArrayList<>();



    //服务信息
    private List<ServiceInfo> serviceInfoList=new ArrayList<>();


    private String time;

    public List<ServiceInfo> getServiceInfoList() {
        return serviceInfoList;
    }

    public void setServiceInfoList(List<ServiceInfo> serviceInfoList) {
        this.serviceInfoList = serviceInfoList;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public List<AppInfo> getAppInfoList() {
        return appInfoList;
    }

    public void setAppInfoList(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
    }


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getMemSize() {
        return memSize;
    }

    public void setMemSize(String memSize) {
        this.memSize = memSize;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

}
