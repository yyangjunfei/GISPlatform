package cc.wanshan.gis.dao;


import cc.wanshan.gis.entity.drawlayer.Layer;
import org.apache.ibatis.annotations.*;
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
          + "tb_layer ("
          + "layer_name,"
          + "layer_name_zh,"
          + "store_id,"
          + "user_id,"
          + "thematic_id,"
          + "thematic_name,"
          + "thematic_name_zh,"
          + "first_classification_name,"
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
  public int insertLayer(Layer layer);

  @Delete({
      "delete from "
          + "tb_layer "
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
  public int deleteLayerByLayerNameAndStoreId(String layerName, String storeId);



  @Update({
      "update "
          + "tb_layer "
          + "set "
          + "layer_name_zh=#{layerNameZH},"
          + "store_id=#{store.storeId},"
          + "user_id=#{userId},"
          + "thematic_id=#{thematic.thematicId},"
          + "thematic_name=#{thematic.thematicName},"
          + "thematic_name_zh=#{thematic.thematicNameZH},"
          + "first_classification_name=#{firstClassificationName},"
          + "second_classification_id=#{secondClassificationId},"
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
  public int updateLayer(Layer layer);


  @Delete({"delete from "
      + "tb_layer "
      + "where "
      + "layer_id=#{layerId}"
  })
  /**
   * description: 删除layer
   *
   * @param layerId
   * @return int
   */
  public int deleteLayer(String layerId);

  @Select({"select "
      + "* "
      + "from "
      + "tb_layer "
      + "where "
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
  public List<Layer> findLayersByStoreId(String storeId);
  @Select({"select "
      + "* "
      + "from "
      + "tb_layer "
      + "where "
      + "layer_id =#{layerId}"
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
   * description: 根据layerId查询Layer
   *
   * @param layerId
   * @return
   */
  public Layer findLayerByLayerId(String layerId);


  @Select({"select "
      + "* "
      + "from "
      + "tb_layer "
      + "where "
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
      @Result(column = "layer_name", property = "ruleNameList",many = @Many(select = "cc.wanshan.gis.dao.RuleNameDao.findRuleNamesByLayerName",fetchType = FetchType.LAZY)),
  })
  /**
   * description:
   *
   * @param secondClassId
   * @return cc.wanshan.gis.entity.drawlayer.Layer
   **/
  public Layer findLayerBySecondClassId(String secondClassId);
  @Select({
      "select "
          + "count(*) "
          + "from "
          + "tb_layer as l "
          + "where "
          + "l.layer_name = #{layerName} and "
          + "l.store_id = ("
          + "select "
          + "s.store_id "
          + "from "
          + "tb_user as u "
          + "inner join "
          + "tb_store as s "
          + "on "
          + "u.u_id = s.u_id "
          + "where u.username=#{username})"
  })
  /**
   * description:
   *
   * @param username
   * @param layerName
   * @return int
   */
  public int findLayerCountByUsernameAndLayerName(String username, String layerName);
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
      + "tb_layer "
      + "where "
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
  public List<Layer> findLayerByThematicIdAndNullUserId(String thematicId);
}
