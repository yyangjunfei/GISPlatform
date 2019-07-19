package cc.wanshan.gis.controller.geoserver;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.service.geoserver.UserPropsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Li Cheng
 * @date 2019/7/19 9:17
 */
@Api(value = "UserPropsController", tags = "用户Authkey管理")
@RestController
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/userprops")
public class UserPropsController {

  private static final Logger logger = LoggerFactory.getLogger(UserPropsController.class);
  @Resource(name = "userPropsServiceImpl")
  private UserPropsService userPropsServiceImpl;

  @ApiOperation(value = "查找UserProps", notes = "根据用户名查找UserProps")
  @ApiImplicitParam(name = "username", value = "用户名", required = true)
  @PostMapping(value = "/findbyusername")
  @ResponseBody
  public Result findByUsername(@RequestParam("username") String username) {
    logger.info("findByUsername::username = [{}]", username);
    return userPropsServiceImpl.findUserPropsByUsername(username);
  }

  @ApiOperation(value = "保存userProps")
  @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", required = true),
      @ApiImplicitParam(name = "propName", value = "属性名", required = true)})
  @PostMapping(value = "/save")
  @ResponseBody
  public Result save(@RequestParam("username") String username,
      @RequestParam("propName") String propName) {
    logger.info("saveUserProps::username = [{}], propName = [{}]", username, propName);
    return userPropsServiceImpl.saveUserProps(username, propName);
  }

  @ApiOperation(value = "更新userProps")
  @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", required = true),
      @ApiImplicitParam(name = "propName", value = "属性名", required = true)})
  @PostMapping(value = "/update")
  @ResponseBody
  public Result update(@RequestParam("username") String username,
      @RequestParam("propName") String propName) {
    logger.info("update::username = [{}], propName = [{}]",username, propName);
    return userPropsServiceImpl.updateUserProps(username, propName);
  }

  @ApiOperation(value = "删除userProps")
  @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", required = true),
      @ApiImplicitParam(name = "propName", value = "属性名", required = true)})
  @DeleteMapping(value = "/delete")
  @ResponseBody
  public Result delete(@RequestParam("username") String username,
      @RequestParam("propName") String propName) {
    logger.info("delete::username = [{}], propName = [{}]",username, propName);
    return userPropsServiceImpl.deleteUserProps(username, propName);
  }
}
