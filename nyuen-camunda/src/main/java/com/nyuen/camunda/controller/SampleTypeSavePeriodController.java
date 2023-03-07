package com.nyuen.camunda.controller;

import com.nyuen.camunda.common.SampleTypeEnums;
import com.nyuen.camunda.domain.po.SampleTypeSavePeriod;
import com.nyuen.camunda.mapper.SampleTypeSavePeriodMapper;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.SampleTypeSavePeriodService;
import com.nyuen.camunda.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;

/**
 * TODO
 *
 * @author chengjl
 * @description 样本保存周期控制类
 * @date 2023/3/6
 */
@Api(tags = "样本保存周期控制类")
@RestController
@RequestMapping("/savePeriod")
public class SampleTypeSavePeriodController {
    @Resource
    private SampleTypeSavePeriodService sampleTypeSavePeriodService;

    @ApiOperation(value = "获取所有样本保存周期信息", httpMethod = "GET")
    @GetMapping("/getSavePeriodList")
    public Result getSavePeriodList(){
        return ResultFactory.buildSuccessResult(sampleTypeSavePeriodService.getSavePeriodList());
    }

    @ApiOperation(value = "添加样本保存周期信息", httpMethod = "POST")
    @PostMapping("/addSavePeriod")
    public Result addSavePeriod(@RequestBody SampleTypeSavePeriod sampleTypeSavePeriod, HttpServletRequest request){
        if(StringUtil.isEmpty(sampleTypeSavePeriod.getSampleType()) || null == sampleTypeSavePeriod.getSavePeriod()){
            return ResultFactory.buildFailResult("样本类型或保存周期不能为空");
        }
        if(!SampleTypeEnums.contains(sampleTypeSavePeriod.getSampleType())){
            return ResultFactory.buildFailResult("请选择正确的样本类型！");
        }
        Integer savePeriod = sampleTypeSavePeriodService.getPeriodBySampleType(sampleTypeSavePeriod.getSampleType());
        if(null != savePeriod){
            return ResultFactory.buildFailResult("该样本类型已有保存周期，请勿重复添加！");
        }
        String loginUserName = null==request.getHeader("loginUserName")?null: URLDecoder.decode(request.getHeader("loginUserName"));
        Long loginUserId;
        try {
            loginUserId = Long.parseLong(request.getHeader("loginUserId"));
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        sampleTypeSavePeriod.setId(null);
        sampleTypeSavePeriod.setCreateUserId(loginUserId);
        sampleTypeSavePeriod.setCreateUserName(loginUserName);
        sampleTypeSavePeriod.setCreateTime(new Date());
        sampleTypeSavePeriodService.addSavePeriod(sampleTypeSavePeriod);
        return ResultFactory.buildSuccessResult(null);
    }

    @ApiOperation(value = "删除样本保存周期信息", httpMethod = "DELETE")
    @DeleteMapping("/delSavePeriod")
    public Result delSavePeriod(@RequestParam("id") int id){
        sampleTypeSavePeriodService.delSavePeriod(id);
        return ResultFactory.buildSuccessResult(null);
    }

    @ApiOperation(value = "修改样本保存周期信息", httpMethod = "POST")
    @PostMapping("/updateSavePeriod")
    public Result updateSavePeriod(@RequestBody SampleTypeSavePeriod sampleTypeSavePeriod){
        if(null == sampleTypeSavePeriod.getId()){
            return ResultFactory.buildFailResult("主键不能为空");
        }
        if(StringUtil.isEmpty(sampleTypeSavePeriod.getSampleType()) || null == sampleTypeSavePeriod.getSavePeriod()){
            return ResultFactory.buildFailResult("样本类型或保存周期不能为空");
        }
        sampleTypeSavePeriodService.updateSavePeriod(sampleTypeSavePeriod);
        return ResultFactory.buildSuccessResult(null);
    }


}
