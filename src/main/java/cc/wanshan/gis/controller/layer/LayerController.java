package cc.wanshan.gis.controller.layer;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.drawlayer.LineString;
import cc.wanshan.gis.entity.drawlayer.Point;
import cc.wanshan.gis.entity.drawlayer.Polygon;
import cc.wanshan.gis.entity.style.Style;
import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.service.layer.LayerService;
import cc.wanshan.gis.service.style.StyleService;
import cc.wanshan.gis.service.thematic.ThematicService;
import cc.wanshan.gis.service.thematicuser.ThematicUserService;
import cc.wanshan.gis.utils.ResultUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.io.IOException;

@Controller
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

  @RequestMapping("/savelayer")
  @ResponseBody
  public Result saveLayer(@RequestBody JSONObject jsonObject) throws IOException {
    Layer layer = null;
    logger.info("saveLayer::jsonObject = [{}]", jsonObject);
    if (jsonObject != null
        && StringUtils.isNotBlank(jsonObject.getString("layerNameZH"))
        && StringUtils.isNotBlank(jsonObject.getString("layerName"))
        && StringUtils.isNotBlank(jsonObject.getString("epsg"))
        && StringUtils.isNotBlank(jsonObject.getString("type"))
        && StringUtils.isNotBlank(jsonObject.getString("userId"))
    ) {
      layer = JSON.parseObject(jsonObject.toJSONString(), Layer.class);
      logger.info("saveLayer::layer值为：" + layer.toString());
      String layerName = jsonObject.getString("layerName");
      String userId = jsonObject.getString("userId");
      String type = jsonObject.getString("type");
      layer.setPublishTime(new Date());
      layer.setUpdateTime(new Date());
      layer.setUploadTime(new Date());
      JSONArray features = jsonObject.getJSONArray("features");
      Layer searchLayer = layerService.findLayer(userId, layerName);
      if (features.size() > 0) {
        List featureList = getFeatures(features, type, searchLayer);
        if (searchLayer != null) {
          Result result = layerService
              .insertFeatures(layerName, type, featureList);
          if (result.getCode() == 0) {
            Layer newLayer = layerService.findLayerByLayerId(searchLayer.getLayerId());
            return ResultUtil.success(newLayer);
          } else {
            logger.warn("警告:" + result.getMsg());
            return ResultUtil.error(1, result.getMsg());
          }
        } else {
          Result insertLayer = layerService.insertLayer(layer);
          if (insertLayer.getCode() == 0) {
            Layer newlayer = (Layer) insertLayer.getData();
            List features1 = getFeatures(features, type, newlayer);
            Result result = layerService
                .insertFeatures(layerName, type, features1);
            if (result.getCode() == 0) {
              Layer newLayer = layerService.findLayerByLayerId(newlayer.getLayerId());
              return ResultUtil.success(newLayer);
            } else {
              logger.warn("警告:" + result.getMsg());
              return ResultUtil.error(1, result.getMsg());
            }
          } else {
            logger.warn("警告：saveLayer::jsonObject = [{}]", insertLayer.getMsg());
            return insertLayer;
          }
        }
      } else {
        return ResultUtil.error(2, "features为空");
      }
    } else {
      logger.warn("图层参数为null:" + jsonObject);
      return ResultUtil.error(2, "jsonObject为空");
    }
  }

  @RequestMapping("/deletelayer")
  @ResponseBody
  public Result deleteLayer(@RequestBody JSONArray jsonArray) {
    logger.info("deleteLayer::jsonArray = [{}]",jsonArray);
    if (jsonArray != null && jsonArray.size() > 0
    ) {
      List<Layer> layers = JSON.parseArray(jsonArray.toJSONString(), Layer.class);
      return layerService.deleteLayer(layers);
    }
    logger.error("deleteLayer::jsonObject = [{}]", jsonArray + "参数为null");
    return ResultUtil.error("参数为null");
  }


  @RequestMapping("/deletefeature")
  @ResponseBody
  public Result deleteFeature(@RequestBody JSONObject jsonObject) {
    logger.info("deleteFeature::jsonObject = [{}]", jsonObject);
    if (jsonObject != null && jsonObject.getJSONArray("features").size() > 0 && StringUtils
        .isNotBlank(jsonObject.getString("type"))) {
      String type = jsonObject.getString("type");
      if ("point".equals(type.toLowerCase())) {
        List<Point> points = JSON.parseArray(jsonObject.getJSONArray("features").toJSONString(), Point.class);
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
        List<Polygon> polygons = JSON.parseArray(jsonObject.getJSONArray("features").toJSONString(), Polygon.class);
        Boolean aBoolean = layerService.deleteFeature(polygons, type);
        if (aBoolean) {
          return ResultUtil.success();
        }
      }
    }
    return ResultUtil.error(4, "参数为null");
  }

  @RequestMapping("/findlayerbyuseridlayername")
  @ResponseBody
  public Result findLayerCountByLayerName(@RequestParam String userId, String layerName) {
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

  @RequestMapping("/searchlayer")
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

  private final List getFeatures(JSONArray features, String type, Layer layer) {
    logger.info("getFeatures::features = [{}]", features, type);
    List<Point> pointList = new ArrayList<>();
    List<LineString> lineStringList = new ArrayList<>();
    List<Polygon> polygonList = new ArrayList<>();
    Point point = null;
    LineString lineString = null;
    Polygon polygon = null;
    if (features.size() > 0) {
      if ("point".equals(type.toLowerCase())) {
        List<Point> deletePoint=new ArrayList<>();
        for (int i = 0; i < features.size(); i++) {
          point = JSON.parseObject(features.getJSONObject(i).toString(), Point.class);
          point.setLayer(layer);
          point.setInsertTime(new Date());
          point.setUpdateTime(new Date());
          if (point.getFeatureId()!=null){
            deletePoint.add(point);
            logger.info("deletePoint.add(point)::"+deletePoint);
          }
          pointList.add(point);
          logger.info("pointList.add(point)::"+pointList);
        }
        if (deletePoint.size()>0){
          //删除已存在point元素
          Boolean point1 = layerService.deleteFeature(deletePoint, "point");
          logger.info("layerService.deleteFeature(deletePoint, \"point\")::"+point1);
        }
        return pointList;
      }
      if ("linestring".equals(type.toLowerCase())) {
        List<LineString> deleteLineString =new ArrayList<>();
        for (int i = 0; i < features.size(); i++) {
          lineString = JSON.parseObject(features.getJSONObject(i).toString(), LineString.class);
          lineString.setLayer(layer);
          lineString.setInsertTime(new Date());
          lineString.setUpdateTime(new Date());
          if (lineString.getFeatureId()!=null){
            deleteLineString.add(lineString);
            logger.info("deleteLineString.add(lineString)"+deleteLineString);
          }
          lineStringList.add(lineString);
          logger.info("lineStringList.add(lineString)::"+lineStringList);
        }
        if (deleteLineString.size()>0){
          Boolean linestring = layerService.deleteFeature(deleteLineString, "linestring");
          logger.info("layerService.deleteFeature(deleteLineString, \"linestring\")::"+linestring);
        }
        return lineStringList;
      }
      if ("polygon".equals(type.toLowerCase())) {
        List<Polygon> deletePolygon=new ArrayList<>();
        for (int i = 0; i < features.size(); i++) {
          polygon = JSON.parseObject(features.getJSONObject(i).toString(), Polygon.class);
          polygon.setLayer(layer);
          polygon.setInsertTime(new Date());
          polygon.setUpdateTime(new Date());
          if (polygon.getFeatureId()!=null){
            deletePolygon.add(polygon);
            logger.info("deletePolygon.add(polygon)::"+deletePolygon);
          }
          polygonList.add(polygon);
          logger.info("polygonList.add(polygon)::"+polygonList);
        }
        if (deletePolygon.size()>0){
          Boolean polygon1 = layerService.deleteFeature(deletePolygon, "polygon");
          logger.info("layerService.deleteFeature(deletePolygon, polygon)::"+polygon1);
        }
        return polygonList;
      } else {
        logger.error("type异常", type);
        return null;
      }
    } else {
      logger.error("features为null", features);
      return null;
    }
  }

  @RequestMapping(value = "/findthematiclayers")
  @ResponseBody
  public Result findThematicLayers(@RequestBody JSONObject jsonObject) {
    logger.info("findThematicLayers::jsonObject = [{}]", jsonObject);
    if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("userId"))) {
      List<Thematic> thematicList = thematicUserServiceImpl
          .findThematicLayersByUserId(jsonObject.getString("userId"));
      logger.info("findThematicLayers::thematicId = [{}], bindingResult = [{}]" + thematicList);
      JSONArray jsonArray = (JSONArray) JSONArray.toJSON(thematicList);
      return ResultUtil.success(jsonArray);
    }
    return ResultUtil.error("参数为null");
  }

  @RequestMapping(value = "/updatelayer")
  @ResponseBody
  public Result updateLayer(@RequestBody JSONObject jsonObject) {
    logger.info("updateLayer::jsonObject = [{}]", jsonObject);
    if (jsonObject != null
        && StringUtils.isNotBlank(jsonObject.getString("layerNameZH"))
        && StringUtils.isNotBlank(jsonObject.getString("security"))
        && StringUtils.isNotBlank(jsonObject.getString("userId"))
        && StringUtils.isNotBlank(jsonObject.getString("layerId"))
    ) {
      Layer layer = JSON.parseObject(jsonObject.toJSONString(), Layer.class);
      layer.setUpdateTime(new Date());
      Boolean updateLayer = layerService.updateLayer(layer);
      if (updateLayer) {
        return ResultUtil.success();
      } else {
        logger.error("更新失败" + updateLayer);
        return ResultUtil.error(1, "操作异常");
      }
    }
    logger.error("参数为null", jsonObject);
    return ResultUtil.error(2, "参数为null");
  }

  @RequestMapping("/findlayerbylayerid")
  @ResponseBody
  public Result findLayerByLayerId(@RequestBody JSONObject jsonObject) {
    logger.info("findLayerByLayerId::jsonObject = [{}]", jsonObject);
    if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("layerId"))) {
      Layer layer = layerService.findLayerByLayerId(jsonObject.getString("layerId"));
      return ResultUtil.success(layer);
    }
    logger.error("参数为null", jsonObject);
    return ResultUtil.error(2, "参数为null");
  }

  @RequestMapping("/findstylebythematicid")
  @ResponseBody
  public Result findStyleByThematicId(@RequestBody JSONObject jsonObject) {
    logger.info("findStyleByThematicId::jsonObject = [{}]", jsonObject);
    if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("thematicId"))) {
      Thematic thematicId = thematicServiceImpl
          .findByThematicId(jsonObject.getString("thematicId"));
      if (thematicId != null) {
        JSONObject jsonObject1 = (JSONObject) JSONObject.toJSON(thematicId);
        return ResultUtil.success(jsonObject1);
      }
    }
    logger.error("参数为null", jsonObject);
    return ResultUtil.error(1, "参数为null");
  }

  @RequestMapping("/findstylebystylename")
  @ResponseBody
  public Result findStyleByStyleName(@RequestBody JSONObject jsonObject) {
    logger.info("findStyleByStyleName::jsonObject = [{}]", jsonObject);
    if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("styleName"))) {
      List<Style> styleName = styleServiceImpl
          .findStyleByStyleName(jsonObject.getString("styleName"));
      if (styleName.size() > 0) {
        JSONArray jsonArray = (JSONArray) JSONArray.toJSON(styleName);
        return ResultUtil.success(jsonArray);
      }
    }
    logger.error("参数为null", jsonObject);
    return ResultUtil.error(1, "参数为null");
  }
  @RequestMapping("/findlayerbyuserid")
  @ResponseBody
  public Result findLayerByUserId(@RequestBody JSONObject jsonObject){
    if (jsonObject!=null&&StringUtils.isNotBlank(jsonObject.getString("userId"))) {
      List<Layer> layerList = layerService.findByUserId(jsonObject.getString("userId"));
      JSONArray o = (JSONArray) JSONObject.toJSON(layerList);
      return ResultUtil.success(layerList);
    }
    return ResultUtil.error(4,"参数为null");
  }
}
