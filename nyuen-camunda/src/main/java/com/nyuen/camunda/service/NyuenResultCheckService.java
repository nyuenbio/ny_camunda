package com.nyuen.camunda.service;

import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.po.NyuenResultCheck;
import com.nyuen.camunda.domain.vo.SampleRowAndCell;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/3/1
 */
public interface NyuenResultCheckService {

    PageBean getNyuenResultCheckByCondition(Map<String,Object> params);

}
