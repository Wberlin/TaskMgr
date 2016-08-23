package com.wbl.taskmanager.models;

import android.content.ComponentName;

/**
 * Created by djtao on 2016/8/20.
 */

public class ServiceInfo {


    private String servicename; //Service Name
    private int pid; //所在进程
    private String processname; //进程名
    private boolean isStart;//Service是否启动
    private long activesince;//Service初次启动的时间
    private ComponentName servicemessage;//获得该Service的组件信息 包含了pkgname / servicename信息
    private long lastactivitytime;//Activity最近一次关联Service时间
    private ProcessInfo processInfo;



    public ProcessInfo getProcessInfo() {
        return processInfo;
    }

    public void setProcessInfo(ProcessInfo processInfo) {
        this.processInfo = processInfo;
    }


    public long getLastactivitytime() {
        return lastactivitytime;
    }

    public void setLastactivitytime(long lastactivitytime) {
        this.lastactivitytime = lastactivitytime;
    }



    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public long getActivesince() {
        return activesince;
    }

    public void setActivesince(long activesince) {
        this.activesince = activesince;
    }
    public void setStart(boolean start) {
        isStart = start;
    }
    public boolean isStart() {
        return isStart;
    }


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public ComponentName getServicemessage() {
        return servicemessage;
    }

    public void setServicemessage(ComponentName servicemessage) {
        this.servicemessage = servicemessage;
    }

}
