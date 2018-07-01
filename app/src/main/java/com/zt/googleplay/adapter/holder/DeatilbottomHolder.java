package com.zt.googleplay.adapter.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zt.googleplay.R;
import com.zt.googleplay.bean.AppInfo;
import com.zt.googleplay.utils.UIUtils;

/**
 * Author: ZT on 2017/1/3.
 */
public class DeatilbottomHolder extends BaseViewHolder<AppInfo> implements View.OnClickListener {
    private TextView appDesText, appNameText;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private LinearLayout.LayoutParams lp;


    @Override
    protected View initView() {
        View view = UIUtils.inflate(R.layout.detail_bottom_layout);
        appDesText = (TextView) view.findViewById(R.id.detail_app_desc);
        appNameText = (TextView) view.findViewById(R.id.detail_app_name);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.detail_realativelayout);
        imageView = (ImageView) view.findViewById(R.id.detail_app_downimage);
        relativeLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        appDesText.setText(data.des);
        appNameText.setText(data.name);

        //放入消息队列（仍在主线程）。晚执行一会 appNameText.setText(data.name)
        // 解决当appNameText的文字小于7行时任显示7行高度的bug
        appDesText.post(new Runnable() {
            @Override
            public void run() {
                //获取appDesText的lp在添加动画刷新打开和关闭的动画效果
                lp = (LinearLayout.LayoutParams) appDesText.getLayoutParams();
                lp.height = getShortHeight();  //最大显示7行的高度
                appDesText.setLayoutParams(lp);
            }
        });
    }
    private boolean open;
    @Override
    public void onClick(View view) {
        //若7行的文字高度小于介绍文字的总的高度就不用设置动画了
        if(getLongHeight()<getShortHeight()){
            return;
        }
        //属性动画
        ValueAnimator valueAnimator;
        if(!open){
            //打开
            imageView.setImageDrawable(UIUtils.getDrawable(R.mipmap.arrow_up));
            valueAnimator = ValueAnimator.ofInt(getShortHeight(), getLongHeight());
        }else {
            //关闭
            imageView.setImageDrawable(UIUtils.getDrawable(R.mipmap.arrow_down));
            valueAnimator = ValueAnimator.ofInt(getLongHeight(),getShortHeight());
        }
        open = !open;

        //添加动画更新监听器
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int hg = (int) valueAnimator.getAnimatedValue();
                lp.height = hg;
                appDesText.setLayoutParams(lp);
            }
        });
        //添加动画执行监听器
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }
            //动画执行完毕
            @Override
            public void onAnimationEnd(Animator animator) {
                final ScrollView scrollView = getDesTextScrollview();
                //跟随滑动
                //scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部

                //提高安全和性能 。立即调用.fullScroll()，view可能没有显示出来，所有会失败
                // 放入消息队列（仍在主线程）晚执行一会,appDesText绘制完后
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        //动画时间
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }
    /**
     * 获取7行文字的高度
     */
    private int getShortHeight(){
        //虚拟一个textview来计算其7行的高度值
        //注：在xml里定义的控件不知道其宽高是多少时，获取其宽高值要先view.measure(0,0);
        // 交由Android系统底层其计算 然后通过appDesText.getMeasuredWidth(); appDesText.getMeasuredHeight()获取其真实宽高

        int width = appDesText.getMeasuredWidth();
        TextView textView = new TextView(UIUtils.getContext());
        textView.setText(getData().des);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setMaxLines(7);//最大7行

        int widthMeasureSpace = TextView.MeasureSpec.makeMeasureSpec(width, TextView.MeasureSpec.EXACTLY);//match-parent 确定模式
        int heigthMeasureSpace = TextView.MeasureSpec.makeMeasureSpec(2000, TextView.MeasureSpec.AT_MOST);//2000为默认设置的高度。至多模式
        textView.measure(widthMeasureSpace,heigthMeasureSpace);

        return textView.getMeasuredHeight();//返回最大7行的高度
    }
    /**
     * 获取完整的appDesText文字总的高度
     */
    private int getLongHeight(){
        //虚拟一个textview来计算其7行的高度值
        //但在xml里定义的控件不知道其宽高是，获取其宽高值要先view.measure(0,0);
        int width = appDesText.getMeasuredWidth();

        TextView textView = new TextView(UIUtils.getContext());
        textView.setText(getData().des);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        int widthMeasureSpace = TextView.MeasureSpec.makeMeasureSpec(width, TextView.MeasureSpec.EXACTLY);//match-parent 确定模式
        int heigthMeasureSpace = TextView.MeasureSpec.makeMeasureSpec(2000, TextView.MeasureSpec.AT_MOST);//2000为暂且设置的高度。至多模式
        textView.measure(widthMeasureSpace,heigthMeasureSpace);

        return textView.getMeasuredHeight();//返回appDesText文字总的高度
    }

    //获取appDesText的根控件ScrollView
    private ScrollView getDesTextScrollview(){
        ViewParent parent = appDesText.getParent();
        //注此方法获取ScrollView必须改控件的父控件或祖宗控件为ScrollView ,否则会死循环
        while (!(parent instanceof ScrollView)){
            parent = parent.getParent();
        }
        return (ScrollView)parent;
    }
}
