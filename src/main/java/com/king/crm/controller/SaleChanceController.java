package com.king.crm.controller;

import com.king.crm.annoation.RequiredPermission;
import com.king.crm.base.BaseController;
import com.king.crm.base.ResultInfo;
import com.king.crm.enums.StateStatus;
import com.king.crm.query.SaleChanceQuery;
import com.king.crm.service.SaleChanceService;
import com.king.crm.utils.CookieUtil;
import com.king.crm.utils.LoginUserUtil;
import com.king.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/14
 */
@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 多条件分⻚查询营销机会 101001
     * @param query
     * @return
     */
    @RequiredPermission(code = "101001")
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query, Integer flag, HttpServletRequest request) {
        // 查询参数 flag=1 代表当前查询为开发计划数据，设置查询分配⼈参数
        if (null != flag && flag == 1) {
            // 查询客户开发计划
            // 设置分配状态
            query.setState(StateStatus.STATED.getType());
            // 获取当前登录用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            query.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(query);
    }

    /**
     * 进⼊营销机会⻚⾯ 1010
     * @return
     */
    @RequiredPermission(code = "1010")
    @RequestMapping("index")
    public String index () {
        return "saleChance/sale_chance";
    }

    /**
     * 添加营销机会 101002
     * @param saleChance
     * @param request
     * @return
     */
    @RequiredPermission(code = "101002")
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request) {
        // 从Cookie中获取当前登录的用户名
        String userName = CookieUtil.getCookieValue(request, "userName");
        // 设置用户名到营销机会对象
        saleChance.setCreateMan(userName);
        // 调用Service层的添加方法
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功！");
    }

    /**
     *
     * @return
     */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId, HttpServletRequest request) {
        // 判断saleChanceId是否为空
        if (saleChanceId != null) {
            // 通过ID查询营销机会数据
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            // 将数据设置到请求域中
            request.setAttribute("saleChance", saleChance);
        }

        return "saleChance/add_update";
    }


    /**
     * 更新营销机会   101004
     * @param saleChance
     * @return
     */
    @RequiredPermission(code = "101004")
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance) {
        // 调用Service层的添加方法
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功！");
    }

    /**
     * 删除营销机会   101003
     * @param ids
     * @return
     */
    @RequiredPermission(code = "101003")
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids) {
        // 调用Service层的删除方法
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功！");
    }

    /**
     * 更新营销机会的开发状态
     * @param id
     * @param devResult
     * @return
     */
    @PostMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult) {

        saleChanceService.updateSaleChanceDevResult(id, devResult);
        return success("开发状态更新成功！");
    }
}
