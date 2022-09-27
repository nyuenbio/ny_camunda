package com.nyuen.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/7/29
 */
@Service
public class DemoService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("\n\n -------------------------------------------------------------------------------");
        System.out.println("=======>              service task 调用service成功  ");
        System.out.println("\n\n -------------------------------------------------------------------------------");
    }

    public String getUser(){
        return "wangwu";
    }

    public List<String> findUsers(String param){
        System.out.println("findUsers param  -------------> "+param);
        List<String> userList =new ArrayList<>();
        userList.add("zhangsan");
        userList.add("wangwu");
        userList.add("zhaoliu");
        return userList;
    }

}
