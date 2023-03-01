package com.nyuen.camunda.controller;

import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.vo.NyuenResultCheckVo;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.NyuenResultCheckService;
import com.nyuen.camunda.utils.ObjectUtil;
import com.nyuen.camunda.utils.PageConvert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 样本下机结果相关查询
 *
 * @author chengjl
 * @description NyuenResultCheckController
 * @date 2023/3/1
 */
@Api(tags = "样本下机结果相关查询")
@RestController
@RequestMapping("/nyuenResult")
public class NyuenResultCheckController {
    @Resource
    private NyuenResultCheckService nyuenResultCheckService;

    @ApiOperation(value = "根据样本编号或位点编号查询检测结果", httpMethod = "POST")
    @PostMapping("/getNyuenResultCheckList")
    public Result getNyuenResultCheckList(@RequestBody NyuenResultCheckVo nyuenResultCheckVo) throws IllegalAccessException {
        Map<String,Object> map = ObjectUtil.objectToMap(nyuenResultCheckVo);
        PageConvert.currentPageConvertStartIndex(map);
        PageBean pageBean = nyuenResultCheckService.getNyuenResultCheckByCondition(map);
        return ResultFactory.buildSuccessResult(pageBean);
    }

}
