/*
package cc.wanshan.gisdev.controller.user;


import cc.wanshan.gisdev.entity.Result;
import cc.wanshan.gisdev.entity.usermanagement.Role;
import cc.wanshan.gisdev.entity.usermanagement.User;
import cc.wanshan.gisdev.service.geoserverservice.GeoserverService;
import cc.wanshan.gisdev.service.userservice.UserService;
import cc.wanshan.gisdev.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.instrument.util.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.util.Date;

@Controller
@MapperScan("cc.wanshan.demo.entity")
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/user")
public class UserController {
    private static final Logger logger= LoggerFactory.getLogger(UserController.class);
    @Resource(name = "userServiceImpl")
    private UserService userService;
    @Resource(name = "geoserverServiceImpl")
    private GeoserverService geoserverService;
    @RequestMapping("/findAllUser")
    @ResponseBody
    public Result findAllUser(){
        Result user = userService.findAllUser();
        if (user.getCode()==0){
            return ResultUtil.success(user.getData());
        }else {
            return ResultUtil.error(1,user.getMsg());
        }
    }
    @RequestMapping("/insertUser")
    @ResponseBody
    public Result insertUser(@RequestBody JSONObject jsonObject){
        logger.info("insertUser::jsonObject = [{}]",jsonObject);
        User user= new User();
        Role role = new Role();
        if (jsonObject!=null&& StringUtils.isNotBlank(jsonObject.getString("username"))&& StringUtils.isNotBlank(jsonObject.getString("password"))&&jsonObject.getInteger("roleId")!=null&&jsonObject.getInteger("roleId")!=0){
            user.setUsername(jsonObject.getString("username"));
            user.setPassword(jsonObject.getString("password"));
            user.setInsertTime(new Date());
            user.setUpdateTime(new Date());
            role.setRoleId(jsonObject.getInteger("roleId"));
            user.setRole(role);
            user.setStatus(0);
            user.setDelete(0);
            return userService.insertUser(user);
        }else {
            logger.warn("json为null");
            return ResultUtil.error(1,"json为null");
        }
    }
    @RequestMapping("/findUserCountByUsername")
    @ResponseBody
    public Result findUserCountByUsername(@RequestParam String username){
        logger.info("findUserCountByUsername::username = [{}]",username);
        if (StringUtils.isNotBlank(username)){
            Result result= userService.findUserCountByUsername(username);
            if (result.getCode()==0){
                return ResultUtil.success();
            }else {
                return ResultUtil.error(1,result.getMsg());
            }
        }else {
            logger.warn("用户名为null");
            return ResultUtil.error(1,"用户名为null");
        }
    }
    @RequestMapping("/findUserByUsername")
    @ResponseBody
    public Result findUserByUsername(@RequestParam String username){
        logger.info("findUserByUsername::username = [{}]",username);
        if (StringUtils.isNotBlank(username)){
            User user = userService.findUserByUsername(username);
            if (user!=null){
                return ResultUtil.success();
            }else {
                logger.warn("当前用户不存在");
                return ResultUtil.error(1,"当前用户不存在");
            }
        }else {
            logger.warn("username为null");
         return ResultUtil.error(2,"username为null");
        }
    }
    @RequestMapping("/findUserByUserId")
    @ResponseBody
    public Result findUserByUserId(@RequestParam Integer userId){
        logger.info("findUserByUserId::userId = [{}]",userId);
        if (userId!=0){
            Result user = userService.findUserByUserId(userId);
            if (user.getCode()==0){
                return ResultUtil.success(user.getData());
            }else {
                return ResultUtil.error(1,user.getMsg());
            }
        }else {
            logger.warn("userId为null");
            return ResultUtil.error(1,"userId为null");
        }
    }
    @RequestMapping("/updateUser")
    @ResponseBody
    public Result updateUser(@RequestBody JSONObject jsonObject){
        logger.info("updateUser::jsonObject = [{}]",jsonObject);
        User user = new User();
        Role role = new Role();
        if (jsonObject!=null&&jsonObject.getInteger("userId")!=null&&jsonObject.getInteger("roleId")!=null){
            role.setRoleId(jsonObject.getInteger("roleId"));
            user.setUserId(jsonObject.getInteger("userId"));
            user.setRole(role);
            user.setUpdateTime(new Date());
            if (StringUtils.isNotBlank(jsonObject.getString("password"))){
                user.setPassword(jsonObject.getString("password"));
                Result result = userService.updateUserPassword(user);
                if (result.getCode()==0){
                    return ResultUtil.success();
                }else {
                    return ResultUtil.error(1,result.getMsg());
                }
            }else {
                Result result = userService.updateUser(user);
                if (result.getCode()==0){
                    return ResultUtil.success();
                }else {
                    return ResultUtil.error(1,result.getMsg());
                }
            }
        }else {
            logger.warn("json为null");
            return ResultUtil.error(1,"json为null");
        }
    }
    @RequestMapping("/updateUserStatus")
    @ResponseBody
    public Result updateUserStatus(@RequestParam Integer userId,Integer status,String username) throws URISyntaxException {
        logger.info("updateUserStatus::userId = [{}], status = [{}], username = [{}]",userId, status, username);
        if (userId!=null&&status!=null){
            User user = new User();
            user.setUserId(userId);
            user.setStatus(status);
            user.setUpdateTime(new Date());
            Result result = userService.updateUserStatus(user);
            if (result.getCode()==0){
                Result creatWorkspace = geoserverService.creatWorkspace(username);
                if (creatWorkspace.getCode()==0||creatWorkspace.getCode()==1){
                    return ResultUtil.success();
                }else {
                    return creatWorkspace;
                }
            }else {
                return result;
            }
        }else {
            logger.warn("json为null");
            return ResultUtil.error(1,"json为null");
        }
    }
    @RequestMapping("/updatePassword")
    @ResponseBody
    public Result updatePassword(@RequestBody JSONObject jsonObject) {
        logger.info("updatePassword::jsonObject = [{}]",jsonObject);
        if (jsonObject != null) {
            if (StringUtils.isNotBlank(jsonObject.getString("username"))&& StringUtils.isNotBlank(jsonObject.getString("oldPassword"))&& StringUtils.isNotBlank(jsonObject.getString("newpassword"))){
                User user = userService.findUserByUsername(jsonObject.getString("username"));
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                boolean matches = encoder.matches(jsonObject.getString("oldPassword"), user.getPassword());
                if (matches){
                    user.setPassword(jsonObject.getString("newpassword"));
                    Result result = userService.updateUserPassword(user);
                    if (result.getCode()==0){
                        return ResultUtil.success();
                    }else {
                        return result;
                    }
                }else {
                    logger.warn("旧密码输入错误，请重新输入");
                    return ResultUtil.error(1,"旧密码输入错误，请重新输入");
                }
            }else {
                logger.warn("参数为null");
                return ResultUtil.error(1,"参数为null");
            }
        } else{
            logger.warn("json为null");
            return ResultUtil.error(1,"json为null");
        }
    }
    @RequestMapping("/deleteUser")
    @ResponseBody
    public Result deleteUser(@RequestBody JSONObject jsonObject){
        logger.info("deleteUser::jsonObject = [{}]",jsonObject);
        if (jsonObject!=null){
            Integer userId = jsonObject.getInteger("userId");
            Result result = userService.deleteUser(userId);
            if (result.getCode()==0){
                return ResultUtil.success();
            }else {
                return ResultUtil.error(1,result.getMsg());
            }
        }else{
            logger.warn("json为null");
            return ResultUtil.error(1,"json为null");
        }
    }
}
*/
