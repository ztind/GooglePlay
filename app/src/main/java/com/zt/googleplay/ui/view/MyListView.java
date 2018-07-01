package com.zt.googleplay.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Author: ZT on 2016/12/30.
 */
public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
        init();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        this.setSelector(new ColorDrawable());//设置listview默认状态选择器为全透明
        this.setDivider(null);//去除listview的分割线
        this.setCacheColorHint(Color.TRANSPARENT);//有时候列表上滑会有黑色的背景，添加此句解决
        this.setVerticalScrollBarEnabled(false);//去除滚动条
    }
}
