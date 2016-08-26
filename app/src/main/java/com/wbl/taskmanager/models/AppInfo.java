package com.wbl.taskmanager.models;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by djtao on 2016/8/19.
 */

public class AppInfo{

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }
    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(appLabel);
//        parcel.writeString(pkgName);
//        parcel.writeString(time);
//        parcel.writeParcelable(intent,0);
//        parcel.writeParcelable(appIcon,0);
//
//    }

    private String appLabel;//应用程序标签
    private Drawable appIcon;//应用程序图像
    private Intent intent;//启动应用程序的Intent
    private String pkgName;//应用程序所对应的包名
    //当前应用运行时间
    private String time;

    public AppInfo(){

    }

    private AppInfo(Parcel in){
        appLabel=in.readString();
        pkgName=in.readString();
        time=in.readString();
        intent=in.readParcelable(Thread.currentThread().getContextClassLoader());
        appIcon=in.readParcelable(Thread.currentThread().getContextClassLoader());
    }
    private AppInfo(Parcel in,ClassLoader classLoader){
        appLabel=in.readString();
        pkgName=in.readString();
        time=in.readString();
        intent=in.readParcelable(classLoader);
        appIcon=in.readParcelable(classLoader);
    }
//    public static final Parcelable.Creator<AppInfo> CREATOR= new ClassLoaderCreator<AppInfo>() {
//        @Override
//        public AppInfo createFromParcel(Parcel parcel) {
//            return new AppInfo(parcel);
//        }
//
//        @Override
//        public AppInfo[] newArray(int i) {
//            return new AppInfo[i];
//        }
//
//        @Override
//        public AppInfo createFromParcel(Parcel parcel, ClassLoader classLoader) {
//            return new AppInfo(parcel,classLoader);
//        }
//    };



}
