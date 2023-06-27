package com.king.crm.query;

import com.king.crm.base.BaseQuery;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/21
 */
public class CustomerOrderQuery extends BaseQuery {

    private Integer cusId;  // 客户ID


    public Integer getCusId() {
        return cusId;
    }

    public void setCusId(Integer cusId) {
        this.cusId = cusId;
    }
}
