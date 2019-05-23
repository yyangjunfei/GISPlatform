package cc.wanshan.gis.controller.layer;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.drawlayer.Store;
import cc.wanshan.gis.entity.thematic.Thematic;
import cc.wanshan.gis.service.layerservice.LayerService;
import cc.wanshan.gis.utils.ResultUtil;

import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.instrument.util.StringUtils;
import java.util.Date;
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

  @RequestMapping("/saveLayer")
  @ResponseBody
  public Result saveLayer(@RequestBody JSONObject jsonObject) throws IOException {
    logger.info("saveLayer::jsonObject = [{}]", jsonObject);
    if (StringUtils.isNotBlank(jsonObject.getString("thematicName"))
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
      Layer layer = new Layer();
      Thematic thematic = new Thematic();
      Store store = new Store();
      String thematicName = jsonObject.getString("thematicName");
      thematic.setThematicId(jsonObject.getString("thematicId"));
      thematic.setThematicName(thematicName);
      thematic.setThematicNameZH(jsonObject.getString("thematicNameZH"));
      layer.setLayerNameZH(jsonObject.getString("layerNameZH"));
      layer.setLayerName(jsonObject.getString("layerName"));
      layer.setEpsg(jsonObject.getString("epsg"));
      layer.setType(jsonObject.getString("type"));
      layer.setFirstClassification(jsonObject.getString("firstClassification"));
      layer.setSecondClassification(jsonObject.getString("secondClassification"));
      layer.setSecurity(jsonObject.getString("security"));
      layer.setPublishTime(new Date());
      layer.setUpdateTime(new Date());
      layer.setUploadTime(new Date());
      store.setStoreId(jsonObject.getString("storeId"));
      layer.setStore(store);
      layer.setUserId(jsonObject.getString("userId"));
      layer.setThematic(thematic);
      Result insertLayer = layerService.insertLayer(layer, thematicName);
      if (insertLayer.getCode() == 0) {
        Result result = layerService.insertFeatures(jsonObject);
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
    } else {
      logger.warn("图层参数为null:" + jsonObject.toString());
      return ResultUtil.error(2, "jsonObject为空");
    }
  }

  @RequestMapping("/deletelayer")
  @ResponseBody
  public Result deleteLayer(@RequestBody JSONObject jsonObject) {
    return layerService.deleteLayer(jsonObject);
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

  @RequestMapping("/searchLayer")
  @ResponseBody
  public Result searchLayer(@RequestBody JSONObject jsonObject) {
    logger.info("searchLayer::jsonObject = [{}]", jsonObject);
    Result result = layerService.searchLayer(jsonObject);
    if (result.getCode() == 0) {
      return result;
    } else {
      logger.warn("警告!" + result.getMsg());
      return result;
    }
  }
}
