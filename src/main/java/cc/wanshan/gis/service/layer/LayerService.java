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
    public Result insertLayer(Layer layer);
    /**
     * @Author Li Cheng
     * @Description 根据分装的layer实体类的layerId和storeId删除对应的layer
     * @Date 8:44 2019/4/1
     * @Param [jsonObject] 封装了layer信息的json对象
     * @return cc.wanshan.demo.entity.Result
     **/
    public Result deleteLayer(List<Layer> layers);
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
    public Result insertFeatures(String layerName, String type,List<Feature> features) throws IOException;
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
    /**
     * description: 根据用户名和图层名查询图层是否存在
     *
     * @param userId 用户名
    	* @param layerName 图层名
     * @return java.lang.Boolean
     **/
    public Layer findLayer(String userId,String layerName);
    /**
     * description: 根据featuresId批量删除feature
     *
     * @param features 元素集合
    	* @param type 元素类型
     * @return int
     **/
    public Boolean deleteFeature(List features,String type);
    /**
     * description: 根据用户Id查询所有图层
     *
     * @param userId 用户Id
     * @return java.util.List<cc.wanshan.gis.entity.drawlayer.Layer>
     **/
    public List<Layer> findByUserId(String userId);
    /**
     * description: 保存图层
     *
     * @param layer 图层对象
     * @return java.lang.Boolean
     **/
    public Result saveLayer(Layer layer);
}
