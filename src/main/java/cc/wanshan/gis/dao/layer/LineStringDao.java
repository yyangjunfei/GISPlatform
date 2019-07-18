package cc.wanshan.gis.dao.layer;

import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.drawlayer.LineString;
import cc.wanshan.gis.entity.provider.LineStringDaoProvider;
import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

/**
 * @author Li Cheng
 * @date 2019/6/4 11:36
 */
@Mapper
@Component
public interface LineStringDao {

  @InsertProvider(type = LineStringDaoProvider.class, method = "insertAll")
  /**
   * description: 批量插入点元素
   *
   * @param LineStringList
   * @return int
   **/
   int insertAllLineString(@Param("list") List<LineString> LineStringList);

  @SelectProvider(type = LineStringDaoProvider.class, method = "select")
  @Results({
      @Result(id = true, column = "feature_id", property = "featureId"),
      @Result(column = "feature_name", property = "featureName"),
      @Result(column = "ST_AsGeoJSON", property = "geom"),
      @Result(column = "feature_class", property = "featureClass"),
      @Result(column = "epsg", property = "epsg"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "fill_color", property = "fillColor"),
      @Result(column = "stroke_color", property = "strokeColor"),
      @Result(column = "stroke_width", property = "strokeWidth"),
      @Result(column = "opacity", property = "opacity"),
  })
  /**
   * description:
   *
   * @param layerId 图层名
   * @param featureName 元素名
   * @param featureId 元素Id
   * @param featureClass 元素类别
   * @return java.util.List<cc.wanshan.gis.entity.drawlayer.LineString>
   **/
   List<LineString> findLineString(@Param("layerId") String layerId,
      @Param("featureName") String featureName, @Param("featureId") String featureId,
      @Param("featureClass") String featureClass);
  @SelectProvider(type = LineStringDaoProvider.class,method = "findByLayerId")
  @Results({
      @Result(id = true, column = "feature_id", property = "featureId"),
      @Result(column = "feature_name", property = "featureName"),
      @Result(column = "ST_AsGeoJSON", property = "geom"),
      @Result(column = "feature_class", property = "featureClass"),
      @Result(column = "epsg", property = "epsg"),
      @Result(column = "describe", property = "describe"),
      @Result(column = "fill_color", property = "fillColor"),
      @Result(column = "stroke_color", property = "strokeColor"),
      @Result(column = "stroke_width", property = "strokeWidth"),
      @Result(column = "opacity", property = "opacity"),
  })
  /**
   * description: 根据图层Id查询feature
   *
   * @param layerId 图层Id
   * @return java.util.List<cc.wanshan.gis.entity.drawlayer.LineString>
   **/
   List<LineString> findLineStringByLayerId(@Param("layerId") String layerId);
  @DeleteProvider(type = LineStringDaoProvider.class,method ="delete")
  /**
   * description: 根据条件删除
   *
   * @param layerId 图层Id
   * @param featureId 元素Id
   * @param featureName 元素名
   * @param featureClass 元素类别
   * @return int 返回值为0或1
   **/
   int delete(@Param("layerId") String layerId, @Param("featureId") String featureId,
      @Param("featureName") String featureName, @Param("featureClass") String featureClass);
  @DeleteProvider(type = LineStringDaoProvider.class,method = "deleteAll")
  /**
   * description: 根据feature批量删除
   *
   * @param list featureId集合
   * @return int
   **/
   int deleteAll(@Param("list") List<LineString> list);
  @DeleteProvider(type = LineStringDaoProvider.class,method = "deleteAllByLayerId")
  /**
   * description: 根据layerId批量删除
   *
   * @param list 图层集合
   * @return int
   **/
   int deleteAllByLayerId(@Param("list") List<Layer> list);

}
