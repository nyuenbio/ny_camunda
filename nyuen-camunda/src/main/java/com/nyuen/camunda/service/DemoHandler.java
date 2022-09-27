package com.nyuen.camunda.service;


import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

/**
 * 在listener中指派（Assignments in Listeners）
 *
 * @author chengjl
 * @description
 * @date 2022/8/1
 */
@Service
public class DemoHandler implements TaskListener {

    /**
     * 这个传递给TaskListener实现的委托任务，允许设置assignee和候选users和groups
     * @param delegateTask 委托任务
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("admin");
        delegateTask.addCandidateUser("zhangsan");
        delegateTask.addCandidateGroup("admin");
        //do something
        delegateTask.complete();
    }

}
