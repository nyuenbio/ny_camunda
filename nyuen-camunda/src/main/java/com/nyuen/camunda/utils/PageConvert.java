package com.nyuen.camunda.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class PageConvert {

    public static void currentPageConvertStartIndex(Map<String, Object> params) {
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        int currentPage = Integer.parseInt(params.get("currentPage").toString());
        params.put("startIndex", (currentPage - 1) * pageSize);
    }

    public static void currentPageConvertStartIndex(JSONObject jsonObject){
        int pageSize = Integer.parseInt(jsonObject.get("pageSize").toString());
        int currentPage = Integer.parseInt(jsonObject.get("currentPage").toString());
        jsonObject.put("startIndex", (currentPage - 1) * pageSize);
    }

}
