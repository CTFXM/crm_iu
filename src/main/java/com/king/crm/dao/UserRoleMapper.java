package com.king.crm.dao;

import com.king.crm.base.BaseMapper;
import com.king.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole, Integer> {

    // 根据用户ID查询用户角色记录
    Integer countUserRoleByUserId(Integer userId);

    // 根据用户ID删除用户角色记录
    Integer deleteUserRoleByUserId(Integer userId);
}