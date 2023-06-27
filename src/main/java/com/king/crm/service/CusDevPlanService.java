package com.king.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.king.crm.base.BaseService;
import com.king.crm.dao.CusDevPlanMapper;
import com.king.crm.dao.SaleChanceMapper;
import com.king.crm.query.CusDevPlanQuery;
import com.king.crm.query.SaleChanceQuery;
import com.king.crm.utils.AssertUtil;
import com.king.crm.vo.CusDevPlan;
import com.king.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.dc.pr.PRError;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/15
 */
@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;



    /**
     * 多条件分⻚查询客户开发计划 (BaseService 中有对应的⽅法)
     * @param query
     * @return
     */
    public Map<String, Object> queryCusDevPlanByParams (CusDevPlanQuery query) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(), query.getLimit());
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(query));
        map.put("code",0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }


    /**
     * 添加客户开发计划项数据
     *  1. 参数校验
     *      营销机会ID   非空，数据存在
     *      计划项内容   非空
     *      计划时间     非空
     *  2. 设置参数的默认值
     *      是否有效    默认有效
     *      创建时间    系统当前时间
     *      修改时间    系统当前时间
     *  3. 执行添加操作，判断受影响的行数
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        /* 1. 参数校验 */
        checkCusDevPlanParams(cusDevPlan);
        /* 2. 设置参数的默认值 */
        //    是否有效    默认有效
        cusDevPlan.setIsValid(1);
        //    创建时间    系统当前时间
        cusDevPlan.setCreateDate(new Date());
        //    修改时间    系统当前时间
        cusDevPlan.setUpdateDate(new Date());
        /* 3. 执行添加操作，判断受影响的行数 */
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) != 1, "计划项数据添加失败！");
    }

    /**
     * 添加客户开发计划项数据
     *  1. 参数校验
     *      计划项ID    非空，数据存在
     *      营销机会ID   非空，数据存在
     *      计划项内容   非空
     *      计划时间     非空
     *  2. 设置参数的默认值
     *      修改时间    系统当前时间
     *  3. 执行添加操作，判断受影响的行数
     *
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan) {
        /* 1. 参数校验 */
        // 计划项ID    非空，数据存在
        System.out.println(cusDevPlan.getId());

        AssertUtil.isTrue(null == cusDevPlan.getId() || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null, "数据异常，请重试！");
        checkCusDevPlanParams(cusDevPlan);
        /* 2. 设置参数的默认值 */
        //    修改时间    系统当前时间
        cusDevPlan.setUpdateDate(new Date());
        /* 3. 执行添加操作，判断受影响的行数 */
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "计划项数据更新失败！");
    }

    /**
     *  参数校验
     *      营销机会ID   非空，数据存在
     *      计划项内容   非空
     *      计划时间     非空
     *
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        Integer saleChanceId = cusDevPlan.getSaleChanceId();
        // 营销机会ID   非空，数据存在
        AssertUtil.isTrue( null == saleChanceId || saleChanceMapper.selectByPrimaryKey(saleChanceId) == null, "数据异常，请重试！");
        // 计划项内容   非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()), "计划项内容不能为空！");
        // 计划时间     非空
        AssertUtil.isTrue( null == cusDevPlan.getPlanDate(), "计划时间不能为空！");
    }


    /**
     * 删除计划项
     *  1.判断ID是否为空，且数据存在
     *  2.修改isValid属性
     *  3.执行更新操作
     * @param id
     */
    public void deleteCusDevPlan(Integer id) {
        // 1.判断ID是否为空，且数据存在
        AssertUtil.isTrue(null == id || cusDevPlanMapper.selectByPrimaryKey(id) == null, "待删除记录不存在！");
        // 通过ID查询计划项对象
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        // 2.修改isValid属性 (设置记录无效）
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        // 3.执行更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "计划项数据删除失败！");
    }
}
