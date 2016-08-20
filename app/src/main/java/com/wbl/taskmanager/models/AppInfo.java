package com.wbl.taskmanager.models;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by djtao on 2016/8/19.
 */

public class AppInfo {
    private String appLabel;//应用程序标签
    private Drawable appIcon;//应用程序图像
    private Intent intent;//启动应用程序的Intent
    private String pkgName;//应用程序所对应的包名


    //当前应用运行时间
    private String time;



    private ProcessInfo processInfo;//应用程序所在进程
    public ProcessInfo getProcessInfo() {
        return processInfo;
    }

    public void setProcessInfo(ProcessInfo processInfo) {
        this.processInfo = processInfo;
    }
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
}
