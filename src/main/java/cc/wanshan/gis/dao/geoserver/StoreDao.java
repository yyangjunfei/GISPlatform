package cc.wanshan.gis.dao.geoserver;

import cc.wanshan.gis.entity.drawlayer.Store;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * store 实体类的增删改查
 *
 * @Author Li Cheng
 * @Date 9:11 2019/5/18
 **/
@Mapper
@Component
public interface StoreDao {

  @Select({
      "select * "
          + "from "
          + "store "
          + "where "
          + "store_id=#{storeId}"
  })
  @Results({
      @Result(id = true, column = "store_id", property = "storeId"),
      @Result(column = "store_name", property = "storeName"),
      @Result(column = "user_id", property = "security.userId"),
  })
  /**
   * description: 根据storeid查询store
   *
   * @param storeId
   * @return cc.wanshan.gisdev.entity.drawlayer.Store
   */
  Store findStoreByStoreId(String storeId);

  @Select({
      "select "
          + "* "
          + "from "
          + "store "
          + "where "
          + "user_id=#{userId}"
  })
  @Results({
      @Result(id = true, column = "store_id", property = "storeId"),
      @Result(column = "user_id", property = "security.userId"),
      @Result(column = "store_name", property = "storeName")
  })
  /**
   * description: 根据userId查询store
   *
   * @param userId
   * @return java.util.List<cc.wanshan.gisdev.entity.drawlayer.Store>
   */
  List<Store> findStoreByUserId(String userId);


  @Insert({
      "insert into "
          + "store "
          + "("
          + "store_name,"
          + "user_id,"
          + "insert_time,"
          + "update_time "
          + ") "
          + "values "
          + "("
          + "#{storeName},"
          + "#{security.userId},"
          + "#{insertTime,jdbcType=TIMESTAMP},"
          + "#{updateTime,jdbcType=TIMESTAMP}"
          + ")"
  })
  @Options(useGeneratedKeys = true, keyProperty = "storeId", keyColumn = "store_id")
  /**
   * description:新增store
   *
   * @param store
   * @return int
   */
  int insertStore(Store store);


  @Update({
      "update "
          + "store "
          + "set "
          + "store_id=#{storeId},"
          + "store_name=#{storeName},"
          + "user_id=#{security.userId}, "
          + "update_time=#{updateTime,jdbcType=TIMESTAMP} "
          + "where "
          + "store_id=#{storeId}"
  })

  /**
   * description: 更新store
   *
   * @param store
   * @return int
   */
  int updateStore(Store store);


  @Delete({
      "delete from "
          + "store "
          + "where "
          + "store_id=#{storeId}"
  })
  /**
   * description:删除store
   *
   * @param storeId
   * @return int
   */
  int deleteStore(String storeId);

  /*@Select({
      "select "
          + "* "
          + "from "
          + "store as s "
          + "where "
          + "s.user_id="
          + "(select "
          + "u.user_id "
          + "from "
          + "security as u "
          + "where "
          + "u.username=#{username})"
  })*/
  @Select({
      "select u.user_id,u.thematic_id,u.security,s.store_id,s.store_name from store as s inner join security as u on u.user_id = s.user_id where u.delete = 0 and u.status=1 and u.username=#{username}"})
  @Results(value = {
      @Result(id = true, column = "store_id", property = "storeId"),
      @Result(column = "store_name", property = "storeName"),
      @Result(column = "security", property = "security.security"),
      @Result(column = "user_id", property = "security.userId"),
      @Result(column = "thematic_id", property = "security.thematic", many = @Many(select = "cc.wanshan.gis.dao.layer.ThematicDao.findByThematicId")),
      @Result(column = "store_id", property = "layerList",
          many = @Many(select = "cc.wanshan.gis.dao.layer.LayerDao.findLayersByStoreId")),
  })
  /**
   * description: 根据用户名查找stores
   *
   * @param username
   * @return java.util.List<cc.wanshan.gisdev.entity.drawlayer.Store>
   */

  List<Store> findStoresByUsername(String username);
}