package com.zt.googleplay.utils;

import com.lidroid.xutils.BitmapUtils;

/**
 * Author: ZT on 2016/12/30
 * 懒汉式
 */
public class MyBitmapUtils {
    private static BitmapUtils bitmapUtils;
    public static BitmapUtils getInstanceBitmapUtils(){
        if(bitmapUtils==null){
            synchronized (MyBitmapUtils.class){
                if(bitmapUtils==null){
                    return bitmapUtils = new BitmapUtils(UIUtils.getContext());
                }
            }
        }
        return bitmapUtils;
    }
}
