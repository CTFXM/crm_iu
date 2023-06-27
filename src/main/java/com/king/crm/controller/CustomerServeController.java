package com.king.crm.controller;

import com.king.crm.base.BaseController;
import com.king.crm.base.ResultInfo;
import com.king.crm.query.CustomerServeQuery;
import com.king.crm.service.CustomerServeService;
import com.king.crm.utils.LoginUserUtil;
import com.king.crm.vo.CustomerServe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/22
 */
@RequestMapping("customer_serve")
@Controller
public class CustomerServeController extends BaseController {

    @Resource
    private CustomerServeService customerServeService;

    /**
     * 服务数据的列表
     * @param customerServeQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String ,Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery, Integer flag, HttpServletRequest request) {
        // 判断是否执行服务处理，如果是则查询分配给当前登录用户的服务记录
        if (flag != null && flag == 1) {
            // 设置查询条件：分配人
            customerServeQuery.setAssigner(LoginUserUtil.releaseUserIdFromCookie(request));
        }
        return customerServeService.queryCustomerServeByParams(customerServeQuery);
    }

    /**
     * 通过不同的类型进入不同的服务页面
     * @return
     */
    @RequestMapping("index/{type}")
    public String index(@PathVariable Integer type) {
        // 判断类型是否为空
        if (type != null) {
            if (type == 1) {

                // 服务创建
                return "customerServe/customer_serve";

            } else if (type == 2) {

                // 服务分配
                return "customerServe/customer_serve_assign";

            } else if (type == 3) {

                // 服务处理
                return "customerServe/customer_serve_proce";

            } else if (type == 4) {

                // 服务反馈
                return "customerServe/customer_serve_feed_back";

            } else if (type == 5) {

                //服务归档
                return "customerServe/customer_serve_archive";

            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    @RequestMapping("toAddCustomerServePage")
    public String toAddCustomerServePage() {
        return "/customerServe/customer_serve_add";
    }

    /**
     * 添加
     * @param customerServe
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCustomerServe(CustomerServe customerServe) {
        customerServeService.addCustomerServe(customerServe);
        return success("添加成功！");
    }

    /**
     * 更新
     * @param customerServe
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCustomerServe(CustomerServe customerServe) {
        customerServeService.updateCustomerServe(customerServe);
        return success("服务更新成功！");
    }

    /**
     * 打开服务分配页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("toCustomerServeAssignPage")
    public String toCustomerServeAssignPage(Integer id, Model model) {
        // 通过id查询服务记录，并设置到请求域中
        model.addAttribute("customerServe", customerServeService.selectByPrimaryKey(id));
        return "customerServe/customer_serve_assign_add";
    }

    /**
     * 打开服务处理页面
     * @return
     */
    @RequestMapping("toCustomerServeProcePage")
    public String toCustomerServeProcePage(Integer id,Model model) {
        // 通过id查询服务记录，并设置到请求域中
        model.addAttribute("customerServe", customerServeService.selectByPrimaryKey(id));
        return "customerServe/customer_serve_proce_add";

    }

    /**
     * 服务反馈页面
     * @param id
     * @return
     */
    @RequestMapping("toCustomerServeBackPage")
    public String toCustomerServeBackPage(Integer id,Model model) {
        // 通过id查询服务记录，并设置到请求域中
        model.addAttribute("customerServe", customerServeService.selectByPrimaryKey(id));
        return "customerServe/customer_serve_feed_back_add";
    }
}
