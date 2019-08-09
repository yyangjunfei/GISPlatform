package cc.wanshan.gis.service.layer.thematic;

import cc.wanshan.gis.common.pojo.Result;
import cc.wanshan.gis.entity.layer.thematic.FirstClassification;
import cc.wanshan.gis.entity.plot.of2d.Feature;
import cc.wanshan.gis.entity.plot.of2d.Layer;
import cc.wanshan.gis.entity.plot.of2d.LineString;
import cc.wanshan.gis.entity.plot.of2d.Point;
import cc.wanshan.gis.entity.plot.of2d.Polygon;
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

    Result newLayer(String workspace, String layerName, String type, String epsg);


    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 根据分装的layer实体类的layerId和storeId删除对应的layer
     * @Date 8:44 2019/4/1
     * @Param [jsonObject] 封装了layer信息的json对象
     **/
    Result delete(List<Layer> layerIdList);

    /**
     * @return cc.wanshan.demo.entity.Result
     * @Author Li Cheng
     * @Description 查找图层是否为已发布图层
     * @Date 16:37 2019/4/10
     * @Param [jsonObject]
     **/
    Result searchLayer(String thematicName, String layerName);

    /**
     * @return void
     * @Author Li Cheng
     * @Description 创建图层对应表发布图层
     * @Date 14:43 2019/2/28
     * @Param [object]
     **/
    Result insertFeatures(String layerName, String type, List<Feature> features) throws IOException;


    /**
     * description: 修改图层属性信息
     *
     * @param layer 图层对象
     * @return java.lang.Boolean
     **/
    Result update(Layer layer);


    /**
     * description: 根据用户名和图层名查询图层是否存在
     *
     * @param userId    用户Id
     * @param layerName 图层名
     * @return java.lang.Boolean
     **/
    Result findByUserIdAndLayerName(String userId, String layerName);
    /**
     * description: 根据Id查找图层
     *
     * @param layerId 图层Id
     * @return cc.wanshan.gis.common.pojo.Result
     **/
    Result findByLayerId(String layerId);

    /**
     * description: 根据featuresId批量删除feature
     *
     * @param features 元素集合
     * @param type     元素类型
     * @return int
     **/
    Result deleteFeature(List features, String type);

    /**
     * description: 根据用户Id查询所有图层
     *
     * @param userId 用户Id
     * @return java.util.List<cc.wanshan.gis.entity.drawlayer.Layer>
     **/
    Result findByUserId(String userId);

    /**
     * description: 保存图层
     * @param layer 图层对象
     * @return cc.wanshan.gis.common.pojo.Result
     **/
    Result save(Layer layer);
}
