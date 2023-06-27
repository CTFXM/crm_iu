package com.king.crm.query;

import com.king.crm.base.BaseQuery;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/20
 */
public class CustomerQuery extends BaseQuery {

    private String customerName;    // 客户名称

    private String customerNo;      // 客户编号

    private String level;           // 客户级别

    private String phone;           // 手机号

    private String time;            // 订单时间
    private Integer type;           // 金额区间

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
