package com.wbl.taskmanager.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.view.View;
import android.view.Window;




public class BaseActivity extends FragmentActivity implements View.OnClickListener{




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //添加Activity到堆栈
        ((MyApplication)getApplication()).addActivity(this);

    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().finishActivity(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v){

    }

    /**
     * 当无参数需要传递时，可调用单参的该函数
     * @param pClass
     */
    protected void openActivity(Class<?> pClass){
        openActivity(pClass,null);
    }

    /**
     * 当无参数需要传递,但有返回结果，可向Bundle传递null
     * @param pClass
     * @param requestCode
     */
    protected  void openActivity(Class<?> pClass,int requestCode){
        openActivity(pClass,null,requestCode);
    }

    /**
     * 传递不带返回值的意图
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass,Bundle pBundle){
        Intent i=new Intent(this,pClass);
        if(pBundle!=null){
            i.putExtras(pBundle);
        }
        startActivity(i);
    }

    /**
     * 传递带有返回值的activity，简化了意图的代码
     * @param pClass
     * @param pBundle
     * @param requestCode
     */
    protected void openActivity(Class<?> pClass,Bundle pBundle,int requestCode){
        Intent i=new Intent(this,pClass);
        if(pBundle!=null){
            i.putExtras(pBundle);
        }
        startActivityForResult(i,requestCode);
    }

}
