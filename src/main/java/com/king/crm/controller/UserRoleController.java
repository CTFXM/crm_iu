package com.king.crm.controller;

import com.king.crm.base.BaseController;
import com.king.crm.dao.UserRoleMapper;
import com.king.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author KING
 * @version 1.0
 * @date 2023/6/17
 */
@Controller
public class UserRoleController extends BaseController {

    @Resource
    private UserRoleService userRoleService;
}
