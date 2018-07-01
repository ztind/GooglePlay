package com.zt.googleplay.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.zt.googleplay.utils.DrawableUtils;

/**
 * Created by Administrator on 2018/7/2.
 * 自定义TextView实现 点击状态shape
 */

public class CheckStateTextView extends android.support.v7.widget.AppCompatTextView {
    private int radius;
    private int bgColor;
    private int tvColor;
    private int lrPadding, tbPadding;
    public CheckStateTextView(Context context,int bgColor,int tvColor,int radius,int lrPadding,int tbPadding) {
        super(context);
        this.bgColor = bgColor;
        this.tvColor = tvColor;
        this.radius = radius;
        this.lrPadding = lrPadding;
        this.tbPadding = tbPadding;
        init();
    }
    private void init(){
        this.setTextColor(tvColor);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        this.setGravity(Gravity.CENTER);
        this.setPadding(lrPadding,tbPadding,lrPadding,tbPadding);
        this.setBackgroundDrawable(DrawableUtils.getGadientDrawabe(bgColor,radius));

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setShape(Color.RED,Color.WHITE);
               if(iTvClickListener!=null){
                   iTvClickListener.tvClick((CheckStateTextView) v);
               }
            }
        });
    }

    //tv shape
    public void setShape(int bjColor,int tvColor){
        this.setBackgroundDrawable(DrawableUtils.getGadientDrawabe(bjColor,radius));
        this.setTextColor(tvColor);
    }

    public interface  ITvClickListener{
        void tvClick(CheckStateTextView textView);
    }
    private ITvClickListener iTvClickListener;
    public void setITvClickListener(ITvClickListener iTvClickListener){
        this.iTvClickListener = iTvClickListener;
    }
}
