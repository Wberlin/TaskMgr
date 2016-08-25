package com.wbl.taskmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.wbl.taskmanager.R;
import com.wbl.taskmanager.models.ServiceInfo;

import java.util.List;

/**
 * Created by djtao on 2016/8/22.
 */

public class GridView2Adapter extends BaseSwipeAdapter {
    private Context mContext;
    private List<ServiceInfo> serviceInfos=null;
    public GridView2Adapter(Context context,List<ServiceInfo> serviceInfos) {
        this.mContext=context;
        this.serviceInfos=serviceInfos;
    }
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {

        return LayoutInflater.from(mContext).inflate(R.layout.gridview_item_2,null);
    }

    @Override
    public void fillValues(final int position, View convertView) {
        TextView tvPid=(TextView)convertView.findViewById(R.id.item2_tv_pid);
        TextView tvServiceName=(TextView)convertView.findViewById(R.id.item2_tv_service_name);
        TextView tvProcessName=(TextView)convertView.findViewById(R.id.item2_tv_process_name);
        TextView tvTimer=(TextView)convertView.findViewById(R.id.item2_tv_timer);



        tvPid.setText("Pid:"+serviceInfos.get(position).getPid());
        tvServiceName.setText("服务名:"+serviceInfos.get(position).getServicename());
        tvProcessName.setText("进程名："+serviceInfos.get(position).getProcessname());

        //tvTimer.setTimeData(serviceInfos.get(position).getActivesince());
        //tvTimer.start();

    }

    @Override
    public int getCount() {
        return serviceInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return serviceInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
