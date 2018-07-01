package com.zt.googleplay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zt.googleplay.R;
import com.zt.googleplay.fragment.BaseFragment;
import com.zt.googleplay.fragment.FragmentFactory;
import com.zt.googleplay.utils.L;
import com.zt.googleplay.utils.UIUtils;

/**
 * Author: ZT on 2016/12/27.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        titles = UIUtils.getStringArray(R.array.tab_names);
    }
    /**
     *1,该方法在所有Fragment对象创建后就不会再调用了。由于VP的缓存机制走fragment的onCreateView和ondestoryView()方法。销毁调用destroyItem
     * 2,若继承的是PagerAdapter则instantiateItem()方法会不断的调用，销毁调用destroyItem
     */
    @Override
    public Fragment getItem(int position) {
        L.v("getItem--:"+position);
        BaseFragment fragment =  FragmentFactory.getFragment(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
