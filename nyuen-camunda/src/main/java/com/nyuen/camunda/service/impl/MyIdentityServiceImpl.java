package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.domain.po.ActIdGroup;
import com.nyuen.camunda.domain.po.ActIdUser;
import com.nyuen.camunda.mapper.ActIdGroupMapper;
import com.nyuen.camunda.mapper.ActIdUserMapper;
import com.nyuen.camunda.service.MyIdentityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/10/19
 */
@Service
public class MyIdentityServiceImpl implements MyIdentityService {
    @Resource
    private ActIdGroupMapper groupMapper;
    @Resource
    private ActIdUserMapper userMapper;

    @Override
    public void updateGroup(ActIdGroup group) {
        groupMapper.updateByPrimaryKeySelective(group);
    }

    @Override
    public void updateUser(ActIdUser user) {
        userMapper.updateByPrimaryKeySelective(user);
    }
}
