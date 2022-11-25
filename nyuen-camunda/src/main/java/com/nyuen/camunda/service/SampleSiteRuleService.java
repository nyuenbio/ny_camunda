package com.nyuen.camunda.service;

import com.nyuen.camunda.domain.po.SampleSiteRule;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/21
 */
public interface SampleSiteRuleService {

    List<SampleSiteRule> getHoleAndAssayByProductName(List<String> productNames);
}
