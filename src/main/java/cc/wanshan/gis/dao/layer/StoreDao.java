package cc.wanshan.gis.dao.layer;

import cc.wanshan.gis.entity.plot.of2d.Store;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    /**
     * description: 根据storeid查询store
     *
     * @param storeId
     * @return cc.wanshan.gisdev.entity.drawlayer.Store
     */
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
            @Result(column = "user_id", property = "user.userId"),
    })
    Store findStoreByStoreId(String storeId);


    /**
     * description: 根据userId查询store
     *
     * @param userId
     * @return java.util.List<cc.wanshan.gisdev.entity.drawlayer.Store>
     */
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
            @Result(column = "user_id", property = "user.userId"),
            @Result(column = "store_name", property = "storeName")
    })
    List<Store> findStoreByUserId(String userId);


    /**
     * description:新增store
     *
     * @param store
     * @return int
     */
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
                    + "#{user.userId},"
                    + "#{insertTime,jdbcType=TIMESTAMP},"
                    + "#{updateTime,jdbcType=TIMESTAMP}"
                    + ")"
    })
    @Options(useGeneratedKeys = true, keyProperty = "storeId", keyColumn = "store_id")
    int insertStore(Store store);

    /**
     * description: 更新store
     *
     * @param store
     * @return int
     */
    @Update({
            "update "
                    + "store "
                    + "set "
                    + "store_id=#{storeId},"
                    + "store_name=#{storeName},"
                    + "user_id=#{user.userId}, "
                    + "update_time=#{updateTime,jdbcType=TIMESTAMP} "
                    + "where "
                    + "store_id=#{storeId}"
    })
    int updateStore(Store store);


    /**
     * description:删除store
     *
     * @param storeId
     * @return int
     */
    @Delete({
            "delete from "
                    + "store "
                    + "where "
                    + "store_id=#{storeId}"
    })
    int deleteStore(String storeId);

    /**
     * description: 根据用户名查找stores
     *
     * @param username
     * @return java.util.List<cc.wanshan.gisdev.entity.drawlayer.Store>
     */
    @Select({
            "select u.user_id,u.thematic_id,u.user,s.store_id,s.store_name from store as s inner join user as u on u.user_id = s.user_id where u.delete = 0 and u.status=1 and u.username=#{username}"})
    @Results(value = {
            @Result(id = true, column = "store_id", property = "storeId"),
            @Result(column = "store_name", property = "storeName"),
            @Result(column = "user", property = "user.user"),
            @Result(column = "user_id", property = "user.userId"),
            @Result(column = "thematic_id", property = "user.thematic", many = @Many(select = "cc.wanshan.gis.dao.layer.thematic.ThematicDao.findByThematicId")),
            @Result(column = "store_id", property = "layerList",
                    many = @Many(select = "cc.wanshan.gis.dao.layer.LayerDao.findLayersByStoreId")),
    })
    List<Store> findStoresByUsername(String username);
}