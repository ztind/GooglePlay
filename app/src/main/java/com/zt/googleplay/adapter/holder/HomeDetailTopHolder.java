package com.zt.googleplay.adapter.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zt.googleplay.R;
import com.zt.googleplay.bean.AppInfo;
import com.zt.googleplay.http.HttpHelper;
import com.zt.googleplay.utils.MyBitmapUtils;
import com.zt.googleplay.utils.UIUtils;

/**
 * Author: ZT on 2017/1/2.
 */
public class HomeDetailTopHolder extends BaseViewHolder<AppInfo> {
    private ImageView appIcon;
    private TextView appName,downloadNum,appVersion,appDate, appSize;
    private RatingBar ratingBar;
    private BitmapUtils myBitmapUtils;
    @Override
    protected View initView() {
        View view = UIUtils.inflate(R.layout.top_layout);
        appIcon = (ImageView) view.findViewById(R.id.detail_appicon);
        appName = (TextView) view.findViewById(R.id.detail_appname);
        downloadNum = (TextView) view.findViewById(R.id.detail_downloadnumber);
        appVersion = (TextView) view.findViewById(R.id.detail_version);
        appDate = (TextView) view.findViewById(R.id.detail_date);
        appSize = (TextView) view.findViewById(R.id.detail_size);
        ratingBar = (RatingBar) view.findViewById(R.id.detail_starts);
        myBitmapUtils = MyBitmapUtils.getInstanceBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        myBitmapUtils.display(appIcon, HttpHelper.URL+"image?name="+data.iconUrl);
        appName.setText(data.name);
        ratingBar.setRating(data.stars);
        downloadNum.setText("下载量:"+data.downloadNum);
        appVersion.setText("版本号:"+data.version);
        appDate.setText(data.date);
        appSize.setText(Formatter.formatFileSize(UIUtils.getContext(),data.size));//将一个long类型的数据转化为单位为MB的数据大小
    }
}
