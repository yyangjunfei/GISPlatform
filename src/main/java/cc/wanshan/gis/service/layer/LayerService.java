package cc.wanshan.gis.service.layer;

import cc.wanshan.gis.entity.Result;
import cc.wanshan.gis.entity.drawlayer.Feature;
import cc.wanshan.gis.entity.drawlayer.Layer;
import cc.wanshan.gis.entity.thematic.FirstClassification;

import java.io.IOException;
import java.util.List;

/**
 * @Author Li Cheng
 * @Description layer表增删改查
 * @Date 15:32 2019/3/21
 * @Param
 * @return
 **/
public interface LayerService {
    public Result newLayer(String workspace, String layerName, String type, String epsg);
    /**
     * @Author Li Cheng
     * @Description 保存图层
     * @Date 8:44 2019/4/1
     * @Param layer图层实体类
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result insertLayer(Layer layer, String workspace);
    /**
     * @Author Li Cheng
     * @Description 根据分装的layer实体类的layerId和storeId删除对应的layer
     * @Date 8:44 2019/4/1
     * @Param [jsonObject] 封装了layer信息的json对象
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result deleteLayer(String layerName,String thematicName,String storeId,String storeName);
    /**
     * @Author Li Cheng
     * @Description 查找图层是否为已发布图层
     * @Date 16:37 2019/4/10
     * @Param [jsonObject]
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result searchLayer(String thematicName,String layerName);
    /**
     * @Author Li Cheng
     * @Description 创建图层对应表发布图层
     * @Date 14:43 2019/2/28
     * @Param [object]
     * @return void
     **/
    public Result insertFeatures(String thematicName,String layerName, String storeName,List<Feature> features) throws IOException;
    /**
     * description:
     *
     * @param username
     * @param layerName
     * @return
     */
    public Result findLayerCountByUsernameAndLayerName(String username, String layerName);
    /**
     * description: 根据thematicId和nullUserId查询layer
     *
     * @param thematicId
     * @return
     */
    public List<FirstClassification> findLayerByThematicIdAndNullUserId(String thematicId);
    /**
     * description: 修改图层属性信息
     *
     * @param layer 图层对象
     * @return java.lang.Boolean
     **/
    public Boolean updateLayer(Layer layer);
    /**
     * description: 根据图层id查询图层
     *
     * @param layerId 图层id
     * @return cc.wanshan.gis.entity.drawlayer.Layer
     **/
    public Layer findLayerByLayerId(String layerId);
}
