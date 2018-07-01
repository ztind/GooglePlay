package com.zt.googleplay.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.zt.googleplay.R;
import com.zt.googleplay.adapter.holder.DeatilbottomHolder;
import com.zt.googleplay.adapter.holder.DownLoadHoler;
import com.zt.googleplay.adapter.holder.HomeDetailSafeHolder;
import com.zt.googleplay.adapter.holder.HomeDetailTopHolder;
import com.zt.googleplay.adapter.holder.HorizontalScrollHolder;
import com.zt.googleplay.bean.AppInfo;
import com.zt.googleplay.http.protocol.HomeDetailProtocol;
import com.zt.googleplay.ui.view.LoadPager;
import com.zt.googleplay.utils.UIUtils;

/**
 * Author: ZT on 2017/1/1.
 */
public class HomeDetailActivity extends BaseActivity {
    private LoadPager mloadPager;
    private String packageName;
    private AppInfo appInfo;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mloadPager = new LoadPager(this) {
            @Override
            public View onCreateSuccessView() {
                return HomeDetailActivity.this.onCreateSuccessView();
            }
            @Override
            public ResultState onLoadData() {
                return HomeDetailActivity.this.onLoadData();
            }
        };
        setContentView(mloadPager);
        //bar
        initActionBar();
    }
    private void initActionBar() {

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);//显示左上角的返回键
        bar.setHomeButtonEnabled(true);//home可点击

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
           finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private LoadPager.ResultState onLoadData(){
        //获取传递过来的packageName
        packageName = getIntent().getStringExtra("packageName");
        HomeDetailProtocol homeDetailProtocol = new HomeDetailProtocol(packageName);
        appInfo = homeDetailProtocol.getData(0);
        if(appInfo!=null){
            return LoadPager.ResultState.STATE_SUCCESS;
        }
        return LoadPager.ResultState.STATE_FAIL;
    }

    //加载布局，更新UI.数据加载成功回调此方法
    private View onCreateSuccessView(){
        View view = UIUtils.inflate(R.layout.homedetail_layout);
        FrameLayout frameLayout1 = (FrameLayout) view.findViewById(R.id.homedetail_framelayout1);
        FrameLayout frameLayout2 = (FrameLayout) view.findViewById(R.id.homedetail_framelayout2);
        //头部视图布局
        HomeDetailTopHolder homeDetailTopHolder = new HomeDetailTopHolder();
        homeDetailTopHolder.setData(appInfo);
        frameLayout1.addView(homeDetailTopHolder.getRootView());

        //安全验证信息布局视图
        HomeDetailSafeHolder homeDetailSafeHolder = new HomeDetailSafeHolder();
        homeDetailSafeHolder.setData(appInfo);
        frameLayout2.addView(homeDetailSafeHolder.getRootView());

        //应用截图view
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.horizontal_scrollview);
        HorizontalScrollHolder horizontalScrollHolder = new HorizontalScrollHolder();
        horizontalScrollHolder.setData(appInfo);
        horizontalScrollView.addView(horizontalScrollHolder.getRootView());

        //底部应用介绍
        FrameLayout bottom = (FrameLayout) view.findViewById(R.id.botton_framelayout);
        DeatilbottomHolder deatilbottomHolder = new DeatilbottomHolder();
        deatilbottomHolder.setData(appInfo);
        bottom.addView(deatilbottomHolder.getRootView());

        //下载模块UI
        FrameLayout downLaodView = (FrameLayout) view.findViewById(R.id.detail_frameLayout);
        DownLoadHoler downHoler = new DownLoadHoler();
        downHoler.setData(appInfo);
        downLaodView.addView(downHoler.getRootView());

        return view;
    }
}
