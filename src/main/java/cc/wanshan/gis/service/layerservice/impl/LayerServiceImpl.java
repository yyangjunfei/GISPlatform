package cc.wanshan.gis.service.layerservice.impl;

import cc.wanshan.gis.dao.LayerDao;
import cc.wanshan.gis.dao.createschemadao.impl.CreateSchemaDaoImpl;
import cc.wanshan.gis.dao.cretelayerstabledao.CreatLayerTableDao;
import cc.wanshan.gis.dao.droplayerdao.DropLayerDao;
import cc.wanshan.gis.dao.insertfeaturedao.InsertFeatureDao;
import cc.wanshan.gis.dao.searchlayertabledao.SearchLayerTableDao;
import cc.wanshan.gis.dao.searchschemadao.impl.SearchSchemaDaoImpl;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Feature;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.thematic.FirstClassification;
import cc.wanshan.gis.entity.thematic.SecondClassification;
import cc.wanshan.gis.service.geoserverservice.GeoserverService;
import cc.wanshan.gis.service.layerservice.LayerService;
import cc.wanshan.gis.utils.GeoserverUtils;
import cc.wanshan.gis.utils.ResultUtil;
import io.micrometer.core.instrument.util.StringUtils;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.validator.internal.util.logging.formatter.CollectionOfObjectsToStringFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;

@Service(value = "layerServiceImpl")
public class LayerServiceImpl implements LayerService {

  private static final Logger logger = LoggerFactory.getLogger(LayerServiceImpl.class);
  @Resource
  private LayerDao layerDao;
  @Resource
  private DropLayerDao dropLayerDao;
  @Resource
  private CreateSchemaDaoImpl createSchemaDao;
  @Resource
  private SearchSchemaDaoImpl searchSchemaDao;
  @Resource
  private GeoserverUtils geoserverUtils;
  @Resource(name = "searchLayerTableDaoImpl")
  private SearchLayerTableDao searchLayerTableDao;
  @Resource(name = "createLayerTableDaoImpl")
  private CreatLayerTableDao createLayerTableDao;
  @Resource(name = "insertLayerDaoImpl")
  private InsertFeatureDao insertFeatureDao;
  @Resource(name = "geoserverServiceImpl")
  private GeoserverService geoserverServiceImpl;

  @Override
  @Transactional
  public Result insertLayer(Layer layer, String workspace) {
    logger.info("insertLayer::layer = [{}], workspace = [{}]", layer, workspace);
    //在layer中插入新记录
    int i = layerDao.insertLayer(layer);
    if (i == 1) {
      //在对应Thematic中创建对应layer表
      Result result = newLayer(workspace, layer.getLayerName(), layer.getType(), layer.getEpsg());
      if (result.getCode() == 0) {
        return ResultUtil.success(layer);
      } else {
        logger.warn("新建图层失败", result.toString());
        return result;
      }
    } else {
      logger.warn("保存出错" + layer.toString());
      return ResultUtil.error(2, "保存出错");
    }
  }

  @Override
  @Transactional
  public Result deleteLayer(String layerName, String thematicName, String storeId,
      String storeName) {
    logger.info(
        "deleteLayer::layerName = [{}], thematicName = [{}], storeId = [{}], StoreName = [{}]",
        layerName, thematicName, storeId, storeName);
    Boolean searchLayer = geoserverServiceImpl.searchLayer(thematicName, layerName);
    if (searchLayer) {
      Boolean deleteLayer = geoserverServiceImpl.deleteLayer(thematicName, storeName, layerName);
      if (deleteLayer) {
        Result result = dropLayerDao.dropLayer(thematicName, layerName);
        if (result.getCode() == 0) {
          int i = layerDao.deleteLayerByLayerNameAndStoreId(layerName, storeId);
          if (i == 1) {
            return ResultUtil.success();
          } else {
            logger.warn("删除layer记录失败" + layerName);
            return ResultUtil.error(1, "异常操作");
          }
        } else {
          logger.error("删除图层对应表失败", layerName, thematicName, storeId, storeName);
          return ResultUtil.error(1, "异常操作");
        }
      } else {
        logger.warn("删除geoserver图层删除失败");
        return ResultUtil.error(1, "异常操作");
      }
    } else {
      logger.error("geoserver未找到当前图层", layerName, thematicName, storeId, storeName);
      return ResultUtil.error(1, "异常操作");
    }
  }

  @Override
  public Result searchLayer(String thematicName, String layerName) {
    logger.info("searchLayer::thematicName = [{}], layerName = [{}]", thematicName, layerName);
    RESTLayer layer = GeoserverUtils.manager.getReader().getLayer(thematicName, layerName);
    if (layer == null) {
      logger.warn("用户" + thematicName + "图层未发布" + layerName);
      return ResultUtil.error(1, "图层未发布");
    } else {
      return ResultUtil.success();
    }
  }

  @Override
  @Transactional
  public Result newLayer(String workspace, String layerName, String type, String epsg) {
    logger.info("newLayer::workspace = [{}], layerName = [{}], type = [{}], epsg = [{}]", workspace,
        layerName, type, epsg);
    if (layerName != null && !"".equals(layerName) && type != null && !"".equals(type)
        && epsg != null && !"".equals(epsg)) {
      Result creatTable = createLayerTableDao.creatTable(workspace, layerName, type, epsg);
      if (creatTable.getCode() == 0) {
        return ResultUtil.success();
      } else {
        return creatTable;
      }
    } else {
      logger.warn("空指针异常");
      return ResultUtil.error(2, "空指针异常");
    }
  }

  @Override
  @Transactional
  public Result insertFeatures(String thematicName, String layerName, String storeName,
      List<Feature> features)
      throws IOException {
    if (features != null && StringUtils.isNotBlank(layerName) && StringUtils
        .isNotBlank(thematicName)) {
      Result insertLayers = insertFeatureDao.insertFeatures(features, layerName, thematicName);
      if (insertLayers.getCode() == 0) {
        return publishLayer(thematicName, storeName, layerName);
      } else {
        logger.warn(layerName + "插入失败");
        return insertLayers;
      }
    }
    logger.warn("参数为空");
    return ResultUtil.error(2, "参数为空");
  }

  @Override
  public Result findLayerCountByUsernameAndLayerName(String username, String layerName) {
    logger.info("findLayerCountByUsernameAndLayerName::username = [{}], layerName = [{}]", username,
        layerName);
    int layerCount = layerDao.findLayerCountByUsernameAndLayerName(username, layerName);
    Result result = searchLayerTableDao.searchLayer(layerName, username);
    if (layerCount > 0 || result.getCode() == 0) {
      logger.warn(layerName + "图层名已存在，请重新命名");
      return ResultUtil.error(1, "图层名已存在，请重新命名");
    } else {
      return ResultUtil.success();
    }
  }

  @Override
  public List<FirstClassification> findLayerByThematicIdAndNullUserId(String thematicId) {
    logger.info("findLayerByThematicIdAndNullUserId::thematicId = [{}]", thematicId);
    List<FirstClassification> firstClassifications = new ArrayList<>();
    List<Layer> layers = layerDao
        .findLayerByThematicIdAndNullUserId(thematicId);
    ArrayList<String> objects = new ArrayList<>();
    objects.add("境界与政区");
    objects.add("POI数据");
    objects.add("水系");
    objects.add("道路");
    objects.add("居民地及设施");
    if (layers.size() > 0) {
      FirstClassification  firstClassification =null;
      for (String object : objects) {
        //拿到第一个类型
        firstClassification = new FirstClassification();
        List<SecondClassification> secondClassifications = new ArrayList<>();
        for (Layer layer : layers) {
          //确定第二个类型
          if (layer.getFirstClassification().equals(object)) {
            SecondClassification  secondClassification =  new SecondClassification();
            secondClassification.setSecondClassificationName(layer.getSecondClassification());
            secondClassification.setLayer(layer);
            secondClassifications.add(secondClassification);
          }
        }
        firstClassification.setFirstClassificationName(object);
        firstClassification.setSecondClassifications(secondClassifications);
        firstClassifications.add(firstClassification);
      }
    }
    return firstClassifications;
  }


  private Result publishLayer(String workspace, String store, String layerName) {
    logger.info("publishLayer::workspace = [{}], store = [{}], layerName = [{}]", workspace, store,
        layerName);
    RESTLayer layer = GeoserverUtils.manager.getReader().getLayer(workspace, layerName);
    if (layer == null) {
      Result result = geoserverUtils.publishLayer(workspace, store, layerName);
      if (result.getCode() == 0 || result.getCode() == 1) {
        return ResultUtil.success();
      } else {
        logger.warn(layerName + "发布失败");
        return result;
      }
    } else {
      return ResultUtil.success();
    }
  }
}
