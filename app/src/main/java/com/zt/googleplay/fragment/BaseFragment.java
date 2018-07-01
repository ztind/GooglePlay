package com.zt.googleplay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zt.googleplay.ui.view.LoadPager;
import com.zt.googleplay.utils.L;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/27.
 * 布局加载策略：先加载数据，数据加载成功才加载布局文件更新UI，若数据加载失败则没必要加载布局浪费性能和内存
 * onCreateView非空判断 二次加载时直接返回填充过数据的View
 */
public abstract class BaseFragment extends Fragment {
    private LoadPager myLoadPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(myLoadPager==null){
            L.v("---BaseFragment，new LoadPager对象啦--");
            myLoadPager = new LoadPager(getContext()) {
                //数据加载成功后才回调
                @Override
                public View onCreateSuccessView() {
                    L.v("load 布局：");
                    return BaseFragment.this.onCreateSuccessView();

                }
                //先
                @Override
                public LoadPager.ResultState onLoadData() {
                    L.v("load 数据：");
                    return BaseFragment.this.onLoadData();
                }
            };
        }else {
            L.v("缓存的pager布局不为空");
        }
        return myLoadPager;
    }

    //交由其子类实现各自的布局
    protected abstract View onCreateSuccessView();

    //子类实现数据加载
    protected abstract LoadPager.ResultState onLoadData();

    //检查获取的数据是否有效，从而返回相应的状态码
    protected LoadPager.ResultState check(Object obj){
        if(obj!=null){
            if(obj instanceof ArrayList){
                ArrayList list = (ArrayList) obj;
                if(list.isEmpty()){
                    return LoadPager.ResultState.STATE_EMPTY;
                }else {
                    return LoadPager.ResultState.STATE_SUCCESS;
                }
            }
        }
        return LoadPager.ResultState.STATE_FAIL;
    }
}
