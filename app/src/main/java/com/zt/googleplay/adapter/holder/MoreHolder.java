package com.zt.googleplay.adapter.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zt.googleplay.R;
import com.zt.googleplay.utils.UIUtils;

/**
 * Author: ZT on 2016/12/28.
 */
public class MoreHolder extends BaseViewHolder<Integer> {
    public static final int STATE_MORE_MORE = 1;//加载更多状态
    public static final int STATE_MORE_NONE = 2; //没有更多数据
    public static final int STATE_MORE_ERROR = 3; //加载更多数据失败

    private LinearLayout loadMoreLL;
    private TextView loadMoreError;
    public MoreHolder(boolean state){
        setData(state?STATE_MORE_MORE:STATE_MORE_NONE);//根据传入的状态值刷新UI
    }
    @Override
    protected View initView() {
        View moreView = UIUtils.inflate(R.layout.item_more);
        loadMoreLL = (LinearLayout) moreView.findViewById(R.id.more_load_more);
        loadMoreError = (TextView) moreView.findViewById(R.id.more_load_error);
        return moreView;
    }

    @Override
    public void refreshView(Integer data) {
        switch (data){
            case STATE_MORE_MORE:
                loadMoreLL.setVisibility(View.VISIBLE);
                loadMoreError.setVisibility(View.GONE);
                break;
            case STATE_MORE_NONE:
                loadMoreLL.setVisibility(View.GONE);
                loadMoreError.setVisibility(View.GONE);
                break;
            case STATE_MORE_ERROR:
                loadMoreLL.setVisibility(View.GONE);
                loadMoreError.setVisibility(View.VISIBLE);
                break;
        }
    }

}
