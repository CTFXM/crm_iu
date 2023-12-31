package com.king.crm.controller;

import com.king.crm.base.BaseController;
import com.king.crm.service.PermissionService;
import com.king.crm.service.UserService;
import com.king.crm.utils.LoginUserUtil;
import com.king.crm.vo.User;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/13
 */
@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;

    /**
     * 系统登录⻚
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    // 系统界⾯欢迎⻚
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
    /**
     * 后端管理主⻚⾯
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        // 通过⼯具类，从cookie中获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调⽤对应Service层的⽅法，通过userId主键查询⽤户对象
        User user = (User) userService.selectByPrimaryKey(userId);
        // 将⽤户对象设置到request作⽤域中
        request.getSession().setAttribute("user", user);

        // 通过当前登录用户ID查询当前登录用户拥有的资源列表（查询对应资源的授权码）
        List<String> permissions = permissionService.queryUserHasRoleHasPermissionByUserId(userId);
        // 将集合设置到session作用域中
        request.getSession().setAttribute("permissions",permissions);

        return "main";
    }

}
