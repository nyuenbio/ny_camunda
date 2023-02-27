package com.nyuen.camunda.utils;

public class StringUtil {

    public static boolean isNotEmpty(String str){
         if(str == null){
             return false;
         }
         if(str.trim().length() == 0){
             return false;
         }
         return true;
    }

    public static boolean isEmpty(String str){
        if(str == null){
            return true;
        }
        if(str.trim().length() == 0){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String s = "        ";
        System.out.println(StringUtil.isNotEmpty(s));
    }

}
