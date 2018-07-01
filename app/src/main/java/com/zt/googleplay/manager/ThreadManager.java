package com.zt.googleplay.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: ZT on 2017/1/6.
 * 线程管理器
 */
public class ThreadManager {

    private static ThreadPool mThreadPool;

    //获取线程池单列
    public static ThreadPool getThreadPool(){
        if(mThreadPool==null){
            synchronized (ThreadManager.class){
                if(mThreadPool==null){
//                    获取cpu数量
//                    int cupCount = Runtime.getRuntime().availableProcessors();
                    //核心线程数
//                    int corePoolSize = cupCount * 2 + 1;

                    int corePoolSize = 10;//默认开启10个线程来工作

                    mThreadPool = new ThreadPool(corePoolSize,corePoolSize,1L);
                }
            }
        }
        return mThreadPool;
    }

    //线程池
    public static class ThreadPool{
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private ThreadPoolExecutor executor;

        private ThreadPool(int corePoolSize,int maximumPoolSize,long keepAliveTime){
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }
        /**
         * 参1：核心线程数
         * 参2：最大线程数
         * 参3：线程休息时间
         * 参4：时间单位
         * 参5：线程队列
         * 参6：生产线程的工厂
         * 参7：线程异常处理策略
         */
        public void execute(Runnable r){
            //线程池执行器
            if(executor==null){
                executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
            }
            //执行一个Runnable对象【在Runnable的run方法里执行耗时操作】
            executor.execute(r);
        }

        //消息线程
        public void cancel(Runnable r){
            if(executor!=null){
                executor.getQueue().remove(r);//从线程队列里移除runnable对象
            }
        }
    }

}
