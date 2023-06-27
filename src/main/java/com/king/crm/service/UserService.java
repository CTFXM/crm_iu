package com.king.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.king.crm.base.BaseService;
import com.king.crm.dao.UserMapper;
import com.king.crm.dao.UserRoleMapper;
import com.king.crm.model.UserModel;
import com.king.crm.utils.*;
import com.king.crm.vo.User;
import com.king.crm.vo.UserRole;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/14
 */
@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 用户登录
     * @param userName  //前台传入的用户名
     * @param userPwd   //前台传入的用户密码
     */
    public UserModel userLogin(String userName, String userPwd) {
        // 1.验证参数
        checkLoginParams(userName, userPwd);
        // 2.根据用户名，查询用户对象
        User user = userMapper.queryUserByUserName(userName);
        // 3.判断用户是否存在（用户对象为空，记录不存在，方法结束）
        AssertUtil.isTrue(null == user, "用户不存在或已注销！");
        // 4.用户对象不为空（用户存在，密码校验。密码不正确，方法结束）
        checkLoginPwd(userPwd,user.getUserPwd());
        // 5.密码正确（用户登录成功，返回用户的相关信息）
        return buildUserInfo(user);

    }

    /**
     * ⽤户密码修改
     *      1. 参数校验
     *          ⽤户ID：userId ⾮空 ⽤户对象必须存在
     *          原始密码：oldPassword ⾮空 与数据库中密⽂密码保持⼀致
     *          新密码：newPassword ⾮空 与原始密码不能相同
     *          确认密码：confirmPassword ⾮空 与新密码保持⼀致
     *      2. 设置⽤户新密码
     *          新密码进⾏加密处理
     *      3. 执⾏更新操作
     *          受影响的⾏数⼩于1，则表示修改失败
     *
     *          注：在对应的更新⽅法上，添加事务控制
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId, String oldPassword, String newPassword, String confirmPassword) {

        // 通过userId获取⽤户对象
        User user = userMapper.selectByPrimaryKey(userId);
        // 1. 参数校验
        checkPasswordParams(user, oldPassword, newPassword, confirmPassword);
        // 2. 设置⽤户新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        // 3. 执⾏更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户密码更新失败！");

    }

    /**
     * 验证⽤户密码修改参数
     *      ⽤户ID：userId ⾮空 ⽤户对象必须存在
     *      原始密码：oldPassword ⾮空 与数据库中密⽂密码保持⼀致
     *      新密码：newPassword ⾮空 与原始密码不能相同
     *      确认密码：confirmPassword ⾮空 与新密码保持⼀致
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmPassword) {
        // user对象，非空验证
        AssertUtil.isTrue(null == user, "用户未登录或不存在！");
        // 原始密码 ⾮空验证
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword), "请输入原始密码！");
        // 原始密码要与数据库中的密⽂密码保持⼀致
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))), "原始密码不正确！");
        // 新密码 ⾮空校验
        AssertUtil.isTrue(StringUtils.isBlank(newPassword), "请输入新密码！");
        // 新密码与原始密码不能相同
        AssertUtil.isTrue(oldPassword.equals(newPassword), "新密码不能与原始密码相同！");
        // 确认密码 ⾮空校验
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword), "请输入确认密码！");
        // 新密码要与确认密码保持⼀致
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)), "新密码与确认密码不一致！");
    }

    /**
     * 构建返回的用户信息
     * @param user  用户对象
     * @return
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        // 设置用户信息（将 userId 加密）
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 验证密码登录
     * @param userPwd   前台传递的密码
     * @param pwd       数据库中查询的密码
     */
    private void checkLoginPwd(String userPwd, String pwd) {
        // 数据库中的密码是经过加密的，将前台传递的密码先加密，再与数据库中的密码作比较
        userPwd = Md5Util.encode(userPwd);
        // 比较密码
        AssertUtil.isTrue(!userPwd.equals(pwd), "用户密码不正确！");
    }

    /**
     * 验证用户登录参数
     * @param userName  前台传递的用户名
     * @param userPwd   前台传递的用户密码
     */
    private void checkLoginParams(String userName, String userPwd) {
        // 判断姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户姓名不能为空！");
        // 判断密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "用户密码不能为空！");
    }

    /**
     * 查询所有的销售⼈员
     * @return
     */
    public List<Map<String, Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }

    /**
     * 添加用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        /* 参数校验 */
        checkUserParams(user);
        /* 设置默认值 */
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(userMapper.insertSelective(user) < 1, "添加用户失败！");

        /* 用户角色关联 */
        /**
         * 用户ID
         *  userId
         * 角色ID
         *  roleIds
         */
        relationUserRole(user.getId(), user.getRoleIds());
    }


    /**
     * 用户角色关联
     *  添加操作
     *      原始角色不存在
     *          1. 不添加新的角色记录    不操作用户角色表
     *          2. 添加新的角色记录      给指定用户绑定相关的角色记录
     *
     *  更新操作
     *      原始角色不存在
     *          1. 不添加新的角色记录     不操作用户角色表
     *          2. 添加新的角色记录       给指定用户绑定相关的角色记录
     *      原始角色存在
     *          1. 添加新的角色记录       判断已有的角色记录不添加，添加没有的角色记录
     *          2. 清空所有的角色记录     删除用户绑定角色记录
     *          3. 移除部分角色记录       删除不存在的角色记录，存在的角色记录保留
     *          4. 移除部分角色，添加新的角色    删除不存在的角色记录，存在的角色记录保留，添加新的角色
     *
     *   如何进行角色分配？？？
     *      判断用户对应的角色记录存在，先将用户原有的角色记录删除，再添加新的角色记录
     *
     *  删除操作
     *      删除指定用户绑定的角色记录
     *
     *
     * @param userId  用户ID
     * @param roleIds 角色ID
     */
    private void relationUserRole(Integer userId, String roleIds) {
        // 通过用户ID查询角色记录
        Integer count = userRoleMapper.countUserRoleByUserId(userId);
        // 判断角色记录是否存在
        if (count > 0) {
            // 如果角色记录存在，则删除该用户对应的角色记录
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "用户角色分配失败！");
        }

        // 判断角色ID是否存在，如果存在，则添加该用户对应的角色记录
        if (StringUtils.isNotBlank(roleIds)) {
            // 将用户角色数据设置到集合中，执行批量添加
            List<UserRole> userRoleList = new ArrayList<>();
            // 将角色ID字符串转换成数组
            String[] roleIdsArray = roleIds.split(",");
            // 遍历数组，得到对应的用户角色对象，并设置到集合中
            for (String roleId : roleIdsArray) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                // 设置到集合中
                userRoleList.add(userRole);
            }
            // 批量添加用户角色记录
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != userRoleList.size(), "用户角色分配失败!");
        }

    }

    /**
     * 更新用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        // 判断用户ID是否为空，且数据存在
        AssertUtil.isTrue(null == user.getId(), "待更新记录不存在！");
        // 通过id查询数据
        User temp = userMapper.selectByPrimaryKey(user.getId());
        // 判断是否存在
        AssertUtil.isTrue(null == temp, "待更新记录不存在！");
        // 参数校验
        checkUserParams(user);
        // 设置默认值
        user.setUpdateDate(new Date());

        // 执行更新操作，判断受影响行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1, "用户更新失败!");

        /* 用户角色关联 */
        /**
         * 用户ID
         *  userId
         * 角色ID
         *  roleIds
         */
        relationUserRole(user.getId(), user.getRoleIds());
    }

    /**
     * 用户信息参数校验
     * @param user
     */
    private void checkUserParams(User user) {
        // 判断用户名
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()), "用户姓名不能为空！");
        // 判断用户名的唯一性
        User temp = userMapper.queryUserByUserName(user.getUserName());
        // 如果用户对象为空，则表示用户名可用；如果用户对象不为空，则表示用户名不可用
        AssertUtil.isTrue(null != temp && (!temp.getId().equals(user.getId())), "用户名已存在，请重新输入！");

        // 邮箱
        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()), "邮箱不能为空！");
        // 校验邮箱合法性
        AssertUtil.isTrue(!EmailUtil.isMobile(user.getEmail()), "邮箱格式不正确！");
        // 手机号
        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()), "手机号不能为空！");
        // 检验手机号是否正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()), "用户号码不能为空！");
    }

    /**
     * 用户删除
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids) {
        // 判断ids是否为空，长度是否大于0
        AssertUtil.isTrue(ids == null || ids.length == 0, "待删除记录不存在！");
        // 执行删除操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.deleteBatch(ids) != ids.length, "用户删除失败！");

        // 遍历用户ID的数组
        for (Integer userId : ids) {
            // 通过用户ID查询对应的用户角色记录
            Integer count = userRoleMapper.countUserRoleByUserId(userId);
            // 判断用户角色记录是否存在
            if (count > 0) {
                //  通过用户ID删除对应的用户角色记录
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "删除用户失败！");
            }
        }
    }

    /**
     * 查询所有的客户经理
     * @return
     */
    public List<Map<String, Object>> queryAllCustomerManagers() {
        return userMapper.queryAllCustomerManagers();
    }
}
