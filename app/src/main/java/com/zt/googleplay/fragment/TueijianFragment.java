package com.zt.googleplay.fragment;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zt.googleplay.http.protocol.TueiJianProtocol;
import com.zt.googleplay.ui.fly.ShakeListener;
import com.zt.googleplay.ui.fly.StellarMap;
import com.zt.googleplay.ui.view.LoadPager;
import com.zt.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Author: ZT on 2016/12/27.
 * fly包里的类:StellarMap实现星星动画效果
 */
public class TueijianFragment extends BaseFragment {

    private ArrayList<String> data;

    @Override
    protected View onCreateSuccessView() {
        final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        stellarMap.setAdapter(new MyAdapter());
        //随机方式 6列9行
        stellarMap.setRegularity(6, 9);
        //设置内边距
        int padd = UIUtils.dip2px(10);
        stellarMap.setInnerPadding(padd,padd,padd,padd);
        //设置默认界面
        stellarMap.setGroup(0, true);//第一组播放动画

        //设置手机摇动是，更换组数据的效果
        final ShakeListener shakeListener = new ShakeListener(UIUtils.getContext());
        shakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                stellarMap.zoomIn();//跳到下一组数据
            }
        });
        return stellarMap;
    }

    @Override
    protected LoadPager.ResultState onLoadData() {
//        TueiJianProtocol tueiJianProtocol = new TueiJianProtocol();
//        data = tueiJianProtocol.getData(0);
//        return check(data);
        data = new ArrayList<>();
        data.add("年金搭建1");
        data.add("年金搭");
        data.add("年建");
        data.add("年金搭建2");
        data.add("年金3搭建");
        data.add("年金搭建4");
        data.add("年金搭建5");
        data.add("年金搭建6");
        data.add("年金搭建7");
        data.add("年金搭建8");
        return LoadPager.ResultState.STATE_SUCCESS;
    }

     class MyAdapter implements StellarMap.Adapter {

         //返回组的个数
         @Override
         public int getGroupCount() {
             return 2;
         }
         //返回某组的item的个数
         @Override
         public int getCount(int group) {
             int count = data.size() / getGroupCount();
             //将余出来的书添加到最后一个组里
             if(group==getGroupCount()-1){
                 count += data.size() % getGroupCount();
             }
             return count;
         }
        //初始化布局
         @Override
         public View getView(int group,  int position, View convertView) {
             //因为position每次滑动时都是都0开始，故从集合里获取的数据到是一样，所以要修改

             position += group * getCount(group - 1);

             final String itemString = data.get(position);
             TextView textView = new TextView(UIUtils.getContext());
             textView.setTextColor(Color.BLACK);
             textView.setText(itemString);

             Random random = new Random();
             //随机大小 16--25
             int size = 16 + random.nextInt(10);
             textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);

             //随机颜色 30--230
             int r = 30 + random.nextInt(201);
             int g = 30 + random.nextInt(201);
             int b = 30 + random.nextInt(201);
             int color = Color.rgb(r, g, b);
             textView.setTextColor(color);

             textView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Toast.makeText(UIUtils.getContext(),itemString, Toast.LENGTH_SHORT).show();
                 }
             });

             return textView;
         }

         //返回下一组的id
         @Override
         public int getNextGroupOnZoom(int group, boolean isZoomIn) {
//             Log.v("TAG", "-getNextGroupOnZoom-:" + group + "--" + isZoomIn);
             //isZoomIn 上滑false 下滑true
             if(isZoomIn){
                 //下滑加载上一组数据
                 if(group>0){
                     group--;
                 }else {
                     group = getGroupCount() - 1;//最后一组
                 }
             }else {
                 //上滑加载下一组数据
                 group++;
                 if(group==getGroupCount()){
                     group = 0;//回到第一组
                 }
             }
             return group;
         }
     }

}
