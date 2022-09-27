package com.nyuen.camunda.service;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/8/9
 */
@Service
public class DemoListener implements TaskListener, ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String eventName = delegateExecution.getEventName();
        System.out.println("------------eventName --> "+eventName+"-------------");
        listenerLogic(eventName);
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        System.out.println("------------eventName --> "+eventName+"-------------");
        listenerLogic(eventName);
    }


    private void listenerLogic(String eventName) {
        if("create".equals(eventName)){
            System.out.println("create===========流程启动");
        }else if("assigment".equals(eventName)){
            System.out.println("assigment===========流程部署");
        }else if("complete".equals(eventName)){
            System.out.println("complete===========流程完成");
        }else if("delete".equals(eventName)){
            System.out.println("delete===========流程结束");
        }else if("start".equals(eventName)){
            System.out.println("start===========流程启动");
        }else if("end".equals(eventName)){
            System.out.println("end===========流程结束");
        }
    }

}
