package com.zt.googleplay.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ZT on 2016/12/30.
 * 首页信息封装类:
 * Intent中传递对象的2中方法：对象实现Serializable或Parcelable接口
 */
public class AppInfo {
    public String des;
    public String downloadUrl;
    public String iconUrl;
    public String id;
    public String name;
    public String packageName;
    public long size;
    public float stars;
    //轮播图片的url集合
    public List<String> pictures;

    //详情页数据字段
    public String author;
    public String date;
    public String downloadNum;
    public String version;
    public ArrayList<SafeInfo> safeList;
    public ArrayList<String> screenList;

    public class SafeInfo{
        public String safeDes;
        public String safeUrl;
        public String safeDesUrl;

        @Override
        public String toString() {
            return "SafeInfo{" +
                    "safeDes='" + safeDes + '\'' +
                    ", safeUrl='" + safeUrl + '\'' +
                    ", safeDesUrl='" + safeDesUrl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "des='" + des + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", size=" + size +
                ", stars=" + stars +
                ", pictures=" + pictures +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", downloadNum='" + downloadNum + '\'' +
                ", version='" + version + '\'' +
                ", safeList=" + safeList +
                ", screenList=" + screenList +
                '}';
    }
}
