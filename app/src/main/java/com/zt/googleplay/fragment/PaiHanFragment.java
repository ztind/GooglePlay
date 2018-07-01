package com.zt.googleplay.fragment;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zt.googleplay.http.protocol.PaiHanProtocol;
import com.zt.googleplay.ui.view.CheckStateTextView;
import com.zt.googleplay.ui.view.FlowLayout;
import com.zt.googleplay.ui.view.LoadPager;
import com.zt.googleplay.utils.DrawableUtils;
import com.zt.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Author: ZT on 2016/12/27.
 * 使用FlowLayout实现效果
 */
public class PaiHanFragment extends BaseFragment {
    private ArrayList<String> data;

//    @Override
//    protected View onCreateSuccessView() {
//        ScrollView scrollView = new ScrollView(UIUtils.getContext());
//        FlowLayout flowLayout = new FlowLayout(UIUtils.getContext());
//        int padding = UIUtils.dip2px(10);
//        int padding2 = UIUtils.dip2px(5);
//        int padding3 = UIUtils.dip2px(2);
//        flowLayout.setPadding(padding, padding, padding, padding);
//        flowLayout.setVerticalSpacing(10);
//        flowLayout.setHorizontalSpacing(10);
//        for (int i=0;i<data.size();i++){
//            final TextView textView = new TextView(UIUtils.getContext());
//            textView.setTextColor(Color.WHITE);
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
//            textView.setText(data.get(i));
//            textView.setGravity(Gravity.CENTER);
//            textView.setPadding(padding2,padding3,padding2,padding3);
//            //代码动态设置textview的背景状态选择器
//            Random random = new Random();
//            //随机颜色 30--230
//            int r = 30 + random.nextInt(200);
//            int g = 30 + random.nextInt(200);
//            int b = 30 + random.nextInt(200);
//            int normalColor = Color.rgb(r, g, b);
//            int pressColor =Color.BLUE;
//            StateListDrawable selector = DrawableUtils.getSelector(normalColor, pressColor, 16);
//            textView.setBackgroundDrawable(selector);//添加布局选择器
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(UIUtils.getContext(), textView.getText(), Toast.LENGTH_SHORT).show();
//                }
//            });
//            //添加到流布局里
//            flowLayout.addView(textView);
//        }
//        scrollView.addView(flowLayout);
//        return scrollView;
//    }

    private CheckStateTextView lastCheckTv;
    @Override
    protected View onCreateSuccessView() {
        ScrollView scrollView = new ScrollView(UIUtils.getContext());
        final FlowLayout flowLayout = new FlowLayout(UIUtils.getContext());
        int padding = UIUtils.dip2px(10);
        flowLayout.setPadding(padding, padding, padding, padding);
        flowLayout.setVerticalSpacing(10);
        flowLayout.setHorizontalSpacing(10);

        for (int i=0;i<data.size();i++){
            CheckStateTextView textView = new CheckStateTextView(UIUtils.getContext(),Color.GRAY,Color.BLACK,10,UIUtils.dip2px(5),UIUtils.dip2px(3));
            textView.setText(data.get(i));
            textView.setITvClickListener(new CheckStateTextView.ITvClickListener() {
                @Override
                public void tvClick(CheckStateTextView tv) {
                    if(lastCheckTv!=null)lastCheckTv.setShape(Color.GRAY,Color.BLACK);
                    tv.setShape(Color.RED,Color.WHITE);
                    lastCheckTv = tv;
                }
            });
            //添加到流布局里
            flowLayout.addView(textView);
        }
        scrollView.addView(flowLayout);
        return scrollView;
    }

    @Override
    protected LoadPager.ResultState onLoadData() {
//        PaiHanProtocol paiHanProtocol = new PaiHanProtocol();
//        data = paiHanProtocol.getData(0);
//        return check(data);
        data = new ArrayList<>();
        data.add("年内");
        data.add("年内");
        data.add("年内");
        data.add("年内");
        data.add("年内");
        data.add("年内");
        data.add("年内");
        data.add("年金搭建7");
        data.add("年金搭建4");
        data.add("年内");
        data.add("年内");
        data.add("年金搭建7");
        data.add("年内");
        data.add("年金搭建7");
        data.add("年内");
        data.add("年金搭建1");
        data.add("年金搭");
        data.add("年建");
        data.add("年金搭建2");
        data.add("年金3搭建");
        data.add("年金搭建5");
        data.add("年金搭建6");
        data.add("年金搭建8");
        data.add("年金搭建8");
        data.add("年金搭建8");
        return LoadPager.ResultState.STATE_SUCCESS;
    }
}
