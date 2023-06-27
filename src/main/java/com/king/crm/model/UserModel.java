package com.king.crm.model;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/14
 */
public class UserModel {

//    private Integer userId;     // 用户id
    private String userName;    // 用户姓名
    private String trueName;    // 用户正确信息

    private String userIdStr;   // 加密后用户Id

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }
/*public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }*/

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userName='" + userName + '\'' +
                ", trueName='" + trueName + '\'' +
                ", userIdStr='" + userIdStr + '\'' +
                '}';
    }
}
