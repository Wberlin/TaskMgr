package com.wbl.taskmanager.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbl.taskmanager.R;
import com.wbl.taskmanager.base.BaseListViewAdapter;
import com.wbl.taskmanager.models.ServiceInfo;
import com.wbl.taskmanager.utils.DateUtil;

/**
 * Created by djtao on 2016/8/25.
 */

public class ServiceAdapter extends BaseListViewAdapter<ServiceInfo> {
    public ServiceAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       ViewHolder vHolder;

        if(convertView==null){
            convertView=getView(R.layout.item_lv_service);
            vHolder=new ViewHolder(convertView);
            convertView.setTag(vHolder);
        }else{
            vHolder=(ViewHolder) convertView.getTag();
        }

        final ServiceInfo serviceInfo=mList.get(position);
        vHolder.tvName.setText(serviceInfo.getServicename().substring(
                serviceInfo.getServicename().lastIndexOf(".")+1,
                serviceInfo.getServicename().length()));
        String time= DateUtils.formatElapsedTime(new StringBuilder(128),
                (SystemClock.elapsedRealtime()-serviceInfo.getActivesince())/1000);
        vHolder.tvTime.setText(time);
        vHolder.ivIcon.setImageDrawable(serviceInfo.getPackageIcon());
        vHolder.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG","ServiceAdapter->"+position);

                AlertDialog dialog=new AlertDialog.Builder(mActivity)
                        .setTitle("停止系统服务?")
                        .setMessage("如果您停止该服务，设备的部分功能可能停止正常运作直至您关机然后再次开机")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    if(serviceInfo.getServiceIntent()!=null){
                                        boolean isSuccess=mActivity.stopService(serviceInfo.getServiceIntent());
                                        if(isSuccess){
                                            mList.remove(serviceInfo);
                                            notifyDataSetChanged();
                                        }

                                    }

                                }catch (SecurityException e){
                                    e.printStackTrace();
                                    new AlertDialog.Builder(mActivity).setTitle("权限不够")
                                            .setMessage("对不起，您的权限不够,无法停止Service")
                                            .create().show();
                                }

                            }
                        })
                        .setNegativeButton("取消",null).create();
                dialog.show();

            }
        });
        return convertView;
    }


    class ViewHolder{
        private TextView tvName;
        private ImageView ivIcon;
        private TextView tvTime;
        private TextView tvDecr;
        private Button btnStop;

        public ViewHolder(View convertView){
            tvName=(TextView)convertView.findViewById(R.id.tv_service_name);
            tvTime=(TextView)convertView.findViewById(R.id.tv_service_run_time);
            tvDecr=(TextView)convertView.findViewById(R.id.tv_service_description);
            btnStop=(Button)convertView.findViewById(R.id.btn_service_stop);
            ivIcon=(ImageView)convertView.findViewById(R.id.iv_service_icon);
        }

    }
}
