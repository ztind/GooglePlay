package com.zt.googleplay.adapter.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zt.googleplay.R;
import com.zt.googleplay.bean.AppInfo;
import com.zt.googleplay.http.HttpHelper;
import com.zt.googleplay.utils.MyBitmapUtils;
import com.zt.googleplay.utils.UIUtils;

/**
 * 应用item Holder
 * Author: ZT on 2016/12/31.
 */
public class AppHolder extends BaseViewHolder<AppInfo> {

    private ImageView appIcon,downImage;
    private TextView appName,appSize, downText,appDes;
    private RatingBar appStars;

    //加载listview的item的布局，init控件
    @Override
    protected View initView() {
        View view = UIUtils.inflate(R.layout.list_item_home);
        initFindView(view);
        return view;
    }

    //空item的控件赋值
    @Override
    public void refreshView(final AppInfo data) {
        MyBitmapUtils.getInstanceBitmapUtils().display(appIcon, HttpHelper.URL+"image?name="+data.iconUrl);
        appName.setText(data.name);
        String size = Formatter.formatFileSize(UIUtils.getContext(), data.size);//long格式转化为MB文件格式
        appSize.setText(size);
        appDes.setText(data.des);
        appStars.setRating(data.stars);//星星数

//        downImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(UIUtils.getContext(), "下载 "+data.name, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    //init 控件
    private void initFindView(View view){
        appIcon = (ImageView) view.findViewById(R.id.list_home_icon);
//        downImage = (ImageView) view.findViewById(R.id.list_home_downImage);
        appName = (TextView) view.findViewById(R.id.list_home_appname);
        appStars = (RatingBar) view.findViewById(R.id.list_home_starts);
        downText = (TextView) view.findViewById(R.id.list_home_downText);
        appDes = (TextView) view.findViewById(R.id.list_home_appdes);
        appSize = (TextView) view.findViewById(R.id.list_home_appsize);
    }
}
