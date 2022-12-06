package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.domain.po.Reference;
import com.nyuen.camunda.mapper.ReferenceMapper;
import com.nyuen.camunda.service.ReferenceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/22
 */
@Service
public class ReferenceServiceImpl implements ReferenceService {
    @Resource
    private ReferenceMapper referenceMapper;


    @Override
    public List<Reference> getReferenceByCode(String code) {
        return referenceMapper.getReferenceByCode(code);
    }

    @Override
    public void updateSearchTimes(String code) {
        referenceMapper.updateSearchTimes(code);
    }
}
