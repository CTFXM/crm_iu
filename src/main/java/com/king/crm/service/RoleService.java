package com.king.crm.service;

import com.king.crm.base.BaseService;
import com.king.crm.dao.ModuleMapper;
import com.king.crm.dao.PermissionMapper;
import com.king.crm.dao.RoleMapper;
import com.king.crm.dao.UserRoleMapper;
import com.king.crm.utils.AssertUtil;
import com.king.crm.vo.Permission;
import com.king.crm.vo.Role;
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
 * @date 2023/6/17
 */
@Service
public class RoleService extends BaseService<Role, Integer> {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有的角色列表
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId) {

        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空！");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp, "角色名称已存在，请重新输入！");

        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());

        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1, "角色添加失败！");

    }

    /**
     * 修改角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        // 角色ID （非空且存在）
        AssertUtil.isTrue(null == role.getId(),"待更新记录不存在！");
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(null == temp, "待更新记录不存在！");
        // 用户名称 （非空且不存在）
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空！");
        temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp && (!temp.getId().equals(role.getId())),"角色名称已存在，不可使用！");

        // 设置默认值
        role.setUpdateDate(new Date());

        // 执行更新
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "修改角色失败！");


    }

    /**
     * 删除角色
     * @param roleId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId) {
        // 判断角色ID是否为空
        AssertUtil.isTrue(null == roleId, "待删除记录不存在！");
        // 通过角色ID查询
        Role role = roleMapper.selectByPrimaryKey(roleId);
        // 判断角色记录是否存在
        AssertUtil.isTrue(null == role, "待删除记录不存在！");

        // 设置删除状态
        role.setIsValid(0);
        role.setUpdateDate(new Date());

        // 执行更新操作
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, " 角色删除失败！");
    }

    /**
     * 角色授权
     *  将对应的角色ID与资源ID，添加到对应的权限表中
     *      直接添加权限：不合适，会出现重复的权限数据（执行修改权限操作后删除权限操作时）
     *      推荐使用：
     *          先将已有的权限记录删除，再将需要设置的权限记录添加
     *          1.通过角色ID查询对应的权限记录
     *          2.如果权限记录存在，则删除对应的角色拥有的权限记录
     *          3.如果有权限记录，则添加权限记录 (批量添加）
     * @param roleId
     * @param mIds
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mIds) {
        // 1. 通过角色ID查询对应的权限记录
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        // 2.如果权限记录存在，则删除对应的角色拥有的权限记录
        if (count > 0) {
            // 删除权限记录
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        // 3.如果有权限记录，则添加权限记录
        if (mIds != null && mIds.length > 0) {
            // 定义Permission集合
            List<Permission> permissionList = new ArrayList<>();
            // 遍历资源ID数组
            for (Integer mId : mIds) {
                Permission permission = new Permission();
                permission.setModuleId(mId);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                // 将对象设置到集合中
                permissionList.add(permission);
            }

            // 执行批量添加操作，判断受影响的行数
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionList) != permissionList.size(),"角色授权失败！");
        }
    }
}
