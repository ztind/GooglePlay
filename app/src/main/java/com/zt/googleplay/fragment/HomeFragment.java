package com.zt.googleplay.fragment;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;

import com.zt.googleplay.adapter.ListBaseAdapter;
import com.zt.googleplay.adapter.holder.BaseViewHolder;
import com.zt.googleplay.adapter.holder.HomeHeaderHolder;
import com.zt.googleplay.adapter.holder.HomeHolder;
import com.zt.googleplay.bean.AppInfo;
import com.zt.googleplay.http.protocol.HomeProtocol;
import com.zt.googleplay.ui.activity.HomeDetailActivity;
import com.zt.googleplay.ui.view.LoadPager;
import com.zt.googleplay.ui.view.MyListView;
import com.zt.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ZT on 2016/12/27.
 * 布局加载策略：先获取数据，数据获取成功才加载布局(优化了性能，内存优化)
 */
public class HomeFragment extends BaseFragment {

    private ArrayList<AppInfo> data;
    private ArrayList<String> headerData;
    private MyAdapter adapter;

    //当前onLoadData的返回值为Success成功时才回调此方法，加载布局更新UI。此方法运行在主线程
    @Override
    protected View onCreateSuccessView() {
        final MyListView listView = new MyListView(UIUtils.getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(position!=adapter.getListSize()+listView.getHeaderViewsCount()){ //过滤加载更多的点击事件
                     position = position - listView.getHeaderViewsCount();

                     Intent intent = new Intent( UIUtils.getContext(), HomeDetailActivity.class);
                     //将此item对应的javabean对象封装到intent里传递过去
                    intent.putExtra("packageName", adapter.getItem(position).packageName);
                     startActivity(intent);
                }

            }
        });
        //加载listview的vp头布局
        HomeHeaderHolder homeHeaderHolder = new HomeHeaderHolder();
        listView.addHeaderView(homeHeaderHolder.getRootView());
        //填充UI数据
        if(headerData!=null){
            homeHeaderHolder.setData(headerData);
        }

        listView.setAdapter(adapter =  new MyAdapter(data));

        return listView;
    }
    //运行在子线程，执行耗时的网络请求
    @Override
    protected LoadPager.ResultState onLoadData() {

        HomeProtocol homeProtocol = new HomeProtocol();
        data = homeProtocol.getData(0);
        headerData = homeProtocol.getHeaderData();

        return check(data);
    }


    class MyAdapter extends ListBaseAdapter<AppInfo>{

        public MyAdapter(List data) {
            super(data);
        }
        @Override
        protected BaseViewHolder getBaseViewHolder(int position) {
            return new HomeHolder();
        }

        //运行在子线程，执行耗时操作加载数据
        @Override
        protected List<AppInfo> onLoadMoredata() {
            HomeProtocol homeProtocol = new HomeProtocol();
            //index为父类的集合大小开始
            ArrayList<AppInfo> moreDat = homeProtocol.getData(getListSize());

            SystemClock.sleep(2000);
            return moreDat;
        }

    }
}
