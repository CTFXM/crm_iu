package com.king.crm.query;

import com.king.crm.base.BaseQuery;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/16
 */
public class UserQuery extends BaseQuery {

    // 用户名
    private String userName;

    // 邮箱
    private String email;

    // 电话
    private String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
