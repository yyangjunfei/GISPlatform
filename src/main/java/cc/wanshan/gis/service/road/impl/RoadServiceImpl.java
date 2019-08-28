package cc.wanshan.gis.service.road.impl;

import cc.wanshan.gis.common.enums.ResultCode;
import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.dao.road.RoadDao;
import cc.wanshan.gis.entity.road.Road;
import cc.wanshan.gis.entity.road.Stations;
import cc.wanshan.gis.service.road.RoadService;
import cc.wanshan.gis.utils.base.ResultUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Geometry;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "roadServiceImpl")
/**
 * @author Li Cheng
 * @date 2019/8/14 8:42
 */
public class RoadServiceImpl implements RoadService {

  private static final Logger logger = LoggerFactory.getLogger(RoadServiceImpl.class);
  @Resource
  private RoadDao roadDao;

  @Override
  public Integer findSource(String sourcePoint) {
    logger.info("findSource::sourcePoint = [{}]", sourcePoint);
    Road source = roadDao.findSource(sourcePoint);
    if (source != null) {
      return source.getSource();
    }
    return null;
  }

  @Override
  public Integer findTarget(String targetPoint) {
    logger.info("findTarget::targetPoint = [{}]", targetPoint);
    Road target = roadDao.findTarget(targetPoint);
    if (target != null) {
      return target.getTarget();
    }
    return null;
  }

  @Override
  public Stations findStation(String point) {
    logger.info("findStation::point = [{}]", point);
    if (StringUtils.isBlank(point)) {
      return null;
    }
    Stations station = roadDao.findStation(point);
    if (station != null) {
      return station;
    }
    return null;
  }

  @Override
  public Result findRoad(String sourcePoint, String targetPoint) {
    logger.info("findRoad::sourcePoint = [{}], targetPoint = [{}]", sourcePoint, targetPoint);
    if (StringUtils.isBlank(sourcePoint) || StringUtils.isBlank(targetPoint)) {
      return ResultUtil.error(ResultCode.PARAM_IS_NULL);
    }
    Stations sourceStation = findStation(sourcePoint);
    Stations targetStation = findStation(targetPoint);
    Integer source = findSource(sourceStation.getGeom());
    Integer target = findSource(targetStation.getGeom());
    int update = roadDao.update(source, target);
    logger.info("update::" + update);
    String road = roadDao.findRoad();
    if (StringUtils.isNotBlank(road)) {
      return ResultUtil.success(textToJSON(road));
    }
    return ResultUtil.error(ResultCode.FIND_NULL);
  }

  private JSONObject textToJSON(String road) {
    SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
    simpleFeatureTypeBuilder.setName("testType");
    simpleFeatureTypeBuilder.crs(DefaultGeographicCRS.WGS84);
    simpleFeatureTypeBuilder.add("geom", Geometry.class);
    simpleFeatureTypeBuilder.add("name", String.class);
    SimpleFeatureType simpleFeatureType = simpleFeatureTypeBuilder.buildFeatureType();
    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(simpleFeatureType);
    FeatureJSON fjson = new FeatureJSON(new GeometryJSON(15));
    List<SimpleFeature> features = new ArrayList<>();
    SimpleFeatureCollection collection = new ListFeatureCollection(simpleFeatureType, features);
    featureBuilder.add(road);
    featureBuilder.add("road");
    SimpleFeature feature = featureBuilder.buildFeature(null);
    logger.info("featureBuilder.buildFeature(null)" + feature);
    features.add(feature);
    StringWriter writer = new StringWriter();
    try {
      fjson.writeFeatureCollection(collection, writer);
      return JSON.parseObject(writer.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
