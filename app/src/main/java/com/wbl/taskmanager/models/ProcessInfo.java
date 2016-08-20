package com.wbl.taskmanager.models;

import android.graphics.drawable.Drawable;

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
    private int memSize;
    //应用图标信息
    private Drawable icon;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public float getMemSize() {
        return (float)(memSize*1.0/1024);
    }

    public void setMemSize(int memSize) {
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
