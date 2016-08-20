package com.wbl.taskmanager.adapter;

import android.app.Activity;
import android.os.Process;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbl.taskmanager.R;
import com.wbl.taskmanager.base.BaseListViewAdapter;
import com.wbl.taskmanager.models.ProcessInfo;

import java.text.DecimalFormat;
import java.text.Format;

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
            vHolder.tvProName=(TextView)convertView.findViewById(R.id.lv_process_name);
            vHolder.tvProDetail=(TextView)convertView.findViewById(R.id.lv_process_detail);
            vHolder.tvProSize=(TextView)convertView.findViewById(R.id.lv_process_size);
            convertView.setTag(vHolder);
        }else{
            vHolder=(ViewHolder)convertView.getTag();
        }
        ProcessInfo processInfo=mList.get(position);
        vHolder.tvProName.setText(processInfo.getProcessName());
        DecimalFormat df=new DecimalFormat("0.0");
        vHolder.tvProSize.setText(df.format(processInfo.getMemSize())+"MB");

        vHolder.tvProDetail.setText("pid:"+processInfo.getPid());
        return convertView;
    }


    private class ViewHolder{
        TextView tvProName;
        TextView tvProDetail;
        TextView tvProSize;
    }
}
