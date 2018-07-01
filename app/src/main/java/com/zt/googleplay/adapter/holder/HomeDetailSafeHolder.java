package com.zt.googleplay.adapter.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zt.googleplay.R;
import com.zt.googleplay.bean.AppInfo;
import com.zt.googleplay.http.HttpHelper;
import com.zt.googleplay.utils.MyBitmapUtils;
import com.zt.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Author: ZT on 2017/1/2.
 */
public class HomeDetailSafeHolder extends BaseViewHolder<AppInfo> implements View.OnClickListener {
    private ImageView[] mSafeIcons;
    private ImageView[] mDesIcons;
    private TextView[] mMessageTexts;
    private BitmapUtils bitmapUtils;
    private ImageView downImage;
    private LinearLayout detail_textMessage_linearlayout;
    private RelativeLayout detail_item_click;
    private boolean isShow;
    private LinearLayout.LayoutParams lp;
    private int contentHeight;

    @Override
    protected View initView() {
        View view = UIUtils.inflate(R.layout.detail_safe_layout);

        mSafeIcons = new ImageView[4];
        mSafeIcons[0] = (ImageView) view.findViewById(R.id.detail_image_safe1);
        mSafeIcons[1] = (ImageView) view.findViewById(R.id.detail_image_safe2);
        mSafeIcons[2] = (ImageView) view.findViewById(R.id.detail_image_safe3);
        mSafeIcons[3] = (ImageView) view.findViewById(R.id.detail_image_safe4);
        downImage = (ImageView) view.findViewById(R.id.detail_image_down);
        detail_item_click = (RelativeLayout) view.findViewById(R.id.detail_item_click);
        detail_item_click.setOnClickListener(this);

        mDesIcons = new ImageView[4];
        mDesIcons[0] = (ImageView) view.findViewById(R.id.detail_image_state1);
        mDesIcons[1] = (ImageView) view.findViewById(R.id.detail_image_state2);
        mDesIcons[2] = (ImageView) view.findViewById(R.id.detail_image_state3);
        mDesIcons[3] = (ImageView) view.findViewById(R.id.detail_image_state4);

        mMessageTexts = new TextView[4];
        mMessageTexts[0] = (TextView) view.findViewById(R.id.detail_text_message1);
        mMessageTexts[1] = (TextView) view.findViewById(R.id.detail_text_message2);
        mMessageTexts[2] = (TextView) view.findViewById(R.id.detail_text_message3);
        mMessageTexts[3] = (TextView) view.findViewById(R.id.detail_text_message4);


        detail_textMessage_linearlayout = (LinearLayout) view.findViewById(R.id.detail_textMessage_linearlayout);

        bitmapUtils = MyBitmapUtils.getInstanceBitmapUtils();

        return view;
    }
    @Override
    public void refreshView(AppInfo data) {

        //获取图片集
        ArrayList<AppInfo.SafeInfo> safeImageList = data.safeList;

        for (int i=0;i<4;i++){
            if(i<safeImageList.size()){
                AppInfo.SafeInfo safeData = safeImageList.get(i);
                bitmapUtils.display(mSafeIcons[i], HttpHelper.URL + "image?name=" + safeData.safeUrl);
                bitmapUtils.display(mDesIcons[i], HttpHelper.URL + "image?name=" + safeData.safeDesUrl);
                mMessageTexts[i].setText(safeData.safeDes);
            }else {
                mDesIcons[i].setVisibility(View.GONE);
                mMessageTexts[i].setVisibility(View.GONE);
            }
        }
        detail_textMessage_linearlayout.measure(0, 0);
        contentHeight = detail_textMessage_linearlayout.getMeasuredHeight();

        //获取布局参数
        lp = (LinearLayout.LayoutParams) detail_textMessage_linearlayout.getLayoutParams();
        lp.height = 0;
        detail_textMessage_linearlayout.setLayoutParams(lp);
    }

    @Override
    public void onClick(View view) {
        ValueAnimator animator; //属性动画
        if(!isShow){
            downImage.setImageDrawable(UIUtils.getDrawable(R.mipmap.arrow_up));
            animator = ValueAnimator.ofInt(0,contentHeight);
        }else {
            downImage.setImageDrawable(UIUtils.getDrawable(R.mipmap.arrow_down));
            animator = ValueAnimator.ofInt(contentHeight,0);
        }
        isShow = !isShow;


        //添加动画刷新接口
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            //此方法会不断的回调
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int hg = (int) valueAnimator.getAnimatedValue();
                lp.height = hg;
                detail_textMessage_linearlayout.setLayoutParams(lp); //从新设置其高度，实现动画展开和关闭效果
            }
        });
        //添加动画执行状态的回调监听接口
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        //设置动画时间
        animator.setDuration(200);
        //开始动画
        animator.start();
    }
}
