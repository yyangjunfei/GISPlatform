package cc.wanshan.gisdev.dao;


import cc.wanshan.gisdev.entity.drawlayer.Layer;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @Author Li Cheng
 * @Description 图层查询
 * @Date 17:32 2019/3/20
 * @Param
 * @return
 **/
@Mapper
@Component
public interface LayerDao {
    /**
     * @Author Li Cheng
     * @Description 根据图层名和存储点id删除对应图层信息
     * @Date 9:37 2019/4/10
     * @Param [layerName, storeId]
     * @return int
     **/
    @Delete({"delete from tb_layer where layer_name=#{layerName} and store_id=#{storeId}"})
    public int deleteLayerByLayerNameAndStoreId(String layerName, Integer storeId);
    /**
     * @Author Li Cheng
     * @Description 新增layer记录
     * @Date 9:46 2019/4/10
     * @Param [layer]
     * @return int
     **/
    @Insert({"insert into tb_layer (layer_name,store_id,type,epsg) values (#{layerName},#{storeId},#{type},#{epsg})"})
    @Options(useGeneratedKeys = true,keyColumn = "layer_id",keyProperty = "layerId")
    public int insertLayer(Layer layer);
    /**
     * @Author Li Cheng
     * @Description 根据layerId修改layer信息
     * @Date 9:50 2019/4/10
     * @Param [layer]
     * @return int
     **/
    @Update({"update tb_layer set layer_id=#{layerId},layer_name=#{layerName},store_id=#{storeId},type=#{type},epsg=#{epsg} where layer_id=#{layerId}"})
    public int updateLayer(Layer layer);
    /**
     * @Author Li Cheng
     * @Description 根据layerId删除对应图层
     * @Date 9:52 2019/4/10
     * @Param [layerId]
     * @return int
     **/
    @Delete({"delete from tb_layer where layer_id=#{layerId}"})
    public int deleteLayer(Integer layerId);
    @Select({"select * from tb_layer where store_id =#{storeId}"})
    @Results({
            @Result(id = true,column = "layer_id",property = "layerId"),
            @Result(column = "layer_name",property = "layerName"),
            @Result(column = "store_id",property = "storeId"),
            @Result(column = "type",property = "type"),
            @Result(column = "epsg",property = "epsg"),
    })
    public List<Layer> findLayersByStoreId(Integer storeId);
    @Select({"select count(*) from tb_layer as l where l.layer_name = #{layerName} and l.store_id = (select s.store_id from tb_user as u inner join tb_store as s on u.u_id = s.u_id where u.username=#{username})   "})
    public int findLayerCountByUsernameAndLayerName(String username, String layerName);
}
