package com.zt.googleplay.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.zt.googleplay.adapter.holder.BaseViewHolder;
import com.zt.googleplay.adapter.holder.MoreHolder;
import com.zt.googleplay.manager.ThreadManager;
import com.zt.googleplay.utils.UIUtils;

import java.util.List;

/**
 * Author: ZT on 2016/12/28.
 * <T> :java类对象的泛型
 */
public abstract class ListBaseAdapter<T> extends BaseAdapter {
    private List<T> data;
    private static final int YYPE_MORE=0;//加载更多类型
    private static final int TYPE_NORMAL=1; //正常类型

    public ListBaseAdapter(List<T> data){
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size()+1;//加上加载更多布局item
    }

    @Override
    public T getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //获取每个item的布局类型
    @Override
    public int getItemViewType(int position) {
        if(position == getCount()-1){ //显示最后一个item才显示加载更多布局
            return YYPE_MORE;
        }else {
            return getInnerType(position);
        }
    }
    //子类可以重写此方法来更改类型
    public int getInnerType(int position){
        return TYPE_NORMAL;//默认正常类型
    }
    //获取item一共有多少种类型
    @Override
    public int getViewTypeCount() {
        return 2;//此处有2种
    }

    @Override
    public View getView(int i, View contentView, ViewGroup viewGroup) {
        BaseViewHolder vh;
        if(contentView==null){
            if(getItemViewType(i)==YYPE_MORE){
                vh = new MoreHolder(getMoreHolderState()); //加载更多布局*****************
            }else {
                vh = getBaseViewHolder(i);//完成加载布局，findviewById ,setTag【根据传入的id，可以实现不同的item布局*********】
            }
        }else {
            vh = (BaseViewHolder) contentView.getTag();
        }
        //设置数据,赋值控件
        if(getItemViewType(i)!=YYPE_MORE){
            vh.setData(getItem(i));
        }else {
          //加载更多布局的控件无需赋值。在显示加载更多布局时 其加载数据
            MoreHolder moreHolder = (MoreHolder) vh;
            if(moreHolder.getData()==MoreHolder.STATE_MORE_MORE){
                loadMoreDate(moreHolder);
            }
        }
        return vh.getRootView();//返回视图.//contentView：vh.itemRootView
    }
    public boolean getMoreHolderState(){
        return true;//默认加载更多,子类可以实现更改
    }
    protected abstract BaseViewHolder getBaseViewHolder(int position);

    private boolean isLoadMore;//标记当前是否正在加载数据
    //加载更逗数据
    private void loadMoreDate(final MoreHolder moreHolder){
        if(isLoadMore){
            return;
        }
        isLoadMore = true;

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final List<T> moreData = onLoadMoredata();
//                UIUtils.runOnMainThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(moreData!=null){
//                            //若加载返回的数据小于20，则说明没有更多数据了
//                            if (moreData.size()<20){
//                                moreHolder.setData(MoreHolder.STATE_MORE_NONE);
//                                Toast.makeText(UIUtils.getContext(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
//                            }else {
//                                moreHolder.setData(MoreHolder.STATE_MORE_MORE);
//                            }
//                            //刷新列表数据
//                            data.addAll(moreData);
//                            ListBaseAdapter.this.notifyDataSetChanged();
//                        }else {
//                            //数据加载失败，刷新加载更多布局UI
//                            moreHolder.setData(MoreHolder.STATE_MORE_ERROR);
//                        }
//                        isLoadMore = false;
//                    }
//                });
//            }
//        }).start();

        //使用线程池来管理线程
        ThreadManager.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final List<T> moreData = onLoadMoredata();
                UIUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if(moreData!=null){
                            //若加载返回的数据小于20，则说明没有更多数据了
                            if (moreData.size()<20){
                                moreHolder.setData(MoreHolder.STATE_MORE_NONE);
                                Toast.makeText(UIUtils.getContext(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
                            }else {
                                moreHolder.setData(MoreHolder.STATE_MORE_MORE);
                            }
                            //刷新列表数据
                            data.addAll(moreData);
                            ListBaseAdapter.this.notifyDataSetChanged();
                        }else {
                            //数据加载失败，刷新加载更多布局UI
                            moreHolder.setData(MoreHolder.STATE_MORE_ERROR);
                        }
                        isLoadMore = false;
                    }
                });
            }
        });

    }

    //抽象加载更多数据的方法由其子类实现，各自加载数据的业务
    protected abstract List<T> onLoadMoredata();

    public int getListSize(){
        return data.size();
    }
}
