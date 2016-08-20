package com.wbl.taskmanager.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djtao on 2016/7/19.
 */

public abstract class BaseListViewAdapter<T> extends BaseAdapter {
    public List<T> mList=new ArrayList<>();
    public Activity mActivity;
    public ListView mListView;

    public BaseListViewAdapter(Activity activity){
        super();
        this.mActivity=activity;
    }

    /**
     * 清空适配器数据
     */
    public void clearList() {
        if (!(mList==null||mList.size()==0)) {
            mList.clear();
        }
    }
    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 自定义getView 布局内容
     */
    abstract public View getView(int position, View convertView, ViewGroup parent);

    /**
     * 更新数据源<br/>
     * List<T> --> 刷新
     *
     * @param list
     */
    public void setList(List<T> list) {
        if (list == null ) throw new NullPointerException("List data is null !");

        mList.addAll(list);
        ListView listView = getListView();
        if (listView != null) {
            listView.setAdapter(this);
        }
    }

    /**
     * 更新数据源<br/>
     * T[] --> 刷新
     *
     * @param list
     */
    public void setList(T[] list) {
        ArrayList<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setList(arrayList);
    }
    public List<T> getList() {
        return mList;
    }

    /**
     * 设置ListView控件
     *
     * @param listView
     */
    public void setListView(ListView listView) {
        if (listView == null) throw new NullPointerException("ListView view is null!");
        mListView = listView;
    }
    /**
     * 获取ListView控件
     *
     * @return
     */
    public ListView getListView() {
        return mListView;
    }
    /**
     * 获取指定LayoutID的View布局
     *
     * @param layoutId
     * @return
     */
    public View getView(int layoutId) {
        return getLayoutInflater().inflate(layoutId, null);

    }
    /**
     * 获取LayoutInflater对象
     *
     * @return
     */
    public LayoutInflater getLayoutInflater() {
        return mActivity.getLayoutInflater();
    }

}
