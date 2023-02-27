package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.domain.po.LabFridge;
import com.nyuen.camunda.mapper.LabFridgeMapper;
import com.nyuen.camunda.service.LabFridgeService;
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
public class LabFridgeServiceImpl implements LabFridgeService {
    @Resource
    private LabFridgeMapper labFridgeMapper;


    @Override
    public void addLabFridge(LabFridge labFridge) {
        labFridgeMapper.insertSelective(labFridge);
    }

    @Override
    public List<LabFridge> getLabFridgeByNo(String fridgeNo) {
        return labFridgeMapper.getLabFridgeByNo(fridgeNo);
    }

    @Override
    public List<LabFridge> getAllLabFridgeList() {
        return labFridgeMapper.getAllLabFridges();
    }

    @Override
    public void updateFridgeState(LabFridge fridge) {
        labFridgeMapper.updateByPrimaryKeySelective(fridge);
    }
}
