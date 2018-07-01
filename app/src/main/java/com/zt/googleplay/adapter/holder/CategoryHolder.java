package com.zt.googleplay.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zt.googleplay.R;
import com.zt.googleplay.bean.CategoryInfo;
import com.zt.googleplay.http.HttpHelper;
import com.zt.googleplay.utils.MyBitmapUtils;
import com.zt.googleplay.utils.StringUtils;
import com.zt.googleplay.utils.UIUtils;

/**
 * Author: ZT on 2017/1/1.
 */
public class CategoryHolder extends BaseViewHolder<CategoryInfo> implements View.OnClickListener{
    private TextView title,name1,name2, name3;
    private ImageView image1,image2, image3;
    private LinearLayout one,two, three;
    private View view;

    @Override
    protected View initView() {
        view = UIUtils.inflate(R.layout.list_item_category);
        initView(view);
        return view;
    }

    private void initView(View view) {
        title = (TextView) view.findViewById(R.id.categroy_title);
        name1 = (TextView) view.findViewById(R.id.categroy_name1);
        name2 = (TextView) view.findViewById(R.id.categroy_name2);
        name3 = (TextView) view.findViewById(R.id.categroy_name3);
        image1 = (ImageView) view.findViewById(R.id.categroy_image1);
        image2 = (ImageView) view.findViewById(R.id.categroy_image2);
        image3 = (ImageView) view.findViewById(R.id.categroy_image3);
        one = (LinearLayout) view.findViewById(R.id.cateroy_line_one);
        two = (LinearLayout) view.findViewById(R.id.cateroy_line_two);
        three = (LinearLayout) view.findViewById(R.id.cateroy_line_three);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);

    }

    @Override
    public void refreshView(final CategoryInfo data) {
        if(data.isTitle){
            title.setVisibility(View.VISIBLE);
            title.setText(data.title);
            one.setVisibility(View.GONE);
            two.setVisibility(View.GONE);
            three.setVisibility(View.GONE);
        }else {
            title.setVisibility(View.GONE);
            one.setVisibility(View.VISIBLE);
            two.setVisibility(View.VISIBLE);
            three.setVisibility(View.VISIBLE);

            MyBitmapUtils.getInstanceBitmapUtils().display(image1, HttpHelper.URL+"image?name="+data.url1);
            MyBitmapUtils.getInstanceBitmapUtils().display(image2, HttpHelper.URL+"image?name="+data.url2);
            MyBitmapUtils.getInstanceBitmapUtils().display(image3, HttpHelper.URL+"image?name="+data.url3);
            name1.setText(data.name1);
            name2.setText(data.name2);
            name3.setText(data.name3);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cateroy_line_one:
                Toast.makeText(UIUtils.getContext(),getData().name1, Toast.LENGTH_SHORT).show();
                break;
            case R.id.cateroy_line_two:
                Toast.makeText(UIUtils.getContext(),getData().name2, Toast.LENGTH_SHORT).show();
                break;
            case R.id.cateroy_line_three:
                if(!StringUtils.isEmpty(getData().url3)){
                    Toast.makeText(UIUtils.getContext(),getData().name3, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
