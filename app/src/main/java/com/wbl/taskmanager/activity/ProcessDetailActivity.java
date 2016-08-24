package com.wbl.taskmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbl.taskmanager.R;
import com.wbl.taskmanager.base.BaseActivity;
import com.wbl.taskmanager.models.ProcessInfo;


public class ProcessDetailActivity extends BaseActivity {
    private ImageView tvAppIcon;//图标
    private TextView tvAppName;//
    private TextView tvServiceCount;
    private TextView tvMemSize;
    private TextView tvTimer;
    private ProcessInfo processInfo;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_detail);

        intent=getIntent();
        if(processInfo!=null){
        }
        initView();
    }

    private void initView() {

        tvAppIcon=(ImageView)findViewById(R.id.detail_icon);
        tvAppName=(TextView)findViewById(R.id.detail_title);
        tvServiceCount=(TextView)findViewById(R.id.detail_service_count);
        tvMemSize=(TextView)findViewById(R.id.detail_process_size);
        tvTimer=(TextView)findViewById(R.id.detail_process_timer);
    }
}
