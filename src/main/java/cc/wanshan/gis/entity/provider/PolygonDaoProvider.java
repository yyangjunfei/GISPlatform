package cc.wanshan.gis.entity.provider;

import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.drawlayer.Polygon;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Li Cheng
 * @date 2019/6/4 11:32
 */

public class PolygonDaoProvider {

  private static final Logger logger = LoggerFactory.getLogger(PolygonDaoProvider.class);

  /**
   * description: 批量插入polygon
   *
   * @param map 封装polygonlist集合
   * @return java.lang.String
   **/
  public String insertAll(Map map) {
    logger.info("insertAll::map = [{}]", map);
    List<Polygon> list = (List<Polygon>) map.get("list");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("INSERT INTO polygon ");
    stringBuilder.append("(feature_name,"
        + "layer_id,"
        + "feature_class,"
        + "geom,"
        + "circle,"
        + "insert_time,"
        + "update_time,"
        + "epsg,"
        + "describe,"
        + "fill_color,"
        + "stroke_color,"
        + "stroke_width,"
        + "opacity )");
    stringBuilder.append("VALUES ");
    MessageFormat messageFormat = new MessageFormat(
        "(#'{'list[{0}].featureName},#'{'list[{0}].layer.layerId},#'{'list[{0}].featureClass},st_geomfromgeojson(#'{'list[{0}].geom}),#'{'list[{0}].circle},#'{'list[{0}].insertTime,jdbcType=TIMESTAMP},#'{'list[{0}].updateTime,jdbcType=TIMESTAMP},#'{'list[{0}].epsg},#'{'list[{0}].describe},#'{'list[{0}].fillColor},#'{'list[{0}].strokeColor},#'{'list[{0}].strokeWidth},#'{'list[{0}].opacity})");
    for (int i = 0; i < list.size(); i++) {
      stringBuilder.append(messageFormat.format(new Object[]{i}));
      if (i < list.size() - 1) {
        stringBuilder.append(",");
      }
    }
    logger.info("stringBuilder::" + stringBuilder.toString());
    return stringBuilder.toString();
  }

  /**
   * description: 批量更新polygon
   *
   * @param map 封装polygonlist集合
   * @return java.lang.String
   **/
  public String updateAll(Map map) {
    logger.info("updateAll::map = [{}]", map);
    List<Polygon> list = (List<Polygon>) map.get("list");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("INSERT INTO polygon ");
    stringBuilder.append("(feature_id,"
        + "feature_name,"
        + "layer_id,"
        + "feature_class,"
        + "geom,"
        + "insert_time,"
        + "update_time,"
        + "epsg,"
        + "describe,"
        + "fill_color,"
        + "stroke_color,"
        + "stroke_width,"
        + "opacity )");
    stringBuilder.append("VALUES ");
    MessageFormat messageFormat = new MessageFormat(
        "(#'{'list[{0}].featureId},#'{'list[{0}].featureName},#'{'list[{0}].layer.layerId},#'{'list[{0}].featureClass},st_geomfromgeojson(#'{'list[{0}].geom}),#'{'list[{0}].insertTime,jdbcType=TIMESTAMP},#'{'list[{0}].updateTime,jdbcType=TIMESTAMP},#'{'list[{0}].epsg},#'{'list[{0}].describe},#'{'list[{0}].fillColor},#'{'list[{0}].strokeColor},#'{'list[{0}].strokeWidth},#'{'list[{0}].opacity})");
    for (int i = 0; i < list.size(); i++) {
      stringBuilder.append(messageFormat.format(new Object[]{i}));
      if (i < list.size() - 1) {
        stringBuilder.append(",");
      }
    }
    stringBuilder.append("ON CONFLICT (feature_id) DO UPDATE SET ");
    MessageFormat messageFormat1 = new MessageFormat(
        "feature_name=#'{'list[{0}].featureName},feature_class=#'{'list[{0}].featureClass},layer_id=#'{'list[{0}].layer.layerId},geom=st_geomfromgeojson(#'{'list[{0}].geom}),update_time=#'{'list[{0}].updateTime,jdbcType=TIMESTAMP},epsg=#'{'list[{0}].epsg},describe=#'{'list[{0}].describe},fill_color=#'{'list[{0}].fillColor},stroke_color=#'{'list[{0}].strokeColor},stroke_width=#'{'list[{0}].strokeWidth},opacity=#'{'list[{0}].opacity} ");
    for (int i = 0; i < list.size(); i++) {
      stringBuilder.append(messageFormat1.format(new Object[]{i}));
      if (i < list.size() - 1) {
        stringBuilder.append(",");
      }
    }
    logger.info("stringBuilder::" + stringBuilder.toString());
    return stringBuilder.toString();
  }

  /**
   * description: 根据条件动态构建sql查询
   *
   * @param layerId 图层id
   * @param featureName 元素名
   * @param featureId 元素Id
   * @param featureClass 元素类别
   * @return java.lang.String
   **/
  public String select(@Param("layerId") String layerId, @Param("featureName") String featureName,
      @Param("featureId") String featureId, @Param("featureClass") String featureClass) {
    logger.info("select::layerId = [{}], featureName = [{}], featureId = [{}], featureClass = [{}]",
        layerId, featureName, featureId, featureClass);
    return new SQL() {
      {
        SELECT(
            "p.feature_id,p.feature_name,p.feature_class,ST_AsGeoJSON(p.geom),p.circle,p.insert_time,p.update_time,p.epsg,p.describe,p.fill_color,p.stroke_color,p.stroke_width,p.opacity ");
        FROM("public.polygon p");
        WHERE("1=1");
        if (StringUtils.isNotBlank(layerId)) {
          WHERE("p.layer_id=#{layerId}");
        }
        if (StringUtils.isNotBlank(featureName)) {
          WHERE("p.feature_name=#{featureName}");
        }
        if (StringUtils.isNotBlank(featureId)) {
          WHERE("p.feature_id=#{featureId}");
        }
        if (StringUtils.isNotBlank(featureClass)) {
          WHERE("p.feature_class=#{featureClass}");
        }
      }
    }.toString();
  }

  /**
   * description: 根据图层Id查询元素
   *
   * @param layerId 图层id
   * @return java.lang.String
   **/
  public String findByLayerId(@Param("layerId") String layerId) {
    logger.info("findByLayerId::layerId = [{}]", layerId);
    return new SQL() {
      {
        SELECT(
            "p.feature_id,p.feature_name,p.feature_class,ST_AsGeoJSON(p.geom),p.circle,p.epsg,p.describe,p.fill_color,p.stroke_color,p.stroke_width,p.opacity ");
        FROM("public.polygon p");
        WHERE("1=1");
        if (StringUtils.isNotBlank(layerId)) {
          WHERE("p.layer_id=#{layerId}");
        }
      }
    }.toString();
  }

  /**
   * description: 根据条件动态构建删除sql
   *
   * @param layerId 图层id
   * @param featureId 元素id
   * @param featureName 元素名
   * @param featureClass 元素类别
   * @return java.lang.String
   **/
  public String delete(@Param("layerId") String layerId, @Param("featureId") String featureId,
      @Param("featureName") String featureName, @Param("featureClass") String featureClass) {
    logger.info("delete::layerId = [{}], featureId = [{}], featureName = [{}], featureClass = [{}]",
        layerId, featureId, featureName, featureClass);
    return new SQL() {
      {
        DELETE_FROM("polygon p");
        if (StringUtils.isNotBlank(layerId)) {
          WHERE("p.layer_id=#{layerId}");
        }
        if (StringUtils.isNotBlank(featureName)) {
          WHERE("p.feature_name=#{featureName}");
        }
        if (StringUtils.isNotBlank(featureId)) {
          WHERE("p.feature_id=#{featureId}");
        }
        if (StringUtils.isNotBlank(featureClass)) {
          WHERE("p.feature_class=#{featureClass}");
        }
      }
    }.toString();
  }

  /**
   * description: 根据featureId批量删除
   *
   * @param map featureId集合
   * @return java.lang.String
   **/
  public String deleteAll(Map map) {
    logger.info("deleteAll::map = [{}]", map);
    List<Polygon> list = (List<Polygon>) map.get("list");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("DELETE FROM polygon WHERE feature_id in (");
    for (int i = 0; i < list.size(); i++) {
      stringBuilder.append("'").append(list.get(i).getFeatureId()).append("'");
      if (i < list.size() - 1) {
        stringBuilder.append(",");
      }
    }
    stringBuilder.append(")");
    logger.info("stringBuilder.toString()::" + stringBuilder.toString());
    return stringBuilder.toString();
  }

  /**
   * description: 根据layerId批量删除
   *
   * @param map layerId集合
   * @return java.lang.String
   **/
  public String deleteAllByLayerId(Map map) {
    logger.info("deleteAllByLayerId::map = [{}]", map);
    List<Layer> list = (List<Layer>) map.get("list");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("DELETE FROM Polygon WHERE layer_id in (");
    for (int i = 0; i < list.size(); i++) {
      stringBuilder.append("'").append(list.get(i).getLayerId()).append("'");
      if (i < list.size() - 1) {
        stringBuilder.append(",");
      }
    }
    stringBuilder.append(")");
    logger.info("stringBuilder.toString()::" + stringBuilder.toString());
    return stringBuilder.toString();
  }
}
