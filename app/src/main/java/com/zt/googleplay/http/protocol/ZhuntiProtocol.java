package com.zt.googleplay.http.protocol;

import com.zt.googleplay.bean.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/31.
 * 专题数据 网络请求类
 */
public class ZhuntiProtocol extends BaseProtocol<ArrayList<Subject>> {
    @Override
    protected String getKey() {
        return "subject";
    }

    @Override
    protected String getParams() {
        return "";
    }

    @Override
    protected ArrayList<Subject> parseData(String json) {
        try {
            JSONArray array = new JSONArray(json);
            ArrayList<Subject> list = new ArrayList<>();
            for (int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                Subject subject = new Subject();
                subject.iconUrl = obj.getString("url");
                subject.des = obj.getString("des");
                list.add(subject);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
