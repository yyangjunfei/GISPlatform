package cc.wanshan.gis.controller.authorize;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.authorize.Role;
import cc.wanshan.gis.entity.authorize.User;
import cc.wanshan.gis.entity.layer.thematic.Thematic;
import cc.wanshan.gis.entity.layer.thematic.ThematicUser;
import cc.wanshan.gis.entity.plot.of2d.Store;
import cc.wanshan.gis.service.authorize.UserService;
import cc.wanshan.gis.service.layer.geoserver.GeoServerService;
import cc.wanshan.gis.service.layer.geoserver.StoreService;
import cc.wanshan.gis.service.layer.thematic.ThematicUserService;
import cc.wanshan.gis.utils.base.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.manager.StatusManagerServlet;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 * @Author Li Cheng
 * @Date 14:27 2019/6/18
 **/
@Api(value = "UserController", tags = "用户管理模块")
@RestController
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/user")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);
  @Resource(name = "userServiceImpl")
  private UserService userServiceImpl;
  @Resource(name = "geoServerServiceImpl")
  private GeoServerService geoServerService;
  @Resource(name = "thematicUserServiceImpl")
  private ThematicUserService thematicUserServiceImpl;
  @Resource(name = "storeServiceImpl")
  private StoreService storeServiceImpl;

  @ApiOperation(value = "查询所有用户")
  @GetMapping("/find/all")
  @ResponseBody
  public Result findAll() {
    return userServiceImpl.findAll();
  }

  @ApiOperation(value = "插入一个用户")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "username", value = "用户名", required = true),
      @ApiImplicitParam(name = "password", value = "密码", required = true),
      @ApiImplicitParam(name = "roleId", value = "角色Id", required = true),
      @ApiImplicitParam(name = "security", value = "保密级别", required = true),
      @ApiImplicitParam(name = "phoneNum", value = "手机号"),
      @ApiImplicitParam(name = "email", value = "邮箱"),
      @ApiImplicitParam(name = "department", value = "部门")
  })
  @PostMapping("/insert")
  @ResponseBody
  public Result insertUser(@RequestParam(value = "username") String username,
      @RequestParam(value = "password") String password,
      @RequestParam(value = "roleId") String roleId,
      @RequestParam(value = "security") String security,
      @RequestParam(value = "phoneNum", required = false) String phoneNum,
      @RequestParam(value = "email", required = false) String email,
      @RequestParam(value = "department", required = false) String department) {
    logger.info(
        "insertUser::username = [{}], password = [{}], roleId = [{}], security = [{}], phoneNum = [{}], email = [{}], department = [{}], status = [{}], delete = [{}], enabled = [{}]",
        username, password, roleId, security, phoneNum, email, department);
    Store store = new Store();
    String[] thematicId = {"52ffd62e7c7311e9a07b20040ff72212",
        "4194542c7e0411e9b9dc20040ff72212"};
    ThematicUser thematicUser = new ThematicUser();
    Thematic thematic = new Thematic();
    Result insert = userServiceImpl
        .insert(username, password, roleId, security, phoneNum, email, department, 1, 0, "Y");
    if (insert.getCode().equals(ResultCode.SUCCESS.code())) {
      Result byUsername = userServiceImpl.findByUsername(username);
      User user = (User) byUsername.getData();
      thematic.setThematicId("72f0f0747bad11e9ac6420040ff72212");
      for (String s : thematicId) {
        thematicUser.setThematicId(s);
        thematicUser.setUserId(user.getUserId());
        thematicUser.setInsertTime(new Date());
        thematicUser.setUpdateTime(new Date());
        Boolean aBoolean = thematicUserServiceImpl.insertThematicUser(thematicUser);
        logger.info("thematicUserServiceImpl.insertThematicUser::" + aBoolean);
        if (aBoolean) {
          store.setStoreName("newStore");
          store.setUser(user);
          store.setInsertTime(new Date());
          store.setUpdateTime(new Date());
          Boolean insertStore = storeServiceImpl.insertStore(store);
          logger.info("insertStore::" + insertStore);
          if (insertStore) {
            return ResultUtil.success();
          }
        }
      }
    }
    return insert;
  }

  @ApiOperation(value = "根据用户名判断用户是否存在")
  @ApiImplicitParam(name = "username", value = "用户名", required = true)
  @GetMapping("/find/count/username")
  @ResponseBody
  public Result findCountByUsername(@RequestParam(value = "username") String username) {
    logger.info("findCountByUsername::username = [{}]", username);
    return userServiceImpl.findCountByUsername(username);
  }

  @ApiOperation(value = "根据用户名查找用户信息")
  @ApiImplicitParam(name = "username", value = "用户名", required = true)
  @GetMapping("/find/username")
  @ResponseBody
  public Result findByUsername(@RequestParam(value = "username") String username) {
    logger.info("findByUsername::username = [{}]", username);
    return userServiceImpl.findByUsername(username);
  }

  @ApiOperation(value = "根据用户Id查询用户信息")
  @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
  @PostMapping("/find/id")
  @ResponseBody
  public Result findByUserId(@RequestParam(value = "userId") String userId) {
    logger.info("findByUserId::userId = [{}]", userId);
    return userServiceImpl.findByUserId(userId);
  }

  @ApiOperation(value = "根据userId更新用户信息")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户Id",required = true),
      @ApiImplicitParam(name = "roleId", value = "角色Id",required = true),
      @ApiImplicitParam(name = "security", value = "安全级别",required = true),
      @ApiImplicitParam(name = "phoneNum", value = "手机号"),
      @ApiImplicitParam(name = "email", value = "邮箱"),
      @ApiImplicitParam(name = "department", value = "部门"),
  })
  @PutMapping("/update")
  @ResponseBody
  public Result updateUser(@RequestParam(value = "userId") String userId,
      @RequestParam(value = "roleId") String roleId,
      @RequestParam(value = "security") String security,
      @RequestParam(value = "phoneNum", required = false) Integer phoneNum,
      @RequestParam(value = "email", required = false) String email,
      @RequestParam(value = "department", required = false) String department) {
    logger.info(
        "updateUser::userId = [{}], roleId = [{}], security = [{}], phoneNum = [{}], email = [{}], department = [{}]",
        userId, roleId, security, phoneNum, email, department);
    return userServiceImpl.update(userId, roleId, security, phoneNum, email, department);
  }

  @ApiOperation(value = "修改用户激活状态")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户Id",required = true),
      @ApiImplicitParam(name = "status", value = "激活状态",required = true)
  })
  @PutMapping("/update/status")
  @ResponseBody
  public Result updateUserStatus(@RequestParam(value = "userId") String userId,
      @RequestParam(value = "status") Integer status) {
    logger.info("updateUserStatus::userId = [{}], status = [{}]", userId, status);
    return userServiceImpl.updateStatus(userId, status);
  }

  @ApiOperation(value = "修改密码")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "username",value = "用户名",required = true),
      @ApiImplicitParam(name = "oldPassword",value = "旧密码",required = true),
      @ApiImplicitParam(name = "newPassword",value = "新密码",required = true)
  })
  @PutMapping("/update/password")
  @ResponseBody
  public Result updatePassword(@RequestParam(value = "username") String username,
      @RequestParam(value = "oldPassword") String oldPassword,
      @RequestParam(value = "newPassword") String newPassword) {
    logger.info("updatePassword::username = [{}], oldPassword = [{}], newPassword = [{}]", username,
        oldPassword, newPassword);
    return userServiceImpl.updatePassword(username, oldPassword, newPassword);
  }

  @ApiOperation(value = "删除用户")
  @ApiImplicitParam(name = "userId",value = "用户Id",required = true)
  @DeleteMapping("/delete")
  @ResponseBody
  public Result deleteUser(@RequestParam(value = "userId") String userId) {
    logger.info("deleteUser::userId = [{}]", userId);
    return userServiceImpl.delete(userId);
  }
  @ApiOperation(value = "查找当前用户信息")
  @GetMapping(value = "/finduser")
  @ResponseBody
  public Result findUser(Authentication authentication) {
    logger.info("findUser::authentication = [{}]",authentication);
    return userServiceImpl.findCurrent(authentication);
  }
}
