package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.domain.po.SampleSiteRule;
import com.nyuen.camunda.mapper.SampleSiteRuleMapper;
import com.nyuen.camunda.service.SampleSiteRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/21
 */
@Service
public class SampleSiteRuleServiceImpl implements SampleSiteRuleService {
    @Resource
    private SampleSiteRuleMapper sampleSiteRuleMapper;


    @Override
    public List<SampleSiteRule> getHoleAndAssayByProductName(List<String> productNames) {
        return sampleSiteRuleMapper.getHoleAndAssayByProductName(productNames);
    }
}
