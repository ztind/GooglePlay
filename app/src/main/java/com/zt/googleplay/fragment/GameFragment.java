package com.zt.googleplay.fragment;

import android.view.View;

import com.zt.googleplay.ui.view.LoadPager;

/**
 * Author: ZT on 2016/12/27.
 */
public class GameFragment extends BaseFragment {


    @Override
    protected View onCreateSuccessView() {
        return null;
    }

    @Override
    protected LoadPager.ResultState onLoadData() {
        return LoadPager.ResultState.STATE_EMPTY;
    }


}
