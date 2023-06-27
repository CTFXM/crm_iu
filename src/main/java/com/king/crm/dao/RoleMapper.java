package com.king.crm.dao;

import com.king.crm.base.BaseMapper;
import com.king.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role, Integer> {

    // 查询所有的角色列表(只需要id与roleName)
    public List<Map<String, Object>> queryAllRoles(Integer userId);

    public Role selectByRoleName(String roleName);
}