package cc.wanshan.gis.dao.plot.of2d;

import cc.wanshan.gis.entity.layer.provider.LineStringDaoProvider;
import cc.wanshan.gis.entity.plot.of2d.Layer;
import cc.wanshan.gis.entity.plot.of2d.LineString;
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
 * @date 2019/6/4 11:36
 */
@Mapper
@Component
public interface LineStringDao {


    /**
     * description: 批量插入点元素
     *
     * @return int
     **/
    @InsertProvider(type = LineStringDaoProvider.class, method = "insertAll")
    int insertAllLineString(@Param("list") List<LineString> lineStringList);


    /**
     * description:
     *
     * @param layerId      图层名
     * @param featureName  元素名
     * @param featureId    元素Id
     * @param featureClass 元素类别
     * @return java.util.List<cc.wanshan.gis.entity.drawlayer.LineString>
     **/
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
    List<LineString> findLineString(@Param("layerId") String layerId,
                                    @Param("featureName") String featureName, @Param("featureId") String featureId,
                                    @Param("featureClass") String featureClass);

    /**
     * description: 根据图层Id查询feature
     *
     * @param layerId 图层Id
     * @return java.util.List<cc.wanshan.gis.entity.drawlayer.LineString>
     **/
    @SelectProvider(type = LineStringDaoProvider.class, method = "findByLayerId")
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
    List<LineString> findLineStringByLayerId(@Param("layerId") String layerId);

    /**
     * description: 根据条件删除
     *
     * @param layerId      图层Id
     * @param featureId    元素Id
     * @param featureName  元素名
     * @param featureClass 元素类别
     * @return int 返回值为0或1
     **/
    @DeleteProvider(type = LineStringDaoProvider.class, method = "delete")
    int delete(@Param("layerId") String layerId, @Param("featureId") String featureId,
               @Param("featureName") String featureName, @Param("featureClass") String featureClass);

    /**
     * description: 根据feature批量删除
     *
     * @param list featureId集合
     * @return int
     **/
    @DeleteProvider(type = LineStringDaoProvider.class, method = "deleteAll")
    int deleteAll(@Param("list") List<LineString> list);

    /**
     * description: 根据layerId批量删除
     *
     * @param list 图层集合
     * @return int
     **/
    @DeleteProvider(type = LineStringDaoProvider.class, method = "deleteAllByLayerId")
    int deleteAllByLayerId(@Param("list") List<Layer> list);

}
