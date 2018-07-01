package com.zt.googleplay.fragment;

import android.os.SystemClock;
import android.view.View;

import com.zt.googleplay.adapter.ListBaseAdapter;
import com.zt.googleplay.adapter.holder.BaseViewHolder;
import com.zt.googleplay.adapter.holder.ZuantiHolder;
import com.zt.googleplay.bean.Subject;
import com.zt.googleplay.http.protocol.ZhuntiProtocol;
import com.zt.googleplay.ui.view.LoadPager;
import com.zt.googleplay.ui.view.MyListView;
import com.zt.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 专题页
 * Author: ZT on 2016/12/27.
 */
public class ZhuantiFragment extends BaseFragment {

    private ArrayList<Subject> data;

    @Override
    protected View onCreateSuccessView() {
        MyListView myListView = new MyListView(UIUtils.getContext());
        myListView.setAdapter(new ZhuntiAdapter(data));
        return myListView;
    }

    @Override
    protected LoadPager.ResultState onLoadData() {
        ZhuntiProtocol zhuntiProtocol = new ZhuntiProtocol();
        data = zhuntiProtocol.getData(0);
        return check(data);
    }

    class ZhuntiAdapter extends ListBaseAdapter<Subject>{

        public ZhuntiAdapter(List<Subject> data) {
            super(data);
        }

        @Override
        protected BaseViewHolder getBaseViewHolder(int position) {
            return new ZuantiHolder();
        }

        @Override
        protected List<Subject> onLoadMoredata() {
            ZhuntiProtocol zhuntiProtocol = new ZhuntiProtocol();
            ArrayList<Subject> moreData = zhuntiProtocol.getData(getListSize());
            SystemClock.sleep(2000);
            return moreData;
        }
    }
}
