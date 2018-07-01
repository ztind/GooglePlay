package com.zt.googleplay.fragment;

import android.view.View;

import com.zt.googleplay.adapter.ListBaseAdapter;
import com.zt.googleplay.adapter.holder.BaseViewHolder;
import com.zt.googleplay.adapter.holder.CategoryHolder;
import com.zt.googleplay.bean.CategoryInfo;
import com.zt.googleplay.http.protocol.CategoryProtocol;
import com.zt.googleplay.ui.view.LoadPager;
import com.zt.googleplay.ui.view.MyListView;
import com.zt.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ZT on 2016/12/27.
 */
public class CategoryFragment extends BaseFragment {


    private ArrayList<CategoryInfo> data;

    @Override
    protected View onCreateSuccessView() {
        MyListView myListView = new MyListView(UIUtils.getContext());
        myListView.setAdapter(new MyAdapter(data));
        return myListView;
    }

    @Override
    protected LoadPager.ResultState onLoadData() {
        CategoryProtocol categoryProtocol = new CategoryProtocol();
        data = categoryProtocol.getData(0);
        return check(data);
    }

    class MyAdapter extends ListBaseAdapter<CategoryInfo>{

        public MyAdapter(List<CategoryInfo> data) {
            super(data);
        }

        //item view 封装 [可实现不同的item布局，根据返回的holder]
        @Override
        protected BaseViewHolder getBaseViewHolder(int position) {
            return new CategoryHolder();
        }

        @Override
        public boolean getMoreHolderState() {
            return false;//没有加载更多
        }

        //加载更多数据
        @Override
        protected List<CategoryInfo> onLoadMoredata() {
            return null;
        }


    }

}
