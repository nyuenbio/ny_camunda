package com.nyuen.camunda.service;

import com.nyuen.camunda.domain.po.LabFridgeLevel;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/2/21
 */
public interface LabFridgeLevelService {

    List<LabFridgeLevel> getSampleTypeByFridgeNoAndLevel(LabFridgeLevel labFridgeLevel);

    void addLabFridgeLevel(LabFridgeLevel lfl);

    List<LabFridgeLevel> getFridgeUsedLevel(String fridgeNo);
}
