package cc.wanshan.gis.controller.road;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.service.road.RoadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Li Cheng
 * @date 2019/6/12 17:41
 */
@Api(value = "RoadController", tags = "路径规划模块")
@RestController
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/road")
public class RoadController {

  private static final Logger logger = LoggerFactory.getLogger(RoadController.class);
  @Resource(name = "roadServiceImpl")
  private RoadService roadServiceImpl;

  @ApiOperation(value = "查询规划路径", notes = "根据坐标查询规划路径")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "sourcePoint", value = "起点坐标，格式为POINT（lo la）", required = true),
      @ApiImplicitParam(name = "targetPoint", value = "终点坐标，格式为POINT（lo la）", required = true)
  })
  @GetMapping("/findroad")
  @ResponseBody
  public Result findRoad(@RequestParam(value = "sourcePoint") String sourcePoint,
      @RequestParam("targetPoint") String targetPoint) {
    logger.info("findRoad::sourcePoint = [{}], targetPoint = [{}]", sourcePoint, targetPoint);
    //return roadServiceImpl.findSource(sourcePoint);
    return roadServiceImpl.findRoad(sourcePoint, targetPoint);
  }

 /* @ApiOperation(value = "查询终点公交站索引", notes = "根据终点公交站坐标查询起点索引")
  @ApiImplicitParam(name = "targetPoint", value = "终点公交站坐标，格式为POINT（lo la）", required = true)
  @GetMapping("/findtarget/{targetPoint}")
  @ResponseBody
  public Result findTarget(@PathVariable String targetPoint) {
    logger.info("findTarget::TargetPoint = [{}]", targetPoint);
    //return roadServiceImpl.findTarget(targetPoint);
    return null;
  }

  @ApiOperation(value = "查询附近公交站信息", notes = "根据坐标查询最近公交站点")
  @ApiImplicitParam(name = "point", value = "坐标点信息，格式为POINT（lo la）", required = true)
  @GetMapping("/findstation/{point}")
  @ResponseBody
  public Result findStation(@PathVariable String point) {
    logger.info("findStation::point = [{}]", point);
    //return roadServiceImpl.findStation(point);
    return null;
  }*/

}
