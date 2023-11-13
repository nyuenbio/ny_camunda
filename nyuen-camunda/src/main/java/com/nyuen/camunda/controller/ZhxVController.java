package com.nyuen.camunda.controller;

import com.nyuen.camunda.domain.po.SampleLabInfo;
import com.nyuen.camunda.domain.po.SampleSiteRuleV;
import com.nyuen.camunda.domain.vo.BatchStartProcessBean;
import com.nyuen.camunda.domain.vo.SampleReceiveBean;
import com.nyuen.camunda.mapper.CnvDrugMapper;
import com.nyuen.camunda.mapper.SampleSiteRuleVMapper;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.SampleLabInfoService;
import com.nyuen.camunda.service.SampleSiteRuleService;
import com.nyuen.camunda.utils.ListUtil;
import com.nyuen.camunda.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 臻慧选V5版本
 *
 * @author chengjl
 * @description 臻慧选V5版本
 * @date 2023/11/08
 */
@Api(tags = "臻慧选V5版本控制类")
@RestController
@RequestMapping("/zhxv")
public class ZhxVController {
    @Resource
    private IdentityService identityService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private SampleSiteRuleService sampleSiteRuleService;
    @Resource
    private SampleLabInfoService sampleLabInfoService;
    @Resource
    private CnvDrugMapper cnvDrugMapper;
    @Resource
    private SampleSiteRuleVMapper sampleSiteRuleVMapper;


    @ApiOperation(value = "批量开启流程V5",httpMethod = "POST")
    @PostMapping("/batchStartProcessV")
    public Result batchStartProcessV(@RequestBody BatchStartProcessBean batchStartProcessBean){
        String initiator = batchStartProcessBean.getInitiator();
        String procDefId = batchStartProcessBean.getProcDefId();
        for(SampleReceiveBean srBean : batchStartProcessBean.getSampleReceiveList()) {
            identityService.setAuthenticatedUserId(initiator);
            // 如果是臻慧选产品或贝安臻抗癫痫用药，处理套餐与孔位规则
            if(236 == srBean.getProductType() || 243 == srBean.getProductType() ) {
                Map<String, Object> variables = new HashMap<>();
                List<SampleSiteRuleV> sampleSiteRuleList = sampleSiteRuleVMapper.getVHoleByProductName(Arrays.asList(srBean.getProductName().split(",")));
                if(sampleSiteRuleList != null && sampleSiteRuleList.size()>0) {
                    LinkedHashSet<String> holeCodesSet = new LinkedHashSet<>();
                    LinkedHashSet<String> assayCodesSet = new LinkedHashSet<>();
                    boolean isCnv = isCnv(srBean); //  区分自选药和其他套餐是否做cnv判断
                    List<SampleSiteRuleV> resultList = sampleSiteRuleList.stream().peek(m -> {
                        List<String> hole = Arrays.asList(m.getHoleCode().split(","));
                        holeCodesSet.addAll(hole);
                        List<String> assay = Arrays.asList(m.getAssayCode().split(","));
                        assayCodesSet.addAll(assay);
                    }).collect(Collectors.toList());//[A,B,C]
                    String holeCodes = holeCodesSet.toString().substring(1, holeCodesSet.toString().length() - 1);
                    String assayCodes = assayCodesSet.toString().substring(1, assayCodesSet.toString().length() - 1);

                    variables.put("套餐名称", srBean.getProductName());
                    variables.put("孔位", holeCodesSet.size());
                    variables.put("对应编码", holeCodes);
                    variables.put("ASSAY编号", assayCodes);
                    //boolean hlaFlag = isHla(srBean);
                    //variables.put("HLA",hlaFlag?"是":"");
                    ProcessInstance pi = runtimeService.startProcessInstanceById(procDefId, srBean.getSampleInfo(), variables);
                    SampleLabInfo sampleLabInfo = new SampleLabInfo();
                    sampleLabInfo.setSampleInfo(srBean.getSampleInfo());
                    sampleLabInfo.setInitiator(initiator);
                    sampleLabInfo.setProcDefId(procDefId);
                    sampleLabInfo.setProcInstId(pi.getId());
                    sampleLabInfo.setProductType(srBean.getProductType());
                    sampleLabInfo.setProductName(srBean.getProductName());
                    sampleLabInfo.setSampleType(srBean.getSampleType());
                    sampleLabInfo.setHospitalId(srBean.getHospitalId());
                    sampleLabInfo.setHospitalName(srBean.getHospitalName());
                    sampleLabInfo.setHoleNum(holeCodesSet.size());
                    sampleLabInfo.setHoleCode(holeCodes);
                    sampleLabInfo.setAssayCode(assayCodes);
                    sampleLabInfo.setCreateTime(new Date());
                    sampleLabInfo.setRemark(isCnv ? "":"不做CNV");
                    //sampleLabInfo.setHlaRemark(hlaFlag?"是":null);
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


    @ApiOperation(value = "校验样本是否有孔位变化V5",httpMethod = "POST")
    @PostMapping("/checkIfHoleChangeV")
    public Result checkIfHoleChangeV(@RequestBody BatchStartProcessBean batchStartProcessBean){
        StringBuilder result = new StringBuilder();
        for(SampleReceiveBean srBean : batchStartProcessBean.getSampleReceiveList()) {
            SampleLabInfo sampleLabInfo = sampleLabInfoService.getLastSampleLabInfoBySampleNum(srBean.getSampleInfo());
            if(null == sampleLabInfo){
                return ResultFactory.buildFailResult(srBean.getSampleInfo()+"该样本还未发起实验流程，无需追加实验！");
            }
            if(sampleLabInfo.getProductName().equals(srBean.getProductName())){
                break;
            }
            List<SampleSiteRuleV> sampleSiteRuleList = sampleSiteRuleVMapper.getVHoleByProductName(Arrays.asList(srBean.getProductName().split(",")));
            LinkedHashSet<String> holeCodesSet = new LinkedHashSet<>();
            if(sampleSiteRuleList != null && sampleSiteRuleList.size()>0) {
                List<SampleSiteRuleV> resultList = sampleSiteRuleList.stream().peek(m -> {
                    List<String> hole = Arrays.asList(m.getHoleCode().split(","));
                    holeCodesSet.addAll(hole);
                }).collect(Collectors.toList());

                //  区分自选药和其他套餐是否做cnv判断
                if (StringUtil.isNotEmpty(sampleLabInfo.getRemark()) && sampleLabInfo.getRemark().contains("不做CNV")) {
                    boolean isCnv = isCnv(srBean);
                    if (isCnv) {
                        result.append("样本").append(sampleLabInfo.getSampleInfo()).append("追加做CNV。");
                        break;
                    }
                }
            }
            String holeCodes = holeCodesSet.toString().substring(1, holeCodesSet.toString().length() - 1);
            if(!sampleLabInfo.getHoleCode().equals(holeCodes)){
                result.append("样本").append(sampleLabInfo.getSampleInfo()).append("原套餐位点为（").append(sampleLabInfo.getProductName()).append("#").append(sampleLabInfo.getHoleCode()).append(")")
                        .append("，现追加套餐位点为（").append(srBean.getProductName()).append("#").append(holeCodes).append("。");
            }
        }
        return ResultFactory.buildResult(200, result.length()>0?result.toString():"孔位无变化","");
    }

    //追加套餐样本发起流程 remark添加追加的套餐和孔位
    @ApiOperation(value = "追加套餐样本发起流程V5",httpMethod = "POST")
    @PostMapping("/appendSampleBatchStartProcessV")
    public Result appendSampleBatchStartProcessV(@RequestBody BatchStartProcessBean batchStartProcessBean){
        String initiator = batchStartProcessBean.getInitiator();
        String procDefId = batchStartProcessBean.getProcDefId();
        for(SampleReceiveBean srBean : batchStartProcessBean.getSampleReceiveList()) {
            SampleLabInfo sampleLabInfo = sampleLabInfoService.getLastSampleLabInfoBySampleNum(srBean.getSampleInfo());
            //boolean hlaFlag = isHla(srBean);
            if(null == sampleLabInfo){
                return ResultFactory.buildFailResult(srBean.getSampleInfo()+"该样本位点信息不存在！");
            }
            if(sampleLabInfo.getProductName().equals(srBean.getProductName())){
                break;
            }
            List<SampleSiteRuleV> sampleSiteRuleList = sampleSiteRuleVMapper.getVHoleByProductName(Arrays.asList(srBean.getProductName().split(",")));
            LinkedHashSet<String> holeCodesSet = new LinkedHashSet<>();
            LinkedHashSet<String> assayCodesSet = new LinkedHashSet<>();
            boolean cnvAppendFlag = false;
            if(sampleSiteRuleList != null && sampleSiteRuleList.size()>0) {
                List<SampleSiteRuleV> resultList = sampleSiteRuleList.stream().peek(m -> {
                    List<String> hole = Arrays.asList(m.getHoleCode().split(","));
                    holeCodesSet.addAll(hole);
                    List<String> assay = Arrays.asList(m.getAssayCode().split(","));
                    assayCodesSet.addAll(assay);
                }).collect(Collectors.toList());
                if (StringUtil.isNotEmpty(sampleLabInfo.getRemark()) && sampleLabInfo.getRemark().contains("不做CNV")) {
                    //  区分自选药和其他套餐是否做cnv判断
                    cnvAppendFlag = isCnv(srBean);
                }
            }
            String holeCodes = holeCodesSet.toString().substring(1, holeCodesSet.toString().length() - 1);
            String assayCodes = assayCodesSet.toString().substring(1, assayCodesSet.toString().length() - 1);
            if(cnvAppendFlag || !sampleLabInfo.getHoleCode().equals(holeCodes)){
                String appendHoles = holeCodes.replace(sampleLabInfo.getHoleCode(),"");
                String appendProduct = srBean.getProductName().replace(sampleLabInfo.getProductName(),"");
                String appendAssayCodes = assayCodes.replace(sampleLabInfo.getAssayCode(),"");
                // 发起追加样本流程
                // 更新remark添加追加的套餐和孔位
                Map<String, Object> variables = new HashMap<>();
                variables.put("套餐名称", appendProduct);
                variables.put("孔位", appendHoles.replace(",","").length());
                variables.put("对应编码", appendHoles);
                variables.put("ASSAY编号", appendAssayCodes);
                variables.put("CNV",cnvAppendFlag?"是":"");
                ProcessInstance pi = runtimeService.startProcessInstanceById(procDefId, srBean.getSampleInfo(), variables);
                SampleLabInfo sampleLabInfo1 = new SampleLabInfo();
                sampleLabInfo1.setId(sampleLabInfo.getId());
                sampleLabInfo1.setProcInstId(pi.getId());// 导出孔位信息时，需根据该字段导出,所以需要更新
                sampleLabInfo1.setProductName(srBean.getProductName());
                //sampleLabInfo1.setHlaRemark(hlaFlag?"是":null);
                String appendRemark = sampleLabInfo.getRemark() +"。"+
                        "追加孔位[" + appendHoles + "] " +
                        (cnvAppendFlag?",追加做CNV":"")+
                        ",追加套餐[" + appendProduct + "] " +
                        ",追加发起人[" + initiator + "] " +
                        ",原procInstId [" + sampleLabInfo.getProcInstId() + "] ";
                sampleLabInfo1.setRemark(appendRemark);
                sampleLabInfoService.updateSampleLabInfo(sampleLabInfo1);
            }
        }
        return ResultFactory.buildSuccessResult(null);
    }

    @ApiOperation(value = "根据样本编号获取孔位信息V5",httpMethod = "POST")
    @PostMapping("/getHoleBySampleV")
    public Result getHoleBySampleV(@RequestBody List<SampleReceiveBean> sampleReceiveBeanList){
        List<Map<String, Object>> resultList = new ArrayList<>();
        for(SampleReceiveBean srBean : sampleReceiveBeanList) {
            // 如果是臻慧选产品或贝安臻抗癫痫用药，处理套餐与孔位规则
            if (236 == srBean.getProductType() || 243 == srBean.getProductType()) {
                Map<String, Object> variables = new HashMap<>();
                List<SampleSiteRuleV> sampleSiteRuleList = sampleSiteRuleVMapper.getVHoleByProductName(Arrays.asList(srBean.getProductName().split(",")));
                if (sampleSiteRuleList != null && sampleSiteRuleList.size() > 0) {
                    LinkedHashSet<String> holeCodesSet = new LinkedHashSet<>();
                    LinkedHashSet<String> assayCodesSet = new LinkedHashSet<>();
                    boolean isCnv = isCnv(srBean); //  区分自选药和其他套餐是否做cnv判断
                    List<SampleSiteRuleV> tempList = sampleSiteRuleList.stream().peek(m -> {
                        List<String> hole = Arrays.asList(m.getHoleCode().split(","));
                        holeCodesSet.addAll(hole);
                        List<String> assay = Arrays.asList(m.getAssayCode().split(","));
                        assayCodesSet.addAll(assay);
                    }).collect(Collectors.toList());//[A,B,C]
                    String holeCodes = holeCodesSet.toString().substring(1, holeCodesSet.toString().length() - 1);
                    String assayCodes = assayCodesSet.toString().substring(1, assayCodesSet.toString().length() - 1);

                    variables.put("sampleInfo",srBean.getSampleInfo());
                    variables.put("productName", srBean.getProductName());
                    variables.put("holeCodes", holeCodes);
                    variables.put("assayCodes", assayCodes);
                    variables.put("cnvState",isCnv ? 1:0);//0：不做CNV
                    //boolean hlaFlag = isHla(srBean);
                    //variables.put("hla", hlaFlag?"是":"");
                    resultList.add(variables);
                }
            }
        }
        return ResultFactory.buildSuccessResult(resultList);
    }

    private boolean isCnv(SampleReceiveBean srBean){
        boolean cnvFlag = false;
        if("自选药物检测".equals(srBean.getTestType())){
            List<String> cnvDrugList = cnvDrugMapper.getAllCnvDrug();
            String[] medicines = srBean.getMedicines().split(",");
            for (String medicine : medicines){
                if(ListUtil.isStrInList(medicine,cnvDrugList)){
                    cnvFlag = true;
                    break;
                }
            }
        }else {
            List<String> cnvProductList = sampleSiteRuleService.getAllCnvProduct();
            String[] productNames = srBean.getProductName().split(",");
            for(String productName : productNames) {
                if (ListUtil.isStrInList(productName, cnvProductList)) {
                    cnvFlag = true;
                    break;
                }
            }
        }
        return cnvFlag;
    }

    public static void main(String[] args) {
        SampleReceiveBean srBean = new SampleReceiveBean();
        srBean.setProductType(236);
        srBean.setProductName("抗精神病药,抗抑郁药,心境稳定剂,抗凝血药和抗血小板药/抗痛风药,抗高血压药,降糖药和抗糖尿病药," +
                "烟草使用障碍药物" +
                "镇痛药物" +
                "质子泵抑制药物");

    }

}
