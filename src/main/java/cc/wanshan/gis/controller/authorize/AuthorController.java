package cc.wanshan.gis.controller.authorize;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.service.authorize.AuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "AuthorController", tags = "权限管理")
@RestController
@RequestMapping("/author")
public class AuthorController {

  private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
  @Resource(name = "authorityServiceImpl")
  private AuthorityService authorityServiceImpl;

  @ApiOperation(value = "根据url查找权限信息")
  @ApiImplicitParam(name = "url", value = "访问路径", required = true)
  @PostMapping("/find/url")
  @ResponseBody
  public Result findByUrl(@RequestParam(value = "url") String url) {
    logger.info("findByUrl::url = [{}]", url);
    return authorityServiceImpl.findByUrl(url);
  }

  @ApiOperation(value = "根据权限Id查询权限信息")
  @ApiImplicitParam(name = "authorId", value = "权限Id", required = true)
  @PostMapping("/find/id/{authorId}")
  @ResponseBody
  public Result findByAuthorId(@PathVariable String authorId) {
    return authorityServiceImpl.findByAuthorId(authorId);
  }

  @ApiOperation(value = "插入新的权限")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "authorName", value = "权限名", required = true),
      @ApiImplicitParam(name = "url", value = "访问路径", required = true)
  })
  @PostMapping("/insert")
  @ResponseBody
  public Result insert(@RequestParam(value = "authorName") String authorName, @RequestParam(value = "url") String url) {
    return authorityServiceImpl.insert(authorName, url);
  }

  @ApiOperation(value = "更新权限")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "authorId", value = "权限Id", required = true),
      @ApiImplicitParam(name = "authorName", value = "权限名", required = true),
      @ApiImplicitParam(name = "url", value = "访问路径", required = true)
  })
  @PutMapping("/update")
  @ResponseBody
  public Result update(@RequestParam(value = "authorId") String authorId, @RequestParam(value = "authorName") String authorName,
      @RequestParam(value = "url") String url) {
    return authorityServiceImpl.update(authorId, authorName, url);
  }

  @ApiOperation(value = "删除权限")
  @ApiImplicitParam(name = "authorId", value = "权限Id", required = true)
  @DeleteMapping("/delete/{authorId}")
  @ResponseBody
  public Result delete(@PathVariable String authorId) {
    return authorityServiceImpl.delete(authorId);
  }
}
