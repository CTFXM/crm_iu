package com.king.crm.query;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/14
 */

import com.king.crm.base.BaseQuery;

/**
 * 营销机会管理多条件查询条件
 */
public class SaleChanceQuery extends BaseQuery {

    // 营销机会管理 条件查询
    private String customerName; // 客户名称
    private String createMan; // 创建⼈
    private Integer state; // 分配状态

    // 客户开发计划 条件查询
    private String devResult; // 开发状态
    private Integer assignMan; // 指派人

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }
}
