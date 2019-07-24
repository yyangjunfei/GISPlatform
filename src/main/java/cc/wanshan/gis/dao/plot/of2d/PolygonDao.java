package cc.wanshan.gis.dao.plot.of2d;

import cc.wanshan.gis.entity.layer.provider.PolygonDaoProvider;
import cc.wanshan.gis.entity.plot.of2d.Layer;
import cc.wanshan.gis.entity.plot.of2d.Polygon;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Li Cheng
 * @date 2019/6/4 11:38
 */
@Mapper
@Component
public interface PolygonDao {

    /**
     * description: 批量插入点元素
     *
     * @return int
     **/
    @InsertProvider(type = PolygonDaoProvider.class, method = "insertAll")
    int insertAllPolygon(@Param("list") List<Polygon> polygonList);


    /**
     * description: 根据图层Id查询元素
     *
     * @param layerId 图层Id
     * @return java.util.List<cc.wanshan.gis.entity.drawlayer.Polygon>
     **/
    @SelectProvider(type = PolygonDaoProvider.class, method = "findByLayerId")
    @Results({
            @Result(id = true, column = "feature_id", property = "featureId"),
            @Result(column = "feature_name", property = "featureName"),
            @Result(column = "ST_AsGeoJSON", property = "geom"),
            @Result(column = "circle", property = "circle"),
            @Result(column = "feature_class", property = "featureClass"),
            @Result(column = "epsg", property = "epsg"),
            @Result(column = "describe", property = "describe"),
            @Result(column = "fill_color", property = "fillColor"),
            @Result(column = "stroke_color", property = "strokeColor"),
            @Result(column = "stroke_width", property = "strokeWidth"),
            @Result(column = "opacity", property = "opacity"),
    })
    List<Polygon> findPolygonByLayerId(@Param("layerId") String layerId);


    /**
     * description:
     *
     * @param layerId      图层名
     * @param featureName  元素名
     * @param featureId    元素Id
     * @param featureClass 元素类别
     * @return java.util.List<cc.wanshan.gis.entity.drawlayer.Polygon>
     **/
    @SelectProvider(type = PolygonDaoProvider.class, method = "select")
    @Results({
            @Result(id = true, column = "feature_id", property = "featureId"),
            @Result(column = "feature_name", property = "featureName"),
            @Result(column = "ST_AsGeoJSON", property = "geom"),
            @Result(column = "circle", property = "circle"),
            @Result(column = "feature_class", property = "featureClass"),
            @Result(column = "epsg", property = "epsg"),
            @Result(column = "describe", property = "describe"),
            @Result(column = "fill_color", property = "fillColor"),
            @Result(column = "stroke_color", property = "strokeColor"),
            @Result(column = "stroke_width", property = "strokeWidth"),
            @Result(column = "opacity", property = "opacity"),
    })
    List<Polygon> findPolygon(@Param("layerId") String layerId,
                              @Param("featureName") String featureName, @Param("featureId") String featureId,
                              @Param("featureClass") String featureClass);


    /**
     * description: 根据条件删除
     *
     * @param layerId      图层Id
     * @param featureId    元素Id
     * @param featureName  元素名
     * @param featureClass 元素类别
     * @return int 返回值为0或1
     **/
    @DeleteProvider(type = PolygonDaoProvider.class, method = "delete")
    int delete(@Param("layerId") String layerId, @Param("featureId") String featureId,
               @Param("featureName") String featureName, @Param("featureClass") String featureClass);


    /**
     * description: 根据feature批量删除
     *
     * @param list featureId集合
     * @return int
     **/
    @DeleteProvider(type = PolygonDaoProvider.class, method = "deleteAll")
    int deleteAll(@Param("list") List<Polygon> list);


    /**
     * description: 根据layerId批量删除
     *
     * @param list 图层集合
     * @return int
     **/
    @DeleteProvider(type = PolygonDaoProvider.class, method = "deleteAllByLayerId")
    int deleteAllByLayerId(@Param("list") List<Layer> list);
}
