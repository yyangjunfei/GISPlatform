package cc.wanshan.gis.dao;


import cc.wanshan.gis.entity.drawlayer.Layer;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import java.util.List;


/**
 * @Author Li Cheng
 * @Date 17:32 2019/3/20
 **/
@Mapper
@Component
public interface LayerDao {

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
  public int deleteLayerByLayerNameAndStoreId(String layerName, Integer storeId);


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
          + "first_classification,"
          + "second_classification,"
          + "security,"
          + "publish_time,"
          + "upload_time,"
          + "update_time,"
          + "type,"
          + "epsg"
          + ") values ("
          + "#{layerName},"
          + "#{layerNameZH},"
          + "#{store.storeId},"
          + "#{userId},"
          + "#{thematic.thematicId},"
          + "#{thematic.thematicName},"
          + "#{thematic.thematicNameZH},"
          + "#{firstClassification},"
          + "#{secondClassification},"
          + "#{security},"
          + "#{publishTime},"
          + "#{uploadTime},"
          + "#{updateTime},"
          + "#{type},"
          + "#{epsg})"
  })
  @Options(useGeneratedKeys = true, keyColumn = "layer_id", keyProperty = "layerId")
  /**
   * description: 新增图层
   *
   * @param layer
   * @return int
   */
  public int insertLayer(Layer layer);


  @Update({
      "update "
          + "tb_layer "
          + "set "
          + "layer_id=#{layerId},"
          + "layer_name=#{layerName},"
          + "store_id=#{storeId},"
          + "type=#{type},"
          + "epsg=#{epsg} "
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
  public int deleteLayer(Integer layerId);

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
      @Result(column = "first_classification", property = "firstClassification"),
      @Result(column = "second_classification", property = "secondClassification"),
      @Result(column = "security", property = "security"),
      @Result(column = "publish_time", property = "publishTime"),
      @Result(column = "upload_time", property = "uploadTime"),
      @Result(column = "update_time", property = "updateTime"),
      @Result(column = "type", property = "type"),
      @Result(column = "epsg", property = "epsg"),
  })
  /**
   * description: 根据storeId查询图层
   *
   * @param storeId
   * @return java.util.List<cc.wanshan.gisdev.entity.drawlayer.Layer>
   */
  public List<Layer> findLayersByStoreId(String storeId);

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
}
