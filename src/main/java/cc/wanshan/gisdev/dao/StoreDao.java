package cc.wanshan.gisdev.dao;

import cc.wanshan.gisdev.entity.drawlayer.Store;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Li Cheng
 * @Description 存储点查询
 * @Date 17:34 2019/3/20
 * @Param
 * @return
 **/
@Mapper
@Component
public interface StoreDao {
    /**
     * @return cc.wanshan.demo.entity.Store
     * @Author Li Cheng
     * @Description 根据storeId查找当前store
     * @Date 8:40 2019/4/10
     * @Param [storeId]
     **/
    @Select({"select * from tb_store where store_id=#{storeId}"})
    @Results({
            @Result(id = true,column = "store_id",property = "storeId"),
            @Result(column = "store_name",property = "storeName"),
            @Result(column = "u_id",property = "user.userId"),
    })
    public Store findStoreByStoreId(Integer storeId);

    /**
     * @return java.util.List<cc.wanshan.demo.entity.Store> store集合
     * @Author Li Cheng
     * @Description 根据用户id查找当前用户所有store
     * @Date 8:41 2019/4/10
     * @Param [userId] 用户id
     **/
    @Select({"select * from tb_store where u_id=#{userId}"})
    @Results({
            @Result(id = true, column = "store_id", property = "storeId"),
            @Result(column = "u_id", property = "user.userId"),
            @Result(column = "store_name", property = "storeName")
    })
    public List<Store> findStoreByUserId(Integer userId);

    /**
     * @return int
     * @Author Li Cheng
     * @Description 新增store记录
     * @Date 8:49 2019/4/10
     * @Param [store]
     **/
    @Insert({"insert into tb_store (store_name,u_id) values (#{storeName},#{user.userId})"})
    @Options(useGeneratedKeys = true,keyProperty = "storeId",keyColumn = "store_id")
    public int insertStore(Store store);

    /**
     * @return int
     * @Author Li Cheng
     * @Description 修改用户记录
     * @Date 8:49 2019/4/10
     * @Param [store]
     **/
    @Update({"update tb_store set store_id=#{storeId},store_name=#{storeName},u_id=#{user.userId} where store_id=#{storeId}"})
    public int updateStore(Store store);

    /**
     * @return int
     * @Author Li Cheng
     * @Description 根据storeId删除store记录
     * @Date 9:03 2019/4/10
     * @Param [storeId]
     **/
    @Delete({"delete from tb_store where store_id=#{storeId}"})
    public int deleteStore(Integer storeId);

    @Select({"select * from tb_store as s where s.u_id=(select u.u_id from tb_user as u where u.username=#{username})"})
    @Results({
            @Result(id = true,column = "store_id",property = "storeId"),
            @Result(column = "store_name",property = "storeName"),
            @Result(column = "u_id",property = "user.userId"),
            @Result(column = "store_id",property = "layerList",many = @Many(select = "cc.wanshan.demo.repository.LayerDao.findLayersByStoreId",fetchType = FetchType.LAZY)),
    })
    public List<Store> findStoresByUsername(String username);
}
