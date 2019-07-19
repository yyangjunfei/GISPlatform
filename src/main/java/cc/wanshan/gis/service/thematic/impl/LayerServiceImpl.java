package cc.wanshan.gis.service.thematic.impl;

import cc.wanshan.gis.dao.layer.LayerDao;
import cc.wanshan.gis.dao.layer.LineStringDao;
import cc.wanshan.gis.dao.layer.PointDao;
import cc.wanshan.gis.dao.layer.PolygonDao;
import cc.wanshan.gis.dao.cretelayerstable.CreatLayerTableDao;
import cc.wanshan.gis.dao.droplayer.DropLayerDao;
import cc.wanshan.gis.dao.insertfeature.FeatureDao;
import cc.wanshan.gis.dao.searchlayertable.SearchLayerTableDao;
import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.drawlayer.LineString;
import cc.wanshan.gis.entity.drawlayer.Point;
import cc.wanshan.gis.entity.drawlayer.Polygon;
import cc.wanshan.gis.entity.thematic.FirstClassification;
import cc.wanshan.gis.service.geoserver.GeoServerService;
import cc.wanshan.gis.service.thematic.LayerService;
import cc.wanshan.gis.utils.GeoServerUtils;
import cc.wanshan.gis.utils.ResultUtil;
import io.micrometer.core.instrument.util.StringUtils;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

@Service(value = "layerServiceImpl")
public class LayerServiceImpl implements LayerService {

  private static final Logger logger = LoggerFactory.getLogger(LayerServiceImpl.class);
  private static final String POINT = "point";
  private static final String LINESTRING = "linestring";
  private static final String POLYGON = "polygon";
  @Resource
  private LayerDao layerDao;
  @Resource
  private DropLayerDao dropLayerDao;
  @Resource
  private PointDao pointDao;
  @Resource
  private LineStringDao lineStringDao;
  @Resource
  private PolygonDao polygonDao;
  @Resource(name = "searchLayerTableDaoImpl")
  private SearchLayerTableDao searchLayerTableDao;
  @Resource(name = "createLayerTableDaoImpl")
  private CreatLayerTableDao createLayerTableDao;
  @Resource(name = "insertLayerDaoImpl")
  private FeatureDao featureDao;
  @Resource(name = "geoServerServiceImpl")
  private GeoServerService geoServerService;

  @Override
  @Transactional
  public Result insertLayer(Layer layer) {
    logger.info("insertLayer::layer = [{}], workspace = [{}]", layer);
    //在layer中插入新记录
    int i = layerDao.insertLayer(layer);
    if (i == 1) {
      return ResultUtil.success(layer);
    } else {
      logger.warn("保存出错" + layer.toString());
      return ResultUtil.error(2, "保存出错");
    }
  }

  @Override
  @Transactional
  public Result deleteLayer(List<Layer> layers) {
    logger.info("deleteLayer::layers = [{}]", layers);
    List<Layer> pointLayer = new ArrayList<>();
    List<Layer> lineStringLayer = new ArrayList<>();
    List<Layer> polygonLayer = new ArrayList<>();
    boolean point = false;
    boolean lineString = false;
    boolean polygon = false;
    for (int i = 0; i < layers.size(); i++) {
      if (POINT.equals(layers.get(i).getType().toLowerCase())) {
        pointLayer.add(layers.get(i));
      } else if (LINESTRING.equals(layers.get(i).getType().toLowerCase())) {
        lineStringLayer.add(layers.get(i));
      } else if (POLYGON.equals(layers.get(i).getType().toLowerCase())) {
        polygonLayer.add(layers.get(i));
      }
    }
    if (pointLayer.size() > 0) {
      int i = pointDao.deleteAllByLayerId(pointLayer);
      if (i >= 0) {
        int deletePoint = layerDao.deleteAll(pointLayer);
        if (pointLayer.size() != deletePoint) {

        } else {
          point = true;
        }
      }
    } else {
      point = true;
    }
    if (lineStringLayer.size() > 0) {
      int i = lineStringDao.deleteAllByLayerId(lineStringLayer);
      if (i >= 0) {
        int deleteLineString = layerDao.deleteAll(lineStringLayer);
        if (lineStringLayer.size() != deleteLineString) {

        } else {
          lineString = true;
        }
      }
    } else {
      lineString = true;
    }
    if (polygonLayer.size() > 0) {
      int i = polygonDao.deleteAllByLayerId(polygonLayer);
      if (i >= 0) {
        int deletePolygon = layerDao.deleteAll(polygonLayer);
        if (polygonLayer.size() != deletePolygon) {

        } else {
          polygon = true;
        }
      }
    } else {
      polygon = true;
    }
    if (point && lineString && polygon) {
      return ResultUtil.success();
    } else {
      return ResultUtil.error(1, "删除失败");
    }
  }

  @Override
  public Result searchLayer(String thematicName, String layerName) {
    logger.info("searchLayer::thematicName = [{}], layerName = [{}]", thematicName, layerName);
    RESTLayer layer = GeoServerUtils.manager.getReader().getLayer(thematicName, layerName);
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
  public Result insertFeatures(String layerName, String type, List features) {
    logger.info("insertFeatures::layerName = [{}], type = [{}], features = [{}]", layerName, type,
        features);
    if (features != null && StringUtils.isNotBlank(layerName) && StringUtils.isNotBlank(type)) {
      if (POINT.equals(type.toLowerCase())) {
        int i = pointDao.insertAllPoint(features);
        if (i > 0) {
          return ResultUtil.success();
        }
      } else if (LINESTRING.equals(type.toLowerCase())) {
        int i = lineStringDao.insertAllLineString(features);
        if (i > 0) {
          return ResultUtil.success();
        }
      } else if (POLYGON.equals(type.toLowerCase())) {
        int i = polygonDao.insertAllPolygon(features);
        if (i > 0) {
          return ResultUtil.success();
        }
      }
    }
    return ResultUtil.error("");
  }

  @Override
  public List<FirstClassification> findLayerByThematicIdAndNullUserId(String thematicId) {
    /*logger.info("findLayerByThematicIdAndNullUserId::thematicId = [{}]", thematicId);
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
            String secondClassId = UUID.randomUUID().toString().replaceAll("-","");
            secondClassification.setSecondClassificationId(secondClassId);
            secondClassification.setLayer(layer);
            secondClassifications.add(secondClassification);
          }
        }
        firstClassification.setFirstClassificationName(object);
        String firstClassId = UUID.randomUUID().toString().replaceAll("-","");
        firstClassification.setFirstClassificationId(firstClassId);
        firstClassification.setSecondClassifications(secondClassifications);
        firstClassifications.add(firstClassification);
      }
    }
    return firstClassifications;*/
    return null;
  }

  @Override
  public Boolean updateLayer(Layer layer) {
    logger.info("updateLayer::layer = [{}]", layer);
    int i = layerDao.updateLayer(layer);
    if (i > 0) {
      return true;
    } else {
      logger.error("更新失败" + i);
      return false;
    }
  }

  @Override
  public Layer findLayerByLayerId(String layerId) {
    return layerDao.findLayerByLayerId(layerId);
  }

  @Override
  public Layer findLayer(String userId, String layerName) {
    logger.info("findLayer::userId = [{}], layerName = [{}]", userId, layerName);
    return layerDao.findLayerByUserIdAndLayerName(userId, layerName);
  }

  @Override
  public Boolean deleteFeature(List features, String type) {
    logger.info("deleteFeature::features = [{}], type = [{}]", features, type);
    if (POINT.equals(type.toLowerCase())) {
      int i = pointDao.deleteAll(features);
      if (i > 0) {
        return true;
      }
    }
    if (LINESTRING.equals(type.toLowerCase())) {
      int i = lineStringDao.deleteAll(features);
      if (i > 0) {
        return true;
      }
    }
    if (POLYGON.equals(type.toLowerCase())) {
      int i = polygonDao.deleteAll(features);
      if (i > 0) {
        return true;
      }
    }
    return false;
  }

  @Override
  public List<Layer> findByUserId(String userId) {
    logger.info("findByUserId::userId = [{}]", userId);
    List<Layer> layers = layerDao.findByUserId(userId);
    if (layers.size() > 0) {
      return layers;
    }
    return null;
  }

  @Override
  public Result saveLayer(Layer layer) {
    Layer newLayer = new Layer();
    logger.info("saveLayer::layer = [{}]", layer);
    if (layer != null) {
      logger.info("saveLayer::layer值为：" + layer.toString());
      String layerName = layer.getLayerName();
      String userId = layer.getUserId();
      String type = layer.getType();
      layer.setPublishTime(new Date());
      layer.setUpdateTime(new Date());
      layer.setUploadTime(new Date());
      List<Point> pointList = layer.getPointList();
      List<LineString> lineStringList = layer.getLineStringList();
      List<Polygon> polygonList = layer.getPolygonList();
      Layer searchLayer = findLayer(userId, layerName);
        if (searchLayer != null) {
          newLayer.setLayerId(searchLayer.getLayerId());
          Result result = null;
          if (pointList != null && pointList.size() > 0) {
            Boolean point = deleteFeature(pointList, "point");
            logger.info("saveLayer::layer = [{}]" + point);
            for (Point point1 : pointList) {
              point1.setLayer(newLayer);
              point1.setUpdateTime(new Date());
              point1.setInsertTime(new Date());
            }
            result = insertFeatures(layerName, type, pointList);
          }
          if (lineStringList != null && lineStringList.size() > 0) {
            Boolean lineString = deleteFeature(lineStringList, "linestring");
            logger.info("deleteFeature(lineStringList, \"linestring\")" + lineString);
            for (LineString string : lineStringList) {
              string.setLayer(newLayer);
              string.setInsertTime(new Date());
              string.setUpdateTime(new Date());
            }
            result = insertFeatures(layerName, type, lineStringList);
          }
          if (polygonList != null && polygonList.size() > 0) {
            Boolean polygon = deleteFeature(polygonList, "polygon");
            logger.info("saveLayer::layer = [{}]" + polygon);
            for (Polygon polygon1 : polygonList) {
              polygon1.setLayer(newLayer);
              polygon1.setInsertTime(new Date());
              polygon1.setUpdateTime(new Date());
            }
            result = insertFeatures(layerName, type, polygonList);
          }
          assert result != null;
          if (result.getCode() == 0) {
            Layer newLayer1 = findLayerByLayerId(searchLayer.getLayerId());
            return ResultUtil.success(newLayer1);
          } else {
            logger.warn("警告:" + "参数异常");
            return ResultUtil.error(1, "参数异常");
          }
        } else {
          Result insertLayer = insertLayer(layer);
          if (insertLayer.getCode() == 0) {
            Layer newLayer1 = (Layer) insertLayer.getData();
            Result result = null;
            if (pointList != null && pointList.size() > 0) {
              Boolean point = deleteFeature(pointList, "point");
              logger.info("saveLayer::layer = [{}]" + point);
              for (Point point1 : pointList) {
                point1.setLayer(newLayer1);
                point1.setUpdateTime(new Date());
                point1.setInsertTime(new Date());
              }
              result = insertFeatures(layerName, type, pointList);
            }
            if (lineStringList != null && lineStringList.size() > 0) {
              Boolean lineString = deleteFeature(lineStringList, "linestring");
              logger.info("saveLayer::layer = [{}]" + lineString);
              for (LineString lineString1 : lineStringList) {
                lineString1.setLayer(newLayer1);
                lineString1.setUpdateTime(new Date());
                lineString1.setInsertTime(new Date());
              }
              result = insertFeatures(layerName, type, lineStringList);
            }
            if (polygonList != null && polygonList.size() > 0) {
              Boolean polygon = deleteFeature(polygonList, "polygon");
              logger.info("saveLayer::layer = [{}]" + polygon);
              for (Polygon polygon1 : polygonList) {
                polygon1.setLayer(newLayer1);
                polygon1.setUpdateTime(new Date());
                polygon1.setInsertTime(new Date());
              }
              result = insertFeatures(layerName, type, polygonList);
            }
            if (result!=null&&result.getCode() == 0) {
              Layer newLayer2 = findLayerByLayerId(newLayer1.getLayerId());
              return ResultUtil.success(newLayer2);
            } else if (result==null){
             return insertLayer;
            }else {
              logger.warn("警告:" + result.getMsg());
              return ResultUtil.error(1, result.getMsg());
            }
          } else {
            logger.warn("警告：saveLayer::jsonObject = [{}]", insertLayer.getMsg());
            return insertLayer;
          }
        }
    } else {
      logger.warn("图层参数为null:" + layer);
      return ResultUtil.error(2, "jsonObject为空");
    }
  }

  private Result publishLayer(String workspace, String store, String layerName) {
    logger.info("publishLayer::workspace = [{}], store = [{}], layerName = [{}]", workspace, store,
        layerName);
    RESTLayer layer = GeoServerUtils.manager.getReader().getLayer(workspace, layerName);
    if (layer == null) {
      Result result = geoServerService.publishLayer(workspace, store, layerName);
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
