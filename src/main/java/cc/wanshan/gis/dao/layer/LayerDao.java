package cc.wanshan.gis.dao.layer;

import cc.wanshan.gis.entity.layer.provider.LayerDaoProvider;
import cc.wanshan.gis.entity.plot.of2d.Layer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Li Cheng
 * @Date 17:32 2019/3/20
 **/
@Mapper
@Component
public interface LayerDao {

    @Insert({
            "insert into "
                    + "layer ("
                    + "layer_name,"
                    + "layer_name_zh,"
                    + "store_id,"
                    + "user_id,"
                    + "thematic_id,"
                    + "thematic_name,"
                    + "thematic_name_zh,"
                    + "first_classification_name,"
                    + "first_classification_id,"
                    + "second_classification_name,"
                    + "second_classification_id,"
                    + "security,"
                    + "publish_time,"
                    + "upload_time,"
                    + "update_time,"
                    + "type,"
                    + "epsg,"
                    + "department,"
                    + "describe,"
                    + "fill_color,"
                    + "stroke_color,"
                    + "stroke_width,"
                    + "opacity "
                    + ") values ("
                    + "#{layerName},"
                    + "#{layerNameZH},"
                    + "#{store.storeId},"
                    + "#{userId},"
                    + "#{thematic.thematicId},"
                    + "#{thematic.thematicName},"
                    + "#{thematic.thematicNameZH},"
                    + "#{firstClassificationName},"
                    + "#{firstClassificationId},"
                    + "#{secondClassificationName},"
                    + "#{secondClassificationId},"
                    + "#{security},"
                    + "#{publishTime,jdbcType=TIMESTAMP},"
                    + "#{uploadTime,jdbcType=TIMESTAMP},"
                    + "#{updateTime,jdbcType=TIMESTAMP},"
                    + "#{type},"
                    + "#{epsg},"
                    + "#{department},"
                    + "#{describe},"
                    + "#{fillColor},"
                    + "#{strokeColor},"
                    + "#{strokeWidth},"
                    + "#{opacity} "
                    + ")"
    })
    @Options(useGeneratedKeys = true, keyColumn = "layer_id", keyProperty = "layerId")
    /**
     * description: 新增图层
     *
     * @param layer
     * @return int
     */
    int insertLayer(Layer layer);

    @Delete({
            "delete from "
                    + "layer "
                    + "where "
                    + "layer_name=#{layerName} and "
                    + "store_id=#{storeId}"
    })
    /**
     * description: 根据storeId和图层名删除图层
     *
     * @param layerName
     * @param storeId
     * @return int
     */
    int deleteLayerByLayerNameAndStoreId(String layerName, String storeId);

    @DeleteProvider(type = LayerDaoProvider.class, method = "deleteAll")
    /**
     * description: 根据layerId批量删除
     *
     * @param layers 图层集合
     * @return int
     **/
    int deleteAll(@Param("list") List<Layer> layers);

    @Update({
            "update "
                    + "layer "
                    + "set "
                    + "layer_name_zh=#{layerNameZH},"
                    + "store_id=#{store.storeId},"
                    + "user_id=#{userId},"
                    + "thematic_id=#{thematic.thematicId},"
                    + "thematic_name=#{thematic.thematicName},"
                    + "thematic_name_zh=#{thematic.thematicNameZH},"
                    + "first_classification_name=#{firstClassificationName},"
                    + "first_classification_id=#{firstClassificationId},"
                    + "second_classification_id=#{secondClassificationId},"
                    + "second_classification_name=#{secondClassificationName},"
                    + "security=#{security},"
                    + "update_time=#{updateTime,jdbcType=TIMESTAMP},"
                    + "department=#{department},"
                    + "describe=#{describe},"
                    + "fill_color=#{fillColor},"
                    + "stroke_color=#{strokeColor},"
                    + "stroke_width=#{strokeWidth},"
                    + "opacity=#{opacity} "
                    + "where "
                    + "layer_id=#{layerId}"
    })
    /**
     * description: 更新图层信息
     *
     * @param layer
     * @return int
     */
    int updateLayer(Layer layer);


    @Delete({"delete from "
            + "layer "
            + "where "
            + "layer_id=#{layerId}"
    })
    /**
     * description: 删除layer
     *
     * @param layerId
     * @return int
     */
    int deleteLayer(String layerId);

    @Select({"select "
            + "* "
            + "from "
            + "layer "
            + "where "
            + "1=1 and "
            + "store_id =#{storeId}"
    })
    @Results({
            @Result(id = true, column = "layer_id", property = "layerId"),
            @Result(column = "layer_name", property = "layerName"),
            @Result(column = "layer_name_zh", property = "layerNameZH"),
            @Result(column = "store_id", property = "store.storeId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "thematic_id", property = "thematic.thematicId"),
            @Result(column = "thematic_name", property = "thematic.thematicName"),
            @Result(column = "thematic_name_zh", property = "thematic.thematicNameZH"),
            @Result(column = "first_classification_name", property = "firstClassificationName"),
            @Result(column = "second_classification_id", property = "secondClassificationId"),
            @Result(column = "security", property = "security"),
            @Result(column = "publish_time", property = "publishTime"),
            @Result(column = "upload_time", property = "uploadTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "epsg", property = "epsg"),
            @Result(column = "department", property = "department"),
            @Result(column = "describe", property = "describe"),
            @Result(column = "fill_color", property = "fillColor"),
            @Result(column = "stroke_color", property = "strokeColor"),
            @Result(column = "stroke_width", property = "strokeWidth"),
            @Result(column = "opacity", property = "opacity"),
    })
    /**
     * description: 根据storeId查询图层
     *
     * @param storeId
     * @return java.util.List<cc.wanshan.gisdev.entity.drawlayer.Layer>
     */
    List<Layer> findLayersByStoreId(String storeId);

    @Select({"select "
            + "layer_id,"
            + "layer_name,"
            + "layer_name_zh,"
            + "security,"
            + "type,"
            + "epsg,"
            + "describe,"
            + "fill_color,"
            + "stroke_color,"
            + "stroke_width,"
            + "opacity "
            + "from "
            + "layer "
            + "where "
            + "1=1 and "
            + "layer_id =#{layerId}"
    })
    @Results({
            @Result(id = true, column = "layer_id", property = "layerId"),
            @Result(column = "layer_name", property = "layerName"),
            @Result(column = "layer_name_zh", property = "layerNameZH"),
            @Result(column = "security", property = "security"),
            @Result(column = "type", property = "type"),
            @Result(column = "epsg", property = "epsg"),
            @Result(column = "describe", property = "describe"),
            @Result(column = "fill_color", property = "fillColor"),
            @Result(column = "stroke_color", property = "strokeColor"),
            @Result(column = "stroke_width", property = "strokeWidth"),
            @Result(column = "opacity", property = "opacity"),
            @Result(column = "layer_id", property = "pointList", many = @Many(select = "cc.wanshan.gis.dao.layer.PointDao.findPointByLayerId", fetchType = FetchType.LAZY)),
            @Result(column = "layer_id", property = "lineStringList", many = @Many(select = "cc.wanshan.gis.dao.layer.LineStringDao.findLineStringByLayerId", fetchType = FetchType.LAZY)),
            @Result(column = "layer_id", property = "polygonList", many = @Many(select = "cc.wanshan.gis.dao.layer.PolygonDao.findPolygonByLayerId", fetchType = FetchType.LAZY))
    })
    /**
     * description: 根据layerId查询Layer
     *
     * @param layerId
     * @return
     */
    Layer findLayerByLayerId(String layerId);

    @Select({
            "select "
                    + "layer_id "
                    + "from "
                    + "layer "
                    + "where "
                    + "1=1 and "
                    + "user_id=#{userId} and "
                    + "layer_name = #{layerName};"
    })
    /**
     * description:
     *
     * @param userId
     * @param layerName
     * @return int
     */
    Layer findLayerByUserIdAndLayerName(String userId, String layerName);

    @Select({"select "
            + "layer_id,"
            + "layer_name,"
            + "layer_name_zh,"
            + "security,"
            + "type,"
            + "epsg,"
            + "describe,"
            + "fill_color,"
            + "stroke_color,"
            + "stroke_width,"
            + "opacity "
            + "from "
            + "layer "
            + "where "
            + "1=1 and "
            + "user_id =#{userId}"
    })
    @Results({
            @Result(id = true, column = "layer_id", property = "layerId"),
            @Result(column = "layer_name", property = "layerName"),
            @Result(column = "layer_name_zh", property = "layerNameZH"),
            @Result(column = "security", property = "security"),
            @Result(column = "type", property = "type"),
            @Result(column = "epsg", property = "epsg"),
            @Result(column = "describe", property = "describe"),
            @Result(column = "fill_color", property = "fillColor"),
            @Result(column = "stroke_color", property = "strokeColor"),
            @Result(column = "stroke_width", property = "strokeWidth"),
            @Result(column = "opacity", property = "opacity"),
            @Result(column = "layer_id", property = "pointList", many = @Many(select = "cc.wanshan.gis.dao.layer.PointDao.findPointByLayerId", fetchType = FetchType.LAZY)),
            @Result(column = "layer_id", property = "lineStringList", many = @Many(select = "cc.wanshan.gis.dao.layer.LineStringDao.findLineStringByLayerId", fetchType = FetchType.LAZY)),
            @Result(column = "layer_id", property = "polygonList", many = @Many(select = "cc.wanshan.gis.dao.layer.PolygonDao.findPolygonByLayerId", fetchType = FetchType.LAZY))
    })
    /**
     * description: 根据用户Id查询所有图层
     *
     * @param userId 用户Id
     * @return java.util.List<cc.wanshan.gis.entity.plot.of2d.Layer>
     **/
    List<Layer> findByUserId(String userId);

    @Select({"select "
            + "* "
            + "from "
            + "layer "
            + "where "
            + "1=1 and "
            + "second_classification_id =#{secondClassificationId}"
    })
    @Results({
            @Result(id = true, column = "layer_id", property = "layerId"),
            @Result(column = "layer_name", property = "layerName"),
            @Result(column = "layer_name_zh", property = "layerNameZH"),
            @Result(column = "store_id", property = "store.storeId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "thematic_id", property = "thematic.thematicId"),
            @Result(column = "thematic_name", property = "thematic.thematicName"),
            @Result(column = "thematic_name_zh", property = "thematic.thematicNameZH"),
            @Result(column = "first_classification_name", property = "firstClassificationName"),
            @Result(column = "first_classification_id", property = "firstClassificationId"),
            @Result(column = "second_classification_name", property = "secondClassificationName"),
            @Result(column = "second_classification_id", property = "secondClassificationId"),
            @Result(column = "security", property = "security"),
            @Result(column = "publish_time", property = "publishTime"),
            @Result(column = "upload_time", property = "uploadTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "type", property = "type"),
            @Result(column = "epsg", property = "epsg"),
            @Result(column = "department", property = "department"),
            @Result(column = "describe", property = "describe"),
            @Result(column = "fill_color", property = "fillColor"),
            @Result(column = "stroke_color", property = "strokeColor"),
            @Result(column = "stroke_width", property = "strokeWidth"),
            @Result(column = "opacity", property = "opacity"),
            @Result(column = "layer_name", property = "ruleNameList", many = @Many(select = "cc.wanshan.gis.dao.style.RuleNameDao.findRuleNamesByLayerName", fetchType = FetchType.LAZY)),
    })
    /**
     * description:
     *
     * @param secondClassId
     * @return cc.wanshan.gis.entity.plot.of2d.Layer
     **/
    Layer findLayerBySecondClassId(String secondClassId);

    @Select({"select "
            + "layer_id,"
            + "layer_name,"
            + "layer_name_zh,"
            + "thematic_id,"
            + "thematic_name,"
            + "thematic_name_zh,"
            + "first_classification_name,"
            + "second_classification_id,"
            + "security,"
            + "publish_time,"
            + "upload_time,"
            + "update_time,"
            + "epsg,"
            + "department,"
            + "describe,"
            + "fill_color,"
            + "stroke_color,"
            + "stroke_width,"
            + "opacity "
            + "from "
            + "layer "
            + "where "
            + "1=1 and "
            + "thematic_id =#{thematic.thematicId} and "
            + "user_id is null;"
    })
    @Results({
            @Result(id = true, column = "layer_id", property = "layerId"),
            @Result(column = "layer_name", property = "layerName"),
            @Result(column = "layer_name_zh", property = "layerNameZH"),
            @Result(column = "thematic_id", property = "thematic.thematicId"),
            @Result(column = "thematic_name", property = "thematic.thematicName"),
            @Result(column = "thematic_name_zh", property = "thematic.thematicNameZH"),
            @Result(column = "first_classification_name", property = "firstClassificationName"),
            @Result(column = "second_classification_id", property = "secondClassificationId"),
            @Result(column = "security", property = "security"),
            @Result(column = "publish_time", property = "publishTime"),
            @Result(column = "upload_time", property = "uploadTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "epsg", property = "epsg"),
            @Result(column = "department", property = "department"),
            @Result(column = "describe", property = "describe"),
            @Result(column = "fill_color", property = "fillColor"),
            @Result(column = "stroke_color", property = "strokeColor"),
            @Result(column = "stroke_width", property = "strokeWidth"),
            @Result(column = "opacity", property = "opacity"),
    })
    /**
     * description: 根据thematicId查询layer
     *
     * @param thematicId
     * @return
     */
    List<Layer> findLayerByThematicIdAndNullUserId(String thematicId);
}
