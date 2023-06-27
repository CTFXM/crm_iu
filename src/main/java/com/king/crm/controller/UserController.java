package com.king.crm.controller;

import com.king.crm.base.BaseController;
import com.king.crm.base.ResultInfo;
import com.king.crm.exceptions.ParamsException;
import com.king.crm.model.UserModel;
import com.king.crm.query.UserQuery;
import com.king.crm.service.UserService;
import com.king.crm.utils.CookieUtil;
import com.king.crm.utils.LoginUserUtil;
import com.king.crm.vo.SaleChance;
import com.king.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/14
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;


    /**
     * 用户登录
     * @param userName  //前台传入的用户名
     * @param userPwd   //前台传入的用户密码
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        // 调⽤Service层的登录⽅法，得到返回的⽤户对象
        UserModel userModel = userService.userLogin(userName, userPwd);
        /**
         * 登录成功后，有两种处理：
         *  1. 将⽤户的登录信息存⼊ Session （ 问题：重启服务器，Session 失效，客户端需要重复登录 ）
         *  2. 将⽤户信息返回给客户端，由客户端（Cookie）保存
         */
        // 将返回的UserModel对象设置到 ResultInfo 对象中
        resultInfo.setResult(userModel);
        /*// 通过 try catch 捕获 Service 层抛出的异常
        try {
            // 调⽤Service层的登录⽅法，得到返回的⽤户对象
            UserModel userModel = userService.userLogin(userName, userPwd);
            *//**
             * 登录成功后，有两种处理：
             *  1. 将⽤户的登录信息存⼊ Session （ 问题：重启服务器，Session 失效，客户端需要重复登录 ）
             *  2. 将⽤户信息返回给客户端，由客户端（Cookie）保存
             *//*
            // 将返回的UserModel对象设置到 ResultInfo 对象中
            resultInfo.setResult(userModel);
        } catch (ParamsException e) {   // ⾃定义异常
            e.printStackTrace();
            // 设置状态码和提示信息
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败！");
        }*/
        return resultInfo;
    }


    /**
     * 用户密码
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @PostMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPassword, String newPassword, String confirmPassword) {
        ResultInfo resultInfo = new ResultInfo();
        // 获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调用Service层的密码修改⽅法
        userService.updateUserPassword(userId, oldPassword, newPassword, confirmPassword);
        /*// 通过 try catch 捕获 Service 层抛出的异常
        try {
            // 获取userId
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            // 调用Service层的密码修改⽅法
            userService.updateUserPassword(userId, oldPassword, newPassword, confirmPassword);
        } catch (ParamsException e) {   // 自定义异常
            e.printStackTrace();
            // 设置状态码和提示信息
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败！");
        }*/
        return resultInfo;
    }

    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }

    /**
     * 查询所有的销售⼈员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }


    /**
     * 多页多条件查询用户列表
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryUserByParams(UserQuery userQuery) {
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 进入用户列表页面
     */
    @RequestMapping("index")
    public String index() {
        return "user/user";
    }


    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user) {
        ResultInfo resultInfo = new ResultInfo();
        // 调⽤Service层的登录⽅法，得到返回的⽤户对象
        userService.addUser(user);
        return success("用户添加成功！");
    }

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        ResultInfo resultInfo = new ResultInfo();
        // 调⽤Service层的登录⽅法，得到返回的⽤户对象
        userService.updateUser(user);
        return success("用户更新成功！");
    }

    @RequestMapping("toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id, HttpServletRequest request) {

        // 判断id是否为空，不为空表示更新操作，查询用户对象
        if (id != null) {
            // 通过id查询用户对象
            User user = userService.selectByPrimaryKey(id);
            // 将数据设置到请求域中
            request.setAttribute("userInfo", user);
        }
        return "user/add_update";
    }

    /**
     * 用户删除
     * @param ids
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids) {

        userService.deleteByIds(ids);

        return success("用户删除成功！");
    }

    /**
     * 查询所有的客户经理
     * @return
     */
    @RequestMapping("queryAllCustomerManagers")
    @ResponseBody
    public List<Map<String, Object>> queryAllCustomerManagers() {
        return userService.queryAllCustomerManagers();
    }

}
