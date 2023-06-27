package com.king.crm.dao;

import com.king.crm.base.BaseMapper;
import com.king.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User, Integer> {

    // 根据用户名查询用户对象
    User queryUserByUserName(String userName);

    // 查询所有的销售⼈员
    public List<Map<String, Object>> queryAllSales();

    // 查询所有的客户经理
    List<Map<String, Object>> queryAllCustomerManagers();
}