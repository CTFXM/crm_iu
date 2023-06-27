package com.king.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.king.crm.base.BaseService;
import com.king.crm.dao.OrderDetailsMapper;
import com.king.crm.query.OrderDetailsQuery;
import com.king.crm.vo.CustomerOrder;
import com.king.crm.vo.OrderDetails;
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
public class OrderDetailsService extends BaseService<OrderDetails, Integer> {

    @Resource
    private OrderDetailsMapper orderDetailsMapper;

    /**
     * 多条件分页查询订单详情列表
     * @param orderDetailsQuery
     * @return
     */
    public Map<String, Object> queryOrderDetailsByParams(OrderDetailsQuery orderDetailsQuery) {
        Map<String, Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(orderDetailsQuery.getPage(),orderDetailsQuery.getLimit());

        // 得到对应的分页对象
        PageInfo<OrderDetails> pageInfo = new PageInfo<>(orderDetailsMapper.selectByParams(orderDetailsQuery));

        // 设置map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }
}
