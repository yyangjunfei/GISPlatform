package cc.wanshan.gis.dao.plot.of2d;

import cc.wanshan.gis.entity.layer.provider.PointDaoProvider;
import cc.wanshan.gis.entity.plot.of2d.Layer;
import cc.wanshan.gis.entity.plot.of2d.Point;
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
 * @date 2019/6/3 11:38
 */
@Mapper
@Component
public interface PointDao {

    @InsertProvider(type = PointDaoProvider.class, method = "insertAll")
    /**
     * description: 批量插入点元素
     *
     * @param pointList
     * @return int
     **/
    public int insertAllPoint(@Param("list") List<Point> pointList);

    @SelectProvider(type = PointDaoProvider.class, method = "select")
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
     * @return java.util.List<cc.wanshan.gis.entity.plot.of2d.Point>
     **/
    public List<Point> findPoint(@Param("layerId") String layerId,
                                 @Param("featureName") String featureName, @Param("featureId") String featureId,
                                 @Param("featureClass") String featureClass);

    @SelectProvider(type = PointDaoProvider.class, method = "findByLayerId")
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
     * @return java.util.List<cc.wanshan.gis.entity.plot.of2d.Point>
     **/
    public List<Point> findPointByLayerId(@Param("layerId") String layerId);

    @DeleteProvider(type = PointDaoProvider.class, method = "delete")
    /**
     * description: 根据条件删除
     *
     * @param layerId 图层Id
     * @param featureId 元素Id
     * @param featureName 元素名
     * @param featureClass 元素类别
     * @return int 返回值为0或1
     **/
    public int delete(@Param("layerId") String layerId, @Param("featureId") String featureId,
                      @Param("featureName") String featureName, @Param("featureClass") String featureClass);

    @DeleteProvider(type = PointDaoProvider.class, method = "deleteAll")
    /**
     * description: 根据feature批量删除
     *
     * @param list featureId集合
     * @return int
     **/
    public int deleteAll(@Param("list") List<Point> list);

    @DeleteProvider(type = PointDaoProvider.class, method = "deleteAllByLayerId")
    /**
     * description: 根据layerId批量删除
     *
     * @param list 图层集合
     * @return int
     **/
    public int deleteAllByLayerId(@Param("list") List<Layer> list);
}
