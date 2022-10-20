package com.nyuen.camunda.service;

import com.nyuen.camunda.domain.po.ActIdGroup;
import com.nyuen.camunda.domain.po.ActIdUser;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/10/19
 */
public interface MyIdentityService {

    void updateGroup(ActIdGroup group);

    void updateUser(ActIdUser user);
}
