package cc.wanshan.gis.controller.bus;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.bus.NanJingStations;
import cc.wanshan.gis.service.bus.BusService;
import cc.wanshan.gis.utils.base.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Li Cheng
 * @date 2019/6/12 17:41
 */
@Api(value = "BusController", tags = "公交查询模块")
@RestController
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/bus")
public class BusController {

    private static final Logger logger = LoggerFactory.getLogger(BusController.class);
    @Resource(name = "busServiceImpl")
    private BusService busServiceImpl;

    @ApiOperation(value = "查询起点索引", notes = "根据起点公交站坐标查询起点索引")
    @ApiImplicitParam(name = "sourcePoint", value = "起点公交站坐标，格式为POINT（lo la）", required = true)
    @GetMapping("/findsource/{sourcePoint}")
    @ResponseBody
    public Result findSource(@PathVariable String sourcePoint) {
        logger.info("findSource::sourcePoint = [{}]", sourcePoint);
        if (StringUtils.isNotBlank(sourcePoint)) {
            Integer source = busServiceImpl.findSource(sourcePoint);
            if (source != null) {
                return ResultUtil.success(source);
            }
            return ResultUtil.error(1, "未找到对应点");
        }
        return ResultUtil.error(4, "参数为null");
    }

    @ApiOperation(value = "查询终点公交站索引", notes = "根据终点公交站坐标查询起点索引")
    @ApiImplicitParam(name = "targetPoint", value = "终点公交站坐标，格式为POINT（lo la）", required = true)
    @GetMapping("/findtarget/{targetPoint}")
    @ResponseBody
    public Result findTarget(@PathVariable String targetPoint) {
        logger.info("findTarget::TargetPoint = [{}]", targetPoint);
        if (StringUtils.isNotBlank(targetPoint)) {
            Integer target = busServiceImpl.findTarget(targetPoint);
            if (target != null) {
                return ResultUtil.success(target);
            }
            return ResultUtil.error(1, "未找到对应点");
        }
        return ResultUtil.error(4, "参数为null");
    }

    @ApiOperation(value = "查询附近公交站信息", notes = "根据坐标查询最近公交站点")
    @ApiImplicitParam(name = "point", value = "坐标点信息，格式为POINT（lo la）", required = true)
    @GetMapping("/findstation/{point}")
    @ResponseBody
    public Result findStation(@PathVariable String point) {
        logger.info("findStation::point = [{}]", point);
        if (StringUtils.isNotBlank(point)) {
            NanJingStations station = busServiceImpl.findStation(point);
            if (station != null) {
                return ResultUtil.success(station);
            }
            logger.error("busServiceImpl.findStation(point)", station);
            return ResultUtil.error(4, "结果为null");
        }
        logger.error("findStation::point = [{}]", point);
        return ResultUtil.error(4, "参数为null");
    }

}
