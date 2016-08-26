package com.wbl.taskmanager.base;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.anthonycr.grant.PermissionsManager;
import com.wbl.taskmanager.utils.SystemUtil;

import java.util.Stack;


public class MyApplication extends Application {

    private static Stack<Activity> activityStack;
    private static MyApplication singleton;


    @Override
    public void onCreate() {
        super.onCreate();
        String processName=SystemUtil.getProcessName();

        //Log.e("TAG","当前正在运行中进程的名字："+processName);
        if(!TextUtils.isEmpty(processName)&&processName.equals(this.getPackageName())){
            CrashHandler mCrashHandler = CrashHandler.getInstance();
            mCrashHandler.init(getApplicationContext());
            singleton=this;
            //this.getPackageName()获取到的永远是主进程的包名.
        }else{
            //Log.e("TAG","current packageName:"+this.getPackageName());
           //Log.e("TAG","获取的processname:"+processName);
        }



    }

    /**
     * 获取单例实例
     * @return
     */
    public static MyApplication getInstance(){return singleton;}

    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity(){
        Activity activity=activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity(){
        Activity activity=activityStack.lastElement();
        finishActivity(activity);
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls){
        for(Activity activity:activityStack){
            if(activity.getClass().equals(cls)){
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();

            }
        }
        activityStack.clear();
    }



}
