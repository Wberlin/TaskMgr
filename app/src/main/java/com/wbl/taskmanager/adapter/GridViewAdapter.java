package com.wbl.taskmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.wbl.taskmanager.R;
import com.wbl.taskmanager.models.AppInfo;
import com.wbl.taskmanager.models.ProcessInfo;
import com.wbl.taskmanager.models.ServiceInfo;
import com.wbl.taskmanager.view.TimerView;

import java.util.List;


public class GridViewAdapter extends BaseSwipeAdapter {
    private Context mContext;
    private List<ProcessInfo> processInfoList=null;

    public GridViewAdapter(Context mContext,List<ProcessInfo> processInfoList) {
        this.mContext=mContext;
        this.processInfoList=processInfoList;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
    //可以根据位置提供不同的布局
    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.gridview_item,null);
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView tvAppName=(TextView) convertView.findViewById(R.id.item_tv);
        ImageView ivAppIcon=(ImageView)convertView.findViewById(R.id.item_img);
        TextView tvPid=(TextView)convertView.findViewById(R.id.item_tv_pid);
        TextView tvMemSize=(TextView)convertView.findViewById(R.id.item_tv_memsize);
        TextView tvProName=(TextView)convertView.findViewById(R.id.item_tv_pro_name);
        TextView tvTimer=(TextView)convertView.findViewById(R.id.item_tv_timer);
        TextView tvServiceCount=(TextView)convertView.findViewById(R.id.item_tv_service_count);


        List<AppInfo> appInfoList=processInfoList.get(position).getAppInfoList();
        tvPid.setText("Pid:"+processInfoList.get(position).getPid());
        if(processInfoList.get(position).getMemSize()==null)
            tvMemSize.setText("占用内存：正在计算中...");
        else
            tvMemSize.setText("占用内存："+processInfoList.get(position).getMemSize());
        tvProName.setText("进程名："+processInfoList.get(position).getProcessName());
        if(appInfoList!=null&&appInfoList.size()!=0){
            tvAppName.setText(appInfoList.get(0).getAppLabel());
            ivAppIcon.setImageDrawable(appInfoList.get(0).getAppIcon());
        }
        if(processInfoList.get(position).getServiceInfoList().size()==0){
            tvServiceCount.setText("获取服务中...");
        }else{
            tvServiceCount.setText(processInfoList.get(position).getServiceInfoList().size()+"个服务");
            //tvTimer.setText();
        }



    }

    @Override
    public int getCount() {
        return processInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return processInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    class ViewHolder{
        TextView tvAppName;
        ImageView ivAppIcon;
        TextView tvPid;
        TextView tvMemSize;
        TextView tvProName;

        public ViewHolder(View convertView){
            tvAppName=(TextView) convertView.findViewById(R.id.item_tv);
            ivAppIcon=(ImageView)convertView.findViewById(R.id.item_img);
            tvPid=(TextView)convertView.findViewById(R.id.item_tv_pid);
            tvMemSize=(TextView)convertView.findViewById(R.id.item_tv_memsize);
            tvProName=(TextView)convertView.findViewById(R.id.item_tv_pro_name);
        }
    }
}
