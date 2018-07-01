package com.zt.googleplay.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Author: ZT on 2016/12/27.
 */
public class GooglePlayApplication extends Application {
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();//线程id
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

}
