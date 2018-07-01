package com.zt.googleplay.fragment;

import android.os.SystemClock;
import android.view.View;

import com.zt.googleplay.adapter.ListBaseAdapter;
import com.zt.googleplay.adapter.holder.AppHolder;
import com.zt.googleplay.adapter.holder.BaseViewHolder;
import com.zt.googleplay.bean.AppInfo;
import com.zt.googleplay.http.protocol.AppProtocol;
import com.zt.googleplay.ui.view.LoadPager;
import com.zt.googleplay.ui.view.MyListView;
import com.zt.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ZT on 2016/12/27.
 */
public class AppFragment extends BaseFragment {
    private AppAdapter appAdapter;
    private ArrayList<AppInfo> data;
    @Override
    protected View onCreateSuccessView() {
        MyListView myListView = new MyListView(UIUtils.getContext());
        myListView.setAdapter(appAdapter=new AppAdapter(data));
        return myListView;
    }

    //子线程加载网络数据
    @Override
    protected LoadPager.ResultState onLoadData() {
        AppProtocol appProtocol = new AppProtocol();
         data = appProtocol.getData(0);
        return check(data);
    }

    class AppAdapter extends ListBaseAdapter<AppInfo>{

        public AppAdapter(List<AppInfo> data) {
            super(data);
        }

        @Override
        protected BaseViewHolder getBaseViewHolder(int position) {
            return new AppHolder();
        }

        //加载更多数据
        @Override
        protected List<AppInfo> onLoadMoredata() {
            AppProtocol appProtocol = new AppProtocol();
            ArrayList<AppInfo> data = appProtocol.getData(getListSize());
            SystemClock.sleep(2000);
            return data;
        }
    }
}
