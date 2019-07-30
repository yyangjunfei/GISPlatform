package cc.wanshan.gis.controller.authorize;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.service.authorize.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Li Cheng
 * @Date 8:35 2019/7/26
 **/
@Api(value = "RoleController", tags = "角色管理模块")
@RestController
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/role")
public class RoleController {

  private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

  @Resource(name = "roleServiceImpl")
  private RoleService roleService;

  @ApiOperation(value = "查询所有角色")
  @GetMapping("/find/all")
  @ResponseBody
  public Result findAll() {
    logger.info("findAll::");
    return roleService.findAllRole();
  }
  @ApiOperation(value = "插入角色", notes = "新建一个角色")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "roleName", value = "角色英文名", required = true),
      @ApiImplicitParam(name = "roleNameZH", value = "角色中文名", required = true),
      @ApiImplicitParam(name = "describe", value = "角色描述", required = true)
  })
  @PostMapping("/insert")
  @ResponseBody
  public Result insertRole(@RequestParam(value = "roleName") String roleName,
      @RequestParam(value = "roleNameZH") String roleNameZH,
      @RequestParam(value = "describe") String describe) {
    logger.info("insertRole::roleName = [{}], roleNameZH = [{}], describe = [{}]", roleName,
        roleNameZH, describe);
    return roleService.insertRole(roleName, roleNameZH, describe);
  }

  @ApiOperation(value = "根据角色英文名查找角色信息")
  @ApiImplicitParam(name = "roleName", value = "角色英文名", required = true)
  @PostMapping("/find/count/name")
  @ResponseBody
  public Result findRoleByRoleName(@RequestParam(value = "roleName") String roleName) {
    logger.info("findRoleByRoleName::roleName = [{}]", roleName);
    return roleService.findCountByRoleName(roleName);
  }

  @ApiOperation(value = "根据角色名查找角色信息")
  @ApiImplicitParam(name = "roleNameZH", value = "角色名", required = true)
  @PostMapping("/find/count/namezh")
  @ResponseBody
  public Result findRoleByRoleNameZH(@RequestParam(value = "roleNameZH") String roleNameZH) {
    logger.info("findRoleByRoleNameZH::roleNameZH = [{}]", roleNameZH);
    return roleService.findCountByRoleNameZH(roleNameZH);

  }

  @ApiOperation(value = "更新角色")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "roleId", value = "角色Id", required = true),
      @ApiImplicitParam(name = "roleName", value = "角色英文名", required = true),
      @ApiImplicitParam(name = "roleNameZH", value = "角色中文名", required = true),
      @ApiImplicitParam(name = "describe", value = "角色描述", required = true)
  })
  @PutMapping("/update")
  @ResponseBody
  public Result updateRole(@RequestParam(value = "roleId") String roleId,
      @RequestParam(value = "roleName") String roleName,
      @RequestParam(value = "roleNameZH") String roleNameZH,
      @RequestParam(value = "describe") String describe) {
    logger.info("updateRole::roleId = [{}], roleName = [{}], roleNameZH = [{}], describe = [{}]",
        roleId, roleName, roleNameZH, describe);
    return roleService.updateRole(roleId, roleName, roleNameZH, describe);
  }

  @ApiOperation(value = "删除角色")
  @ApiImplicitParam(name = "roleId", value = "角色Id", required = true)
  @DeleteMapping("/delete")
  @ResponseBody
  public Result deleteRole(@RequestParam(value = "roleId") String roleId) {
    logger.info("deleteRole::roleId = [{}]", roleId);
    return roleService.deleteRole(roleId);
  }

  @ApiOperation(value = "根据Id查找角色")
  @ApiImplicitParam(name = "roleId", value = "角色Id", required = true)
  @PostMapping("/find/id")
  @ResponseBody
  public Result findRoleByRoleId(@RequestParam(value = "roleId") String roleId) {
    logger.info("findRoleByRoleId::roleId = [{}]", roleId);
    return roleService.findRoleByRoleId(roleId);
  }
}
