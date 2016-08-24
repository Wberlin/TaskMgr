package com.wbl.taskmanager.models;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by djtao on 2016/8/19.
 */

public class ProcessInfo{


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

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(pid);
//        parcel.writeInt(uid);
//        parcel.writeString(processName);
//        parcel.writeString(memSize);
//        parcel.writeString(time);
//        parcel.writeTypedList(serviceInfoList);
//        parcel.writeTypedList(appInfoList);
//    }

//    public static final Parcelable.Creator<ProcessInfo> CREATOR= new ClassLoaderCreator<ProcessInfo>() {
//        @Override
//        public ProcessInfo createFromParcel(Parcel parcel) {
//            return new ProcessInfo(parcel);
//        }
//
//        @Override
//        public ProcessInfo[] newArray(int i) {
//            return new ProcessInfo[i];
//        }
//
//        @Override
//        public ProcessInfo createFromParcel(Parcel parcel, ClassLoader classLoader) {
//            return new ProcessInfo(parcel,classLoader);
//        }
//    };
    public ProcessInfo(){

    }
//    private ProcessInfo(Parcel in,ClassLoader classLoader){
//        pid=in.readInt();
//        uid=in.readInt();
//        processName=in.readString();
//        memSize=in.readString();
//        time=in.readString();
//        in.readTypedList(serviceInfoList,ServiceInfo.CREATOR);
//        in.readTypedList(appInfoList,AppInfo.CREATOR);
//    }


//    private ProcessInfo(Parcel in){
//        pid=in.readInt();
//        uid=in.readInt();
//        processName=in.readString();
//        memSize=in.readString();
//        time=in.readString();
//        in.readTypedList(serviceInfoList,ServiceInfo.CREATOR);
//        in.readTypedList(appInfoList,AppInfo.CREATOR);
//    }
}
