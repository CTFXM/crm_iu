package com.king.crm.dao;

import com.king.crm.base.BaseMapper;
import com.king.crm.vo.CustomerOrder;

import java.util.Map;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder, Integer> {

    // 通过订单ID查询对应的订单记录
    Map<String, Object> queryOrderById(Integer orderId);

    // 通过客户ID查询最后一条订单记录
    CustomerOrder queryLossCustomerOrderByCustomerId(Integer id);
}