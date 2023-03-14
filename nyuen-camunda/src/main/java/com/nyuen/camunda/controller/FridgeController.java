package com.nyuen.camunda.controller;

import com.nyuen.camunda.domain.po.LabFridge;
import com.nyuen.camunda.domain.po.SampleStorage;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.LabFridgeLevelService;
import com.nyuen.camunda.service.LabFridgeService;
import com.nyuen.camunda.service.SampleStorageService;
import com.nyuen.camunda.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Arrays;

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
    @Resource
    private LabFridgeLevelService labFridgeLevelService;
    @Resource
    private SampleStorageService sampleStorageService;

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

    // 查询一个冰箱目前存放哪几个层级
    @ApiOperation(value = "查询一个冰箱目前存放哪几个层级", httpMethod = "GET")
    @GetMapping("/getFridgeUsedLevel")
    public Result getFridgeUsedLevel(@RequestParam("fridgeNo") String fridgeNo){

        return ResultFactory.buildSuccessResult(labFridgeLevelService.getFridgeUsedLevel(fridgeNo));
    }

    // 查询冰箱层级目前存放哪几个盒子
    @ApiOperation(value = "查询冰箱层级目前存放哪几个盒子(传冰箱编号和层级号)", httpMethod = "POST")
    @PostMapping("/getLevelUsedBox")
    public Result getLevelUsedBox(@RequestBody SampleStorage sampleStorage){
        if(StringUtil.isEmpty(sampleStorage.getFridgeNo()) || null == sampleStorage.getLevelNo()){
            return ResultFactory.buildFailResult("冰箱编号和层级号不能为空!");
        }
        return ResultFactory.buildSuccessResult(sampleStorageService.getLevelUsedBox(sampleStorage));
    }



}
