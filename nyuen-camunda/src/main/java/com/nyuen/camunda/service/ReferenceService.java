package com.nyuen.camunda.service;

import com.nyuen.camunda.domain.po.Reference;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description 参考文献service
 * @date 2022/11/22
 */
public interface ReferenceService {
    List<Reference> getReferenceByCode(String code);

    void updateSearchTimes(String code);
}
