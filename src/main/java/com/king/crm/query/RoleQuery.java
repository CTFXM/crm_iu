package com.king.crm.query;

import com.king.crm.base.BaseQuery;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/17
 */
public class RoleQuery extends BaseQuery {

    // 角色名称
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
