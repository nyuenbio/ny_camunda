package com.nyuen.camunda.controller;

import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.ReferenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 参考文献
 *
 * @author chengjl
 * @description
 * @date 2022/11/22
 */
@Api(tags = "参考文献控制层")
@RestController
@RequestMapping("/ref")
public class ReferenceController {
    @Resource
    private ReferenceService referenceService;

    @ApiOperation(value = "根据编号查询文献",httpMethod = "GET")
    @GetMapping("/getReferenceByCode")
    public Result getReferenceByCode(@ApiParam("文献编号") String code){
        return ResultFactory.buildSuccessResult(referenceService.getReferenceByCode(code));
    }

}
