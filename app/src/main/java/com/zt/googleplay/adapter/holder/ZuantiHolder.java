package com.zt.googleplay.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zt.googleplay.R;
import com.zt.googleplay.bean.Subject;
import com.zt.googleplay.http.HttpHelper;
import com.zt.googleplay.utils.MyBitmapUtils;
import com.zt.googleplay.utils.UIUtils;

/**
 * Author: ZT on 2016/12/31.
 */
public class ZuantiHolder extends BaseViewHolder<Subject> {
    private ImageView imageView;
    private TextView textView;
    @Override
    protected View initView() {
        View view = UIUtils.inflate(R.layout.list_item_zhuanti);
        imageView = (ImageView) view.findViewById(R.id.list_zhuanti_image);
        textView = (TextView) view.findViewById(R.id.list_zhuanti_des);
        return view;
    }

    @Override
    public void refreshView(Subject data) {
        MyBitmapUtils.getInstanceBitmapUtils().display(imageView, HttpHelper.URL+"image?name="+data.iconUrl);
        textView.setText(data.des);
    }
}
