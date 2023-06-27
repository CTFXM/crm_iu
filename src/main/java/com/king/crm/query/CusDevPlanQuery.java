package com.king.crm.query;

import com.king.crm.base.BaseQuery;

import java.lang.ref.PhantomReference;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/16
 */
public class CusDevPlanQuery extends BaseQuery {

    private Integer saleChanceId; // 营销机会的主键

    public Integer getSaleChanceId() {
        return saleChanceId;
    }

    public void setSaleChanceId(Integer saleChanceId) {
        this.saleChanceId = saleChanceId;
    }
}
