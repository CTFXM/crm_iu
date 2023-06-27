package com.king.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.king.crm.base.BaseService;
import com.king.crm.dao.CustomerLossMapper;
import com.king.crm.query.CustomerLossQuery;
import com.king.crm.utils.AssertUtil;
import com.king.crm.vo.CustomerLoss;
import com.king.crm.vo.CustomerOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/21
 */
@Service
public class CustomerLossService extends BaseService<CustomerLoss,Integer> {

    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 分页条件查询
     * @param customerLossQuery
     * @return
     */
    public Map<String, Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery) {
        Map<String, Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(customerLossQuery.getPage(),customerLossQuery.getLimit());

        // 得到对应的分页对象
        PageInfo<CustomerLoss> pageInfo = new PageInfo<>(customerLossMapper.selectByParams(customerLossQuery));

        // 设置map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }

    /**
     * 更新流失客户的流失状态
     * @param id
     * @param lossReason
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerLossStateById(Integer id, String lossReason) {
        /* 1. 参数校验 */
        AssertUtil.isTrue(null == id, "待确认流失的客户不存在！");
        CustomerLoss customerLoss = customerLossMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null == customerLoss, "待确认的流失客户不存在！");
        // 流失原因非空
        AssertUtil.isTrue(StringUtils.isBlank(lossReason),"流失原因不能为空！");

        /* 2. 设置参数的默认值 */
        customerLoss.setState(1);
        customerLoss.setLossReason(lossReason);
        customerLoss.setConfirmLossTime(new Date());
        customerLoss.setUpdateDate(new Date());

        /* 3. 执行更新操作，判断受影响的行数 */
        AssertUtil.isTrue(customerLossMapper.updateByPrimaryKeySelective(customerLoss) < 1, "确认流失失败！");


    }
}
