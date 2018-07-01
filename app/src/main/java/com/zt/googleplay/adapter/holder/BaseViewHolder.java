package com.zt.googleplay.adapter.holder;

import android.view.View;

/**
 * Author: ZT on 2016/12/28.
 */
public abstract class BaseViewHolder<T> {
    private View itemRootView;
    private T data;
    //完成加载布局，findViewById,setTag
    public BaseViewHolder(){
        itemRootView = initView();
        itemRootView.setTag(this);
    }

    public T getData() {
        return data;
    }
    public void setData(T data){
        this.data = data;
        //赋值控件
        refreshView(data);
    }
    //返回view布局
    public View getRootView(){
        return itemRootView;
    }

    protected abstract View initView();

    public abstract void refreshView(T data);


}
