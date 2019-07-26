package cc.wanshan.gis.service.authorize.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.authorize.RoleDao;
import cc.wanshan.gis.dao.authorize.UserDao;
import cc.wanshan.gis.entity.authorize.Role;
import cc.wanshan.gis.entity.authorize.User;
import cc.wanshan.gis.service.authorize.RoleService;
import cc.wanshan.gis.utils.base.ResultUtil;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 角色管理
 * @Author Li Cheng
 * @Date 8:59 2019/7/26
 **/
@Service(value = "roleServiceImpl")
public class RoleServiceImpl implements RoleService {

  private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

  @Resource
  private RoleDao roleDao;

  @Resource
  private UserDao userDao;

  @Override
  public Result findRoleByUsername(String username) {
    logger.info("findRoleByUsername::username = [{}]", username);
    if (StringUtils.isBlank(username)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    Role role = roleDao.findRoleByUserName(username);
    if (role != null) {
      return ResultUtil.success(role);
    }
    return ResultUtil.error(ResultCode.FIND_NULL);

  }

  @Override
  public Result findRoleByRoleId(String roleId) {
    logger.info("findRoleByRoleId::roleId = [{}]", roleId);
    if (StringUtils.isBlank(roleId)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    Role role = roleDao.findByRoleId(roleId);
    if (role != null) {
      return ResultUtil.success(role);
    }
    return ResultUtil.error(ResultCode.FIND_NULL);

  }

  @Override
  public Result findRoleByAuthorId(String authorId) {
    logger.info("findRoleByAuthorId::authorId = [{}]", authorId);
    if (StringUtils.isBlank(authorId)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    List<Role> byAuthorId = roleDao.findByAuthorId(authorId);
    if (byAuthorId != null) {
      return ResultUtil.success(byAuthorId);
    }
    return ResultUtil.error(ResultCode.FIND_NULL);
  }

  @Override
  public Result findAllRole() {
    List<Role> roles = roleDao.findAllRole();
    if (roles != null) {
      return ResultUtil.success(roles);
    } else {
      return ResultUtil.error(ResultCode.FIND_NULL);
    }
  }

  @Override
  public Result insertRole(String roleName, String roleNameZH, String describe) {
    logger.info("insertRole::roleName = [{}], roleNameZH = [{}], describe = [{}]", roleName,
        roleNameZH, describe);
    if (StringUtils.isBlank(roleName) && StringUtils.isBlank(roleNameZH) && StringUtils
        .isBlank(describe)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    int i = roleDao.insertRole(new Role(roleName, roleNameZH, new Date(), new Date(), describe));
    if (i == 1) {
      return ResultUtil.success();
    }
    return ResultUtil.error(ResultCode.SAVE_FAIL);

  }

  @Override
  public Result findRoleByRoleName(String roleName) {
    logger.info("findRoleByRoleName::roleName = [{}]", roleName);
    if (StringUtils.isBlank(roleName)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    Role role = roleDao.findRoleByRoleName(roleName);
    if (role != null) {
      return ResultUtil.success(role);
    }
    return ResultUtil.error(ResultCode.FIND_NULL);

  }

  @Override
  public Result findCountByRoleName(String roleName) {
    logger.info("findCountByRoleName::roleName = [{}]", roleName);
    if (StringUtils.isBlank(roleName)){
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    int count = roleDao.findCountByRoleName(roleName);
    if (count != 0) {
      return ResultUtil.error(1, "角色已存在");
    } else {
      return ResultUtil.success();
    }
  }

  @Override
  public Result findCountByRoleNameZH(String roleNameZH) {
    logger.info("findCountByRoleNameZH::roleNameZH = [{}]", roleNameZH);
    if (StringUtils.isBlank(roleNameZH)){
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    int count = roleDao.findCountByRoleNameZH(roleNameZH);
    if (count != 0) {
      return ResultUtil.error(1, "角色已存在");
    } else {
      return ResultUtil.success();
    }
  }

  @Override
  public Result updateRole(String roleId,String roleName,String roleNameZH,String describe) {
    logger.info("updateRole::roleId = [{}], roleName = [{}], roleNameZH = [{}], describe = [{}]",roleId, roleName, roleNameZH, describe);
    int i = roleDao.updateRole(new Role(roleId,roleName,roleNameZH,new Date(),new Date(),describe));
    if (i != 0) {
      return ResultUtil.success();
    } else {
      return ResultUtil.error(ResultCode.UPDATE_FAIL);
    }
  }

  @Override
  public Result deleteRole(String roleId) {
    logger.info("deleteRole::roleId = [{}]", roleId);
    if (StringUtils.isBlank(roleId)){
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    List<User> users = userDao.findUsersByRoleId(roleId);
    if (users.size() > 0) {
      return ResultUtil.error(1, "当前角色存在关联用户，不能被删除！");
    } else {
      int i = roleDao.deleteRole(roleId);
      if (i == 1) {
        return ResultUtil.success();
      } else {
        return ResultUtil.error(ResultCode.DELETE_FAIL);
      }
    }
  }
}
