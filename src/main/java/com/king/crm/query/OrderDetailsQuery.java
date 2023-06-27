package com.king.crm.query;

import com.king.crm.base.BaseQuery;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/21
 */
public class OrderDetailsQuery extends BaseQuery {

    private Integer orderId;    // 订单ID

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
