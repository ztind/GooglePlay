package com.zt.googleplay.adapter.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.BitmapUtils;
import com.zt.googleplay.R;
import com.zt.googleplay.http.HttpHelper;
import com.zt.googleplay.utils.MyBitmapUtils;
import com.zt.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Author: ZT on 2017/1/1.
 * 头布局Ui采用全java代码方式实现
 */
public class HomeHeaderHolder extends BaseViewHolder<ArrayList<String>> {

    private BitmapUtils bitmapUtils;
    private ViewPager vp;
    private LinearLayout indicator;

    @Override
    protected View initView() {

        bitmapUtils = MyBitmapUtils.getInstanceBitmapUtils();

        RelativeLayout rootView = new RelativeLayout(UIUtils.getContext());
        //此处RelativeLayout的父容器为listview，故应获取listview的布局参数
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, UIUtils.dip2px(180));
        rootView.setLayoutParams(lp);

        //ViewPager
        vp = new ViewPager(UIUtils.getContext());
        rootView.addView(vp);

        //初始化指示器
        indicator = new LinearLayout(UIUtils.getContext());
        indicator.setOrientation(LinearLayout.HORIZONTAL);
        int padding = UIUtils.dip2px(10);
        indicator.setPadding(padding,padding,padding,padding);
        //设置指示器的布局参数(即指示器在父控件里的位置布局)
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        //为layoutParams添加规则
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        indicator.setLayoutParams(layoutParams);

        rootView.addView(indicator);

        return rootView;
    }

    private int lastSelectPosition;
    @Override
    public void refreshView(final ArrayList<String> data) {
        vp.setAdapter(new HomeHeaderVpAdapter());

        //实例指示器
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i=0;i<data.size();i++){
            ImageView point = new ImageView(UIUtils.getContext());
            if(i==0){
                point.setBackgroundResource(R.mipmap.indicator_selected);
            }else {
                point.setBackgroundResource(R.mipmap.indicator_normal);
                lp.leftMargin = UIUtils.dip2px(4);
                point.setLayoutParams(lp);
            }
            indicator.addView(point);
        }
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //程序运行后，默认会回调一次此方法
            @Override
            public void onPageSelected(int position) {
                position = position % data.size(); //获取当前图片的位置

                ImageView lastPoint = (ImageView) indicator.getChildAt(lastSelectPosition);
                ImageView currentPoint = (ImageView) indicator.getChildAt(position);

                //前一个点没有选中状态，当前点选中
                lastPoint.setBackgroundResource(R.mipmap.indicator_normal);
                currentPoint.setBackgroundResource(R.mipmap.indicator_selected);

                lastSelectPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //长按vp的item停止此item的滑动
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                switch (ev.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //移除handler里的所有消息
                        UIUtils.getHandler().removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_UP://手指抬起
                        UIUtils.getHandler().postDelayed(myHandlerTask, 3000);
                        break;
                    case MotionEvent.ACTION_CANCEL://手指非vp区域抬起，若滑动到listview的列表后抬起
                        UIUtils.getHandler().postDelayed(myHandlerTask, 3000);
                        break;
                }
                return false;
            }
        });

        //3，默认第一张，集合大小*10000
        vp.setCurrentItem(data.size()*10000);
        //vp图片无限循环
        myHandlerTask = new MyHandlerTask();
        myHandlerTask.start();
    }

    private MyHandlerTask myHandlerTask;

    private int currentPosition;
    class MyHandlerTask implements Runnable{
        public void start(){
            //移除所有消息
            UIUtils.getHandler().removeCallbacksAndMessages(null);
            //3s后发送消息
            UIUtils.getHandler().postDelayed(this, 3000);
        }
        @Override
        public void run() {
            currentPosition = vp.getCurrentItem();
            currentPosition++;
            vp.setCurrentItem(currentPosition);
            //又发送消息，达到无限循环
            UIUtils.getHandler().postDelayed(this, 3000);
        }
    }

    class HomeHeaderVpAdapter extends PagerAdapter{
        private ArrayList<String> list;
        public HomeHeaderVpAdapter(){
            this.list = getData();
        }
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;//1，模拟viewpager的无限轮播
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            //2，对position起集合的余数
            position = position % list.size();

            ImageView imageView = new ImageView(UIUtils.getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);//xy填充
            bitmapUtils.display(imageView, HttpHelper.URL+"image?name="+list.get(position));
            container.addView(imageView);

            //若item设置了点击事件，则vp的长按停止也就不会生效了。因为苹果被他儿子吃了
//            final int finalPosition = position;
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(UIUtils.getContext(), list.get(finalPosition), Toast.LENGTH_SHORT).show();
//                }
//            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View) object);
        }
    }
}
