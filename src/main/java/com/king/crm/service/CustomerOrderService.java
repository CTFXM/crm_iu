package com.king.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.king.crm.base.BaseService;
import com.king.crm.dao.CustomerOrderMapper;
import com.king.crm.query.CustomerOrderQuery;
import com.king.crm.vo.Customer;
import com.king.crm.vo.CustomerOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/21
 */
@Service
public class CustomerOrderService extends BaseService<CustomerOrder, Integer> {

    @Resource
    private CustomerOrderMapper customerOrderMapper;

    /**
     * 多条件查询客户订单
     * @param customerOrderQuery
     * @return
     */
    public Map<String, Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery) {
        Map<String, Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(customerOrderQuery.getPage(),customerOrderQuery.getLimit());

        // 得到对应的分页对象
        PageInfo<CustomerOrder> pageInfo = new PageInfo<>(customerOrderMapper.selectByParams(customerOrderQuery));

        // 设置map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }

    /**
     * 通过订单ID查询对应的订单记录
     * @param orderId
     * @return
     */
    public Map<String, Object> queryOrderById(Integer orderId) {
        return customerOrderMapper.queryOrderById(orderId);
    }
}
