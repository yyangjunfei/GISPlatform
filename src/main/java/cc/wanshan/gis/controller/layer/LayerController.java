package cc.wanshan.gis.controller.layer;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Feature;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.drawlayer.Properties;
import cc.wanshan.gis.entity.drawlayer.Store;
import cc.wanshan.gis.entity.style.Style;
import cc.wanshan.gis.entity.thematic.FirstClassification;
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
import java.io.IOException;

@Controller
@MapperScan("cc.wanshan.demo.entity")
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
 /* @RequestMapping("/newlayer")
  @ResponseBody
  public Result newLayer(@RequestBody JSONObject jsonObject) {
    logger.info("newLayer::jsonObject = [{}]", jsonObject);
    if (jsonObject.getInteger("storeId") != null && jsonObject.getInteger("storeId") != 0
        && StringUtils.isNotBlank(jsonObject.getString("layerName")) && StringUtils
        .isNotBlank(jsonObject.getString("type")) && StringUtils
        .isNotBlank(jsonObject.getString("EPSG")) && StringUtils
        .isNotBlank(jsonObject.getString("workspace"))) {
      Store store = new Store();
      store.setStoreId(jsonObject.getString("storeId"));
      String workspace = jsonObject.getString("workspace");
      layer.setLayerName(jsonObject.getString("layerName"));
      layer.setStore(store);
      layer.setType(jsonObject.getString("type"));
      layer.setEpsg(jsonObject.getString("EPSG"));
      return layerService.insertLayer(layer, workspace);
    } else {
      logger.warn("图层参数为null:" + jsonObject.toString());
      return ResultUtil.error(2, "jsonObject为空");
    }
  }*/

  @RequestMapping("/savelayer")
  @ResponseBody
  public Result saveLayer(@RequestBody JSONObject jsonObject) throws IOException {
    Layer layer = null;
    Thematic thematic = null;
    Store store = null;
    logger.info("saveLayer::jsonObject = [{}]", jsonObject);
    if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("thematicName"))
        && StringUtils.isNotBlank(jsonObject.getString("thematicNameZH"))
        && StringUtils.isNotBlank(jsonObject.getString("thematicId"))
        && StringUtils.isNotBlank(jsonObject.getString("layerNameZH"))
        && StringUtils.isNotBlank(jsonObject.getString("layerName"))
        && StringUtils.isNotBlank(jsonObject.getString("epsg"))
        && StringUtils.isNotBlank(jsonObject.getString("type"))
        && StringUtils.isNotBlank(jsonObject.getString("security"))
        && StringUtils.isNotBlank(jsonObject.getString("userId"))
        && StringUtils.isNotBlank(jsonObject.getString("storeId"))
    ) {
      layer = JSON.parseObject(jsonObject.toJSONString(), Layer.class);
      logger.info("saveLayer::layer值为：" + layer.toString());
      thematic = new Thematic();
      store = new Store();
      String thematicName = jsonObject.getString("thematicName");
      String layerName = jsonObject.getString("layerName");
      String storeName = jsonObject.getString("storeName");
      thematic.setThematicId(jsonObject.getString("thematicId"));
      thematic.setThematicName(thematicName);
      thematic.setThematicNameZH(jsonObject.getString("thematicNameZH"));
      //layer.setLayerNameZH(jsonObject.getString("layerNameZH"));
      //layer.setLayerName(layerName);
      //layer.setEpsg(jsonObject.getString("epsg"));
      //layer.setType(jsonObject.getString("type"));
      //layer.setFirstClassification(jsonObject.getString("firstClassification"));
      //layer.setSecondClassification(jsonObject.getString("secondClassification"));
      //layer.setSecurity(jsonObject.getString("security"));
      layer.setPublishTime(new Date());
      layer.setUpdateTime(new Date());
      layer.setUploadTime(new Date());
      store.setStoreId(jsonObject.getString("storeId"));
      layer.setStore(store);
      //layer.setUserId(jsonObject.getString("userId"));
      layer.setThematic(thematic);
      JSONArray features = jsonObject.getJSONArray("features");
      List<Feature> featureList = getFeatures(features);
      if (featureList != null) {
        Result searchLayer = layerService.searchLayer(thematicName, layerName);
        if (searchLayer.getCode() == 0) {
          Result result = layerService
              .insertFeatures(thematicName, layerName, storeName, featureList);
          if (result.getCode() == 0) {
            return ResultUtil.success();
          } else {
            logger.warn("警告:" + result.getMsg());
            return ResultUtil.error(1, result.getMsg());
          }
        } else {
          Result insertLayer = layerService.insertLayer(layer, thematicName);
          if (insertLayer.getCode() == 0) {
            Result result = layerService
                .insertFeatures(thematicName, layerName, storeName, featureList);
            if (result.getCode() == 0) {
              return ResultUtil.success();
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
  public Result deleteLayer(@RequestBody JSONObject jsonObject) {
    logger.info("deleteLayer::jsonObject = [{}]", jsonObject);
    if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("layerName"))
        && StringUtils.isNotBlank(jsonObject.getString("thematicName"))
        && StringUtils.isNotBlank(jsonObject.getString("storeId"))
        && StringUtils.isNotBlank(jsonObject.getString("storeName"))
    ) {
      String layerName = jsonObject.getString("layerName");
      String thematicName = jsonObject.getString("thematicName");
      String storeId = jsonObject.getString("storeId");
      String storeName = jsonObject.getString("storeName");
      return layerService.deleteLayer(layerName, thematicName, storeId, storeName);
    }
    logger.error("deleteLayer::jsonObject = [{}]", jsonObject + "参数为null");
    return ResultUtil.error("参数为null");
  }

  @RequestMapping("/findLayerCountByLayerName")
  @ResponseBody
  public Result findLayerCountByLayerName(@RequestParam String workspace, String layerName) {
    logger.info("findLayerCountByLayerName::workspace = [{}], layerName = [{}]", workspace,
        layerName);
    if (StringUtils.isNotBlank(workspace) && StringUtils.isNotBlank(layerName)) {
      return layerService.findLayerCountByUsernameAndLayerName(workspace, layerName);
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

  private static List<Feature> getFeatures(JSONArray features) {
    logger.info("getFeatures::features = [{}]", features);
    List<Feature> featuresList = new ArrayList<>();
    if (features.size() > 0) {
      for (int i = 0; i < features.size(); i++) {
        Feature feature = new Feature();
        Properties newProperties = new Properties();
        logger.info("geometry:" + features.getJSONObject(i).getJSONObject("geometry"));
        feature.setGeometry(features.getJSONObject(i).getJSONObject("geometry"));
        feature.setGeoId(i);
        JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
        logger.info("properties" + properties.toString());
        newProperties.setFclass(properties.getString("featureClass"));
        newProperties.setName(properties.getString("name"));
        feature.setProperties(newProperties);
        logger.info("getFeatures::features = [{}]", feature);
        featuresList.add(feature);
        logger.info("getFeatures::featuresList = [{}]", featuresList);
      }
      return featuresList;
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
    if (jsonObject != null && StringUtils.isNotBlank(jsonObject.getString("thematicName"))
        && StringUtils.isNotBlank(jsonObject.getString("thematicNameZH"))
        && StringUtils.isNotBlank(jsonObject.getString("thematicId"))
        && StringUtils.isNotBlank(jsonObject.getString("layerNameZH"))
        && StringUtils.isNotBlank(jsonObject.getString("security"))
        && StringUtils.isNotBlank(jsonObject.getString("userId"))
        && StringUtils.isNotBlank(jsonObject.getString("storeId"))
        && StringUtils.isNotBlank(jsonObject.getString("layerId"))
    ) {
      Thematic thematic = new Thematic();
      Store store = new Store();
      store.setStoreId(jsonObject.getString("storeId"));
      thematic.setThematicId(jsonObject.getString("thematicId"));
      thematic.setThematicName(jsonObject.getString("thematicName"));
      thematic.setThematicNameZH(jsonObject.getString("thematicNameZH"));
      Layer layer = JSON.parseObject(jsonObject.toJSONString(), Layer.class);
      layer.setThematic(thematic);
      layer.setStore(store);
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
    logger.error("参数为null",jsonObject);
    return ResultUtil.error(1,"参数为null");
  }
}
