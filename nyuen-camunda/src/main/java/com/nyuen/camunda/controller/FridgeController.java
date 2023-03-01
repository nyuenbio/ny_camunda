package com.nyuen.camunda.controller;

import com.nyuen.camunda.domain.po.LabFridge;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.LabFridgeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author chengjl
 * @description 冰箱信息控制类
 * @date 2023/2/27
 */
@Api(tags = "冰箱信息控制类")
@RestController
@RequestMapping("/fridge")
public class FridgeController {
    @Resource
    private LabFridgeService labFridgeService;

    @ApiOperation(value = "获取所有冰箱信息（开启、未开启）", httpMethod = "GET")
    @GetMapping("/getAllFridges")
    public Result getAllFridges(){
        return ResultFactory.buildSuccessResult(labFridgeService.getAllLabFridgeList());
    }

    @ApiIgnore
    @ApiOperation(value = "更新冰箱状态（1开启、0未开启,冰箱id必传）", httpMethod = "POST")
    @PostMapping("/updateFridgeState")
    public Result updateFridgeState(@RequestBody LabFridge labFridge){
        if(null == labFridge || null == labFridge.getId()){
            return ResultFactory.buildFailResult("冰箱id不能为空");
        }
        if(0 != labFridge.getFridgeState() || 1 != labFridge.getFridgeState()){
            return ResultFactory.buildFailResult("状态只能为开启或未开启");
        }
        labFridgeService.updateFridgeState(labFridge);
        return ResultFactory.buildSuccessResult(null);
    }


}
