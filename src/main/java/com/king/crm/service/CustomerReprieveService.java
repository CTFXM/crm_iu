package com.king.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.king.crm.base.BaseService;
import com.king.crm.base.ResultInfo;
import com.king.crm.dao.CustomerLossMapper;
import com.king.crm.dao.CustomerReprieveMapper;
import com.king.crm.query.CustomerReprieveQuery;
import com.king.crm.utils.AssertUtil;
import com.king.crm.vo.CustomerLoss;
import com.king.crm.vo.CustomerReprieve;
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
public class CustomerReprieveService extends BaseService<CustomerReprieve, Integer> {

    @Resource
    private CustomerReprieveMapper customerReprieveMapper;
    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 分页查询流失客户暂缓操作的列表
     * @param customerReprieveQuery
     * @return
     */
    public Map<String, Object> queryCustomerReprieveByParams(CustomerReprieveQuery customerReprieveQuery) {
        Map<String, Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(customerReprieveQuery.getPage(),customerReprieveQuery.getLimit());

        // 得到对应的分页对象
        PageInfo<CustomerReprieve> pageInfo = new PageInfo<>(customerReprieveMapper.selectByParams(customerReprieveQuery));

        // 设置map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }

    /**
     * 添加暂缓数据
     * @param customerReprieve
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerRepr(CustomerReprieve customerReprieve) {
        /* 1. 参数校验 */
        checkParams(customerReprieve.getLossId(), customerReprieve.getMeasure());

        /* 2. 设置参数的默认值 */
        customerReprieve.setIsValid(1);
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());

        /* 3. 执行添加操作，判断受影响行数 */
        AssertUtil.isTrue(customerReprieveMapper.insertSelective(customerReprieve) < 1, "添加暂缓数据失败！");
    }

    /**
     * 修改暂缓操作
     * @param customerReprieve
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerRepr(CustomerReprieve customerReprieve) {
        /* 1. 参数校验 */
        AssertUtil.isTrue(null == customerReprieve.getId() || customerReprieveMapper.selectByPrimaryKey(customerReprieve.getId()) == null ,"待更新记录不存在！");
        checkParams(customerReprieve.getLossId(), customerReprieve.getMeasure());

        /* 2. 设置参数的默认值 */
        customerReprieve.setUpdateDate(new Date());

        /* 3. 执行添加操作，判断受影响行数 */
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve) < 1, "修改暂缓数据失败！");
    }

    public void checkParams(Integer lossId, String measure) {
        AssertUtil.isTrue( null == lossId || customerLossMapper.selectByPrimaryKey(lossId) == null, "流失客户记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(measure),"暂缓措施不能为空！");
    }

    /**
     * 删除暂缓操作
     * @param id
     */
    public void deleteCustomerRepr(Integer id) {
        /* 1. 参数校验 */
        AssertUtil.isTrue(null == id, "待删除记录不存在！");
        // 通过ID查询暂缓数据
        CustomerReprieve customerReprieve = customerReprieveMapper.selectByPrimaryKey(id);
        // 判断数据是否为空
        AssertUtil.isTrue(null == customerReprieve, "待删除记录不存在！");

        /* 2. 设置参数的默认值 */
        customerReprieve.setIsValid(0);
        customerReprieve.setUpdateDate(new Date());

        /* 3. 执行添加操作，判断受影响行数 */
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve) < 1, "删除暂缓数据失败！");
    }
}
