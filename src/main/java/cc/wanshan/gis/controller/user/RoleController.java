
package cc.wanshan.gis.controller.user;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.usermanagement.Role;
import cc.wanshan.gis.service.role.RoleService;
import cc.wanshan.gis.utils.ResultUtil;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.instrument.util.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@MapperScan("cc.wanshan.demo.entity")
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/role")
public class RoleController {
    private static final Logger logger= LoggerFactory.getLogger(RoleController.class);
    @Resource
    private ResultUtil resultUtil;
    @Resource(name = "roleServiceImpl")
    private RoleService roleService;
    @RequestMapping("/findAllRole")
    @ResponseBody
    public Result findAllRole(){
        logger.info("findAllRole::");
        Result roles = roleService.findAllRole();
        if (roles.getCode()==0){
            return ResultUtil.success(roles.getData());
        }else {
            return ResultUtil.error(1,roles.getMsg());
        }
    }
    @RequestMapping("/insertRole")
    @ResponseBody
    public Result insertRole(@RequestBody JSONObject jsonObject){
        logger.info("insertRole::jsonObject = [{}]",jsonObject);
        Role role = new Role();
        if (StringUtils.isNotBlank(jsonObject.getString("roleName"))&& StringUtils.isNotBlank(jsonObject.getString("roleNameZH"))&& StringUtils.isNotBlank(jsonObject.getString("describe"))){
            role.setRoleName(jsonObject.getString("roleName"));
            role.setRoleNameZH(jsonObject.getString("roleNameZH"));
            role.setDescribe(jsonObject.getString("describe"));
            Result result = roleService.insertRole(role);
            if (result.getCode()==0){
                return result;
            }else {
                return result;
            }
        }else {
            logger.warn("警告！传入参数存在null值");
            return ResultUtil.error(1,"json为空");
        }
    }
    @RequestMapping("/findRoleCountByRoleName")
    @ResponseBody
    public Result findRoleByRoleName(@RequestParam  String roleName){
        logger.info("findRoleByRoleName::roleName = [{}]",roleName);
        if (StringUtils.isNotBlank(roleName)){
            Result count= roleService.findCountByRoleName(roleName);
            if (count.getCode()==0){
                return ResultUtil.success();
            }else {
                return ResultUtil.error(1,count.getMsg());
            }
        }else {
            logger.warn("警告！传入参数存在null值");
            return ResultUtil.error(1,"roleName为空");
        }
    }
    @RequestMapping("/findRoleCountByRoleNameZH")
    @ResponseBody
    public Result findRoleByRoleNameZH(@RequestParam  String roleNameZH){
        logger.info("findRoleByRoleNameZH::roleNameZH = [{}]",roleNameZH);
        if (StringUtils.isNotBlank(roleNameZH)){
            Result count = roleService.findCountByRoleNameZH(roleNameZH);
            if (count.getCode()==0){
                return ResultUtil.success();
            }else {
                return ResultUtil.error(1,count.getMsg());
            }
        }else {
            logger.warn("警告！传入参数存在null值");
            return ResultUtil.error(1,"roleNameZH为空");
        }
    }
    @RequestMapping("/updateRole")
    @ResponseBody
    public Result updateRole(@RequestBody JSONObject jsonObject){
        logger.info("updateRole::jsonObject = [{}]",jsonObject);
        Role role = new Role();
        if (jsonObject!=null&&jsonObject.getInteger("roleId")!=null&&jsonObject.getInteger("roleId")!=0&& StringUtils.isNotBlank(jsonObject.getString("roleName"))&& StringUtils.isNotBlank(jsonObject.getString("roleNameZH"))&& StringUtils.isNotBlank(jsonObject.getString("describe"))){
            role.setRoleId(jsonObject.getString("roleId"));
            role.setRoleName(jsonObject.getString("roleName"));
            role.setRoleNameZH(jsonObject.getString("roleNameZH"));
            role.setDescribe(jsonObject.getString("describe"));
            Result result = roleService.updateRole(role);
            if (result.getCode()==0){
                logger.info("执行结果："+result.getMsg());
                logger.info("返回值："+result.getData().toString());
                return ResultUtil.success();
            }else {
                return ResultUtil.error(1,"修改失败！");
            }
        }else{
            logger.warn("警告！传入参数存在null值");
            return ResultUtil.error(1,"jsonObject为null");
        }
    }
    @RequestMapping("/deleteRole")
    @ResponseBody
    public Result deleteRole(@RequestParam String roleId){
        logger.info("deleteRole::roleId = [{}]",roleId);
        if (roleId!=null){
            Result result = roleService.deleteRole(roleId);
            if (result.getCode()==0){
                logger.info("执行结果："+result.getMsg());
                return ResultUtil.success();
            }else {
                return ResultUtil.error(1,result.getMsg());
            }
        }else {
            logger.warn("警告！传入参数存在null值");
            return ResultUtil.error(1,"roleId为null");
        }
    }
    @RequestMapping("/findRoleByRoleId")
    @ResponseBody
    public Result findRoleByRoleId(@RequestParam String roleId){
        logger.info("findRoleByRoleId::roleId = [{}]",roleId);
        if (roleId!=null){
            Result role = roleService.findRoleByRoleId(roleId);
            if (role.getCode()==0){
                logger.info("执行结果："+role.getMsg());
                logger.info("返回值："+role.getData().toString());
                return ResultUtil.success(role.getData());
            }else {
                return ResultUtil.error(1,role.getMsg());
            }
        }else {
            logger.warn("警告！传入参数存在null值");
            return ResultUtil.error(1,"roleId为null");
        }
    }
}
