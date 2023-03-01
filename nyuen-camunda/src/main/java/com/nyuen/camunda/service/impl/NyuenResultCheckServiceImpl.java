package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.po.NyuenResultCheck;
import com.nyuen.camunda.domain.vo.SampleRowAndCell;
import com.nyuen.camunda.mapper.NyuenResultCheckMapper;
import com.nyuen.camunda.service.NyuenResultCheckService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/3/1
 */
@Service
public class NyuenResultCheckServiceImpl implements NyuenResultCheckService {
    @Resource
    private NyuenResultCheckMapper nyuenResultCheckMapper;


    @Override
    public PageBean getNyuenResultCheckByCondition(Map<String, Object> params) {
        List<NyuenResultCheck> nyuenResultCheckList = nyuenResultCheckMapper.getNyuenResultCheckByCondition(params);
        int total = nyuenResultCheckMapper.getNyuenResultCheckCountByCondition(params);
        return new PageBean(total, nyuenResultCheckList);
    }

}
