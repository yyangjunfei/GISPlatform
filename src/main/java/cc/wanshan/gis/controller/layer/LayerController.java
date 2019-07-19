package cc.wanshan.gis.controller.layer;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.drawlayer.LineString;
import cc.wanshan.gis.entity.drawlayer.Point;
import cc.wanshan.gis.entity.drawlayer.Polygon;
import cc.wanshan.gis.entity.style.Style;
import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.service.thematic.LayerService;
import cc.wanshan.gis.service.style.StyleService;
import cc.wanshan.gis.service.thematic.ThematicService;
import cc.wanshan.gis.service.thematic.ThematicUserService;
import cc.wanshan.gis.utils.ResultUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @Author Li Cheng
 * @Date 14:28 2019/6/18
 **/
@Api(value = "LayerController", tags = "二维标绘功能")
@RestController
@EnableTransactionManagement(proxyTargetClass = true)
@RequestMapping("/layer")
public class LayerController {

  private static final Logger logger = LoggerFactory.getLogger(LayerController.class);
  @Resource(name = "layerServiceImpl")
  private LayerService layerService;
  @Resource(name = "thematicServiceImpl")
  private ThematicService thematicServiceImpl;
  @Resource(name = "thematicUserServiceImpl")
  private ThematicUserService thematicUserServiceImpl;
  @Resource(name = "styleServiceImpl")
  private StyleService styleServiceImpl;

  @ApiOperation(value = "保存标绘图层", notes = "新增标绘图层或者根据图层Id更新图层")
  @ApiImplicitParam(name = "layer", value = "实体类对象", required = true)
  @PostMapping("/savelayer")
  @ResponseBody
  public Result saveLayer(@RequestBody Layer layer) {
    logger.info("saveLayer::layer = [{}]", layer);
    return layerService.saveLayer(layer);
  }

  @ApiOperation(value = "删除标绘图层", notes = "根据图层集合批量删除图层,只需要封装layerId即可")
  @ApiImplicitParam(name = "layerList", value = "封装了layerId的layer集合", required = true)
  @PostMapping("/deletelayer")
  @ResponseBody
  public Result deleteLayer(@RequestBody List<Layer> layerList) {
    logger.info("deleteLayer::layerList = [{}]", layerList);
    if (layerList != null && layerList.size() > 0
    ) {
      return layerService.deleteLayer(layerList);
    }
    logger.error("deleteLayer::jsonObject = [{}]", layerList + "参数为null");
    return ResultUtil.error("参数为null");
  }

  @ApiOperation(value = "删除标绘要素", notes = "根据featureId集合批量删除feature")
  @ApiImplicitParam(name = "jsonObject", value = "封装了type类型和featureId集合的features", required = true)
  @DeleteMapping("/deletefeature")
  @ResponseBody
  public Result deleteFeature(@RequestBody JSONObject jsonObject) {
    logger.info("deleteFeature::jsonObject = [{}]", jsonObject);
    if (jsonObject != null && jsonObject.getJSONArray("features").size() > 0 && StringUtils
        .isNotBlank(jsonObject.getString("type"))) {
      String type = jsonObject.getString("type");
      if ("point".equals(type.toLowerCase())) {
        List<Point> points = JSON
            .parseArray(jsonObject.getJSONArray("features").toJSONString(), Point.class);
        Boolean aBoolean = layerService.deleteFeature(points, type);
        if (aBoolean) {
          return ResultUtil.success();
        }
      }
      if ("linestring".equals(type.toLowerCase())) {
        List<LineString> lineStrings = JSON
            .parseArray(jsonObject.getJSONArray("features").toJSONString(), LineString.class);
        Boolean aBoolean = layerService.deleteFeature(lineStrings, type);
        if (aBoolean) {
          return ResultUtil.success();
        }
      }
      if ("polygon".equals(type.toLowerCase())) {
        List<Polygon> polygons = JSON
            .parseArray(jsonObject.getJSONArray("features").toJSONString(), Polygon.class);
        Boolean aBoolean = layerService.deleteFeature(polygons, type);
        if (aBoolean) {
          return ResultUtil.success();
        }
      }
    }
    return ResultUtil.error(4, "参数为null");
  }

  @ApiOperation(value = "查询图层", notes = "根据userId和layerName查询图层")
  @ApiImplicitParams({@ApiImplicitParam(name = "layerName", value = "图层名", required = true),
      @ApiImplicitParam(name = "userId", value = "用户Id", required = true)})
  @GetMapping(value = "/findbyuseridlayername/{userId}/{layerName}")
  @ResponseBody
  public Result findLayerCountByLayerName(@PathVariable String userId,
      @PathVariable String layerName) {
    logger.info("findLayerCountByLayerName::userId = [{}], layerName = [{}]", userId, layerName);
    if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(layerName)) {
      Layer layer = layerService.findLayer(userId, layerName);
      if (layer != null) {
        return ResultUtil.success(layer);
      } else {
        return ResultUtil.error(1, "图层不存在");
      }
    } else {
      logger.warn("警告！传入参数存在null值");
      return ResultUtil.error(1, "传入参数存在null值");
    }
  }

  @PostMapping("/searchlayer")
  @ResponseBody
  public Result searchLayer(@RequestBody JSONObject jsonObject) {
    logger.info("searchLayer::jsonObject = [{}]", jsonObject);
    if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("thematicName"))
        && StringUtils.isNotBlank(jsonObject.getString("layerName"))) {
      String thematicName = jsonObject.getString("thematicName");
      String layerName = jsonObject.getString("layerName");
      Result result = layerService.searchLayer(thematicName, layerName);
      if (result.getCode() == 0) {
        return result;
      } else {
        logger.warn("警告!" + result.getMsg());
        return result;
      }
    } else {
      logger.error("searchLayer::jsonObject = [{}]", jsonObject + "参数为null");
      return ResultUtil.error("参数为null");
    }
  }

  @ApiOperation(value = "查询专题图层", notes = "根据userId查询所属专题图层")
  @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
  @GetMapping(value = "/findthematiclayers/{userId}")
  @ResponseBody
  public Result findThematicLayers(@PathVariable String userId) {
    logger.info("findThematicLayers::userId = [{}]", userId);
    if (StringUtils.isNotBlank(userId)) {
      List<Thematic> thematicList = thematicUserServiceImpl
          .findThematicLayersByUserId(userId);
      logger.info("findThematicLayers::thematicId = [{}], bindingResult = [{}]" + thematicList);
      JSONArray jsonArray = (JSONArray) JSONArray.toJSON(thematicList);
      return ResultUtil.success(jsonArray);
    }
    return ResultUtil.error("参数为null");
  }

  @ApiOperation(value = "修改图层", notes = "修改图层属性")
  @ApiImplicitParam(name = "layer", value = "实体类对象", required = true)
  @PostMapping(value = "/updatelayer")
  @ResponseBody
  public Result updateLayer(@RequestBody Layer layer) {
    logger.info("updateLayer::layer = [{}]", layer);
    if (layer != null) {
      layer.setUpdateTime(new Date());
      Boolean updateLayer = layerService.updateLayer(layer);
      if (updateLayer) {
        return ResultUtil.success();
      } else {
        logger.error("更新失败" + updateLayer);
        return ResultUtil.error(1, "操作异常");
      }
    }
    logger.error("参数为null", layer);
    return ResultUtil.error(2, "参数为null");
  }

  @ApiOperation(value = "查询图层", notes = "根据layerId查询图层信息")
  @ApiImplicitParam(name = "layerId", value = "图层 Id", required = true)
  @GetMapping("/findlayerbylayerid/{layerId}")
  @ResponseBody
  public Result findLayerByLayerId(@PathVariable String layerId) {
    logger.info("findLayerByLayerId::layerId = [{}]", layerId);
    if (StringUtils.isNotBlank(layerId)) {
      Layer layer = layerService.findLayerByLayerId(layerId);
      return ResultUtil.success(layer);
    }
    logger.error("参数为null", layerId);
    return ResultUtil.error(2, "参数为null");
  }

  @ApiOperation(value = "查询风格列表", notes = "根据专题Id查询风格列表")
  @ApiImplicitParam(name = "thematicId", value = "专题Id", required = true)
  @GetMapping("/findstylebythematicid/{thematicId}")
  @ResponseBody
  public Result findStyleByThematicId(@PathVariable String thematicId) {
    logger.info("findStyleByThematicId::thematicId = [{}]", thematicId);
    if (StringUtils.isNotBlank(thematicId)) {
      Thematic thematic = thematicServiceImpl
          .findByThematicId(thematicId);
      if (thematic != null) {
        JSONObject jsonObject1 = (JSONObject) JSONObject.toJSON(thematic);
        return ResultUtil.success(jsonObject1);
      }
    }
    logger.error("参数为null", thematicId);
    return ResultUtil.error(1, "参数为null");
  }

  @ApiOperation(value = "查询风格", notes = "根据风格名查询风格")
  @ApiImplicitParam(name = "styleName", value = "风格名", required = true)
  @GetMapping("/findstylebystylename/{styleName}")
  @ResponseBody
  public Result findStyleByStyleName(@PathVariable String styleName) {
    logger.info("findStyleByStyleName::styleName = [{}]", styleName);
    if (StringUtils.isNotBlank(styleName)) {
      List<Style> style = styleServiceImpl
          .findStyleByStyleName(styleName);
      if (style.size() > 0) {
        JSONArray jsonArray = (JSONArray) JSONArray.toJSON(style);
        return ResultUtil.success(jsonArray);
      }
    }
    logger.error("参数为null", styleName);
    return ResultUtil.error(1, "参数为null");
  }

  @ApiOperation(value = "查询图层列表", notes = "根据userId查询图层列表")
  @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
  @GetMapping("/findbyuserid/{userId}")
  @ResponseBody
  public Result findLayerByUserId(@PathVariable String userId) {
    logger.info("findLayerByUserId::userId = [{}]", userId);
    if (StringUtils.isNotBlank(userId)) {
      List<Layer> layerList = layerService.findByUserId(userId);
      return ResultUtil.success(layerList);
    }
    logger.error("参数为null", userId);
    return ResultUtil.error(4, "参数为null");
  }
}
