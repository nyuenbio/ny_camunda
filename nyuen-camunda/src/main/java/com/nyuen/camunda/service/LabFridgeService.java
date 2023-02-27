package com.nyuen.camunda.service;

import com.nyuen.camunda.domain.po.LabFridge;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/2/21
 */
public interface LabFridgeService {

    void addLabFridge(LabFridge labFridge);

    List<LabFridge> getLabFridgeByNo(String fridgeNo);

    List<LabFridge> getAllLabFridgeList();

    void updateFridgeState(LabFridge fridge);
}
