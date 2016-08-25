package com.wbl.taskmanager.utils.aysntask;

/**
 * Created by djtao on 2016/8/25.
 */

public interface onFinishListener {

    //用于异步线程，当任务完成时调用接口
    void onFinish(boolean isfinish);
}
