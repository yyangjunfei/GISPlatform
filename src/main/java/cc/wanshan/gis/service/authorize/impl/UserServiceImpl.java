package cc.wanshan.gis.service.authorize.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.authorize.RoleDao;
import cc.wanshan.gis.dao.authorize.UserDao;
import cc.wanshan.gis.entity.authorize.User;
import cc.wanshan.gis.entity.authorize.UserDetailsImpl;
import cc.wanshan.gis.service.authorize.UserService;
import cc.wanshan.gis.utils.base.ResultUtil;
import java.util.Collection;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserDetailsService, UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
  @Resource
  private UserDao userDao;
  @Resource
  private RoleDao roleDao;

  @Override
  public Result findByUsername(String username) {
    logger.info("findUserByUsername::username = [{}]", username);
    if (StringUtils.isBlank(username)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    User user = userDao.findByUsername(username);
    if (user != null) {
      return ResultUtil.success(user);
    }
    return ResultUtil.error(ResultCode.FIND_NULL);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.info("loadUserByUsername::s = [{}]", username);
    User user = userDao.findByUsername(username);
    return new UserDetailsImpl(user, roleDao.findRoleByUserName(username));
  }

  @Override
  @Transactional
  public Result insert(String username, String password, String roleId, String security,
      String phoneNum, String email, String department, Integer status,
      Integer delete, String enabled) {
   logger.info("insert::username = [{}], password = [{}], roleId = [{}], security = [{}], phoneNum = [{}], email = [{}], department = [{}], status = [{}], delete = [{}], enabled = [{}]",username, password, roleId, security, phoneNum, email, department, status, delete, enabled);
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    if (findCountByUsername(username).getCode().equals(ResultCode.USER_HAS_EXISTED.code())) {
      return ResultUtil.error(ResultCode.USER_HAS_EXISTED);
    }
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encode = encoder.encode(password);
    logger.info("password：" + encode);
    int i = userDao
        .insert(username, encode, roleId, security, new Date(), new Date(), phoneNum, email,
            department, status, delete, enabled);
    if (i > 0) {
      return ResultUtil.success();
    } else {
      return ResultUtil.error(ResultCode.SAVE_FAIL);
    }
  }

  @Override
  @ResponseBody
  public Result findAll() {
    List<User> user = userDao.findAllByRoleAsc();
    if (user != null) {
      return ResultUtil.success(user);
    } else {
      return ResultUtil.error(ResultCode.FIND_NULL);
    }
  }

  @Override
  public Result findCountByUsername(String username) {
    logger.info("findUserCountByUsername::username = [{}]", username);
    if (StringUtils.isBlank(username)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    int count = userDao.findCountByUsername(username);
    if (count != 0) {
      return ResultUtil.error(ResultCode.USER_HAS_EXISTED);
    } else {
      return ResultUtil.success();
    }
  }

  @Override
  public Result findByUserId(String userId) {
    logger.info("findUserByUserId::userId = [{}]", userId);
    if (StringUtils.isBlank(userId)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    User user = userDao.findByUserId(userId);
    if (user != null) {
      return ResultUtil.success(user);
    } else {
      return ResultUtil.error(ResultCode.FIND_NULL);
    }
  }

  @Override
  public Result update(String userId, String roleId, String security, Integer phoneNum,
      String email, String department) {
    logger.info(
        "update::userId = [{}], roleId = [{}], security = [{}], updateTime = [{}], phoneNum = [{}], email = [{}], department = [{}]",
        userId, roleId, security, phoneNum, email, department);
    if (StringUtils.isBlank(userId) || StringUtils.isBlank(roleId)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    int i = userDao.update(userId, roleId, security, new Date(), phoneNum, email, department);
    if (i != 0) {
      return ResultUtil.success();
    } else {
      return ResultUtil.error(ResultCode.UPDATE_FAIL);
    }
  }

  @Override
  public Result updatePassword(String username, String oldPassword, String newPassword) {

    logger.info("updatePassword::userId = [{}], password = [{}], newPassword = [{}]",username, oldPassword, newPassword);
    if (StringUtils.isBlank(username) || StringUtils.isBlank(oldPassword)||StringUtils.isBlank(newPassword)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    User user = userDao.findByUsername(username);
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    boolean matches = encoder.matches(oldPassword, user.getPassword());
    String encode = encoder.encode(newPassword);
    if (!matches){
      return ResultUtil.error(ResultCode.USER_PASSWORD_ERROR);
    }
    int i = userDao.updatePassword(user.getUserId(), encode, new Date());
    if (i == 1) {
      return ResultUtil.success();
    } else {
      return ResultUtil.error(ResultCode.UPDATE_FAIL);
    }
  }

  @Override
  public Result delete(String userId) {

    logger.info("deleteUser::userId = [{}]", userId);
    if (StringUtils.isBlank(userId)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    int i = userDao.delete(userId);
    if (i == 1) {
      return ResultUtil.success();
    } else {
      return ResultUtil.error(ResultCode.DELETE_FAIL);
    }
  }

  @Override
  public Result updateStatus(String userId, Integer status) {
    logger.info("updateStatus::userId = [{}], status = [{}]",userId, status);
    int i = userDao.updateStatus(userId, status, new Date());
    if (i == 1) {
      return ResultUtil.success();
    } else {
      return ResultUtil.error(1, "修改失败");
    }
  }

  @Override
  public Result findCurrent(Authentication authentication) {
    logger.info("findCurrent::authentication = [{}]",authentication);
    if (authentication==null){
      return ResultUtil.error(ResultCode.USER_NOT_LOGGED_IN);
    }
    String username = (String) authentication.getPrincipal();
    if (StringUtils.isBlank(username)){
      return ResultUtil.error(ResultCode.USER_NOT_LOGGED_IN);
    }
    User user = userDao.findByUsername(username);
    HashMap<String, String> map = new HashMap<>();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    for (GrantedAuthority authority : authorities) {
      String authority1 = authority.getAuthority();
      map.put("role", authority1);
    }
    map.put("username", username);
    map.put("userId", user.getUserId());
    return ResultUtil.success(map);
  }

}
