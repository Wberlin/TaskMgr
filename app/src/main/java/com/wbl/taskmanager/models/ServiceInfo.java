package com.wbl.taskmanager.models;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by djtao on 2016/8/20.
 */

public class ServiceInfo implements Parcelable{

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(servicename);
        parcel.writeString(processname);
        parcel.writeInt(pid);
        parcel.writeInt(isStart? 1:0);
        parcel.writeLong(activesince);
        parcel.writeLong(lastactivitytime);
        parcel.writeParcelable(processInfo,0);
        parcel.writeParcelable(servicemessage,0);
    }

    public static final Parcelable.Creator<ServiceInfo> CREATOR= new ClassLoaderCreator<ServiceInfo>() {
        @Override
        public ServiceInfo createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new ServiceInfo(parcel,classLoader);
        }

        @Override
        public ServiceInfo createFromParcel(Parcel parcel) {
            return new ServiceInfo(parcel);
        }

        @Override
        public ServiceInfo[] newArray(int i) {
            return new ServiceInfo[i];
        }
    };

    private String servicename; //Service Name
    private int pid; //所在进程
    private String processname; //进程名
    private boolean isStart;//Service是否启动
    private long activesince;//Service初次启动的时间
    private ComponentName servicemessage;//获得该Service的组件信息 包含了pkgname / servicename信息
    private long lastactivitytime;//Activity最近一次关联Service时间
    private ProcessInfo processInfo;

    public ServiceInfo(){

    }

    private ServiceInfo(Parcel in){
        servicename=in.readString();
        processname=in.readString();
        pid=in.readInt();
        isStart=in.readInt()==1;
        activesince=in.readInt();
        lastactivitytime=in.readInt();
        processInfo=in.readParcelable(Thread.currentThread().getContextClassLoader());
        servicemessage=in.readParcelable(Thread.currentThread().getContextClassLoader());
    }
    private ServiceInfo(Parcel in,ClassLoader classLoader){
        servicename=in.readString();
        processname=in.readString();
        pid=in.readInt();
        isStart=in.readInt()==1;
        activesince=in.readInt();
        lastactivitytime=in.readInt();
        processInfo=in.readParcelable(classLoader);
        servicemessage=in.readParcelable(classLoader);
    }
}
