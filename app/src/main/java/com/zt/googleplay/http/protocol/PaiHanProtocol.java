package com.zt.googleplay.http.protocol;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/31.
 */
public class PaiHanProtocol extends BaseProtocol<ArrayList<String>> {

    @Override
    protected String getKey() {
        return "hot";
    }

    @Override
    protected String getParams() {
        return "";
    }

    @Override
    protected ArrayList<String> parseData(String json) {
        try {
            JSONArray array = new JSONArray(json);
            ArrayList<String> list = new ArrayList<>();
            for (int i=0;i<array.length();i++){
                String str = array.getString(i);
                list.add(str);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
