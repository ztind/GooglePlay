package com.zt.googleplay.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.zt.googleplay.R;
import com.zt.googleplay.adapter.ViewPagerAdapter;
import com.zt.googleplay.ui.view.PagerTab;

/**
 * Google Play
 */
public class MainActivity extends BaseActivity {
    private PagerTab pagerTab;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDrawerLayout();
    }

    private void initDrawerLayout() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);//显示左上角的返回键
        bar.setHomeButtonEnabled(true);//home可点击

        //开关
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_str,R.string.close_str);

        toggle.syncState();//将开关和DrawerLayout关联在一起
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            toggle.onOptionsItemSelected(item);//切换抽屉
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        pagerTab = (PagerTab) findViewById(R.id.pagerTab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        pagerTab.setViewPager(viewPager);
        //点击tab实现数据加载
        pagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                //点击tab时，内部才加载数据 加载布局
//                BaseFragment fragment = FragmentFactory.getFragment(position);
//                fragment.loadData(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
