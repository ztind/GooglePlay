package com.zt.googleplay.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.view.View;

import com.zt.googleplay.global.GooglePlayApplication;

/**
 * Author: ZT on 2016/12/27.
 */
public class UIUtils {
    public static Context getContext(){
        return GooglePlayApplication.getContext();
    }
    public static Handler getHandler(){
        return GooglePlayApplication.getHandler();
    }
    public static int getMainThreadId(){
        return GooglePlayApplication.getMainThreadId();
    }

    //获取res资源文件里的资源
    public static String getString(int id){
        return getContext().getResources().getString(id);
    }
    public static String[] getStringArray(int id){
        return getContext().getResources().getStringArray(id);
    }
    public static Drawable getDrawable(int id){
        return getContext().getResources().getDrawable(id);
    }
    public static int getColor(int id){
        return getContext().getResources().getColor(id);
    }
    public static float getDimens(int id){
        return getContext().getResources().getDimensionPixelSize(id);//获取像素值
    }

    //获取color文件夹里的状态选择器
    public static ColorStateList getColorStateList(int mTabTextColorResId) {
        return getContext().getResources().getColorStateList(mTabTextColorResId);
    }

    //dp 与 px 的转化（考虑屏幕适配）
    public static int dip2px(float dip){
        float density = getContext().getResources().getDisplayMetrics().density;//获取设备密度
        int pix = (int) (dip * density + 0.5f);
        return pix;
    }

    public static float pix2dip(int pix){
        float density = getContext().getResources().getDisplayMetrics().density;//获取设备密度
        float dip = pix / density;
        return dip;
    }

    //加载布局文件
    public static View inflate(int layoutId){
       return View.inflate(getContext(), layoutId, null);
    }

    //判断当前线程是否运行在主线程
    public static boolean isRunMainThread(){
        int currentThreadId = Process.myTid();
        if(currentThreadId == getMainThreadId()){
            return true;
        }
        return false;
    }

    //将子线程运行在UI线程
    public static void runOnMainThread(Runnable runnable){
       if(isRunMainThread()){
           runnable.run();//回调其run方法执行操作
       }else{
           //若为子线程，则调用post方法运行在U线程
           getHandler().post(runnable);
       }
    }

}
