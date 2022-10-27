package com.nyuen.camunda.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/10/26
 */
public class CamundaObjectUtil {

    public static JSONArray objectListToJSONArray(Object objectList){
        String str = JSONArray.toJSONString(objectList, SerializerFeature.IgnoreErrorGetter);
        return JSONArray.parseArray(str);
    }

    public static List objectListToJSONArray(Object objectList, Class clazz){
        String str = JSONArray.toJSONString(objectList, SerializerFeature.IgnoreErrorGetter);
        return JSONArray.parseArray(str,clazz);
    }
}
