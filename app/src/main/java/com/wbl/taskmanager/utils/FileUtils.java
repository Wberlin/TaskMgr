package com.wbl.taskmanager.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * 文件操作类
 * 必须有读写的权限
 * 在<application>节点下加入读写权限</application>
 *  * <!-- 读写权限 -->
 *<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 *<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 */

public class FileUtils {


    //默认存储文件夹
    private static String DIR="wbl";
    //默认存储文件
    private static String FILENAME="test.txt";

    public static String getDIR() {
        return DIR;
    }


    //外存所在路径
    private static String SDPATH=Environment.getExternalStorageDirectory().getAbsolutePath();

    public static String getSDPATH() {
        return SDPATH;
    }

    public static void setSDPATH(String SDPATH) {
        FileUtils.SDPATH = SDPATH;
    }
    /**
     * 向SD卡中写入数据
     * 默认路径为/storage/emulated/0/wbl/test.txt
     * @param contentBuffer 比特流数据
     * @param  isappend 是否在原有的内容上添加内容
     */
    public static void write(byte[] contentBuffer,boolean isappend) throws IOException{
        if(getStorageState()){
            File file=new File(SDPATH+File.separator+DIR+File.separator+FILENAME);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            FileOutputStream fout=null;
            try{
                fout=new FileOutputStream(file,isappend);
                fout.write(contentBuffer);
                fout.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }


        }else{
            Log.e("TAG","保存失败，SD卡不存在！");
        }
    }
    /**
     * 向SD卡中写入数据
     * 默认路径为/storage/emulated/0/+dir+/+filename
     * @param dir 可指定文件夹名
     * @param filename 可指定文件名
     * @param  isappend 是否在原有的内容上添加内容
     * @param contentBuffer 可指定文件内容
     */
    public static void write(String dir,String filename,byte[] contentBuffer,boolean isappend) throws IOException{
        if(getStorageState()){
            if(dir==null||dir.equals("")) dir=DIR;
            if(filename==null||filename.equals("")) filename=FILENAME;
            File file=new File(SDPATH+File.separator+dir+File.separator+filename);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            FileOutputStream fout=null;
            try{
                fout=new FileOutputStream(file,isappend);
                fout.write(contentBuffer);
                fout.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }else{
            Log.e("TAG","保存失败，SD卡不存在！");
        }
    }
    /**
     * 向SD卡中写入数据
     * 默认路径为/storage/emulated/0/wbl/+filename
     * @param filename 可指定文件名
     * @param contentBuffer 可指定文件内容
     */
    public static void write(String filename,byte[] contentBuffer) throws IOException{
        write(null,filename,contentBuffer,false);
    }

    /**
     * 默认路径为/storage/emulated/0/+dir+/+filename
     * @param dir 可指定文件夹名
     * @param filename 可指定文件名
     * @param contentbuffer 可指定文件内容
     * @throws IOException
     */
    public static void write(String dir,String filename,byte[] contentbuffer) throws IOException{
        write(dir,filename,contentbuffer,false);
    }


    /**
     * 向SD卡中写入数据
     * 默认路径为/storage/emulated/0/+dir+/+filename
     * @param dir 可指定文件夹名
     * @param filename 可指定文件名
     * @param content 可指定文件内容
     * @param  isappend 是否在原有的内容上添加内容
     * @throws IOException
     */
    public static void write(String dir,String filename,String content,boolean isappend) throws IOException{
        if(getStorageState()){
            if(dir==null||dir.equals("")) dir=DIR;
            if(filename==null||filename.equals("")) filename=FILENAME;
            File file=new File(SDPATH+File.separator+dir+File.separator+filename);
            if(!file.getParentFile().exists()){//父文件夹不存在
                file.getParentFile().mkdirs();//创建文件夹
            }

            PrintStream out=null;
            out=new PrintStream(new FileOutputStream(file,isappend));
            out.println(content);

            if(out!=null){
                out.close();
            }

        }else{
            Log.e("TAG","保存失败，SD卡不存在！");
        }
    }
    /**
     * 向SD卡中写入数据
     * 默认路径为/storage/emulated/0/wbl/+filename
     * @param filename 可指定文件名
     * @param content 可指定文件内容
     * @throws IOException
     */
    public static void write(String filename,String content) throws IOException{
        write(null,filename,content,false);
    }

    /**
     * 向sdcard上写文件
     *  默认路径为/storage/emulated/0/wbl/test.txt
     * DIR 默认文件夹名 wbl
     * FILENAME 默认文件名 text.txt
     * @param content 内容
     * @param isappend 是否在原有内容上添加
     */
    public static void write(String content,boolean isappend) throws IOException{

        if(getStorageState()){
            File file=new File(SDPATH+File.separator+DIR+File.separator+FILENAME);
            if(!file.getParentFile().exists()){ //父文件夹不存在
                file.getParentFile().mkdirs();//创建文件夹
            }
            PrintStream out=null;//打印流对象用于输出
            out=new PrintStream(new FileOutputStream(file,isappend));
            out.println(content);
            if(out!=null){
                out.close();//关闭打印流
            }

        }else{
           Log.e("TAG","保存失败，SD卡不存在！");
        }
    }
    /**
     * 向sdcard上写文件
     *  默认路径为/storage/emulated/0/wbl/test.txt
     * DIR 默认文件夹名 wbl
     * FILENAME 默认文件名 text.txt
     * @param content 内容
     */
    public static void write(String content) throws IOException{
        write(content,false);
    }

    /**
     * 向SD卡中写入数据
     * 默认路径为/storage/emulated/0/+dir+/+filename
     * @param dir 可指定文件夹名
     * @param filename 可指定文件名
     * @param bt 可传入bitmap类型的数据
     *
     * @throws IOException
     */
    public static void write(String dir,String filename,Bitmap bt) throws IOException{
        if(bt!=null){
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bt.compress(Bitmap.CompressFormat.PNG,100,stream);
            write(dir,filename,stream.toByteArray());
        }else{
            Log.e("TAG","传入图片为空");
        }
    }
    /**
     * 向SD卡中写入数据
     * 默认路径为/storage/emulated/0/wbl/+filename
     * @param filename 可指定文件名
     * @param bt 可传入bitmap类型的数据
     * @throws IOException
     */
    public static void write(String filename,Bitmap bt) throws IOException{
        write(null,filename,bt);
    }

    /**
     * 向sdcard上写文件
     *  默认路径为/storage/emulated/0/wbl/test.txt
     * DIR 默认文件夹名 wbl
     * FILENAME 默认文件名 text.txt
     * 内容添加到原有内容后面
     * @param content 内容
     */
    public static void writeAppend(String content) throws IOException{
        write(content,true);
    }
    /**
     * 向sdcard上写文件
     *  默认路径为/storage/emulated/0/wbl/test.txt
     * DIR 默认文件夹名 wbl
     * FILENAME 默认文件名 text.txt
     * 内容添加到原有内容后面
     * @param contentBuffer 内容
     */
    public static void writeAppend(byte[] contentBuffer) throws IOException{
        write(contentBuffer,true);
    }
    /**
     * 向SD卡中写入数据
     * 默认路径为/storage/emulated/0/wbl/+filename
     * @param filename 可指定文件名
     * @param content 可指定文件内容
     * @throws IOException
     */
    public static void writeAppend(String filename,String content) throws IOException{
        write(null,filename,content,true);
    }
    /**
     * 向SD卡中写入数据
     * 默认路径为/storage/emulated/0/wbl/+filename
     * @param filename 可指定文件名
     * @param contentBuffer 可指定文件内容
     */
    public static void writeAppend(String filename,byte[] contentBuffer) throws IOException{
        write(null,filename,contentBuffer,true);
    }
    /**
     * 向SD卡中写入数据
     * 默认路径为/storage/emulated/0/+dir+/+filename
     * @param dir 可指定文件夹名
     * @param filename 可指定文件名
     * @param content 可指定文件内容
     */
    public static void writeAppend(String dir,String filename,String content) throws IOException{
        write(dir,filename,content,true);
    }
    /**
     * 向SD卡中写入数据
     * 默认路径为/storage/emulated/0/+dir+/+filename
     * @param dir 可指定文件夹名
     * @param filename 可指定文件名
     * @param contentBuffer 可指定文件内容
     */
    public static void writeAppend(String dir,String filename,byte[] contentBuffer) throws IOException{
        write(dir,filename,contentBuffer,true);
    }

    /**
     * 读取SD卡上的文件数据
     * 默认路径为/storage/emulated/0/wbl/test.txt
     * 默认的wbl文件夹下的test.txt
     */
    public static String read() throws IOException{
        if(getStorageState()){
            File file=new File(SDPATH
                    +File.separator
                    +DIR
                    +File.separator
                    +FILENAME);//定义File类对象

           if(!file.exists()) return null;
            Scanner scan=null;//扫描输入
            StringBuilder sb=new StringBuilder();
            try{
                scan=new Scanner(new FileInputStream(file));//实例化Scanner
                while(scan.hasNext()){//循环读取
                    sb.append(scan.next()+"\n");//设置文本
                }
                return sb.toString();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(scan!=null){
                    scan.close();//关闭打印流
                }
            }

        }else{// SDCard不存在
            Log.e("TAG","保存失败，SD卡不存在！");
        }
        return null;
    }
    /**
     * 从SD卡中读取相应的文件数据
     * 默认路径为/storage/emulated/0/wbl/+filename
     * @param filename 文件名
     * @return 返回结果以String的类型输出
     * @throws IOException
     */
    public static String read(String filename) throws IOException{
        return read(null,filename);
    }
    /**
     * 从SD卡中读取相应的文件数据
     * 默认路径为/storage/emulated/0/+dir+/+filename
     * @param dir 相对于外存路径的文件夹名称
     * @param filename 文件名
     * @return 返回结果以String的类型输出
     * @throws IOException
     */
    public static String read(String dir,String filename) throws IOException{
        if(getStorageState()){
            if(dir==null||dir.equals("")) dir=DIR;
            if(filename==null||filename.equals("")) filename=FILENAME;

            File file=new File(SDPATH
                    +File.separator
                    +dir
                    +File.separator
                    +filename);//定义File类对象
            if(!file.exists()){
                return null;
            }
            //Scanner scan=null;//扫描输入
            StringBuilder sb=new StringBuilder();
            BufferedReader bReader=null;
            try{
//                scan=new Scanner(new FileInputStream(file));//实例化Scanner
//                while(scan.hasNext()){//循环读取
//                    sb.append(scan.next()+"\n");//设置文本
//                }
                bReader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line="";
                while((line=bReader.readLine())!=null){
                    sb.append(line+"\n");
                }
                bReader.close();
                return sb.toString();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
//                if(scan!=null){
//                    scan.close();//关闭打印流
//                }
                if(bReader!=null){
                    try {
                        bReader.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }

        }else{// SDCard不存在
            Log.e("TAG","保存失败，SD卡不存在！");
        }
        return null;
    }


    /**
     * 读取SD的文件数据，
     * 默认路径为/storage/emulated/0/wbl/test.txt
     * @return 返回比特流数组
     * @throws IOException
     */
    public static byte[] read2Byte() throws IOException{
        if(getStorageState()){
            File file=new File(SDPATH
                    + File.separator
                    +DIR
                    +File.separator
                    +FILENAME);
            if(!file.exists()) return null;
            FileInputStream fin=null;
            byte[] buffer;
            try{
                fin=new FileInputStream(file);
                int length=fin.available();
                buffer=new byte[length];
                fin.read(buffer);
                fin.close();
                return buffer;
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }else{
            Log.e("TAG","保存失败，SD卡不存在！");
        }
        return null;
    }
    /**
     * 读取SD中的文件数据
     * 默认路径为/storage/emulated/0/wbl/+filename
     * @param filename 文件名
     * @return 返回比特流数组
     * @throws IOException
     */
    public static byte[] read2Byte(String filename) throws IOException{
        return read2Byte(null,filename);
    }
    /**
     * 读取SD中的文件数据
     * 默认路径为/storage/emulated/0/+dir+/+filename
     * @param dir 相对于外存路径的文件夹名称
     * @param filename 文件名
     * @return 返回比特流数组
     * @throws IOException
     */
    public static byte[] read2Byte(String dir,String filename) throws IOException{
        if(getStorageState()){
            if(dir==null||dir.equals("")) dir=DIR;
            if(filename==null||filename.equals("")) filename=FILENAME;
            File file=new File(SDPATH
                    + File.separator
                    +dir
                    +File.separator
                    +filename);
            if(!file.exists()) return null;
            FileInputStream fin=null;
            byte[] buffer;
            try{
                fin=new FileInputStream(file);
                int length=fin.available();
                buffer=new byte[length];
                fin.read(buffer);
                fin.close();
                return buffer;
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }else{
            Log.e("TAG","保存失败，SD卡不存在！");
        }
        return null;
    }

    /**
     * 将文件转换为比特流
     * @param file
     * @return
     */
    public static byte[] readFile(File file) throws IOException{
        if(!file.exists()){
            throw  new NullPointerException("file is null");
        }
        FileInputStream fin=null;
        byte[] buffer;
        fin=new FileInputStream(file);
        int length=fin.available();
        buffer=new byte[length];
        fin.read(buffer);
        fin.close();

        return buffer;


    }
    /**
     * 读取指定文件夹下的文件，存入指定文件夹
     * @param fromDir 来自哪个文件夹,绝对路径
     * @param toDir 到达哪个文件夹，绝对路径
     * @return 是否操作成功
     */
    public static boolean readFile2SaveDir(String fromDir,String toDir) throws IOException{
        Log.e("TAG","从什么文件夹来:"+fromDir);
        Log.e("TAG","到什么文件夹去:"+toDir);
        if(getStorageState()){
            File fromFile=new File(fromDir);
            File toFile=new File(toDir);
            if(fromFile.exists()){
                FileInputStream fin=new FileInputStream(fromFile);
                int length=fin.available();
                byte[] buffer=new byte[length];
                fin.read(buffer);

                FileOutputStream fout=new FileOutputStream(toFile);
                fout.write(buffer);
                fout.close();
                fin.close();
                return true;
            }else{

                Log.e("TAG","读取的文件不存在");
                return false;
            }
        }else{
            Log.e("TAG","保存失败，SD卡不存在！");
            return false;
        }
    }


    /**
     * 改变默认路径
     * @param dir
     */
    public static void setDir(String dir){
        DIR=dir;
    }

    /**
     * 改变默认文件名
     * @param filename
     */
    public static void setFileName(String filename){
        FILENAME=filename;
    }

    /**
     * 创建文件
     * @param fileName
     */
    public static void createFile1(String fileName) throws IOException{
        Log.e("TAG","系统外存目录"+Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.e("TAG","系统根目录"+Environment.getRootDirectory().getAbsolutePath());
        Log.e("TAG","系统提醒铃声存放的标准目录"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS));
        Log.e("TAG","相机拍摄照片和视频的标准目录。"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
        Log.e("TAG","下载的标准目录。"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        Log.e("TAG","电影存放的标准目录。"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
        Log.e("TAG","音乐存放的标准目录"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
        Log.e("TAG","系统通知铃声存放的标准目录"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS));
        Log.e("TAG","图片存放的标准目录"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        Log.e("TAG","系统广播存放的标准目录"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS));
        Log.e("TAG","系统铃声存放的标准目录"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES));

    }

    /**
     * 删除指定文件
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) throws IOException{
        if(getStorageState()){
            File file=new File(path);
            if(file.exists()){
                file.delete();
                return true;
            }
        }
        return false;
    }

    /**
     * 创建指定文件
     * 默认路径为/storage/emulated/0/wbl/+fileName
     * @param fileName
     * @throws IOException
     */
    public void createFile(String fileName) throws IOException{
        if(getStorageState()){
            File file=new File(SDPATH+File.separator+DIR+File.separator);
            File configFile=new File(SDPATH+File.separator+DIR+File.separator+fileName);

            if(!file.exists()){
                file.mkdirs();
            }

            if(!configFile.exists()){
                configFile.createNewFile();
            }


        }else{
            Log.e("TAG","SD不存在");
        }
    }



    /**
     * 判断SD卡是否存在
     */
    public static boolean getStorageState(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 从resource的raw中读取文件数据
     *
     * @return 以字符串的形式返回获取到文件数据
     */
//    public String readFileFromRaw(Context context){
//        String res="";
//        try{
//            //得到资源中的Raw数据流
//            InputStream in=context.getResources().openRawResource();
//        }
//    }

    /**
     * 读取asserts文件夹下的文件
     * @param context
     * @param filename
     * @return 以String的形式返回结果
     * @throws IOException
     */
    public static String readFileFromAsset(Context context,String filename) throws IOException{
        StringBuffer res=new StringBuffer();
        try{
            //得到资源中的asset数据流
            InputStream in=context.getResources().getAssets().open(filename);
            BufferedReader bReader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while((line=bReader.readLine())!=null){
                res.append(line+"\n");
            }
            in.close();
            bReader.close();
            return res.toString();

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从resource的raw中读取文件数据
     * @param context 上下文
     * @param rawId /raw文件夹下的资源文件
     * @param charSet 转换为String类型时，需要转换的格式,
     */
    public static String readFileFromRaw(Context context,@RawRes int rawId,String charSet) throws IOException{
        StringBuffer res=new StringBuffer();
            //得到资源中的Raw数据流
            InputStream in=context.getResources().openRawResource(rawId);
            BufferedReader bReader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while((line=bReader.readLine())!=null){
                res.append(line+"\n");
            }
            in.close();
            bReader.close();
        return res.toString();
    }

    /**
     * 向/data/data/<应用程序名>/files目录写入文件
     * @param context 上下文
     * @param fileName 文件名
     * @param content 文件内容
     */
    public static void writeFile(Context context,String fileName,String content) throws IOException{
        try{
            FileOutputStream fOut=context.openFileOutput(fileName,Context.MODE_PRIVATE);
            byte[] buffer=content.getBytes();
            fOut.write(buffer);
            fOut.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 读取/data/data/<应用程序名>/files目录中的文件
     * @param context 上下文
     * @param fileName 文件名
     */
    public static String readFile(Context context,String fileName) throws IOException{
        StringBuffer res=new StringBuffer();
        try{
            FileInputStream fin=context.openFileInput(fileName);
            BufferedReader fReader=new BufferedReader(new InputStreamReader(fin));
            String line="";
            while((line=fReader.readLine())!=null){
                res.append(line+"\n");
            }
            fReader.close();
            fin.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return res.toString();

    }

    public static String getImageDiscCache(){
        String sdDefaultPath=SDPATH+File.separator+DIR+File.separator;
        File sdfile=new File(sdDefaultPath);
        if(!sdfile.exists()){
            sdfile.mkdirs();
        }
        String path=sdDefaultPath+"cache/image/";
        File file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }

    /**
     * 拿到文件的后缀名
     * @param file
     * @return
     */
    public static String getFileSuffix(File file){
        if(file.exists()){
            String name=file.getName();
            return name.substring(name.lastIndexOf(".")+1,name.length());
        }
        return "";
    }

    /**
     * 拿到文件的后缀名
     * @param uri
     * @return
     */
    public static String getFileSuffix(Uri uri){
        if(uri!=null){
            String name=uri.getPath();
            return name.substring(name.lastIndexOf(".")+1,name.length());
        }
        return "";
    }
}
