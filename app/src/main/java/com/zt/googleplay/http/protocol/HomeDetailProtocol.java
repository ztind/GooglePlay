package com.zt.googleplay.http.protocol;

import com.zt.googleplay.bean.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Author: ZT on 2017/1/1.
 */
public class HomeDetailProtocol extends BaseProtocol<AppInfo> {
    private String packageName;
    public HomeDetailProtocol(String packageName){
        this.packageName = packageName;
    }
    @Override
    protected String getKey() {
        return "detail";
    }

    @Override
    protected String getParams() {
        return "&packageName="+packageName;
    }

    @Override
    protected AppInfo parseData(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            AppInfo appInfo = new AppInfo();
            appInfo.des = obj.getString("des");
            appInfo.downloadUrl = obj.getString("downloadUrl");
            appInfo.iconUrl = obj.getString("iconUrl");
            appInfo.id = obj.getString("id");
            appInfo.name = obj.getString("name");
            appInfo.packageName = obj.getString("packageName");
            appInfo.size = obj.getLong("size");
            appInfo.stars= (float) obj.getDouble("stars");

            appInfo.author = obj.getString("author");
            appInfo.date = obj.getString("date");
            appInfo.downloadNum = obj.getString("downloadNum");
            appInfo.version = obj.getString("version");

            JSONArray safeArray = obj.getJSONArray("safe");
            ArrayList<AppInfo.SafeInfo> safeList = new ArrayList<>();

            for (int i=0;i<safeArray.length();i++){
                JSONObject jsonObj = safeArray.getJSONObject(i);

                AppInfo.SafeInfo safeInfo = new AppInfo().new SafeInfo();

                safeInfo.safeDes = jsonObj.getString("safeDes");
                safeInfo.safeUrl = jsonObj.getString("safeUrl");
                safeInfo.safeDesUrl = jsonObj.getString("safeDesUrl");
                safeList.add(safeInfo);
            }
            appInfo.safeList = safeList;

            JSONArray screenArray = obj.getJSONArray("screen");
            ArrayList<String> screenList = new ArrayList<>();
            for (int i=0;i<screenArray.length();i++){
                String screenUrl = screenArray.getString(i);
                screenList.add(screenUrl);
            }
            appInfo.screenList = screenList;

            return appInfo;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
