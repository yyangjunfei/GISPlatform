package cc.wanshan.gis.controller.user;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Store;
import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.entity.thematic.ThematicUser;
import cc.wanshan.gis.entity.usermanagement.Role;
import cc.wanshan.gis.entity.usermanagement.User;
import cc.wanshan.gis.service.geoserver.GeoServerService;
import cc.wanshan.gis.service.store.StoreService;
import cc.wanshan.gis.service.thematicuser.ThematicUserService;
import cc.wanshan.gis.service.user.UserService;
import cc.wanshan.gis.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.util.Date;

@Controller
@MapperScan("cc.wanshan.demo.entity")
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource(name = "userServiceImpl")
    private UserService userServiceImpl;
    @Resource(name = "geoServerServiceImpl")
    private GeoServerService geoserverService;
    @Resource(name = "thematicUserServiceImpl")
    private ThematicUserService thematicUserServiceImpl;
    @Resource(name = "storeServiceImpl")
    private StoreService storeServiceImpl;

    @RequestMapping("/findalluser")
    @ResponseBody
    public Result findAllUser() {
        Result user = userServiceImpl.findAllUser();
        if (user.getCode() == 0) {
            return ResultUtil.success(user.getData());
        } else {
            return ResultUtil.error(1, user.getMsg());
        }
    }

    @RequestMapping("/insertuser")
    @ResponseBody
    public Result insertUser(@RequestBody JSONObject jsonObject) {
        logger.info("insertUser::jsonObject = [{}]", jsonObject);
        User user = new User();
        if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("username"))
                && StringUtils.isNotBlank(jsonObject.getString("password"))) {
            Role role = new Role();
            Store store = new Store();
            String[] thematicId = {"52ffd62e7c7311e9a07b20040ff72212",
                    "4194542c7e0411e9b9dc20040ff72212"};
            ThematicUser thematicUser = new ThematicUser();
            Thematic thematic = new Thematic();
            user.setUsername(jsonObject.getString("username"));
            user.setPassword(jsonObject.getString("password"));
            user.setStatus(0);
            user.setDelete(0);
            user.setUpdateTime(new Date());
            user.setInsertTime(new Date());
            Result i = userServiceImpl.insertUser(user);
            logger.info("userServiceImpl.insertUser::" + i);
            if (i.getCode() == 0) {
                thematic.setThematicId("72f0f0747bad11e9ac6420040ff72212");
                role.setRoleId("2d8aa47c7c2911e9a6f820040ff72212");
                user.setSecurity("秘密");
                user.setStatus(1);
                user.setRole(role);
                user.setThematic(thematic);
                user.setUpdateTime(new Date());
                Result updateUserStatus = userServiceImpl.updateUserStatus(user);
                logger.info("userServiceImpl.updateUserStatus::" + updateUserStatus);
                if (updateUserStatus.getCode() == 0) {
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
            }
            return ResultUtil.error(1, "新增失败");
        } else {
            logger.warn("json为null");
            return ResultUtil.error(1, "json为null");
        }
    }

    @RequestMapping("/findusercountbyusername")
    @ResponseBody
    public Result findUserCountByUsername(@RequestBody JSONObject jsonObject) {
        logger.info("findUserCountByUsername::username = [{}]", jsonObject);
        if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("username"))) {
            Result result = userServiceImpl.findUserCountByUsername(jsonObject.getString("username"));
            if (result.getCode() == 0) {
                return ResultUtil.success();
            } else {
                return ResultUtil.error(1, result.getMsg());
            }
        } else {
            logger.warn("用户名为null");
            return ResultUtil.error(1, "用户名为null");
        }
    }

    @RequestMapping("/finduserbyusername")
    @ResponseBody
    public Result findUserByUsername(@RequestBody JSONObject jsonObject) {
        logger.info("findUserByUsername::username = [{}]", jsonObject);
        if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("username"))) {
            User user = userServiceImpl.findUserByUsername(jsonObject.getString("username"));
            if (user != null) {
                return ResultUtil.success();
            } else {
                logger.warn("当前用户不存在");
                return ResultUtil.error(1, "当前用户不存在");
            }
        } else {
            logger.warn("username为null");
            return ResultUtil.error(2, "username为null");
        }
    }

    @RequestMapping("/finduserbyuserid")
    @ResponseBody
    public Result findUserByUserId(@RequestBody JSONObject jsonObject) {
        logger.info("findUserByUserId::userId = [{}]", jsonObject);
        if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("userId"))) {
            Result user = userServiceImpl.findUserByUserId(jsonObject.getString("userId"));
            if (user.getCode() == 0) {
                return ResultUtil.success(user.getData());
            } else {
                return ResultUtil.error(1, user.getMsg());
            }
        } else {
            logger.warn("userId为null");
            return ResultUtil.error(1, "userId为null");
        }
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public Result updateUser(@RequestBody JSONObject jsonObject) {
        logger.info("updateUser::jsonObject = [{}]", jsonObject);
        User user = new User();
        Role role = new Role();
        if (jsonObject != null && jsonObject.getInteger("userId") != null
                && jsonObject.getInteger("roleId") != null) {
            role.setRoleId(jsonObject.getString("roleId"));
            user.setUserId(jsonObject.getString("userId"));
            user.setRole(role);
            user.setUpdateTime(new Date());
            if (StringUtils.isNotBlank(jsonObject.getString("password"))) {
                user.setPassword(jsonObject.getString("password"));
                Result result = userServiceImpl.updateUserPassword(user);
                if (result.getCode() == 0) {
                    return ResultUtil.success();
                } else {
                    return ResultUtil.error(1, result.getMsg());
                }
            } else {
                Result result = userServiceImpl.updateUser(user);
                if (result.getCode() == 0) {
                    return ResultUtil.success();
                } else {
                    return ResultUtil.error(1, result.getMsg());
                }
            }
        } else {
            logger.warn("json为null");
            return ResultUtil.error(1, "json为null");
        }
    }

    @RequestMapping("/updateUserStatus")
    @ResponseBody
    public Result updateUserStatus(@RequestBody JSONObject jsonObject) throws URISyntaxException {
        logger.info("updateUserStatus::userId = [{}], status = [{}], username = [{}]", jsonObject);
        if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("userId"))
                && StringUtils.isNotBlank(jsonObject.getString("username"))
                && jsonObject.getInteger("status") != null) {
            User user = new User();
            user.setUserId(jsonObject.getString("userId"));
            user.setStatus(jsonObject.getInteger("status"));
            user.setUpdateTime(new Date());
            Result result = userServiceImpl.updateUserStatus(user);
            if (result.getCode() == 0) {
                Result creatWorkspace = geoserverService.creatWorkspace(jsonObject.getString("username"));
                if (creatWorkspace.getCode() == 0 || creatWorkspace.getCode() == 1) {
                    return ResultUtil.success();
                } else {
                    return creatWorkspace;
                }
            } else {
                return result;
            }
        } else {
            logger.warn("json为null");
            return ResultUtil.error(1, "json为null");
        }
    }

    @RequestMapping("/updatePassword")
    @ResponseBody
    public Result updatePassword(@RequestBody JSONObject jsonObject) {
        logger.info("updatePassword::jsonObject = [{}]", jsonObject);
        if (jsonObject != null) {
            if (StringUtils.isNotBlank(jsonObject.getString("username"))
                    && StringUtils.isNotBlank(jsonObject.getString("oldPassword"))
                    && StringUtils.isNotBlank(jsonObject.getString("newpassword"))) {
                User user = userServiceImpl.findUserByUsername(jsonObject.getString("username"));
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                boolean matches = encoder.matches(jsonObject.getString("oldPassword"), user.getPassword());
                if (matches) {
                    user.setPassword(jsonObject.getString("newpassword"));
                    Result result = userServiceImpl.updateUserPassword(user);
                    if (result.getCode() == 0) {
                        return ResultUtil.success();
                    } else {
                        return result;
                    }
                } else {
                    logger.warn("旧密码输入错误，请重新输入");
                    return ResultUtil.error(1, "旧密码输入错误，请重新输入");
                }
            } else {
                logger.warn("参数为null");
                return ResultUtil.error(1, "参数为null");
            }
        } else {
            logger.warn("json为null");
            return ResultUtil.error(1, "json为null");
        }
    }

    @RequestMapping("/deleteuser")
    @ResponseBody
    public Result deleteUser(@RequestBody JSONObject jsonObject) {
        logger.info("deleteUser::jsonObject = [{}]", jsonObject);
        if (jsonObject != null) {
            String userId = jsonObject.getString("userId");
            Result result = userServiceImpl.deleteUser(userId);
            if (result.getCode() == 0) {
                return ResultUtil.success();
            } else {
                return ResultUtil.error(1, result.getMsg());
            }
        } else {
            logger.warn("json为null");
            return ResultUtil.error(1, "json为null");
        }
    }
}
