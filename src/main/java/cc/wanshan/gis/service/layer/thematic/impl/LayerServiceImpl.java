package cc.wanshan.gis.service.layer.thematic.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.layer.CreatLayerTableDao;
import cc.wanshan.gis.dao.layer.DropLayerDao;
import cc.wanshan.gis.dao.layer.FeatureDao;
import cc.wanshan.gis.dao.layer.LayerDao;
import cc.wanshan.gis.dao.layer.SearchLayerTableDao;
import cc.wanshan.gis.dao.plot.of2d.LineStringDao;
import cc.wanshan.gis.dao.plot.of2d.PointDao;
import cc.wanshan.gis.dao.plot.of2d.PolygonDao;
import cc.wanshan.gis.entity.plot.of2d.Layer;
import cc.wanshan.gis.entity.plot.of2d.LineString;
import cc.wanshan.gis.entity.plot.of2d.Point;
import cc.wanshan.gis.entity.plot.of2d.Polygon;
import cc.wanshan.gis.service.layer.geoserver.GeoServerService;
import cc.wanshan.gis.service.layer.thematic.LayerService;
import cc.wanshan.gis.utils.base.ResultUtil;
import cc.wanshan.gis.utils.geo.GeoServerUtils;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
  public Result delete(List<Layer> layers) {
    logger.info("deleteLayer::layers = [{}]", layers);
    if (layers == null || layers.size() == 0) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
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
  public Result update(Layer layer) {
    logger.info("updateLayer::layer = [{}]", layer);
    if (layer == null) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    layer.setUpdateTime(new Date());
    int i = layerDao.updateLayer(layer);
    if (i > 0) {
      return ResultUtil.success();
    } else {
      logger.error("更新失败" + i);
      return ResultUtil.error(ResultCode.UPDATE_FAIL);
    }
  }


  @Override
  public Result findByUserIdAndLayerName(String userId, String layerName) {
    logger.info("findLayer::userId = [{}], layerName = [{}]", userId, layerName);
    if (StringUtils.isBlank(userId) && StringUtils.isBlank(layerName)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    Layer layer = layerDao.findLayerByUserIdAndLayerName(userId, layerName);
    if (layer != null) {
      return ResultUtil.success(layer);
    }
    return ResultUtil.error(ResultCode.FIND_NULL);
  }

  @Override
  public Result findByLayerId(String layerId) {
    logger.info("findByLayerId::layerId = [{}]", layerId);
    if (StringUtils.isBlank(layerId)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    Layer layer = layerDao.findLayerByLayerId(layerId);
    if (layer != null) {
      return ResultUtil.success(layer);
    }
    return ResultUtil.error(ResultCode.FIND_NULL);
  }

  @Override
  public Result deleteFeature(List features, String type) {
    logger.info("deleteFeature::features = [{}], type = [{}]", features, type);
    if (features == null && StringUtils.isBlank(type)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    if (POINT.equals(type.toLowerCase())) {
      int i = pointDao.deleteAll(features);
      if (i > 0) {
        return ResultUtil.success();
      }
    }
    if (LINESTRING.equals(type.toLowerCase())) {
      int i = lineStringDao.deleteAll(features);
      if (i > 0) {
        return ResultUtil.success();
      }
    }
    if (POLYGON.equals(type.toLowerCase())) {
      int i = polygonDao.deleteAll(features);
      if (i > 0) {
        return ResultUtil.success();
      }
    }
    return ResultUtil.error(ResultCode.DELETE_FAIL);
  }

  private Boolean deleteFeatures(List features, String type) {
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
  public Result findByUserId(String userId) {
    logger.info("findByUserId::userId = [{}]", userId);
    if (StringUtils.isBlank(userId)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    List<Layer> layers = layerDao.findByUserId(userId);
    if (layers != null) {
      return ResultUtil.success(layers);
    }
    return ResultUtil.error(ResultCode.FIND_NULL);
  }
  @Override
  public Result save(Layer layer) {
    Layer newLayer = new Layer();
    logger.info("saveLayer::layer = [{}]", layer);
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
      Layer searchLayer = layerDao.findLayerByUserIdAndLayerName(userId, layerName);
      if (searchLayer != null) {
        newLayer.setLayerId(searchLayer.getLayerId());
        Result result = null;
        if (pointList != null && pointList.size() > 0) {
          Boolean point = deleteFeatures(pointList, "point");
          logger.info("saveLayer::layer = [{}]" + point);
          for (Point point1 : pointList) {
            point1.setLayer(newLayer);
            point1.setUpdateTime(new Date());
            point1.setInsertTime(new Date());
          }
          result = insertFeatures(layerName, type, pointList);
        }
        if (lineStringList != null && lineStringList.size() > 0) {
          Boolean lineString = deleteFeatures(lineStringList, "linestring");
          logger.info("deleteFeature(lineStringList, \"linestring\")" + lineString);
          for (LineString string : lineStringList) {
            string.setLayer(newLayer);
            string.setInsertTime(new Date());
            string.setUpdateTime(new Date());
          }
          result = insertFeatures(layerName, type, lineStringList);
        }
        if (polygonList != null && polygonList.size() > 0) {
          Boolean polygon = deleteFeatures(polygonList, "polygon");
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
          Layer newLayer1 = layerDao.findLayerByLayerId(searchLayer.getLayerId());
          return ResultUtil.success(newLayer1);
        } else {
          logger.warn("警告:" + "参数异常");
          return ResultUtil.error(1, "参数异常");
        }
      } else {
        int insert = layerDao.insert(layer);
        if (insert>0) {
          Result result = null;
          if (pointList != null && pointList.size() > 0) {
            Boolean point = deleteFeatures(pointList, "point");
            logger.info("saveLayer::layer = [{}]" + point);
            for (Point point1 : pointList) {
              point1.setLayer(layer);
              point1.setUpdateTime(new Date());
              point1.setInsertTime(new Date());
            }
            result = insertFeatures(layerName, type, pointList);
          }
          if (lineStringList != null && lineStringList.size() > 0) {
            Boolean lineString = deleteFeatures(lineStringList, "linestring");
            logger.info("saveLayer::layer = [{}]" + lineString);
            for (LineString lineString1 : lineStringList) {
              lineString1.setLayer(layer);
              lineString1.setUpdateTime(new Date());
              lineString1.setInsertTime(new Date());
            }
            result = insertFeatures(layerName, type, lineStringList);
          }
          if (polygonList != null && polygonList.size() > 0) {
            Boolean polygon = deleteFeatures(polygonList, "polygon");
            logger.info("saveLayer::layer = [{}]" + polygon);
            for (Polygon polygon1 : polygonList) {
              polygon1.setLayer(layer);
              polygon1.setUpdateTime(new Date());
              polygon1.setInsertTime(new Date());
            }
            result = insertFeatures(layerName, type, polygonList);
          }
          if (result==null){
            return ResultUtil.error(ResultCode.SAVE_FAIL);
          }
          if (result.getCode().equals(ResultCode.SUCCESS.code())) {
            Layer newLayer2 = layerDao.findLayerByLayerId(layer.getLayerId());
            return ResultUtil.success(newLayer2);
          }
          return result;
        }
        return ResultUtil.error(ResultCode.SAVE_FAIL);
      }
  }


  private Result publishLayer(String workspace, String store, String layerName,
      String defaultStyle) {
    logger.info("publishLayer::workspace = [{}], store = [{}], layerName = [{}]", workspace, store,
        layerName);
    RESTLayer layer = GeoServerUtils.manager.getReader().getLayer(workspace, layerName);
    if (layer == null) {
      Result result = geoServerService.publishLayer(workspace, store, layerName, defaultStyle);
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
