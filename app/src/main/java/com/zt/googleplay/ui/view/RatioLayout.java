package com.zt.googleplay.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.zt.googleplay.R;

/**
 * Author: ZT on 2016/12/31.
 * ratio：比例
 * 自定义view：
 *   解决图片比例适配问题,改控件包裹的ImageView就无需设置中心裁剪和 X，Y拉伸，
 *  即可根据，该控件设置的ration宽高比例，完美视频图片原有的大小宽高比。
 */
public class RatioLayout extends FrameLayout {

    private  float ratio;//当前类控件的宽高比率

    public RatioLayout(Context context) {
        super(context);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取xml里的自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        //id=属性名_属性字段名（此id系统自动生成）
        ratio = typedArray.getFloat(R.styleable.RatioLayout_ratio, -1);

        typedArray.recycle();//回收typedArray，提高性能

    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //重写onMeasure方法重新测量，最后在将测量的值交由父类去处理
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //widthMeasureSpec ：模式+实际宽度值组成
        //heightMeasureSpec：模式+实际高度值组成

        //3种模式
//        MeasureSpec.AT_MOST 至多模式，内容有多大就包裹多大 类似于wrap_content
//        MeasureSpec.EXACTLY 确定模式，宽高值确定，也类似于match_parent
//        MeasureSpec.UNSPECIFIED 不确定模式，宽高值不确定

        //获取模式和当前类控件实际宽高
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //当前控件若宽度确定，高度不确定，ratio合法.则高度值为
        if(widthMode==MeasureSpec.EXACTLY && heightMode!=MeasureSpec.EXACTLY && ratio>0){

            int imageWidth = width - getPaddingLeft() - getPaddingRight();//图片宽度
            int imageHeight = (int) (imageWidth / ratio+0.5f);//图片高度

            //当前控件的高度
            height = imageHeight + getPaddingTop() + getPaddingBottom();

            //根据控件的实际高度，重新生成 heightMeasureSpec（模式+高度）
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);//高度确定模式
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);//最后在将测量的值交由父类去处理
    }
}
