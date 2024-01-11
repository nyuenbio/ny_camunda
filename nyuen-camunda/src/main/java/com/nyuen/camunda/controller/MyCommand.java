package com.nyuen.camunda.controller;

import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.io.Serializable;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/10
 */
public class MyCommand implements Command<ProcessInstance>, Serializable {
    @Override
    public ProcessInstance execute(CommandContext commandContext) {
        return null;
    }
}
