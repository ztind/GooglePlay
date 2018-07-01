package com.zt.googleplay.http.protocol;

import com.zt.googleplay.bean.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/30.
 * 首页
 */
public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>> {
    private ArrayList<String> pictrues;
    @Override
    protected String getKey() {
        return "home";
    }

    @Override
    protected String getParams() {
        return "";
    }

    @Override
    protected ArrayList<AppInfo> parseData(String json) {
        try {
            JSONObject jsonObj= new JSONObject(json);
            JSONArray jsonArray = jsonObj.getJSONArray("list");
            ArrayList<AppInfo> appInfoArrayList = new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++){
                JSONObject obj = (JSONObject) jsonArray.get(i);
                AppInfo appInfo = new AppInfo();
                appInfo.des = obj.getString("des");
                appInfo.downloadUrl = obj.getString("downloadUrl");
                appInfo.iconUrl = obj.getString("iconUrl");
                appInfo.id = obj.getString("id");
                appInfo.name = obj.getString("name");
                appInfo.packageName = obj.getString("packageName");
                appInfo.size = obj.getLong("size");
                appInfo.stars= (float) obj.getDouble("stars");
                appInfoArrayList.add(appInfo);
            }
            //解析轮播图url
            pictrues = new ArrayList<>();
            JSONArray urls = (JSONArray) jsonObj.get("picture");
            for (int i=0;i<urls.length();i++){
                String pic = (String) urls.get(i);
                pictrues.add(pic);
            }

            return appInfoArrayList;  //返回数据集合

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取头布局的图片url数据
    public ArrayList<String> getHeaderData(){
        return pictrues;
    }


}
