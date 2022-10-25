package com.nyuen.camunda.controller;

import com.nyuen.camunda.domain.vo.SimpleQueryBean;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.MyTaskService;
import com.nyuen.camunda.utils.ObjectUtil;
import com.nyuen.camunda.utils.PageConvert;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/10/24
 */
@RestController
@RequestMapping("/process")
public class ProcessController {
    @Resource
    private MyTaskService myTaskService;
    @Resource
    private TaskService taskService;

    @ApiOperation(value = "根据样本编号查询流程",httpMethod = "POST")
    @PostMapping("/getSampleProcessList")
    public Result getTodoList(@RequestBody SimpleQueryBean sqBean) throws IllegalAccessException {
        if(sqBean == null){
            return ResultFactory.buildFailResult("参数不能为空");
        }
        if(StringUtils.isEmpty(sqBean.getName())) {
            // 查询全部
            sqBean.setName("");
        }
        Map<String,Object> params =  ObjectUtil.objectToMap(sqBean);
        PageConvert.currentPageConvertStartIndex(params);
        return ResultFactory.buildSuccessResult(myTaskService.getSampleProcessList(params));
    }



}
