package com.wbl.taskmanager.models;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by djtao on 2016/8/19.
 */

public class AppInfo {
    private String appLabel;//应用程序标签
    private Drawable appIcon;//应用程序图像
    private Intent intent;//启动应用程序的Intent
    private String pkgName;//应用程序所对应的包名
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
