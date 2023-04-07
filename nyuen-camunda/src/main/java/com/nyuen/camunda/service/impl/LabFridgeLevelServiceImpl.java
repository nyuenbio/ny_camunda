package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.domain.po.LabFridgeLevel;
import com.nyuen.camunda.mapper.LabFridgeLevelMapper;
import com.nyuen.camunda.service.LabFridgeLevelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/2/21
 */
@Service
public class LabFridgeLevelServiceImpl implements LabFridgeLevelService {
    @Resource
    private LabFridgeLevelMapper labFridgeLevelMapper;


    @Override
    public List<LabFridgeLevel> getInfoByFridgeNoAndLevel(LabFridgeLevel labFridgeLevel) {
        return labFridgeLevelMapper.getInfoByFridgeNoAndLevel(labFridgeLevel);
    }

    @Override
    public void addLabFridgeLevel(LabFridgeLevel lfl) {
        labFridgeLevelMapper.insertSelective(lfl);
    }

    @Override
    public List<LabFridgeLevel> getFridgeUsedLevel(String fridgeNo) {
        return labFridgeLevelMapper.getFridgeUsedLevel(fridgeNo);
    }
}
