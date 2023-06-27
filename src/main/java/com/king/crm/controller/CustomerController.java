package com.king.crm.controller;

import com.king.crm.base.BaseController;
import com.king.crm.base.ResultInfo;
import com.king.crm.query.CustomerQuery;
import com.king.crm.service.CustomerService;
import com.king.crm.utils.PhoneUtil;
import com.king.crm.vo.Customer;
import com.king.crm.vo.CustomerServe;
import com.king.crm.vo.Module;
import com.king.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.StringReader;
import java.util.Map;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/20
 */
@RequestMapping("customer")
@Controller
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;

    @RequestMapping("index")
    public String index() {
        return "customer/customer";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery) {
        return customerService.queryCustomerByParams(customerQuery);
    }

    /**
     * 添加客户
     * @param customer
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer) {
        customerService.addCustomer(customer);
        return success("添加成功！");
    }

    /**
     * 修改客户
     * @param customer
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer) {
        customerService.updateCustomer(customer);
        return success("修改成功！");
    }

    /**
     * 打开添加/修改客户的对话框
     * @return
     */
    @RequestMapping("toAddOrUpdateCustomerPage")
    public String toAddOrUpdateCustomerPage(Integer id, HttpServletRequest request) {
        // 判断Id是否为空
       if (id != null) {
            // 通过ID查询营销机会数据
            Customer customer = customerService.selectByPrimaryKey(id);
            // 将数据设置到请求域中
            request.setAttribute("customer", customer);
       }
        return "customer/add_update";
    }

    /**
     * 删除客户
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCustomer(Integer id) {
        customerService.deleteCustomer(id);
        return success("删除成功！");
    }

    /**
     * 打开客户的订单页面
     * @return
     */
    @RequestMapping("toCustomerOrderPage")
    public String toCustomerOrderPage(Integer customerId, Model model) {
        // 通过客户ID查询客户记录，设置到请求域中
        model.addAttribute("customer", customerService.selectByPrimaryKey(customerId));
        return "customer/customer_order";
    }

    /**
     * 客户贡献分析
     * @param customerQuery
     * @return
     */
    @RequestMapping("queryCustomerContributionByParams")
    @ResponseBody
    public Map<String ,Object> queryCustomerContributionByParams(CustomerQuery customerQuery) {
        return customerService.queryCustomerContributionByParams(customerQuery);
    }

    /**
     * 查询客户构成 折线图
     * @return
     */
    @RequestMapping("countCustomerMake")
    @ResponseBody
    public Map<String, Object> countCustomerMake() {
        return customerService.countCustomerMake();
    }

    /**
     * 查询客户构成 饼状图
     * @return
     */
    @RequestMapping("countCustomerMake02")
    @ResponseBody
    public Map<String, Object> countCustomerMake02() {
        return customerService.countCustomerMake02();
    }
}
