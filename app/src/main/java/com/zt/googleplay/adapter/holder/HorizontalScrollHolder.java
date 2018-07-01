package com.zt.googleplay.adapter.holder;

import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.zt.googleplay.R;
import com.zt.googleplay.bean.AppInfo;
import com.zt.googleplay.http.HttpHelper;
import com.zt.googleplay.utils.MyBitmapUtils;
import com.zt.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Author: ZT on 2017/1/3.
 */
public class HorizontalScrollHolder extends BaseViewHolder<AppInfo> {
    private ImageView[] imageViews;
    private BitmapUtils bitmapUtils;
    @Override
    protected View initView() {
        View view = UIUtils.inflate(R.layout.detail_horizontalscroll);
        imageViews = new ImageView[5];
        imageViews[0] = (ImageView) view.findViewById(R.id.screen_image1);
        imageViews[1] = (ImageView) view.findViewById(R.id.screen_image2);
        imageViews[2] = (ImageView) view.findViewById(R.id.screen_image3);
        imageViews[3] = (ImageView) view.findViewById(R.id.screen_image4);
        imageViews[4] = (ImageView) view.findViewById(R.id.screen_image5);
        bitmapUtils = MyBitmapUtils.getInstanceBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        ArrayList<String> screenList = data.screenList;
        for (int i=0;i<5;i++){
            if(i<screenList.size()){
                bitmapUtils.display(imageViews[i], HttpHelper.URL + "image?name=" + screenList.get(i));
            }
        }
    }
}
