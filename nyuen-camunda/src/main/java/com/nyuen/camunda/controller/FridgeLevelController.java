package com.nyuen.camunda.controller;

import com.nyuen.camunda.common.SampleTypeEnums;
import com.nyuen.camunda.domain.po.LabFridge;
import com.nyuen.camunda.domain.po.LabFridgeLevel;
import com.nyuen.camunda.domain.po.SampleStorage;
import com.nyuen.camunda.domain.vo.FridgeLevelBoxVo;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.LabFridgeLevelService;
import com.nyuen.camunda.service.SampleStorageService;
import com.nyuen.camunda.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

/**
 * 冰箱-层级信息控制类
 *
 * @author chengjl
 * @description 冰箱-层级信息控制类
 * @date 2023/4/6
 */
@Api(tags = "冰箱-层级信息控制类")
@RestController
@RequestMapping("/fridgeLevel")
public class FridgeLevelController {
    @Resource
    private LabFridgeLevelService labFridgeLevelService;

    // 创建新的层级-盒子
    @ApiOperation(value = "创建新的层级-盒子", httpMethod = "POST")
    @PostMapping("/createLevelBox")
    public Result createLevelBox(@RequestBody FridgeLevelBoxVo flbVo, HttpServletRequest request) {
        if (StringUtil.isEmpty(flbVo.getFridgeNo()) || 0 == flbVo.getLevelNo() || 0 == flbVo.getBoxNum()) {
            return ResultFactory.buildFailResult("冰箱编码、层级数、盒子数不能为空");
        }
        if(!SampleTypeEnums.contains(flbVo.getSampleType())){
            return ResultFactory.buildFailResult("样本类型错误");
        }
        String loginUserIdStr = request.getHeader("loginUserId");
        String loginUserName = null==request.getHeader("loginUserName")?null: URLDecoder.decode(request.getHeader("loginUserName"));
        Long loginUserId;
        try {
            loginUserId = Long.parseLong(loginUserIdStr);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
        String sampleType = flbVo.getSampleType();
        //校验新增层级数的样本类型是否与现有冲突
        //查询层级目前已有盒子数量和编号:计算后续盒子编号
        LabFridgeLevel lfl = new LabFridgeLevel();
        lfl.setFridgeNo(flbVo.getFridgeNo());
        lfl.setLevelNo(flbVo.getLevelNo());
        List<LabFridgeLevel> lflList =labFridgeLevelService.getInfoByFridgeNoAndLevel(lfl);
        int curBoxIndex = 1;//新的层级
        //已有层级
        if(null != lflList && 0 != lflList.size()){
            LabFridgeLevel lastLabFridgeLevel = lflList.get(0);
            if(StringUtil.isEmpty(sampleType) || !sampleType.equals(lastLabFridgeLevel.getSampleType())){
                return ResultFactory.buildFailResult("样本类型冲突！该层级原有样本类型为 "+SampleTypeEnums.getDescByCode(sampleType));
            }
            curBoxIndex = lastLabFridgeLevel.getBoxIndex()+1;
        }
        for(int i=0; i<flbVo.getBoxNum(); i++){
            LabFridgeLevel labFridgeLevel = new LabFridgeLevel();
            labFridgeLevel.setFridgeNo(flbVo.getFridgeNo());
            labFridgeLevel.setLevelNo(flbVo.getLevelNo());
            labFridgeLevel.setSampleType(sampleType);
            labFridgeLevel.setBoxIndex(curBoxIndex+i);
            if(curBoxIndex+i < 10){
                labFridgeLevel.setBoxNo(sampleType+"0"+labFridgeLevel.getBoxIndex());
            }
            labFridgeLevel.setBoxNo(sampleType+labFridgeLevel.getBoxIndex());
            labFridgeLevel.setCreateUserId(loginUserId);
            labFridgeLevel.setCreateUserName(loginUserName);
            labFridgeLevel.setCreateTime(new Date());
            labFridgeLevelService.addLabFridgeLevel(labFridgeLevel);
        }
        return ResultFactory.buildSuccessResult(null);
    }

    //获取已创建的冰箱的层级的盒子
    @ApiOperation(value = "查询冰箱层级已创建的盒子(传冰箱编号和层级号)", httpMethod = "POST")
    @PostMapping("/getFridgeLevelBox")
    public Result getFridgeLevelBox(@RequestBody LabFridgeLevel labFridgeLevel){
        if(StringUtil.isEmpty(labFridgeLevel.getFridgeNo()) || null == labFridgeLevel.getLevelNo()){
            return ResultFactory.buildFailResult("冰箱编号和层级号不能为空!");
        }
        return ResultFactory.buildSuccessResult(labFridgeLevelService.getInfoByFridgeNoAndLevel(labFridgeLevel));
    }

}
