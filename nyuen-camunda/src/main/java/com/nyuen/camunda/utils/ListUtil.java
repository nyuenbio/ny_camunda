package com.nyuen.camunda.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/5/11
 */
public class ListUtil {

    public static boolean isStrInList(String str,String[] strList){
        if(null == strList || strList.length == 0){
            return false;
        }
        for (String s : strList){
            if(s.equals(str)){
                return true;
            }
        }
        return false;
    }

    public static boolean isStrInList(String str,List<String> strList){
        if(null == strList || strList.size() == 0){
            return false;
        }
        for (String s : strList){
            if(s.equals(str)){
                return true;
            }
        }
        return false;
    }

    //不适用于数组中有重复元素
    // List<String> l3 = Arrays.asList("A07","A02","A08","A07");
    // List<String> l4 = Arrays.asList("A08","A02","A07","A08");
    public static List<String> getNoDumpListDif(List<String> l1, List<String> l2){
        long start = System.currentTimeMillis();
        List<String> dif = new ArrayList<>();
        Map<String,Integer> map = new HashMap<>();

        for (String str: l1){
            map.put(str,1);
        }
        for(String str : l2){
            if(map.get(str) != null){
                map.put(str,2);
            }else {
                map.put(str,1);
            }
        }
        for(Map.Entry<String,Integer> en : map.entrySet()){
            if(en.getValue()==1){
                dif.add(en.getKey());
            }
        }
        long end = System.currentTimeMillis();
        long t= start-end;
        System.out.println("getNoDumpListDif ===>  "+t);
        return dif;
    }

    //不适用于数组中有重复元素
    public static List<String> getDif1(List<String> l1, List<String> l2){
        long start = System.currentTimeMillis();
        List<String> dif = new ArrayList<>();
        for(String str : l1){
            if(!l2.contains(str)){
                dif.add(str);
            }
        }
        for(String str : l2){
            if(!l1.contains(str)){
                dif.add(str);
            }
        }
        long end = System.currentTimeMillis();
        long t= start-end;
        System.out.println("getDif1 ===>  "+t);
        return dif;

    }

    public static List<String> getDif(List<String> l1, List<String> l2){
        //long start = System.currentTimeMillis();
        List<String> res = new ArrayList<>();
        List<String> dif = new ArrayList<>(l1);
        //先求出两个list的交集；
        dif.retainAll(l2);
        res.addAll(l1);
        res.addAll(l2);
        //用合集去掉交集，就是不同的元素；
        res.removeAll(dif);
        //long end = System.currentTimeMillis();
        //long t= start-end;
        //System.out.println("getDif ===>  "+t);
        return res;
    }

    // 获取list中重复的元素
    public static List<String> getDump(List<String> l1){
        List<String> dump = new ArrayList<>();
        Map<String,Integer> map = new HashMap<>();

        for(String str : l1){
            map.merge(str, 1, Integer::sum);
        }
        for(Map.Entry<String,Integer> en : map.entrySet()){
            if(en.getValue() != 1){
                dump.add(en.getKey());
            }
        }
        return dump;
    }

}
