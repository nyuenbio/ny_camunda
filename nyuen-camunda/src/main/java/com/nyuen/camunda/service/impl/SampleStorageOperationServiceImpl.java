package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.mapper.SampleStorageOperationMapper;
import com.nyuen.camunda.service.SampleStorageOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/2/21
 */
@Service
public class SampleStorageOperationServiceImpl implements SampleStorageOperationService {
    @Resource
    private SampleStorageOperationMapper sampleStorageOperationMapper;



}
