package com.king.crm.service;

import com.king.crm.base.BaseService;
import com.king.crm.dao.UserRoleMapper;
import com.king.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/17
 */
@Service
public class UserRoleService extends BaseService<UserRole, Integer> {

    @Resource
    private UserRoleMapper userRoleMapper;

}
