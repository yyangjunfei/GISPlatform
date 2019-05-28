package cc.wanshan.gis.service.role.impl;


import cc.wanshan.gis.dao.RoleDao;
import cc.wanshan.gis.dao.UserDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.usermanagement.Role;
import cc.wanshan.gis.entity.usermanagement.User;
import cc.wanshan.gis.service.role.RoleService;
import cc.wanshan.gis.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service(value = "roleServiceImpl")
public class RoleServiceImpl implements RoleService {
    private static final Logger logger= LoggerFactory.getLogger(RoleServiceImpl.class);
    @Resource
    private RoleDao roleDao;
    @Resource
    private UserDao userDao;
    @Override
    public Role findRoleByUsername(String username) {
        logger.info("findRoleByUsername::username = [{}]",username);
        return roleDao.findRoleByUserName(username);
    }
    @Override
    public Result findRoleByRoleId(String roleId) {
        logger.info("findRoleByRoleId::roleId = [{}]",roleId);
        Role role = roleDao.findByRoleId(roleId);
        if (role!=null){
            return ResultUtil.success(role);
        }else {
            return ResultUtil.error(1,"查询失败");
        }
    }

    @Override
    public List<Role> findRoleByAuthorId(String authorId) {
        logger.info("findRoleByAuthorId::authorId = [{}]",authorId);
         return roleDao.findByAuthorId(authorId);
    }

    @Override
    public Result findAllRole() {
        List<Role> roles = roleDao.findAllRole();
        if (roles!=null){
            return ResultUtil.success(roles);
        }else {
            return null;
        }
    }

    @Override
    public Result insertRole(Role role) {
        logger.info("insertRole::role = [{}]",role);
        int i = roleDao.insertRole(role);
        if (i==1){
            return ResultUtil.success();
        }else {
            return ResultUtil.error(1,"插入失败");
        }
    }
    @Override
    public Result findRoleByRoleName(String roleName) {
        logger.info("findRoleByRoleName::roleName = [{}]",roleName);
        Role role = roleDao.findRoleByRoleName(roleName);
        if (role!=null){
            return ResultUtil.success(role);
        }else {
            return ResultUtil.error(1,"当前角色不存在");
        }
    }

    @Override
    public Result findCountByRoleName(String roleName) {
        logger.info("findCountByRoleName::roleName = [{}]",roleName);
        int count = roleDao.findCountByRoleName(roleName);
        if (count!=0){
            return ResultUtil.error(1,"角色已存在");
        }else {
            return ResultUtil.success();
        }
    }
    @Override
    public Result findCountByRoleNameZH(String roleNameZH) {
        logger.info("findCountByRoleNameZH::roleNameZH = [{}]",roleNameZH);
        int count = roleDao.findCountByRoleNameZH(roleNameZH);
        if (count!=0){
            return ResultUtil.error(1,"角色已存在");
        }else {
            return ResultUtil.success();
        }
    }
    @Override
    public Result updateRole(Role role) {
        logger.info("updateRole::role = [{}]",role);
        int i = roleDao.updateRole(role);
        if (i!=0){
            return ResultUtil.success();
        }else {
            return ResultUtil.error(1,"修改失败");
        }
    }
    @Override
    public Result deleteRole(String roleId) {
        logger.info("deleteRole::roleId = [{}]",roleId);
        List<User> users = userDao.findUsersByRoleId(roleId);
        if (users.size()>0){
            return ResultUtil.error(1,"当前角色存在关联用户，不能被删除！");
        }else {
            int i = roleDao.deleteRole(roleId);
            if (i==1){
                return ResultUtil.success();
            }else {
                return ResultUtil.error(1,"删除失败");
            }
        }
    }
}
