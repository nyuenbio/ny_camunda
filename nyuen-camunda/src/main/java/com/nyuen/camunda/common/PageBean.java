package com.nyuen.camunda.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PageBean {
    //列表总数
    private long total;
    //当前页记录
    private List rows;

//    public PageBean(){
//        this.total = 0l;
//        this.rows = new ArrayList();
//    }

    public PageBean(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public List getRows() {
        return rows;
    }


    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("12345");
        System.out.println(sb.substring(0));
    }
}
