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

    @Select({
            "select * "
                    + "from "
                    + "tb_store "
                    + "where "
                    + "store_id=#{storeId}"
    })
    @Results({
            @Result(id = true, column = "store_id", property = "storeId"),
            @Result(column = "store_name", property = "storeName"),
            @Result(column = "user_id", property = "authorize.userId"),
    })
    /**
     * description: 根据storeid查询store
     *
     * @param storeId
     * @return cc.wanshan.gisdev.entity.drawlayer.Store
     */
    public Store findStoreByStoreId(String storeId);

    @Select({
            "select "
                    + "* "
                    + "from "
                    + "tb_store "
                    + "where "
                    + "user_id=#{userId}"
    })
    @Results({
            @Result(id = true, column = "store_id", property = "storeId"),
            @Result(column = "user_id", property = "authorize.userId"),
            @Result(column = "store_name", property = "storeName")
    })
    /**
     * description: 根据userId查询store
     *
     * @param userId
     * @return java.util.List<cc.wanshan.gisdev.entity.drawlayer.Store>
     */
    public List<Store> findStoreByUserId(String userId);


    @Insert({
            "insert into "
                    + "tb_store "
                    + "("
                    + "store_name,"
                    + "user_id,"
                    + "insert_time,"
                    + "update_time "
                    + ") "
                    + "values "
                    + "("
                    + "#{storeName},"
                    + "#{authorize.userId},"
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
    public int insertStore(Store store);


    @Update({
            "update "
                    + "tb_store "
                    + "set "
                    + "store_id=#{storeId},"
                    + "store_name=#{storeName},"
                    + "user_id=#{authorize.userId}, "
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
    public int updateStore(Store store);


    @Delete({
            "delete from "
                    + "tb_store "
                    + "where "
                    + "store_id=#{storeId}"
    })
    /**
     * description:删除store
     *
     * @param storeId
     * @return int
     */
    public int deleteStore(String storeId);

    /*@Select({
        "select "
            + "* "
            + "from "
            + "tb_store as s "
            + "where "
            + "s.user_id="
            + "(select "
            + "u.user_id "
            + "from "
            + "tb_user as u "
            + "where "
            + "u.username=#{username})"
    })*/
    @Select({
            "select u.user_id,u.thematic_id,u.security,s.store_id,s.store_name from tb_store as s inner join tb_user as u on u.user_id = s.user_id where u.delete = 0 and u.status=1 and u.username=#{username}"})
    @Results(value = {
            @Result(id = true, column = "store_id", property = "storeId"),
            @Result(column = "store_name", property = "storeName"),
            @Result(column = "security", property = "authorize.security"),
            @Result(column = "user_id", property = "authorize.userId"),
            @Result(column = "thematic_id", property = "authorize.thematic", many = @Many(select = "cc.wanshan.gis.dao.thematic.ThematicDao.findByThematicId")),
            @Result(column = "store_id", property = "layerList",
                    many = @Many(select = "cc.wanshan.gis.dao.layer.LayerDao.findLayersByStoreId")),
    })
    /**
     * description: 根据用户名查找stores
     *
     * @param username
     * @return java.util.List<cc.wanshan.gisdev.entity.drawlayer.Store>
     */

    public List<Store> findStoresByUsername(String username);
}