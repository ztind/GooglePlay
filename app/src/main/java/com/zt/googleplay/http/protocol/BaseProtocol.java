package com.zt.googleplay.http.protocol;

import com.zt.googleplay.http.HttpHelper;
import com.zt.googleplay.utils.IOUtils;
import com.zt.googleplay.utils.LogUtils;
import com.zt.googleplay.utils.StringUtils;
import com.zt.googleplay.utils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Author: ZT on 2016/12/30.
 * 数据获取类，网络请求httpclient
 */
public abstract class BaseProtocol<T>{

    public T getData(int index){

        //读缓存
        String cacheData = getCache(index);

        if(!StringUtils.isEmpty(cacheData)){
            LogUtils.v("*********读缓存********");
            return parseData(cacheData);
        }

        //缓存没有/缓存失效 则从网络获取数据
        String serviceData = getDataFromService(index);
        if(serviceData!=null){
            LogUtils.v("*********服务器获取********");
            return parseData(serviceData);
        }

        return null;
    }

    private String url;
    public String getDataFromService(int index){
        // http://127.0.0.1:8090/home?index=1$name=jack&age=18
        HttpHelper.HttpResult client = HttpHelper.get(url = HttpHelper.URL+getKey()+"?index="+index+getParams());
        String result=null;
        if(client!=null){
            result = client.getString();
        }

        //缓存本地file
        if(!StringUtils.isEmpty(result)){
                setCache(index,result);
                return result;
        }

        return null;
    }
    //有子类来实现
    protected abstract String getKey();

    protected abstract String getParams();

    protected abstract T parseData(String json);

    //以url为文件名，json数据为值，缓存本地的缓存文件夹
    public void setCache(int index,String json){
        File cacheDir= UIUtils.getContext().getCacheDir();//获取手机里的数据缓存文件夹  /data/data/com.zt.googleplay/cache

        File file = new File(cacheDir, getKey()+"?index="+index+getParams());

        //json写入file
        FileWriter fw=null;
        try {
            fw = new FileWriter(file);
            //设置缓存文件的有效期
            long time = System.currentTimeMillis() + 30 * 60 * 1000;
            fw.write(time+"\n");//缓存时间写在第一行
            fw.write(json);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(fw);
        }
    }

    public String getCache(int index){
        File cacheDir= UIUtils.getContext().getCacheDir();
        File cacheFile = new File(cacheDir,getKey()+"?index="+index+getParams());
        if(cacheFile.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(cacheFile));
                String time = br.readLine();//获取文件缓存的有效期
                long cacheTime = Long.parseLong(time);
                long currentTime = System.currentTimeMillis();
                if(currentTime<cacheTime){
                    //json数据本地写入内存
                    StringBuffer sb = new StringBuffer();
                    String len;
                    while ((len=br.readLine())!=null) {
                        sb.append(len);
                    }
                    return sb.toString();//返回读取的json数据
                }else {
                    //缓存过期(删除缓存文件)

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
