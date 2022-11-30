package com.nyuen.camunda.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nyuen.camunda.domain.po.SampleLabInfo;
import com.nyuen.camunda.domain.po.SampleSiteRule;
import com.nyuen.camunda.domain.vo.BatchStartProcessBean;
import com.nyuen.camunda.domain.vo.SampleReceiveBean;
import com.nyuen.camunda.domain.vo.TodoTask;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.MyTaskService;
import com.nyuen.camunda.service.SampleLabInfoService;
import com.nyuen.camunda.service.SampleSiteRuleService;
import com.nyuen.camunda.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.jvnet.hk2.internal.Collector;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    @Resource
    private MyTaskService myTaskService;
    @Resource
    private SampleLabInfoService sampleLabInfoService;


    @ApiOperation(value = "校验样本流程是否已开启",httpMethod = "POST")
    @PostMapping("/checkSampleProcess")
    public Result checkSampleProcess(@RequestBody BatchStartProcessBean batchStartProcessBean){
        // 查询待办任务中是否已存在该样本：需提示再次确认
        List<SampleReceiveBean> sampleReceiveList = batchStartProcessBean.getSampleReceiveList();
        Map<String,Object> params = new HashMap<>();
        params.put("procDefId",batchStartProcessBean.getProcDefId());
        //params.put("nodeName","抽提");
        List<TodoTask> todoTaskList = myTaskService.getTodoTaskByCondition(params);
        if(todoTaskList ==null || todoTaskList.size() == 0){
            return ResultFactory.buildResult(200,"",true);
        }
        Map<String,String> todoBusinessKeyList = todoTaskList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TodoTask::getBusinessKey))), ArrayList::new))
                .stream().collect(Collectors.toMap(TodoTask::getBusinessKey,TodoTask::getBusinessKey));
        List<String> toStartSampleInfoList  = sampleReceiveList.stream().map(SampleReceiveBean::getSampleInfo).collect(Collectors.toList());
        List<String> existSampleList = toStartSampleInfoList.stream().filter(item-> todoBusinessKeyList.get(item)!=null && todoBusinessKeyList.get(item).equals(item)).collect(Collectors.toList());
        return ResultFactory.buildResult(200,existSampleList.size()==0?"":existSampleList.toString()+"以上样本已在待办流程中，请确认是否再次发起？", existSampleList.size() == 0);
    }


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
                    LinkedHashSet<String> holeCodesSet = new LinkedHashSet<>();
                    LinkedHashSet<String> assayCodesSet = new LinkedHashSet<>();
                    List<SampleSiteRule> resultList = sampleSiteRuleList.stream().map(m -> {
                        List<String> hole = Arrays.asList(m.getHoleCode().split(","));
                        holeCodesSet.addAll(hole);
                        List<String> assay = Arrays.asList(m.getAssayCode().split(","));
                        assayCodesSet.addAll(assay);
                        return m;
                    }).collect(Collectors.toList());
                    String holeCodes = holeCodesSet.toString().substring(1, holeCodesSet.toString().length() - 1);
                    String assayCodes = assayCodesSet.toString().substring(1, assayCodesSet.toString().length() - 1);
                    variables.put("套餐名称", srBean.getProductName());
                    variables.put("孔位", holeCodesSet.size());
                    variables.put("对应编码", holeCodes);
                    variables.put("ASSAY编号", assayCodes);
                    ProcessInstance pi = runtimeService.startProcessInstanceById(procDefId, srBean.getSampleInfo(), variables);
                    SampleLabInfo sampleLabInfo = new SampleLabInfo();
                    sampleLabInfo.setSampleInfo(srBean.getSampleInfo());
                    sampleLabInfo.setInitiator(initiator);
                    sampleLabInfo.setProcDefId(procDefId);
                    sampleLabInfo.setProcInstId(pi.getId());
                    sampleLabInfo.setProductType(srBean.getProductType());
                    sampleLabInfo.setProductName(srBean.getProductName());
                    sampleLabInfo.setHoleNum(holeCodesSet.size());
                    sampleLabInfo.setHoleCode(holeCodes);
                    sampleLabInfo.setAssayCode(assayCodes);
                    sampleLabInfo.setCreateTime(new Date());
                    sampleLabInfoService.addSampleLanInfo(sampleLabInfo);
                }else{
                    runtimeService.startProcessInstanceById(procDefId, srBean.getSampleInfo());

                }
            }else{
                runtimeService.startProcessInstanceById(procDefId, srBean.getSampleInfo());
            }
        }
        return ResultFactory.buildSuccessResult(null);
    }

    @ApiOperation(value = "导出样本位点信息",httpMethod = "POST")
    @PostMapping("/exportSampleSiteInfo")
    public void exportSampleSiteInfo(@RequestBody List<String> procInstIdList, HttpServletResponse response) throws Exception {
        List<SampleLabInfo> sampleLabInfoList = sampleLabInfoService.getSampleLabInfoList(procInstIdList);
        // 构建导出excel表头（第一行）
        String[] excelHeader = {"样本编号", "产品名称", "孔位", "孔位编号", "ASSAY编号","创建时间"};
        ExcelUtil.exportExcel(response,excelHeader,sampleLabInfoList,"样本位点信息","样本位点信息");

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
