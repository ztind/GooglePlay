package com.zt.googleplay.http.protocol;

import com.zt.googleplay.bean.CategoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Author: ZT on 2017/1/1.
 * 分类信息也的网络数据请求封装
 */
public class CategoryProtocol extends BaseProtocol<ArrayList<CategoryInfo>> {
    @Override
    protected String getKey() {
        return "category";
    }

    @Override
    protected String getParams() {
        return "";
    }

    @Override
    protected ArrayList<CategoryInfo> parseData(String json) {

        try {
            JSONArray array = new JSONArray(json);
            ArrayList<CategoryInfo> list = new ArrayList<>();

            for (int i=0;i<array.length();i++){
                JSONObject jsonObject = array.getJSONObject(i);

                CategoryInfo categoryInfo = new CategoryInfo();
                if(jsonObject.has("title")){//判断json对象是否有此键
                    String title = jsonObject.getString("title");
                    categoryInfo.title = title;
                    categoryInfo.isTitle = true;
                    list.add(categoryInfo);
                }

                //遍历jsonobject里的数组对象
                if(jsonObject.has("infos")){
                    JSONArray ja = jsonObject.getJSONArray("infos");
                    for (int j=0;j<ja.length();j++){
                        JSONObject inObj = ja.getJSONObject(j);
                        CategoryInfo categoryInfo1 = new CategoryInfo();
                        categoryInfo1.name1 = inObj.getString("name1");
                        categoryInfo1.name2 = inObj.getString("name2");
                        categoryInfo1.name3 = inObj.getString("name3");
                        categoryInfo1.url1 = inObj.getString("url1");
                        categoryInfo1.url2 = inObj.getString("url2");
                        categoryInfo1.url3 = inObj.getString("url3");
                        categoryInfo1.isTitle = false;
                        list.add(categoryInfo1);
                    }
                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
