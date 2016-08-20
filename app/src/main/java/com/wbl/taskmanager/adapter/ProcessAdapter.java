package com.wbl.taskmanager.adapter;

import android.app.Activity;
import android.os.Process;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbl.taskmanager.R;
import com.wbl.taskmanager.base.BaseListViewAdapter;
import com.wbl.taskmanager.models.AppInfo;
import com.wbl.taskmanager.models.ProcessInfo;


/**
 * Created by djtao on 2016/8/19.
 */

public class ProcessAdapter extends BaseListViewAdapter<ProcessInfo> {
    public ProcessAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder=null;
        if(convertView==null){
            vHolder=new ViewHolder();
            convertView=getView(R.layout.item_lv);
            vHolder.ivAppIcon=(ImageView)convertView.findViewById(R.id.lv_app_img);
            vHolder.tvAppName=(TextView)convertView.findViewById(R.id.lv_app_name);
            vHolder.tvProDetail=(TextView)convertView.findViewById(R.id.lv_process_detail);
            vHolder.tvProSize=(TextView)convertView.findViewById(R.id.lv_app_size);
            vHolder.tvTime=(TextView)convertView.findViewById(R.id.lv_process_time);
            vHolder.tvDetail=(TextView)convertView.findViewById(R.id.lv_detail);
            convertView.setTag(vHolder);
        }else{
            vHolder=(ViewHolder)convertView.getTag();
        }
        ProcessInfo processInfo=mList.get(position);

        if(processInfo.getAppInfoList().size()!=0)
        vHolder.tvAppName.setText(processInfo.getAppInfoList().get(0).getAppLabel());
        vHolder.tvProSize.setText(processInfo.getMemSize());

        vHolder.tvProDetail.setText(processInfo.getProcessName());
        if(processInfo.getAppInfoList().size()!=0)
        vHolder.ivAppIcon.setImageDrawable(processInfo.getAppInfoList().get(0).getAppIcon());

        vHolder.tvTime.setText("Pid:"+processInfo.getPid());
        //vHolder.tvDetail.setText(appInfo.getTime());
        return convertView;
    }


    private class ViewHolder{
        ImageView ivAppIcon;
        TextView tvAppName; //应用名称
        TextView tvProDetail;
        TextView tvDetail; //该应用拥有的进程数和服务数
        TextView tvProSize;//进程所占内存大小 --PSS类型
        TextView tvTime; //应用运行时间
    }
}
