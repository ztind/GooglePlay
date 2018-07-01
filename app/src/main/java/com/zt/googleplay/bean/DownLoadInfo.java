package com.zt.googleplay.bean;

import android.os.Environment;

import com.zt.googleplay.manager.DownloadManager;

import java.io.File;

/**
 * Author: ZT on 2017/1/6.
 * 下载信息封装类
 */
public class DownLoadInfo {
    public String id;
    public String name;
    public String downloadUrl;
    public String packageName;
    public long size;
    public String path;

    public int currentPos;//当前下载位置
    public int currentState;//下载状态

    private static String GOOGLE_PLAY = "Google_Play"; //下载的文件根目录
    private static String DOWNLAOD = "download"; //apk文件的存放的目录

    //获取当前的下载进度
    public float getProgress() {
        if(size==0){
            return 0;
        }
        float progress = currentPos / (float) size;
        return progress;
    }

    //获取下载文件的路径
    public String getDownloadFilePath() {
        StringBuffer sb = new StringBuffer();
        String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();

        //拼接下载路径
        sb.append(sdpath).append(File.separator).append(GOOGLE_PLAY).append(File.separator).append(DOWNLAOD);
        String dir = sb.toString();
        if(createDir(dir)){//文件夹创建完成或存在
            sb.append(File.separator).append(name).append(".apk");//拼接完整的下载文件路径
            return sb.toString();
        }
        return null;
    }

    //创建下载文件夹路径
    public boolean createDir(String dir) {
        File dirFile = new File(dir);
        //文件夹不存在或者不是一个文件夹时创建文件夹
        if(!dirFile.exists() || !dirFile.isDirectory()){
            boolean isCreate = dirFile.mkdirs();
            return isCreate;
        }
        return true; //文件夹存在
    }

    //将AppInfo信息转化到DownLoadInfo对象里
    public static DownLoadInfo copy(AppInfo appInfo){
        DownLoadInfo downLoadInfo = new DownLoadInfo();
        downLoadInfo.id = appInfo.id;
        downLoadInfo.name = appInfo.name;
        downLoadInfo.downloadUrl = appInfo.downloadUrl;
        downLoadInfo.size = appInfo.size;
        downLoadInfo.packageName = appInfo.packageName;
        downLoadInfo.currentPos =0;
        downLoadInfo.currentState = DownloadManager.STATE_UNDO;
        downLoadInfo.path = downLoadInfo.getDownloadFilePath();
        return downLoadInfo;
    }
}
