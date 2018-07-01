package com.zt.googleplay.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;

/**
 * Author: ZT on 2016/12/27.
 * com.android.support:appcompat-v7
 * com.android.support:appcompat-v4
 *
 * v4和v7都是兼容低版本的兼容包
 * v4兼容到1.x系列的系统，v7是兼容到2.x系列的系统。由于1.x系列的系统市场占有率低。故Google用v7包兼容到2.x的手机
 */
public class BaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
