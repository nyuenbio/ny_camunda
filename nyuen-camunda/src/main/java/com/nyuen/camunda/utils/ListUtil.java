package com.nyuen.camunda.utils;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/5/11
 */
public class ListUtil {

    public static boolean isStrInList(String str,String[] strList){
        for (String s : strList){
            if(s.equals(str)){
                return true;
            }
        }
        return false;
    }

}
