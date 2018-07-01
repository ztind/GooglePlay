package com.zt.googleplay.http.protocol;

import com.zt.googleplay.bean.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 应用网络请求类
 * Author: ZT on 2016/12/31.
 */
public class AppProtocol extends BaseProtocol<ArrayList<AppInfo>> {
    @Override
    protected String getKey() {
        return "app";
    }

    @Override
    protected String getParams() {
        return "";
    }

    @Override
    protected ArrayList<AppInfo> parseData(String json) {
        try {
            JSONArray array = new JSONArray(json);
            ArrayList<AppInfo> list = new ArrayList<>();
            for (int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                AppInfo appInfo = new AppInfo();
                appInfo.id = obj.getString("id");
                appInfo.name = obj.getString("name");
                appInfo.packageName = obj.getString("packageName");
                appInfo.iconUrl = obj.getString("iconUrl");
                appInfo.stars = (float) obj.getDouble("stars");
                appInfo.size = obj.getLong("size");
                appInfo.downloadUrl = obj.getString("downloadUrl");
                appInfo.des = obj.getString("des");
                list.add(appInfo);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
