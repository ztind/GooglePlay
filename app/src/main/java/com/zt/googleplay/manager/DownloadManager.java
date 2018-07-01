package com.zt.googleplay.manager;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.zt.googleplay.bean.AppInfo;
import com.zt.googleplay.bean.DownLoadInfo;
import com.zt.googleplay.http.HttpHelper;
import com.zt.googleplay.utils.IOUtils;
import com.zt.googleplay.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: ZT on 2017/1/6.
 * 下载管理器 ：使用观察者设计模式来监听下载状态
 * 下载的6种状态：
 * 未下载，正在下载，等待下载，暂停下载，下载失败，下载成功
 */
public class DownloadManager {

    public static final int STATE_UNDO = 1;
    public static final int STATE_DOWNLOADING = 2;
    public static final int STATE_WAITE = 3;
    public static final int STATE_PAUSE = 4;
    public static final int STATE_ERROR= 5;
    public static final int STATE_SUCCESS= 6;

    private static DownloadManager downloadManager = new DownloadManager();//单列饿汉式

    //使用集合维护一推观察者接口
    private ArrayList<DownLoadOberver> obervers = new ArrayList<>();

    //HashMap集合维护DownLoadInfo,使用线程安全的HashMap,ConcurrentHashMap
//    private HashMap<String, DownLoadInfo> mDownloadInfoMap = new HashMap<>();
    private ConcurrentHashMap<String, DownLoadInfo> mDownloadInfoMap = new ConcurrentHashMap<>();

    //HashMap集合维护下载task
//    private HashMap<String, Runnable> mDownLoadTaskMap = new HashMap<>();
    private ConcurrentHashMap<String, Runnable> mDownLoadTaskMap = new ConcurrentHashMap<>();

    public static DownloadManager getDownloadManager(){
        return downloadManager;
    }

    /**
     * 声明一个观察者接口
     */
    public interface DownLoadOberver{
        //下载状态改变
        void downloadStateChange(DownLoadInfo downLoadInfo);
        //下载进度改变
        void downloadProgressChange(DownLoadInfo downLoadInfo);
    }
    /**
     * 注册观察者
     */
    public void registerOberver(DownLoadOberver oberver){
        if(oberver!=null && !obervers.contains(oberver)){
            obervers.add(oberver);
        }
    }
    /**
     * 注销观察者
     */
    public void unregisterOberver(DownLoadOberver oberver){
        if(oberver!=null && obervers.contains(oberver)){
            obervers.remove(oberver);
        }
    }

    //状态改变时，通知所有的观察者回调相应方法
    public void notifyDownloadStateChange(DownLoadInfo downLoadInfo){
        for (DownLoadOberver oberver : obervers) {
            oberver.downloadStateChange(downLoadInfo);
        }
    }
    public void notifyDownloadProgressChange(DownLoadInfo downLoadInfo){
        for (DownLoadOberver oberver : obervers) {
            oberver.downloadProgressChange(downLoadInfo);
        }
    }

    //下载
    public synchronized void down(AppInfo appInfo){
        DownLoadInfo downLoadInfo = mDownloadInfoMap.get(appInfo.id);
        if(downLoadInfo==null){
            downLoadInfo = DownLoadInfo.copy(appInfo);
        }
        //改变状态
        downLoadInfo.currentState = STATE_WAITE;
        Log.v("TAG", "开始等待：" + downLoadInfo.name);
        //通知观察者接口状态改变
        notifyDownloadStateChange(downLoadInfo);
        mDownloadInfoMap.put(appInfo.id,downLoadInfo);

        //开启线程下载apk文件
        DownLaodTask task = new DownLaodTask(downLoadInfo);
        mDownLoadTaskMap.put(appInfo.id, task);
        //此方法是Runnable还在线程队列中有效。若该Runnable线程正在执行则可以在下载的while方法里进行中断*******************************
        ThreadManager.getThreadPool().execute(task);
    }
    //暂停【移除线程池里的Runnable任务】
    public synchronized void pause(AppInfo appInfo){
        DownLoadInfo downloadInfo = mDownloadInfoMap.get(appInfo.id);
        if(downloadInfo!=null){
            //正在下载或等待状态时才可以暂停下载
            if(downloadInfo.currentState==STATE_DOWNLOADING || downloadInfo.currentState==STATE_WAITE){
                //改变下载状态为暂停状态
                downloadInfo.currentState = STATE_PAUSE;
                notifyDownloadStateChange(downloadInfo);

                Runnable task = mDownLoadTaskMap.get(downloadInfo.id);
                if(task!=null){
                    ThreadManager.getThreadPool().cancel(task);//根据传入的Runnable对象从线程池里移除下载该Runnable
                }
            }
        }
    }
    //安装
    public synchronized void install(AppInfo appInfo){
        //跳转到系统的安装界面进行应用安装
        DownLoadInfo downloadInfo = mDownloadInfoMap.get(appInfo.id);
        if(downloadInfo!=null){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" +downloadInfo.path),"application/vnd.android.package-archive");
            UIUtils.getContext().startActivity(intent);
        }
    }
    class DownLaodTask implements Runnable{
        private DownLoadInfo downLoadInfo;

        public DownLaodTask(DownLoadInfo downLoadInfo){
            this.downLoadInfo = downLoadInfo;
        }
        @Override
        public void run() {
            //开始下载
            Log.v("TAG", "开始下载：" + downLoadInfo.name);

            //更改文件下载状态
            downLoadInfo.currentState = STATE_DOWNLOADING;
            notifyDownloadStateChange(downLoadInfo);

            File apkFile = new File(downLoadInfo.path);

            //情况：下载的apk文件不存在/存在但文件的长度不等于上次文件的下载位置【都要删除文件重新下载】/上次文件的下载位置为0
            HttpHelper.HttpResult httpResult;
            if (!apkFile.exists() || apkFile.length() != downLoadInfo.currentPos || downLoadInfo.currentPos == 0) {
                //删除无效的文件
                apkFile.delete();//此方法文件不存在也不会出错
                //更改当前的下载位置置为0
                downLoadInfo.currentPos = 0;

                httpResult = HttpHelper.download(HttpHelper.URL + "download?name=" + downLoadInfo.downloadUrl);

            } else {
                //断点续传
                //range 表示从服务器的哪个位置开始下载数据
                long lastPos = apkFile.length();//获取上次文件下载到的位置
                httpResult = HttpHelper.download(HttpHelper.URL + "download?name="
                        + downLoadInfo.downloadUrl + "&range=" + lastPos);
            }

            //将数据写入apk文件
            InputStream is;
            FileOutputStream out = null;
            if(httpResult!=null && httpResult.getInputStream()!=null){
                is = httpResult.getInputStream();
                try {
                    out = new FileOutputStream(apkFile, true);//追加模式
                    byte[] bytes = new byte[1024];
                    int len;
                    //边读边写入文件[只有状态时正在下载状态才继续轮询,解决下载中途暂停的问题]
                    while ((len=is.read(bytes))!=-1 && downLoadInfo.currentState==STATE_DOWNLOADING){
                        out.write(bytes,0,len);
                        out.flush();
                        //刷新进度值
                        downLoadInfo.currentPos += len;
                        notifyDownloadProgressChange(downLoadInfo);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    IOUtils.close(out);
                    IOUtils.close(is);
                }
                //文件下载完成
                if(apkFile.length()==downLoadInfo.size){
                    //文件完整，表示下载成功
                    downLoadInfo.currentState = STATE_SUCCESS;
                    notifyDownloadStateChange(downLoadInfo);
                }else if(downLoadInfo.currentState==STATE_PAUSE){
                    //暂停
                    notifyDownloadStateChange(downLoadInfo);
                }else {
                    //下载失败
                    apkFile.delete();//删除无效文件
                    downLoadInfo.currentPos = 0;
                    downLoadInfo.currentState = STATE_ERROR;
                    notifyDownloadStateChange(downLoadInfo);
                }
            }else {
                //网络异常
                apkFile.delete();//删除无效文件
                downLoadInfo.currentPos = 0;
                downLoadInfo.currentState = STATE_ERROR;
                notifyDownloadStateChange(downLoadInfo);
            }

            //移除下载任务
            mDownLoadTaskMap.remove(downLoadInfo.id);

        }
    }
    public DownLoadInfo getDownLoadInfo(String id){
        DownLoadInfo downLoadInfo = mDownloadInfoMap.get(id);
        return downLoadInfo;
    }
}
