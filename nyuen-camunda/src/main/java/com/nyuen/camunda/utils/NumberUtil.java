package com.nyuen.camunda.utils;

import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/4/13
 */
public class NumberUtil {

    //判断是否是正整数
    public static  boolean isWholeNumber(String str){
        if(StringUtil.isEmpty(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("^[1-9]\\d*");
        return pattern.matcher(str).matches();
    }

    public static void main(String[] args) {
        System.out.println(isWholeNumber("10"));
        System.out.println(isWholeNumber("01"));
        System.out.println(isWholeNumber("001"));
        System.out.println(isWholeNumber("100.11"));
        System.out.println(isWholeNumber("A2"));
    }
}
