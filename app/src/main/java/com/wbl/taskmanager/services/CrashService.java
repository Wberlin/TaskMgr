package com.wbl.taskmanager.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wbl.taskmanager.utils.DateUtil;
import com.wbl.taskmanager.utils.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by djtao on 2016/8/17.
 */

public class CrashService extends Service {
    private static String PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/wbl/log";
    private Timer timer;
    private String HttpUrl="http://183.246.86.117:7070/api/submitIssue";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer=new Timer();
        timer.schedule(new CrashTask(),0,12*60*60*1000);
        Log.e("TAG","CrashService->onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("TAG","CrashService->onStartCommand");

        return START_STICKY;


    }

    //做一些清理工作，例如关闭线程等
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();

        Log.e("TAG","CrashService->onDestroy");
    }



    private class CrashTask extends TimerTask{

        @Override
        public void run() {
            updateCrash();

            Log.e("TAG","CrashService->发送一次网络请求");
        }
    }
    //上传异常文件
    private void updateCrash() {

        Calendar c=Calendar.getInstance();
        Date currentTime=new Date(System.currentTimeMillis());
        List<File> uploadFiles=new ArrayList<>();
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file=new File(PATH);
            if(file.isDirectory()&&file.exists()){
                List<File> fileList=getFile(file);
                for(File f:fileList){
                    try{
                        String fileName=f.getName();
                        String date=fileName.substring(5,fileName.indexOf("."));
                        if(DateUtil.isValidDate(date)){//判断是否是合法的日期格式
                            String[] dates=date.split("-");
                                c.set(Integer.parseInt(dates[0]),Integer.parseInt(dates[1])-1,Integer.parseInt(dates[2]));

                                if(c.getTime().getTime()<currentTime.getTime()&& DateUtil.daysOfTwo(currentTime,c.getTime())==1){
                                    uploadFiles.add(f);
                                }
                        }

                    } catch (NumberFormatException e){
                        e.printStackTrace();
                    }

                }
            }
        }
        else{
            Log.e("TAG","没有SD卡存在");
        }

        //上传文件
        parperHttp(uploadFiles);


    }

    /**
     *
     * @param uploadFile 上传的文件列表
     */
    private void parperHttp(List<File> uploadFile) {
        PostFormBuilder pfb=OkHttpUtils.post().url(HttpUrl)
                .addHeader("x-auth-token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODg3Nzc3NjY2NiIsImlhdCI6MTQ3MTQ4MzUxMywic3ViIjoiYXV0aG9yaXphdGlvbiIsImlzcyI6ImNwcyIsImV4cCI6NDYyNTA4MzUxM30.CF6xT-ahNxSWGKbQX5zo4ePbiEN3FwYXls9awo3NxlQ")
                .addParams("title","nihao")
                .addParams("description","iamhack")
                .addParams("appId","0")
                .addParams("platform","0");
        for(int i=0;i<uploadFile.size();i++){
            pfb.addFile(("File"+i),uploadFile.get(i).getName(),uploadFile.get(i));
        }
        pfb.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                Log.e("TAG","id:"+id);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("TAG","response:"+response);
                Log.e("TAG","id:"+id);
            }
        });


    }

    private List<File> getFile(File file) {
        List<File> fileList=new ArrayList<>();
        File[] files=file.listFiles();
        for(File f:files){
            if(f.isFile()){
                if(FileUtils.getFileSuffix(f).equals("trace"))
                fileList.add(f);
            }else{
                List<File>  childFiles=getFile(f);
                if(childFiles!=null)
                fileList.addAll(childFiles);
            }
        }
        return fileList;
    }
}
