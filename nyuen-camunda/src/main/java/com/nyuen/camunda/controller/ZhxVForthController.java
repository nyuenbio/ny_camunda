package com.nyuen.camunda.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nyuen.camunda.domain.po.SampleSiteRule;
import com.nyuen.camunda.domain.vo.BatchStartProcessBean;
import com.nyuen.camunda.domain.vo.SampleReceiveBean;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.SampleSiteRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author chengjl
 * @description 臻慧选V4版本
 * @date 2022/11/21
 */
@Api(tags = "臻慧选V4版本控制类")
@RestController
@RequestMapping("/zhxf")
public class ZhxVForthController {
    @Resource
    private IdentityService identityService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private SampleSiteRuleService sampleSiteRuleService;


    @ApiOperation(value = "批量开启流程",httpMethod = "POST")
    @PostMapping("/batchStartProcess")
    public Result batchStartProcess(@RequestBody BatchStartProcessBean batchStartProcessBean){
        String initiator = batchStartProcessBean.getInitiator();
        String procDefId = batchStartProcessBean.getProcDefId();
        for(SampleReceiveBean srBean : batchStartProcessBean.getSampleReceiveList()) {
            identityService.setAuthenticatedUserId(initiator);
            // 如果是臻慧选产品或贝安臻抗癫痫用药，处理套餐与孔位规则
            if(236 == srBean.getProductType() || 243 == srBean.getProductType() ) {
                Map<String, Object> variables = new HashMap<>();
                List<SampleSiteRule> sampleSiteRuleList = sampleSiteRuleService.getHoleAndAssayByProductName(Arrays.asList(srBean.getProductName().split(",")));
                if(sampleSiteRuleList != null && sampleSiteRuleList.size()>0) {
                    LinkedHashSet<String> holeCodes = new LinkedHashSet<>();
                    LinkedHashSet<String> assayCodes = new LinkedHashSet<>();
                    List<SampleSiteRule> resultList = sampleSiteRuleList.stream().map(m -> {
                        List<String> hole = Arrays.asList(m.getHoleCode().split(","));
                        holeCodes.addAll(hole);
                        List<String> assay = Arrays.asList(m.getAssayCode().split(","));
                        assayCodes.addAll(assay);
                        return m;
                    }).collect(Collectors.toList());
                    variables.put("套餐名称", srBean.getProductName());
                    variables.put("孔位", holeCodes.size());
                    variables.put("对应编码", holeCodes.toString().substring(1, holeCodes.toString().length() - 1));
                    variables.put("ASSAY编号", assayCodes.toString().substring(1, assayCodes.toString().length() - 1));
                    runtimeService.startProcessInstanceById(procDefId, srBean.getSampleInfo(), variables);
                }else{
                    runtimeService.startProcessInstanceById(procDefId, srBean.getSampleInfo());
                }
            }else{
                runtimeService.startProcessInstanceById(procDefId, srBean.getSampleInfo());
            }
        }
        return ResultFactory.buildSuccessResult(null);
    }

    public static void main(String[] args) {
        SampleReceiveBean srBean = new SampleReceiveBean();
        srBean.setProductType(236);
        srBean.setProductName("抗精神病药,抗抑郁药,心境稳定剂,抗凝血药和抗血小板药/抗痛风药,抗高血压药,降糖药和抗糖尿病药");
        List<SampleSiteRule> sampleSiteRuleList = new ArrayList<>();
        SampleSiteRule ssRule1 = new SampleSiteRule(4,"A,B,C,D","XJ1-30,XJ31-50,YY1-30,YY31-56");
        SampleSiteRule ssRule2 = new SampleSiteRule(4,"A,B,C,D","XJ1-30,XJ31-50,YY1-30,YY31-56");
        SampleSiteRule ssRule3 = new SampleSiteRule(2,"A,B","XJ1-30,XJ31-50");
        SampleSiteRule ssRule4 = new SampleSiteRule(1,"G","XS1-25");
        SampleSiteRule ssRule5 = new SampleSiteRule(1,"E","G1-31");
        SampleSiteRule ssRule6 = new SampleSiteRule(1,"F","TN1-25");
        sampleSiteRuleList.add(ssRule1);
        sampleSiteRuleList.add(ssRule2);
        sampleSiteRuleList.add(ssRule3);
        sampleSiteRuleList.add(ssRule4);
        sampleSiteRuleList.add(ssRule5);
        LinkedHashSet<String> holeCodes = new LinkedHashSet<>();
        LinkedHashSet<String> assayCodes = new LinkedHashSet<>();
        List<SampleSiteRule> resultList = sampleSiteRuleList.stream().map(m-> {
            List<String> hole = Arrays.asList(m.getHoleCode().split(","));
            holeCodes.addAll(hole);
            List<String> assay = Arrays.asList(m.getAssayCode().split(","));
            assayCodes.addAll(assay);
            return m;
        }).collect(Collectors.toList());

        System.out.println(holeCodes.toString().substring(1,holeCodes.toString().length()-1));
        System.out.println(assayCodes.toString());
//        for(String s: holeCodes){
//            System.out.println(s);
//        }


    }

}
